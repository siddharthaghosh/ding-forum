/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.controller.admin

import com.ding.controller._
import com.ding.model._
import net.liftweb.http._
import net.liftweb.common._
import net.liftweb.util.Helpers._
import net.liftweb.json._
import net.liftweb.json.JsonAST._
import com.ding.util._

object OptionValueController extends ModelController[OptionValue]{

    override def metaModel = MetaModels.metaOptionValue

    override def processAction(action : String) : Box[LiftResponse] = {
        action match {
            case "" => explore()
            case "index" => explore()
            case "explore" => explore()
            case "queryvalue" => queryValue()
            case "queryvaluesave" => queryValueSave()
            case "queryvalueremove" => queryValueRemove()
            case "queryvaluechangeorder" => queryValueChangeOrder()
            case _ => Full(NotFoundResponse())
        }
    }

    private def explore() : Box[LiftResponse] = {
        val reqstr = this.getRequestContent()
//        val reqstr = "[{\"groupId\":1}]"
        try {
            val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
            val og_id = jsonList.head.asInstanceOf[JObject].values("groupId").asInstanceOf[BigInt].toLong
            Full(JsonResponse(JObject(JField("value",this.getAllValuesByGroupIdAsJsonValue(og_id))::Nil)))
        }
    }

    private def queryValue() : Box[LiftResponse] = {
        val reqstr = this.getRequestContent()
//        val reqstr = "[{\"id\": -1}]"
        try {
            val jsonList : List[JValue] = JsonParser.parse(reqstr).asInstanceOf[JArray].arr
            val ov_id = jsonList.head.asInstanceOf[JObject].values("id").asInstanceOf[BigInt].toLong
            val item = if (ov_id == -1)
                metaModel.newInstance()
            else
                metaModel.findOneInstance(ov_id)
            val displayOrder : Int = if(ov_id == -1) 0 else item.getDisplayOrder
            val result = JObject(JField("id", JArray(JObject(JField("id", JInt(ov_id))::Nil)::Nil))
                                 ::
                                 JField("displayOrder", JArray(JObject(JField("displayOrder", JInt(displayOrder))::Nil)::Nil))
                                 ::
                                 JField("optionValueDetail", this.getAllNamesByValueInstanceAsJsonValue(item))
//                                 ::
//                                 JField("value", this.getAllValuesByGroupInstanceAsJsonValue(item))
                                 ::
                                 Nil
            )
            Full(JsonResponse(result))
        }
    }

    private def queryValueSave() : Box[LiftResponse] = {
        val reqstr = this.getRequestContent()
//        val reqstr = "[{  \"id\":-1,  \"groupId\":2,\"displayOrder\":10,  \"optionValueDetail\":[{    \"langId\":22,    \"header\":\"中国\",    \"code\":\"cn\",    \"name\":\"中\"  },{    \"langId\":23,    \"header\":\"America\",    \"code\":\"us\",    \"name\":\"middle\"  }]}]"
        try {
            val jsonList : List[JValue] = JsonParser.parse(reqstr).asInstanceOf[JArray].arr
            val jitem = jsonList.head.asInstanceOf[JObject]
            val ov_id = jitem.values("id").asInstanceOf[BigInt].toLong
            val og_id = jitem.values("groupId").asInstanceOf[BigInt].toLong
            val item = if(ov_id == -1)
                MetaModels.metaOptionValue.newInstance()
            else
                MetaModels.metaOptionValue.findOneInstance(ov_id)
            if (item != null) {
                if(ov_id == -1)
                    item.setGroupID(og_id)
                val ov_details : List[Map[String, Object]] = jitem.values("optionValueDetail").asInstanceOf[List[Map[String, Object]]]
                ov_details.foreach(
                    ov_detail => {
                        val lang_id = ov_detail("langId").asInstanceOf[BigInt].toLong
                        val name = ov_detail("name").asInstanceOf[String]
                        if(name != null && name.length > 0)
                            item.setName(lang_id, name)
                    }
                )
                item.saveInstance()
            }
            Full(JsonResponse(JObject(JField("value",this.getAllValuesByGroupIdAsJsonValue(og_id))::Nil)))
        }
    }

    private def queryValueRemove() : Box[LiftResponse] = {
        val reqstr = this.getRequestContent()
//        val reqstr = "[{\"id\": 4},{\"id\": 5}]"
        try {
            val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
            
            val og_id = if(jsonList.length > 0){
                metaModel.findOneInstance(jsonList.head.asInstanceOf[JsonAST.JObject].values("id").asInstanceOf[BigInt].toLong).getGroupID
            } else {
                -1
            }
            
            jsonList.foreach(
                jsonItem => {
                    val ov_id = jsonItem.asInstanceOf[JsonAST.JObject].values("id").asInstanceOf[BigInt].toLong
                    val item = metaModel.findOneInstance(ov_id)
                    if(item != null)
                        item.deleteInstance()
                }
            )
            Full(JsonResponse(JObject(JField("value",this.getAllValuesByGroupIdAsJsonValue(og_id))::Nil)))
        }
    }

    private def queryValueChangeOrder() : Box[LiftResponse] = {
        val reqstr = this.getRequestContent()
//        val reqstr = ""
        try {
            val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
            val og_id = if(jsonList.length > 0){
                metaModel.findOneInstance(jsonList.head.asInstanceOf[JsonAST.JObject].values("id").asInstanceOf[BigInt].toLong).getGroupID
            } else {
                -1
            }

            jsonList.foreach(
                jsonItem => {
                    val ov_id = jsonItem.asInstanceOf[JsonAST.JObject].values("id").asInstanceOf[BigInt].toLong
                    val display = jsonItem.asInstanceOf[JObject].values("displayOrder").asInstanceOf[BigInt].toInt
                    val item = metaModel.findOneInstance(ov_id)
                    if(item != null) {
                        item.setDisplayOrder(display)
                        item.saveInstance
                    }
                        
                }
            )
            Full(JsonResponse(JObject(JField("value",this.getAllValuesByGroupIdAsJsonValue(og_id))::Nil)))
        }
    }

    private def getAllValuesByGroupIdAsJsonValue(gid : Long) : JsonAST.JValue = {
        val og_item = MetaModels.metaOptionGroup.findOneInstance(gid)
        val values = if(og_item != null) og_item.allValues().sortWith((v1, v2) => v1.getDisplayOrder <= v2.getDisplayOrder).flatMap {
            value_item => {
                val vid = value_item.getID()
                val vname = value_item.getName(this.getDefaultLang())
                List(JsonAST.JField("id", JInt(vid))
                     ++
                     JsonAST.JField("name", JString(vname))
//                     ++
//                     JField("type", JString("value"))
                )
            }
        } else {
            Nil
        }
        JArray(values)
    }

    private def getAllNamesByValueInstanceAsJsonValue(item : OptionValue) : JValue = {
        val supportLangs : List[Language] = MetaModels.metaLanguage.findAllInstances
        val optionValueDetail : List[JValue] = supportLangs.flatMap {
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
        JArray(optionValueDetail)
    }

    override def getRequestContent() = {
        urlDecode(S.param("json").openOr(""))
    }
}
