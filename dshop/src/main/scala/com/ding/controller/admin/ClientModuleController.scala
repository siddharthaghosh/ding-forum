/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.controller.admin

import com.ding.controller._
import net.liftweb.json._
import net.liftweb.json.JsonAST._
import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.util.Helpers._

object ClientModuleController extends BaseController {
    
    private val modules = Array("localization", "product", "fileManager")
    private val apps = Array(Array("language"), Array("product", "option", "manufacturer", "productType", "measurement"), Array("fileManager"))

    override def processAction(action : String) : Box[LiftResponse] = {
        action match {
            case "entry" => {
                    appExplore()
                }
            case _ => {
                    moduleExplore()
                }
        }
    }

    private def moduleExplore() = {
        var i : Int = 0
        val moduleList = this.modules.flatMap {
            module => {
                val entry = apps(i).flatMap {
                    app => {
                        JObject(JField("name", JString(app)) :: Nil) :: Nil
                    }
                }
                JField("entry", JArray(entry.toList))
                i = i + 1
                JObject(JField("id", JInt(i - 1)) :: JField("name", JString(module)) :: JField("entry", JArray(entry.toList)) :: Nil) :: Nil
            }
        }
//        val jsonArr = JArray(JObject(JField("id", JInt(0)) :: JField("name", JString("localization")) :: Nil) ::
//                             JObject(JField("id", JInt(1)) :: JField("name", JString("product")) :: Nil) ::
//                             JObject(JField("id", JInt(2)) :: JField("name", JString("fileManager")) :: Nil) ::
//                             Nil)
        val jsonArr = JArray(moduleList.toList)
        val result = JObject(JField("module", jsonArr) :: Nil)
        Full(JsonResponse(result))
    }

    private def appExplore() = {
        val id = this.getIdFromResquest()
//        val id = 2
        val appArr = apps(id.toInt)
        val appList = appArr.flatMap {
            app => {
                JObject(JField("name", JString(app)) :: Nil) :: Nil
            }
        }
        val result = JObject(JField("entry", JArray(appList.toList)) :: Nil)
        Full(JsonResponse(result))
    }

    private def getIdFromResquest() : Long = {
        val reqstr = this.getRequestContent()
        try {
            val jsonList = JsonParser.parse(reqstr).asInstanceOf[JArray].arr
            val jsonObj = jsonList.head.asInstanceOf[JObject]
            val id = jsonObj.values("id").asInstanceOf[BigInt].toLong
            id
        }
    }

    override def getRequestContent() = {
        urlDecode(S.param("json").openOr(""))
    }

}
