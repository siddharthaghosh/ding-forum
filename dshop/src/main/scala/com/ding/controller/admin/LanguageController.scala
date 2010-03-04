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
            case _ => Full(NotFoundResponse())
        }

    }

    private def add() : Box[LiftResponse] = {

        /*
         * 从reqeust对象内读出信息, 代码未实现
         */

        //生成新实例
        val addRecord : Language = metaModel.newInstance()
        //更新实例对象
        addRecord.updateInstance("chinese", "cc", "cn2", "chinese2", 9)
        //保存实例
        addRecord.saveInstance()
        val results : List[JsonAST.JValue] = (
            JsonAST.JField("lang_id", JsonAST.JInt(addRecord.getID()))
            ++
            JsonAST.JField("lang_name", JsonAST.JString(addRecord.getName()))
            ++
            JsonAST.JField("lang_code", JsonAST.JString(addRecord.getCode()))
            ++
            JsonAST.JField("lang_image", JsonAST.JString(addRecord.getImage()))
            ++
            JsonAST.JField("lang_directory", JsonAST.JString(addRecord.getDirectory()))
            ++
            JsonAST.JField("lang_display_order", JsonAST.JInt(addRecord.getDisplayOrder()))
        ) :: (
            JsonAST.JField("lang_id", JsonAST.JInt(addRecord.getID()))
            ++
            JsonAST.JField("lang_name", JsonAST.JString(addRecord.getName()))
            ++
            JsonAST.JField("lang_code", JsonAST.JString(addRecord.getCode()))
            ++
            JsonAST.JField("lang_image", JsonAST.JString(addRecord.getImage()))
            ++
            JsonAST.JField("lang_directory", JsonAST.JString(addRecord.getDirectory()))
            ++
            JsonAST.JField("lang_display_order", JsonAST.JInt(addRecord.getDisplayOrder()))
        ) :: Nil
        Full(JsonResponse(
                JsonAST.JArray(results)
            )
        )
    }

    private def edit() {
        /*
         * 从reqeust对象内读出信息, 代码未实现
         */
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
