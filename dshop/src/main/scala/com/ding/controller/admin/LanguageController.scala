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
        println("language controller works")
        processAction(reqInfo.is.action)
    }

    def processAction(action : String) : Box[LiftResponse] = {

        action match {
            case "add" => {
                    add()
                }
            case "save" => {
                    edit()
                }
            case "delete" => {
                    delete()
                    Full(OkResponse())
                }
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
                         JsonAST.JField("image", JsonAST.JString( "i18n/flags/" + item.getImage()))
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
                         JsonAST.JField("image",JsonAST.JString(image))
                    )
                }
        }
        Full(JsonResponse(
                JsonAST.JArray(resultList)
            ))
    }

    private def add() : Box[LiftResponse] = {

        /*
         * 从reqeust对象内读出信息, 代码未实现
         */
        val currentReq = S.request.open_!
        println(currentReq.param("name"))
        //生成新实例
        val addRecord : Language = metaModel.newInstance()
        //更新实例对象
        addRecord.updateInstance("chinese", "cc", "cn2", "chinese2", 9)
        //保存实例
        addRecord.saveInstance()
        Full(OkResponse())
    }

    private def edit() = {
        /*
         * 从reqeust对象内读出信息
         */
        val reqbox = S.request
        val req = S.request.open_!
        val reqbody : Array[Byte] = req.body.openOr(Array())
        //val jstr = new String(reqbody, "UTF-8")
        val jstr = "[{\"id\":20, \"name\":\"America\",  \"code\":\"cc\",  \"image\":\"cn2\",  \"directory\":\"chinese2\",  \"displayOrder\":11, \"delete\":true}]"
        println(jstr + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        val jsonList : List[JsonAST.JValue] = JsonParser.parse(jstr).asInstanceOf[JsonAST.JArray].arr
        jsonList.foreach(
            json_item => {
                val jobj : JObject = json_item.asInstanceOf[JObject]
                val jid = jobj \ "id"
                jid match {
                    case JField("id", JInt(id)) => {
                            val jname = jobj \ "name"
                            val jdisplay_order = jobj \ "displayOrder"
                            val delete = jobj \ "delete"
                            (jname, jdisplay_order, delete) match {
                                case (_, _, JField("delete", JBool(true))) => {
                                        if (id >= 0) {
                                            val del_item = metaModel.findOneInstance(id.toInt)
                                            if(del_item != null){
                                                println("delete the lang item id:" + id)
                                                del_item.deleteInstance()
                                            }
                                        }
                                }
                                case (JField("name", JString(name)), JField("displayOrder", JInt(display_order)), JField("delete", JBool(false)))
                                    if(LangProps.findLangProperty(name) != null )
                                        => {
                                        val langProp= LangProps.findLangProperty(name)
                                        val dir = langProp.directory
                                        val code = langProp.code
                                        val image = langProp.image
                                        val edit_item = if(id >= 0) {
                                            metaModel.findOneInstance(id.toInt)
                                        }
                                        else{
                                            println("create new one")
                                            metaModel.newInstance()
                                        }
                                        if (edit_item != null) {
                                            edit_item.updateInstance( name , code, image, dir, display_order.toInt )
                                            edit_item.saveInstance()
                                        }
                                }
                            }
                            
                    }
                    case _ => {}
                }
            }
        )
//        val item_id : Int = 5
//        val edit_item = metaModel.findOneInstance(item_id)
//        if (edit_item != null) {
//            edit_item.updateInstance( "chinese", "cc", "cn2", "chinese2", edit_item.getDisplayOrder() + 1 )
//            edit_item.saveInstance()
//        }
        list()
    }

    private def delete() {
        val item_id : Int = 4
        val del_item = metaModel.findOneInstance(item_id)
        if(del_item != null) {
            del_item.deleteInstance()
        }

    }
}
