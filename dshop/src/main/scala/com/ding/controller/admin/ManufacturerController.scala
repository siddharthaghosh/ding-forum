/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.controller.admin

import com.ding.controller._
import com.ding.model._
import com.ding.util._
import net.liftweb.common._
import net.liftweb.util.Helpers._
import net.liftweb.http._
import net.liftweb.json._
import net.liftweb.json.JsonAST._
import java.io.File
import java.io._

object ManufacturerController extends Controller[Manufacturer] {

    override def metaModel = MetaModels.metaManufacturer

    override def processAction(action : String) : Box[LiftResponse] = {
        action match {
            case "" => {
                    explore()
            }
            case "index" => {
                    explore()
            }
            case "explore" => {
                    explore()
            }
            case "save" => {
                    save()
            }
            case "remove" => {
                    remove()
            }
            case _ => Full(NotFoundResponse())
        }
    }

    private def explore() : Box[LiftResponse] = {
        val reqstr = this.getRequestContent()
        val allList = metaModel.findAllInstances
        val startstr = urlDecode(S.param("offset").openOr("0"))
        val limitstr = urlDecode(S.param("limit").openOr(allList.length.toString))
        try {
            val start = startstr.toInt + 1
            val end = start + limitstr.toInt - 1
            val total = allList.length
            val itemList = if(start > end)
                Nil
            else if (start > total)
                Nil
            else {
                val rightPos = if (end > total) total else end
                val leftPos = start
                allList.take(rightPos).takeRight(rightPos - leftPos + 1)
            }
             val resultList = itemList.flatMap {
                item => {
                    val id = item.getID()
                    val name = item.getName()
                    val image = S.request.open_!.request.contextPath + "/image/manufacturer/" + item.getImage()
                    val addTime = item.getAddTime().toString
                    val updateTime = item.getUpdateTime.toString
                    val url = item.getURL()
                    List(
                        JsonAST.JField("id", JsonAST.JInt(id))
                        ++
                        JsonAST.JField("name", JsonAST.JString(name))
                        ++
                        JField("image", JString(image))
                        ++
                        JField("addTime", JString(addTime))
                        ++
                        JField("updateTime", JString(updateTime))
                        ++
                        JField("url", JString(url))
                    )
                }
            }
            Full(JsonResponse(JsonAST.JObject(
                        JField("total", JInt(total))::JField("manufacturer", JArray(resultList))::Nil
                    )))
        }
    }
    private def save() : Box[LiftResponse] = {
        val reqstr = this.getRequestContent
        try {
            val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
            val jsonItem = jsonList.head.asInstanceOf[JsonAST.JObject]
            val id = jsonItem.values("id").asInstanceOf[BigInt].toLong
            val name = jsonItem.values("name").asInstanceOf[String]
            val url = jsonItem.values("url").asInstanceOf[String]
//            val id = -1
//            val name = "test"
//            val url = "testurl"
            val req = S.request.open_!
//            val id = S.param("id").openOr("-1").toLong
//            val name = S.param("name").openOr("")
//            val url = S.param("url").openOr("")
            val item = if(id == -1) metaModel.newInstance else metaModel.findOneInstance(id)
            if(item != null) {
//                ShopLogger.logger.debug("upload files num: " + req.uploadedFiles.length)
//                req.uploadedFiles.foreach {
//                    file => {
//                        if(file.mimeType.startsWith("image/")){
//                            val filename = (new File(file.fileName)).getName
//                            val filecontent = file.file
//                            if(filename.length > 0 && filecontent.length > 0) {
//                                val out : java.io.FileOutputStream = new java.io.FileOutputStream("d:/tmp/" + filename)
//                                out.write(filecontent)
//                                out.close
//                                item.setImage(filename)
//                            }
//                        }
//                    }
//                }

                //更改URL
                if(item.getURL != url) item.setURL(url)

                val newFileName = item.getID.toString + "-" + (if(name.length > 0) name else "noname")
                val oldFileName = item.getID.toString + "-" + (if(item.getName.length > 0) item.getName else "noname")
                val tmpFileName = req.request.session.attribute("uploadFileName").asInstanceOf[String]
                if(tmpFileName != null) {
                    /*存在文件上传操作*/
                    if(item.getName != name) {
                        //需要更名
                        item.setName(name)
                    }
                    val oldFile = new File(this.getUploadFilePath + oldFileName)
                    if(oldFile.exists) {
                        oldFile.delete()
                        item.setImage("")
                    }
                    val tmpFile = new File(this.getTmpFilePath + tmpFileName)
                    if(tmpFile.exists) {
                        tmpFile.renameTo(new File(this.getUploadFilePath + newFileName))
                        item.setImage(newFileName)
                    }
                } else {
                    /*不存在文件上传操作*/
                    if(item.getName != name) {
                        /*需要更名*/
                        //更名
                        item.setName(name)
                        //更改关联文件名
                        val oldFile = new File(this.getUploadFilePath + oldFileName)
                        if(oldFile.exists) {
                            oldFile.renameTo(new File(this.getUploadFilePath + newFileName))
                            item.setImage(newFileName)
                        } else {
                            item.setImage("")
                        }
                            
                    }
                    //不需要更名无后续操作
                }
                /*
                 * 通过session获取uploadfile控件所上传文件的文件名
                 */
                //保存对象
//                item.saveInstance
//                //将文件由临时目录拷贝到正式图片目录，未实现
//                val oldFileName = req.request.session.attribute("uploadFileName").asInstanceOf[String]
//                //移除session中的上传文件名变量
//                req.request.session.removeAttribute("uploadFileName")
//                //得到扩展名
//                val extpos = oldFileName.lastIndexOf(".")
//                val extName = if(extpos > -1 && extpos < oldFileName.length) oldFileName.substring(extpos + 1) else ""
//                //构建新文件名，ID号-名称.扩展名
//                val newFileName = item.getID.toString + "-" + (if(item.getName.length > 0) item.getName else "noname")/*  + (if(extName.length > 0) "." + extName else "") */
//                val absNewFileName = this.getUploadFilePath + newFileName
//                //移动临时文件到正式文件夹
//                val inputFile = new File(this.getTmpFilePath + oldFileName)
//                val oldFile = new File(absNewFileName)
//                if(oldFile.exists) oldFile.delete
//
////                val success = inputFile.renameTo(new File(newFileName))
//                if(inputFile.exists) {
//                    val outputFile = new File(absNewFileName)
//                    inputFile.renameTo(outputFile)
//                }
                item.saveInstance
            }
//            Full(OkResponse())
            explore()
        }
    }
    private def remove() : Box[LiftResponse] = {
        val reqstr = this.getRequestContent()
//                val reqstr = "[{\"id\": 2}]"
        try {
            val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
            jsonList.foreach(
                jsonItem => {
                    val id = jsonItem.asInstanceOf[JsonAST.JObject].values("id").asInstanceOf[BigInt].toLong
                    val item = metaModel.findOneInstance(id)
                    val imageFileName = item.getImage()
                    val imageFile = new File(this.getUploadFilePath + imageFileName)
                    if(imageFile.exists)
                        imageFile.delete
                    if(item != null)
                        item.deleteInstance()
                }
            )
            explore()
        }
    }
    override def getRequestContent() = {
        urlDecode(S.param("json").openOr(""))
    }
    private def getUploadFilePath = {
//        S.request.open_!.
//        getClass.getResource("/image/manufacturer/").getFile
        "D:/ws-netbeans/dshop/image/manufacturer/"
    }
    private def getTmpFilePath = "d:/temp/uploadfiles/"
}
