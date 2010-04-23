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
import net.liftweb.util.Props
import net.liftweb.http._
import net.liftweb.json._
import net.liftweb.json.JsonAST._
import java.io.File
import java.io._

object ManufacturerController
extends ModelController[Manufacturer]
   /*with ImageSingleUpload*/ {
//    override type A = Manufacturer
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
                    val image = S.request.open_!.request.contextPath + "/image/origin/image?" + Props.get("urlparam.filename").open_! + "=" + urlEncode(/* this.getFirstUploadFile(item) */item.getFirstImageFileLocation)
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
//        val reqstr = this.getRequestContent
        val reqstr = "[{\"id\":1,\"name\":\"m1\",\"url\":\"url1\",\"filename\":[\"root/manufacturer/106ijenf6qmro1271916703873\"]}]"
        try {
            val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
            val jsonItem = jsonList.head.asInstanceOf[JsonAST.JObject]
            val id = jsonItem.values("id").asInstanceOf[BigInt].toLong
            val name = jsonItem.values("name").asInstanceOf[String]
            val url = jsonItem.values("url").asInstanceOf[String]
            val filenameList = jsonItem.values("filename").asInstanceOf[List[String]]
            val jsonFileNameArr = JArray(filenameList.flatMap {
                    filename => {
                        JString(filename)::Nil
                    }
                })
            val jsonStr = Printer.pretty(JsonAST.render(jsonFileNameArr))
            val req = S.request.open_!
            val item = if(id == -1) metaModel.newInstance else metaModel.findOneInstance(id)
            if(item != null) {
                //更改URL
                if(item.getURL != url) item.setURL(url)
                if(item.getName != name) item.setName(name)
                if(item.getImage != jsonStr) item.setImage(jsonStr)
                item.saveInstance()
//                this.storeUploadFiles(item)
//                item.saveInstance
            }
//            req.request.session.removeAttribute(Props.get("upload.sessionkey").open_!)
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
//                    val imageFileName = item.getImage()
//                    val imageFile = new File(this.getImageFilePath + imageFileName)
//                    if(imageFile.isFile && imageFile.exists)
//                        imageFile.delete
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
//    private def getImageFilePath = {
////        S.request.open_!.
////        getClass.getResource("/image/manufacturer/").getFile
//        Props.get("image.dir").open_! + reqInfo.is.application + "/"
//    }
//    private def getTmpFilePath = Props.get("upload.tmpdir").open_!
}
