/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.controller.admin

import com.ding.controller._
import com.ding.util.ShopLogger
import net.liftweb.common._
import net.liftweb.http._
import com.ding.model._
import net.liftweb.json._
import net.liftweb.json.JsonAST._

object OptionController extends Controller[OptionGroup] {
    
    override def metaModel = MetaModels.metaOptionGroup

    override def processAction(action : String) : Box[LiftResponse] = {
        action match {
            case "explore" => {
                    explore()
            }
            case "querygroup" => {
                    queryGroup()
            }
            case "queryvalue" => {
                    queryValue()
            }
            case "changedisplayorder" => {
                    Full(OkResponse())
                }
            case _ => Full(NotFoundResponse())
        }
    }

    private def explore() : Box[LiftResponse] = {
        try{
            val langId = this.getDefaultLang()
            val itemList = metaModel.findAllInstances
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
            Full(JsonResponse(JsonAST.JArray(resultList)))
        }
//        Full(OkResponse())
    }

    private def queryGroup() : Box[LiftResponse] = {
//        val reqstr = this.getRequestContent()
        val reqstr = "[{\"id\": 1}]"
        ShopLogger.logger.debug(reqstr)
        try {
            val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
            val jsonItem = jsonList.head.asInstanceOf[JsonAST.JObject]
            val og_id = jsonItem.values("id").asInstanceOf[BigInt].toLong

            val item = if (og_id == -1)
                metaModel.newInstance()
            else
                metaModel.findOneInstance(og_id)
            val displayOrder : Int = if(og_id == -1) 0 else item.getDisplayOrder

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
            val value : List[JsonAST.JValue] = item.allValues().flatMap {
                value_item => {
                    val vid = value_item.getID()
                    val vname = value_item.getName(this.getDefaultLang())
                    List(JsonAST.JField("id", JInt(vid))
                         ++
                         JsonAST.JField("name", JString(vname))
                    )
                }
            }
            val result = JObject(JField("id", JInt(og_id))
                                 ::
                                 JField("displayOrder", JInt(displayOrder))
                                 ::
                                 JField("optionGroupDetail", JArray(optionGroupDetail))
                                 ::
                                 JField("value", JArray(value))
                                 ::
                                 Nil
            )
            Full(JsonResponse(JArray(result::Nil)))
        }
//        Empty
    }

    private def queryValue() : Box[LiftResponse] = {
        val reqstr = "[{\"id\": 1}]"
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
}
