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

object OptionGroupController extends Controller[OptionGroup] {
    
    override def metaModel = MetaModels.metaOptionGroup

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
            case "querygroup" => {
                    queryGroup()
            }
            case "querygroupsave" => {
                    queryGroupSave()
            }
            case "querygroupremove" => {
                    queryGroupRemove()
            }
//            case "queryvalue" => {
//                    queryValue()
//            }
//            case "queryvaluesave" => {
//                    queryValueSave()
//            }
//            case "queryvalueremove" => {
//                    queryValueRemove()
//            }
            case "querygroupchangeorder" => {
                    queryGroupChangeOrder()
            }
            case _ => Full(NotFoundResponse())
        }
    }

    private def explore() : Box[LiftResponse] = {
        val reqstr = this.getRequestContent
//        val reqstr = "[{\"start\":1, \"end\":5}]"
        val startstr = urlDecode(S.param("offset").openOr("0"))
        val limitstr = urlDecode(S.param("limit").openOr("50"))

        try{
//            val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
//            val jsonObj = jsonList.head.asInstanceOf[JObject]
//            val start = jsonObj.values("start").asInstanceOf[BigInt]
//            val end = jsonObj.values("end").asInstanceOf[BigInt]
            val start = startstr.toInt + 1
            val end = start + limitstr.toInt - 1
            val langId = this.getDefaultLang()
            val allList = metaModel.findAllInstances
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
//                        ++
//                        JsonAST.JField("value", this.getAllValuesByGroupInstanceAsJsonValue(item))
                    )
                }
            }
            Full(JsonResponse(JsonAST.JObject(
                        JField("total", JInt(total))::JField("group", JArray(resultList))::Nil
                    )))
        }
    }

    private def queryGroup() : Box[LiftResponse] = {
        val reqstr = this.getRequestContent()
//        val reqstr = "[{\"id\": -1}]"
        try {
            val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
            val jsonItem = jsonList.head.asInstanceOf[JsonAST.JObject]
            val og_id = jsonItem.values("id").asInstanceOf[BigInt].toLong

            val item = if (og_id == -1)
                metaModel.newInstance()
            else
                metaModel.findOneInstance(og_id)
            val displayOrder : Int = if(og_id == -1) 0 else item.getDisplayOrder
            val result = JObject(JField("id", JArray(JObject(JField("id", JInt(og_id))::Nil)::Nil))
                                 ::
                                 JField("displayOrder", JArray(JObject(JField("displayOrder", JInt(displayOrder))::Nil)::Nil))
                                 ::
                                 JField("optionGroupDetail", this.getAllNamesByGroupInstanceAsJsonValue(item))
//                                 ::
//                                 JField("value", this.getAllValuesByGroupInstanceAsJsonValue(item))
                                 ::
                                 Nil
            )
            Full(JsonResponse(result))
        }
    }

    private def queryGroupSave() : Box[LiftResponse] = {
        val reqstr = this.getRequestContent()
//        val reqstr = "[{\"id\":-1, \"displayOrder\":100, \"optionGroupDetail\":[{\"langId\":22, \"name\":\"测试选项组1\"}]}]"
        try {
            val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
            val jsonItem = jsonList.head.asInstanceOf[JsonAST.JObject]
            val og_id = jsonItem.values("id").asInstanceOf[BigInt].toLong
            val item = if(og_id == -1)
                metaModel.newInstance()
            else
                metaModel.findOneInstance(og_id)
            if(item != null) {
//                item.setDisplayOrder(jsonItem.values("displayOrder").asInstanceOf[BigInt].toInt)
                val og_details : List[Map[String, Object]] = jsonItem.values("optionGroupDetail").asInstanceOf[List[Map[String, Object]]]
                og_details.foreach(
                    og_detail => {
                        val lang_id = og_detail("langId").asInstanceOf[BigInt].toLong
                        val name = og_detail("name").asInstanceOf[String]
                        if(name != null && name.length > 0)
                            item.setName(lang_id, name)
                        else {
                            
                        }
                    }
                )
                item.saveInstance()
                explore()
            } else {
                Full(BadResponse())
            }
        }
    }

    private def queryGroupRemove() : Box[LiftResponse] = {
        val reqstr = this.getRequestContent()
//        val reqstr = "[{\"id\": 6},{\"id\": 5}]"
        try {
            val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
            jsonList.foreach(
                jsonItem => {
                    val og_id = jsonItem.asInstanceOf[JsonAST.JObject].values("id").asInstanceOf[BigInt].toLong
                    val item = metaModel.findOneInstance(og_id)
                    if(item != null)
                        item.deleteInstance()
                }
            )
//            val jsonItem = jsonList.head.asInstanceOf[JsonAST.JObject]
//            val og_id = jsonItem.values("id").asInstanceOf[BigInt].toLong
//            val item = metaModel.findOneInstance(og_id)
//            if(item != null)
//                item.deleteInstance()
            explore()
        }
    }

    private def queryGroupChangeOrder() : Box[LiftResponse] = {
//        val reqstr = this.getRequestContent()
        val reqstr = "[{\"beforeId\":57, \"insert\":[{\"id\":1},{\"id\":2},{\"id\":3},{\"id\":4}]}]"
        try {
            val jsonList : List[JValue] = JsonParser.parse(reqstr).asInstanceOf[JArray].arr
            val jsonObj = jsonList.head.asInstanceOf[JObject]
            /*
             * 中轴项ID，在此项之前插入
             */
            val beforeId = jsonObj.values("beforeId").asInstanceOf[BigInt].toLong
            /*
             * 所有要插入项的ID
             */
            val insertIdList = jsonObj.values("insert").asInstanceOf[List[Map[String, Object]]].flatMap {
                jinsert => {
                    val insertId = jinsert("id").asInstanceOf[BigInt].toLong
                    List(insertId)
                }
            }
            val otherList = this.metaModel.findAllInstances.filterNot({
                    item => {
                        if (insertIdList.contains(item.getID()))
                            true
                        else
                            false
                    }
                })
            val beforePos = otherList.findIndexOf(
                item => {
                    item.getID == beforeId
                }
            )
            /*
             * 插入项之前所有项的列表
             */
            val beforeList = otherList.take(beforePos)
            /*
             * 插入项之后所有项的列表
             */
            val afterList = otherList.takeRight(otherList.length - beforePos -1)
            /*
             * 插入项的列表，包括中轴项，在列表最后一位
             */
            val insertList = insertIdList.flatMap {
                id => {
                    List(this.metaModel.findOneInstance(id))
                }
            } ::: List(this.metaModel.findOneInstance(beforeId))
            object pos extends RequestVar[Int](1)
            beforeList.foreach {
                item => {
                    item.setDisplayOrder(pos.is)
                    item.saveInstance()
                    pos(pos.is + 1)
                }
            }
            insertList.foreach {
                item => {
                    item.setDisplayOrder(pos.is)
                    item.saveInstance()
                    pos(pos.is + 1)
                }
            }
            afterList.foreach {
                item => {
                    item.setDisplayOrder(pos.is)
                    item.saveInstance()
                    pos(pos.is + 1)
                }
            }
            explore()
        }
//        Empty
    }

    private def queryValue() : Box[LiftResponse] = {
//        val reqstr = "[{\"id\": 1}]"
        val reqstr = this.getRequestContent()
        try {
            val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
            val jsonItem = jsonList.head.asInstanceOf[JsonAST.JObject]
            val ov_id = jsonItem.values("id").asInstanceOf[BigInt].toLong

            val item = if (ov_id == -1)
                MetaModels.metaOptionValue.newInstance()
            else
                MetaModels.metaOptionValue.findOneInstance(ov_id)
            val displayOrder : Int = if(ov_id == -1) 0 else item.getDisplayOrder
            val supportLangs : List[Language] = MetaModels.metaLanguage.findAllInstances
            val optionValueDetail : List[JsonAST.JValue] = supportLangs.flatMap {
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

            val result = JObject(JField("id", JInt(ov_id))
                                 ::
                                 JField("displayOrder", JInt(displayOrder))
                                 ::
                                 JField("optionValueDetail", JArray(optionValueDetail))
                                 ::
                                 Nil
            )
            Full(JsonResponse(JArray(result::Nil)))
        }
    }

    private def queryValueSave() : Box[LiftResponse] = {
        val reqstr = this.getRequestContent
//        val reqstr = "[{  \"id\":1,  \"displayOrder\":1,  \"optionValueDetail\":[{    \"langId\":22,    \"header\":\"中国\",    \"code\":\"cn\",    \"name\":\"11米\"  },{    \"langId\":23,    \"header\":\"America\",    \"code\":\"us\",    \"name\":\"11M\"  }]}]"
        try {
            val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
            val jsonItem = jsonList.head.asInstanceOf[JsonAST.JObject]
            val ov_id = jsonItem.values("id").asInstanceOf[BigInt].toLong
            val og_id = jsonItem.values("groupId").asInstanceOf[BigInt].toLong
            val item = if(ov_id == -1)
                MetaModels.metaOptionValue.newInstance()
            else
                MetaModels.metaOptionValue.findOneInstance(ov_id)
            if (item != null) {
                if(ov_id == -1)
                    item.setGroupID(og_id)
                val ov_details : List[Map[String, Object]] = jsonItem.values("optionValueDetail").asInstanceOf[List[Map[String, Object]]]
                ov_details.foreach(
                    ov_detail => {
                        val lang_id = ov_detail("langId").asInstanceOf[BigInt].toLong
                        val name = ov_detail("name").asInstanceOf[String]
//                        if(name != null && name.length != 0)
                            item.setName(lang_id, name)
                    }
                )
                item.saveInstance()
                val gid = item.getGroupID()
                Full(JsonResponse(this.getAllValuesByGroupIdAsJsonValue(gid)))
            } else {
                Full(BadResponse())
            }
        }
    }

    private def queryValueRemove() : Box[LiftResponse] = {
        val reqstr = this.getRequestContent
        try {
            val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
            val item = MetaModels.metaOptionValue.findOneInstance(jsonList.head.asInstanceOf[JObject].values("id").asInstanceOf[BigInt].toLong)
            val og_id =
                if (item != null)
                    item.getGroupID()
            else
                -1
            jsonList.foreach(
                item => {
                    val ov_id = item.asInstanceOf[JObject].values("id").asInstanceOf[BigInt].toLong
                    val ov_item = MetaModels.metaOptionValue.findOneInstance(ov_id)
                    if(ov_item != null)
                        ov_item.deleteInstance()
                }
            )
            Full(JsonResponse(this.getAllValuesByGroupIdAsJsonValue(og_id)))
        }
    }

    private def getAllValuesByGroupIdAsJsonValue(gid : Long) : JsonAST.JValue = {
        val og_item = MetaModels.metaOptionGroup.findOneInstance(gid)
        val values = if(og_item != null) og_item.allValues().flatMap {
            value_item => {
                val vid = value_item.getID()
                val vname = value_item.getName(this.getDefaultLang())
                List(JsonAST.JField("id", JInt(vid))
                     ++
                     JsonAST.JField("name", JString(vname))
                     ++
                     JField("type", JString("value"))
                )
            }
        } else {
            Nil
        }
        JArray(values)
    }
    
    private def getAllValuesByGroupInstanceAsJsonValue(item : OptionGroup) : JsonAST.JValue = {
        val values = item.allValues().flatMap {
            value_item => {
                val vid = value_item.getID()
                val vname = value_item.getName(this.getDefaultLang())
                List(JsonAST.JField("id", JInt(vid))
                     ++
                     JsonAST.JField("name", JString(vname))
                     ++
                     JField("type", JString("value"))
                )
            }
        }
        JArray(values)
    }

    private def getAllNamesByGroupInstanceAsJsonValue(item : OptionGroup) : JsonAST.JValue = {
        val supportLangs : List[Language] = MetaModels.metaLanguage.findAllInstances
        val optionGroupDetail : List[JsonAST.JValue] = supportLangs.flatMap {
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
        JArray(optionGroupDetail)
    }

    override def getRequestContent() = {
        urlDecode(S.param("json").openOr(""))
    }
}
