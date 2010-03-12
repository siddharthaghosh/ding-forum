/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.controller.admin

import net.liftweb.http._
import net.liftweb.common._
import net.liftweb.json._
import com.ding.model._
import com.ding.model.lift._
import com.ding.controller._
import net.liftweb.util._
import net.liftweb.json.JsonAST._
import com.ding.util._

object LanguageController {

    def metaModel : MetaLanguage = LiftLanguage

    def process() : Box[LiftResponse] = {
        ShopLogger.debug("Language Controller works")
        processAction(reqInfo.is.action)
    }

    def processAction(action : String) : Box[LiftResponse] = {

        action match {
//            case "add" => {
//                    add()
//                }
            case "save" => {
                    save()
                }
//            case "delete" => {
//                    delete()
//                    Full(OkResponse())
//                }
            case "list" => {
                    list()
            }
            case "listsupport" => {
                    listSupport()
            }
            case _ => Full(NotFoundResponse())
        }

    }
    
    private def list() : Box[LiftResponse] = {
        val allInsList = metaModel.findAllInstances
        val resultList = allInsList.flatMap {
            case item : Language => {
                    List(JsonAST.JField("id", JsonAST.JInt(item.getID()))
                         ++
                         JsonAST.JField("name", JsonAST.JString(item.getName()))
                         ++
                         JsonAST.JField("code", JsonAST.JString( /*"i18n/flags/" +*/ item.getCode()))
                         ++
                         JsonAST.JField("displayOrder", JsonAST.JInt(item.getDisplayOrder()))
                    )
                }
        }

        Full(JsonResponse(
                JsonAST.JArray(resultList)
            ))
    }
    
    private def listSupport() : Box[LiftResponse] = {
        val resultList : List[JsonAST.JValue] = LangProps.langPropList.flatMap {
            case LangProperty(name, code, directory, image) => {
                    List(JsonAST.JField("name", JsonAST.JString(name))
                         ++
                         JsonAST.JField("code",JsonAST.JString(/*"i18n/flags/" +*/ code))
                    )
                }
        }
        Full(JsonResponse(
                JsonAST.JArray(resultList)
            ))
    }

    private def addItem(name : String, code : String, image : String, dir : String, display_order : Int) : Boolean = {
        if(metaModel.isLanguageExist(name))
            false
        else {
            //生成新实例
            val add_item : Language = metaModel.newInstance()
            //更新实例对象
            add_item.updateInstance(name, code, image, dir, display_order)
            //保存实例
            add_item.saveInstance()
        }
//        //生成新实例
//        val add_item : Language = metaModel.newInstance()
//        //更新实例对象
//        add_item.updateInstance(name, code, image, dir, display_order)
//        //保存实例
//        add_item.saveInstance()
    }

    private def editItem(id : Long, name : String, code : String, image : String, dir : String, display_order : Int) : Boolean = {
        val edit_item = metaModel.findOneInstance(id)
        if(edit_item != null){
            edit_item.updateInstance(name, code, image, dir, display_order)
            edit_item.saveInstance()
        } else {
            false
        }
    }

    private def save() = {
        /*
         * 从reqeust对象内读出信息
         */
        val reqbox = S.request
        val req = S.request.open_!
        val reqbody : Array[Byte] = req.body.openOr(Array())
        val jstr = new String(reqbody, "UTF-8")
        //val jstr = "[{\"id\":20, \"name\":\"America\",  \"code\":\"cc\",  \"image\":\"cn2\",  \"directory\":\"chinese2\",  \"displayOrder\":11, \"delete\":true}]"
        ShopLogger.debug(jstr)
        try {
            val jsonList : List[JsonAST.JValue] = JsonParser.parse(jstr).asInstanceOf[JsonAST.JArray].arr
            jsonList.foreach(
                json_item => {
                    val jobj : JObject = json_item.asInstanceOf[JObject]
                    val jid = jobj \ "id"
                    jid match {
                        case JField("id", JInt(id)) => {
                                val jname = jobj \ "name"
                                val jdisplay_order = jobj \ "displayOrder"
                                ShopLogger.logger.debug(jdisplay_order.toString)
                                val delete = jobj \ "delete"
                                (jname, jdisplay_order, delete) match {
                                    case (_, _, JField("delete", JBool(true))) => {
                                            deleteItem(id.toLong)
                                        }
                                    case (JField("name", JString(name)), JField("displayOrder", JInt(display_order)), JField("delete", JBool(false)))
                                        if(LangProps.findLangProperty(name) != null )
                                            => {
                                                val langProp= LangProps.findLangProperty(name)
                                                val dir = langProp.directory
                                                val code = langProp.code
                                                val image = langProp.image
                                                val edit_item = null
                                                if(id >= 0) {
                                                    editItem(id.toLong, name, code, image, dir, display_order.toInt)
                                                }
                                                else{
                                                    addItem(name, code, image, dir, display_order.toInt)
                                                }
                                            }
                                    case (_, _, _) => {}
                                }

                            }
                        case _ => {}
                    }
                }
            )
//            Full(OkResponse())
            list()
        }
        catch {
            case ex : Exception => {
                    ShopLogger.error(ex.getMessage)
                    ShopLogger.error(ex.getStackTraceString)
                    Full(BadResponse())
            }
        }
//        val item_id : Int = 5
//        val edit_item = metaModel.findOneInstance(item_id)
//        if (edit_item != null) {
//            edit_item.updateInstance( "chinese", "cc", "cn2", "chinese2", edit_item.getDisplayOrder() + 1 )
//            edit_item.saveInstance()
//        }
    }

    private def deleteItem(item_id : Long) {
        if(item_id < 0)
            return
        val del_item = metaModel.findOneInstance(item_id)
        if(del_item != null) {
            del_item.deleteInstance()
        }
    }
}
