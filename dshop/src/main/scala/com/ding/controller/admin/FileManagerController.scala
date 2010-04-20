/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.controller.admin

import com.ding.controller._
import net.liftweb.http._
import net.liftweb.common._
import net.liftweb.util.Helpers._
import net.liftweb.util.Props
import com.ding.util._
import java.io._
import javax.imageio._
import javax.imageio.stream._
import net.liftweb.json._
import net.liftweb.json.JsonAST._

object FileManagerController extends BaseController {

    override def processAction(action : String) : Box[LiftResponse] = {
        action match {
            case "folder" => {
                    folderExplore()
            }
            case _ => Full(NotFoundResponse())
        }
    }

    private def folderExplore() = {
        val baseDir = this.getUploadDir
        val requestFolder = new File(baseDir)
        val resultList : List[JValue] = if (requestFolder.exists && requestFolder.isDirectory) {
            val children = requestFolder.list()
            children.flatMap {
                child => {
                    val dirstr = baseDir + "/" + child
                    val dir = new File(dirstr)
                    if(dir.exists && dir.isDirectory) {
                        val name = child
                        val leaf = !this.isContainSubFolder(dir)
                        JObject(JField("name",JString(name))::JField("leaf", JBool(leaf))::Nil)::Nil
                    } else {
                        Nil
                    }
                }
            }.toList
        } else {
            Nil
        }
        Full(JsonResponse(JObject(JField("folder", JArray(resultList))::Nil)))
//        Full(NotFoundResponse())
    }

    def getUploadDir() : String = Props.get("image.dir").open_! + "/"

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
