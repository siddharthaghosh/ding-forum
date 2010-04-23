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
   with UploadImageAndGenerateThumb[MetaUploadFile]{

    val originDir = Props.get("image.origin.dir").open_!
    val thumbDir = Props.get("image.thumbnail.dir").open_!

    override def processAction(action : String) : Box[LiftResponse] = {
        action match {
            case "folder" => {
                    folderExplore()
                }
            case "addfolder" => {
                    folderAdd()
                }
            case "removefolder" => {
                    folderRemove()
                }
            case "file" => {
                    fileExplore()
                }
            case "upload" => {
                    upload()
                }
            case "fileremove" => {
                    fileRemove()
                }
            case _ => folderExplore()
        }
    }

    override def getRequestContent() = {
        urlDecode(S.param("json").open_!)
    }

    override def getUploadDir : String =  Props.get("image.origin.dir").open_! + "/"

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
//                            val leaf = /* !this.isContainSubFolder(dir) */ false
                            val path = relativePath + "/" + child
                            JObject(JField("name",JString(name))::/* JField("leaf", JBool(leaf)):: */JField("path", JString(path))::Nil)::Nil
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

    private def folderAdd() = {
        val reqstr = this.getRequestContent()
        try {
            val jsonList = JsonParser.parse(reqstr).asInstanceOf[JArray].arr
            val folderName = jsonList.head.asInstanceOf[JObject].values("name").asInstanceOf[String]
            val relativePath = S.param("path").open_!
            val relativeFolderPath = relativePath + "/" + folderName
            addFolder(relativeFolderPath)
            folderExplore()
        }
//        Full(NotFoundResponse())
    }

    private def folderRemove() = {
        val reqstr = this.getRequestContent()
        try {
            val jsonList = JsonParser.parse(reqstr).asInstanceOf[JArray].arr
            val relativePath = jsonList.head.asInstanceOf[JObject].values("path").asInstanceOf[String]
            removeFolder(relativePath)
            folderExplore()
        }
//        removeFolder("root/manufacturer/d1")
//        Full(OkResponse())
    }

    private def upload() = {
        val reqstr = this.getRequestContent
        try{
            val jsonObj = JsonParser.parse(reqstr).asInstanceOf[JArray].arr.head.asInstanceOf[JObject]
//            val relativePath : String = S.param("path").openOr("")
            val relativePath : String = jsonObj.values("path").asInstanceOf[String]
            this.storeUploadFiles(relativePath)
            fileExplore()
        }
    }

    private def fileExplore() = {
        val reqstr = this.getRequestContent()
//        println(reqstr)
//        val reqstr = "[{\"path\": \"manufacturer\"}]"
        try {
            val jsonObj = JsonParser.parse(reqstr).asInstanceOf[JArray].arr.head.asInstanceOf[JObject]
            val relativePath : String = jsonObj.values("path").asInstanceOf[String]
            val baseDir = this.getUploadDir
            val requestFolder = new File(baseDir + relativePath + "/")
            val resultList : List[JValue] = if (requestFolder.exists && requestFolder.isDirectory) {
                val children = requestFolder.list()
                children.flatMap {
                    child => {
                        val fileStr = baseDir + relativePath +"/" + child
                        val file = new File(fileStr)
                        if(file.exists && file.isFile) {
                            val fitem = MetaModels.metaUploadFile.findByRealName(child)
                            val name = if(fitem != null) fitem.getDisplayName else child
                            val filePath = relativePath + "/" + child
                            val baseUrl = S.request.open_!.request.contextPath + "/image"
                            val fileUrl = Props.get("urlparam.filename").open_! + "=" + urlEncode(filePath)
                            val url : String = baseUrl + "/origin/image?" + fileUrl
                            val thumbUrl : String = baseUrl + "/thumbnail/image?" + fileUrl
                            JObject(JField("name",JString(name))::
                                    JField("url",JString(url))::
                                    JField("thumbUrl",JString(thumbUrl))::Nil)::Nil
                            
                        } else {
                            Nil
                        }
                    }
                }.toList
            } else {
                Nil
            }
            Full(JsonResponse(JObject(JField("file", JArray(resultList))::Nil)))
        }
//        Full(NotFoundResponse())
    }

    private def fileRemove() = {
        val reqstr = this.getRequestContent()
        println(reqstr)
        try {
            val jsonList = JsonParser.parse(reqstr).asInstanceOf[JArray].arr
            val tail = jsonList.tail
            val removeList = tail.head.asInstanceOf[JArray].arr
            val originDir = Props.get("image.origin.dir").open_!
            val thumbDir = Props.get("image.thumbnail.dir").open_!
            removeList.foreach {
                removeItem => {
                    val url = removeItem.asInstanceOf[JString].values
                    val params = Props.get("urlparam.filename").open_! + "="
                    val sindex = url.indexOf(params)
                    val path = url.substring(sindex + params.length)
                    val eindex = if(path.indexOf("&") < 0) path.length else path.indexOf("&")
                    removeFile(path.substring(0, eindex))
                }
            }
        }
        fileExplore()
    }

    private def removeFile(relativePath : String) {
        removeOriginFile(relativePath)
        val file = new File(relativePath)
        val filename = file.getName()
        removeThumbnailFile(filename)
        val fileitem = MetaModels.metaUploadFile.findByRealName(filename)
        if(fileitem != null) {
            fileitem.deleteInstance
        }
    }

    private def removeOriginFile(relativePath : String) {
        val of = new File(this.originDir + "/" + relativePath)
        if(of != null && of.exists) {
            of.delete
        }
    }

    private def removeThumbnailFile(name : String) {
        val tf = new File(this.thumbDir + "/" + name)
        if(tf != null && tf.exists) {
            tf.delete
        }
    }

    private def addFolder(folder : String) {
        val absFolderName = this.originDir + folder
        val newFolder = new File(absFolderName)
        if(newFolder != null && !newFolder.exists) {
            newFolder.mkdir()
        }
    }

    private def removeFolder(relativePath : String) {
        if(relativePath == "" || relativePath == "root" || relativePath == "/root" || relativePath == "/root/" || relativePath == "root/")
            return
        val item = new File(this.originDir + relativePath)
        if(item != null && item.exists) {
            if(item.isFile) {
                removeFile(relativePath)
            } else if (item.isDirectory) {
                val children = item.list()
                children.foreach {
                    child => {
                        removeFolder(relativePath + "/" + child)                        
                    }
                }
                item.delete()
            }
        }
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

    private def secureRelativePath(path : String) : String = {
        ""
    }
}
