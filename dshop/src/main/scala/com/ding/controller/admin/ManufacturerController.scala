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
                    val image = S.request.open_!.request.contextPath + "/image/manufacturer/image?filename=" + urlEncode(item.getImage())
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
                //更改URL
                if(item.getURL != url) item.setURL(url)
                if(item.getName != name) item.setName(name)
                item.saveInstance()
//                val fileName = item.getID.toString
                val tmpFileName = req.request.session.attribute("uploadFileName").asInstanceOf[String]
                val tmpFileNames = req.request.session.attribute("uploadFileNames").asInstanceOf[java.util.List[String]]
//                if(tmpFileName != null) {
//                    /*存在文件上传操作*/
//                    val oldFile = new File(this.getUploadFilePath + fileName)
//                    if(oldFile.exists) {
//                        oldFile.delete()
//                        item.setImage("")
//                    }
//                    val tmpFile = new File(this.getTmpFilePath + tmpFileName)
//                    if(tmpFile.exists) {
//                        tmpFile.renameTo(new File(this.getUploadFilePath + fileName))
//                        item.setImage(fileName)
//                    }
//                } else {
//                    /*不存在文件上传操作*/
//                    if(id == -1) {
//                        item.setImage("")
//                    }
//                }
                if(tmpFileNames != null && tmpFileNames.size() > 0) {
//                    tmpFileNames.foreach {
//                        tmpFileName => {
//                            if(id != -1) {
//                                //如果不是新增操作，必须考虑旧文件的删除
//                                if(item.getImage.length > 0) {
//                                    val oldFile = new File(this.getUploadFilePath + item.getImage)
//                                    if(oldFile.exists) {
//                                        oldFile.delete()
//                                        item.setImage("")
//                                    }
//                                }
//                            }
//                            val tmpFile = new File(this.getTmpFilePath + tmpFileName)
//                            if(tmpFile.exists) {
//                                tmpFile.renameTo(new File(this.getUploadFilePath + item.getID.toString + tmpFileName))
//                                item.setImage(item.getID.toString + tmpFileName)
//                            }
//                        }
//                    }
                    var i = tmpFileNames.iterator()
                    while(i.hasNext()) {
                        val tmpFileName = i.next().asInstanceOf[String]
                        if(id != -1) {
                            //如果不是新增操作，必须考虑旧文件的删除
                            if(item.getImage.length > 0) {
                                val oldFile = new File(this.getUploadFilePath + item.getImage)
                                if(oldFile.exists) {
                                    oldFile.delete()
                                    item.setImage("")
                                }
                            }
                        }
                        val tmpFile = new File(this.getTmpFilePath + tmpFileName)
                        if(tmpFile.exists) {
                            tmpFile.renameTo(new File(this.getUploadFilePath + item.getID.toString + "-" + tmpFileName))
                            item.setImage(item.getID.toString + "-" + tmpFileName)
                        }
                    }
                } else {
                    /*不存在文件上传操作*/
                    if(id == -1) {
                        item.setImage("nopic.gif")
                    }
                }
                item.saveInstance
            }
            req.request.session.removeAttribute("uploadFileNames")
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
                    if(imageFile.isFile && imageFile.exists)
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