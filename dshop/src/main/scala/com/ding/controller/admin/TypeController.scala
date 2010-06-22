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
            case "exploreSpec" => {
                    exploreSpec()
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
            case "addoptiongroup" => {
                    addOptionGroup()
                }
            case "removeoptiongroup" => {
                    removeOptionGroup()
                }
            case "parameter" => {
                    parameter()
                }
            case "saveparameter" => {
                    saveParameter()
                }
            case "property" => {
                    property()
                }
            case "saveproperty" => {
                    saveProperty()
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

    private def exploreSpec() : Box[LiftResponse] = {
        val reqstr = this.getRequestContent()
        val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
        val jsonItem = jsonList.head.asInstanceOf[JsonAST.JObject]
        val tid = jsonItem.values("id").asInstanceOf[BigInt].toLong
        val item = metaModel.findOneInstance(tid)
        if(item != null) {
            val id = item.getID
            val name = item.getName(this.getDefaultLang)
            val resultArr = JArray(JObject(
                    JsonAST.JField("id", JsonAST.JInt(id))
                    ::
                    JsonAST.JField("name", JsonAST.JString(name))
                    :: Nil
                ) :: Nil)
            Full(JsonResponse(JsonAST.JObject(
                        JField("total", JInt(1))::JField("type", resultArr)::Nil
                    )))
        } else {
            Full(NotFoundResponse())
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
        val reqstr = this.getRequestContent()
//        val reqstr = "[{\"id\": 9}]"
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

    private def addOptionGroup() : Box[LiftResponse] = {
        val reqstr = this.getRequestContent()
//        val reqstr = "[{\"typeId\": 9, \"optionGroupId\": 2}]"
        val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
        val jsonItem = jsonList.head.asInstanceOf[JsonAST.JObject]
        val tid = jsonItem.values("typeId").asInstanceOf[BigInt].toLong
        val oid = jsonItem.values("optionGroupId").asInstanceOf[BigInt].toLong
        val item = metaModel.findOneInstance(tid)
        if(item != null) {
            item.addSupportOptionGroup(oid)
            val result = JObject(JField("optionGroup", this.getAllOptionGroupsByModelInstanceAsJsonValue(item))
                                 ::
                                 Nil
            )
            Full(JsonResponse(result))
        } else {
            Full(NotFoundResponse())
        }
//        Full(OkResponse())
    }

    private def removeOptionGroup() : Box[LiftResponse] = {
        val reqstr = this.getRequestContent()
//        val reqstr = "[{\"typeId\": 9, \"optionGroupId\": 2}]"
        val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
        val jsonItem = jsonList.head.asInstanceOf[JsonAST.JObject]
        val tid = jsonItem.values("typeId").asInstanceOf[BigInt].toLong
        val oids = jsonItem.values("optionGroup").asInstanceOf[List[Map[String, _]]]
        val item = metaModel.findOneInstance(tid)
        if(item != null) {
//            item.removeSupportOptionGroup(oids)
            oids.foreach {
                oid => {
                    val id = oid("id").asInstanceOf[BigInt].toLong
                    item.removeSupportOptionGroup(id)
                }
            }
            val result = JObject(JField("optionGroup", this.getAllOptionGroupsByModelInstanceAsJsonValue(item))
                                 ::
                                 Nil
            )
            Full(JsonResponse(result))
        } else {
            Full(NotFoundResponse())
        }
    }

    private def parameter() : Box[LiftResponse] = {
        val reqstr = this.getRequestContent()
//        val reqstr = "[{\"id\": 1}]"
        val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
        val jsonItem = jsonList.head.asInstanceOf[JsonAST.JObject]
        val tid = jsonItem.values("id").asInstanceOf[BigInt].toLong
        val item = metaModel.findOneInstance(tid)
        if(item != null) {
            val code = this.getDefaultLangCode
            val result = JsonParser.parse(item.getParameters)
            val resultj = JArray(JString(code) :: result :: Nil)
            Full(JsonResponse(resultj))
        } else {
            val code = this.getDefaultLangCode
            val result = JsonParser.parse("[]")
            val resultj = JArray(JString(code) :: result :: Nil)
            Full(JsonResponse(resultj))
        }
    }

    private def saveParameter() : Box[LiftResponse] = {
        val reqstr = this.getRequestContent()
//        val pastr = "[{\"id\": 1}]"
//        val pj = JField("parameter", JString(pastr))
//        val idj = JField("id", JInt(1))
//        val reqj = JArray(JObject(idj :: pj :: Nil) :: Nil)
//        val jstr = Printer.pretty(JsonAST.render(reqj))
//        println(jstr)
//        val reqstr = jstr
        val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
        val jsonItem = jsonList.head.asInstanceOf[JsonAST.JObject]
        val tid = jsonItem.values("id").asInstanceOf[BigInt].toLong
        val paramstr = jsonItem.values("parameter").asInstanceOf[String]
        val item = metaModel.findOneInstance(tid)
        if(item != null) {
            val pvalue = if(paramstr.length > 0) {
                paramstr
            } else
            {
                "[]"
            }
            item.setParameters(pvalue)
            item.saveInstance
            parameter()
        } else {
            Full(NotFoundResponse())
        }
    }

    private def property() : Box[LiftResponse] = {
        val reqstr = this.getRequestContent()
//        val reqstr = "[{\"id\": 1}]"
        val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
        val jsonItem = jsonList.head.asInstanceOf[JsonAST.JObject]
        val tid = jsonItem.values("id").asInstanceOf[BigInt].toLong
        val item = metaModel.findOneInstance(tid)
        if(item != null) {
            val code = this.getDefaultLangCode
            val result = JsonParser.parse(item.getProperties)
            val resultj = JArray(JString(code) :: result :: Nil)
            Full(JsonResponse(resultj))
        } else {
            val code = this.getDefaultLangCode
            val result = JsonParser.parse("[]")
            val resultj = JArray(JString(code) :: result :: Nil)
            Full(JsonResponse(resultj))
        }
    }

    private def saveProperty() : Box[LiftResponse] = {
        val reqstr = this.getRequestContent()
        val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
        val jsonItem = jsonList.head.asInstanceOf[JsonAST.JObject]
        val tid = jsonItem.values("id").asInstanceOf[BigInt].toLong
        val propstr = jsonItem.values("property").asInstanceOf[String]
        val item = metaModel.findOneInstance(tid)
        if(item != null) {
            val pvalue = if(propstr.length > 0) {
                propstr
            } else
            {
                "[]"
            }
            item.setProperties(pvalue)
            item.saveInstance
            property()
        } else {
            Full(NotFoundResponse())
        }
    }
}
