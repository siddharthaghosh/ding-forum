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
            case "edit" => {
                    edit()
                    Full(OkResponse())
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
                         JsonAST.JField("display_order", JsonAST.JInt(item.getDisplayOrder()))
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

    private def edit() {
        /*
         * 从reqeust对象内读出信息, 代码未实现
         */
        val jstr = "[{\"id\":16, \"name\":\"chinese\",  \"code\":\"cc\",  \"image\":\"cn2\",  \"directory\":\"chinese2\",  \"display_order\":9}]"
        val jsonList : List[JsonAST.JValue] = JsonParser.parse(jstr).asInstanceOf[JsonAST.JArray].arr
        jsonList.foreach(
            json_item => {
                val jobj : JObject = json_item.asInstanceOf[JObject]
                val childrenlist = jobj.children
                val find = childrenlist.find( child => {
                        val jf : JField = child.asInstanceOf[JField]
                        println(jf)
                        println(jf.name)
                        if(jf.name == "id")
                        {
                            println("found id property")
                            return true
                        }
                        return false
                    })
                println("pig!!!!!!!!!!!")
            }
        )
        val item_id : Int = 5
        val edit_item = metaModel.findOneInstance(item_id)
        if (edit_item != null) {
            edit_item.updateInstance( "chinese", "cc", "cn2", "chinese2", edit_item.getDisplayOrder() + 1 )
            edit_item.saveInstance()
        }
    }

    private def delete() {
        val item_id : Int = 4
        val del_item = metaModel.findOneInstance(item_id)
        if(del_item != null) {
            del_item.deleteInstance()
        }

    }
}
