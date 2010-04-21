/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.controller.admin

import com.ding.controller._
import com.ding.util._
import com.ding.model._
import java.io._
import net.liftweb.http._
import net.liftweb.common._
import net.liftweb.util.Helpers._
import net.liftweb.util.Props
import net.liftweb.json._
import net.liftweb.json.JsonAST._

object FileManagerController
extends BaseController
   with UploadAction[MetaUploadFile]{

    override def processAction(action : String) : Box[LiftResponse] = {
        action match {
            case "folder" => {
                    folderExplore()
                }
            case "file" => {
                    fileExplore()
            }
            case "upload" => {
                    upload()
                }
            case _ => Full(NotFoundResponse())
        }
    }

    override def getRequestContent() = {
        urlDecode(S.param("json").openOr(""))
    }

    override def getUploadDir : String =  Props.get("image.dir").open_! + "/"

    override def uploadFileModel = MetaModels.metaUploadFile

    private def folderExplore() = {
//        val reqstr = this.getRequestContent()
        try {
            val relativePath : String = S.param("path").openOr("")
            val baseDir = this.getUploadDir
            val requestFolder = new File(baseDir + relativePath + "/")
            val resultList : List[JValue] = if (requestFolder.exists && requestFolder.isDirectory) {
                val children = requestFolder.list()
                children.flatMap {
                    child => {
                        val dirstr = baseDir + relativePath +"/" + child
                        val dir = new File(dirstr)
                        if(dir.exists && dir.isDirectory) {
                            val name = child
                            val leaf = !this.isContainSubFolder(dir)
                            val path = relativePath + "/" + child
                            JObject(JField("name",JString(name))::JField("leaf", JBool(leaf))::JField("path", JString(path))::Nil)::Nil
                        } else {
                            Nil
                        }
                    }
                }.toList
            } else {
                Nil
            }
            Full(JsonResponse(JObject(JField("folder", JArray(resultList))::Nil)))
        }
//        Full(NotFoundResponse())
    }

    private def upload() = {
        val relativePath : String = S.param("path").openOr("")
        this.storeUploadFiles(relativePath)
        fileExplore()
    }

    private def fileExplore() = {
        Full(NotFoundResponse())
    }

    private def isContainSubFolder(folder : File) : Boolean = {
        if(folder.exists && folder.isDirectory) {
            val children = folder.list()
            children.foreach {
                child => {
                    val file = new File(folder.getAbsolutePath + "/" + child)
                    if(file.exists && file.isDirectory)
                        return true
                }
            }
            false
        } else {
            false
        }
    }

   
}
