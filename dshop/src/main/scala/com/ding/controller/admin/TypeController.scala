/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.controller.admin

import com.ding.controller._
import com.ding.util.ShopLogger
import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.util.Helpers._
import com.ding.model._
import net.liftweb.json._
import net.liftweb.json.JsonAST._

object TypeController extends ModelController[Type]{
    
    override def metaModel = MetaModels.metaType

    override def processAction(action : String) : Box[LiftResponse] = {
        action match {
            case "explore" => {
                    explore()
                }
            case "removetype" => {
                    removeType()
                }
            case "typename" =>{
                    typeName()
                }
            case "savetypename" => {
                    saveTypeName()
                }
            case "optiongroup" => {
                    optionGroup()
                }
            case _ => explore()
        }
    }

    override def getRequestContent() = {
        urlDecode(S.param("json").openOr(""))
    }

    private def explore() : Box[LiftResponse] = {
        val reqstr = this.getRequestContent
//        val reqstr = "[{\"start\":1, \"end\":5}]"
        val allList = metaModel.findAllInstances
        val startstr = urlDecode(S.param("offset").openOr("0"))
        val limitstr = urlDecode(S.param("limit").openOr(allList.length.toString))

        try{
            val start = startstr.toInt + 1
            val end = start + limitstr.toInt - 1
            val langId = this.getDefaultLang()

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
                    val name = item.getName(langId)
                    List(
                        JsonAST.JField("id", JsonAST.JInt(id))
                        ++
                        JsonAST.JField("name", JsonAST.JString(name))
                    )
                }
            }
            Full(JsonResponse(JsonAST.JObject(
                        JField("total", JInt(total))::JField("type", JArray(resultList))::Nil
                    )))
        }
    }

    private def removeType() : Box[LiftResponse] = {
        val reqstr = this.getRequestContent()
//        val reqstr = "[{\"id\": 2}]"
        try {
            val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
            jsonList.foreach(
                jsonItem => {
                    val tid = jsonItem.asInstanceOf[JsonAST.JObject].values("id").asInstanceOf[BigInt].toLong
                    val item = metaModel.findOneInstance(tid)
                    if(item != null)
                        item.deleteInstance()
                }
            )
            explore()
        }
    }

    private def getAllNamesByModelInstanceAsJsonValue(item : Type) : JsonAST.JValue = {
        val supportLangs : List[Language] = MetaModels.metaLanguage.findAllInstances
        val NameDetail : List[JsonAST.JValue] = supportLangs.flatMap {
            lang => {
                val langId = lang.getID
                val header = lang.getName
                val code = lang.getCode
                val name = item.getName(langId)
                List(JsonAST.JField("langId", JInt(langId))
                     ++
                     JField("header", JString(header))
                     ++
                     JField("code", JString(code))
                     ++
                     JField("name", JString(name))
                )
            }
        }
        JArray(NameDetail)
    }

    private def getAllOptionGroupsByModelInstanceAsJsonValue(item : Type) : JValue = {
        val result = item.getAllSupportOptionGroup.flatMap {
            optionGroup => {
                JObject(JField("id", JInt(optionGroup.getID))::
                        JField("name", JString(optionGroup.getName(this.getDefaultLang)))::Nil)::
                Nil
            }
        }
        JArray(result)
    }

    private def saveTypeName() : Box[LiftResponse] = {
        val reqstr = this.getRequestContent()
//        val reqstr = "[{\"id\":-1,\"typeName\":[{\"langId\":22, \"name\":\"测试类型2\"}]}]"
        val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
        val jsonItem = jsonList.head.asInstanceOf[JsonAST.JObject]
        val tid = jsonItem.values("id").asInstanceOf[BigInt].toLong
        val item = if(tid == -1)
            metaModel.newInstance()
        else
            metaModel.findOneInstance(tid)
        if(item != null) {
            val name_details : List[Map[String, Object]] = jsonItem.values("typeName").asInstanceOf[List[Map[String, Object]]]
            name_details.foreach(
                name_detail => {
                    val lang_id = name_detail("langId").asInstanceOf[BigInt].toLong
                    val name = name_detail("name").asInstanceOf[String]
                    if(name != null && name.length > 0)
                        item.setName(lang_id, name)
                    else {

                    }
                }
            )
            item.saveInstance()
            
        }
        explore()
    }

    private def typeName() : Box[LiftResponse] = {
        val reqstr = this.getRequestContent()
//        val reqstr = "[{\"id\": 2}]"
        val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
        val jsonItem = jsonList.head.asInstanceOf[JsonAST.JObject]
        val tid = jsonItem.values("id").asInstanceOf[BigInt].toLong
        val item = if (tid == -1)
            metaModel.newInstance()
        else
            metaModel.findOneInstance(tid)
        if(item != null) {
            val result = JObject(JField("typeName", this.getAllNamesByModelInstanceAsJsonValue(item))
                                 ::
                                 Nil
            )
            Full(JsonResponse(result))
        } else {
            Full(NotFoundResponse())
        }
    }

    private def optionGroup() : Box[LiftResponse] = {
//        val reqstr = this.getRequestContent()
        val reqstr = "[{\"id\": 2}]"
        val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
        val jsonItem = jsonList.head.asInstanceOf[JsonAST.JObject]
        val tid = jsonItem.values("id").asInstanceOf[BigInt].toLong
        val item = metaModel.findOneInstance(tid)
        if(item != null) {
            val result = JObject(JField("optionGroup", this.getAllOptionGroupsByModelInstanceAsJsonValue(item))
                                 ::
                                 Nil
            )
            Full(JsonResponse(result))
        } else {
            Full(NotFoundResponse())
        }
    }
}
