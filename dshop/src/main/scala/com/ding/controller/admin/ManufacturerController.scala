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
                    val image = item.getImage()
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
        try {
            val req = S.request.open_!
            val id = S.param("id").openOr("-1").toLong
//            val id = -1
            val name = S.param("name").openOr("")
            val url = S.param("url").openOr("")
            val item = if(id == -1) metaModel.newInstance else metaModel.findOneInstance(id)
            if(item != null) {
                item.setName(name)
                item.setURL(url)
                ShopLogger.logger.debug("upload files num: " + req.uploadedFiles.length)
                req.uploadedFiles.foreach {
                    file => {
                        if(file.mimeType.startsWith("image/")){
                            val filename = (new File(file.fileName)).getName
                            val filecontent = file.file
                            if(filename.length > 0 && filecontent.length > 0) {
                                val out : java.io.FileOutputStream = new java.io.FileOutputStream("d:/tmp/" + filename)
                                out.write(filecontent)
                                out.close
                                item.setImage(filename)
                            }
                        }
                    }
                }
                item.saveInstance
            }
            Full(OkResponse())
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
}
