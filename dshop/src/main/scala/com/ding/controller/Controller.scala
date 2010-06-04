/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.controller

import net.liftweb.http._
import com.ding.model._
import net.liftweb.util._
import net.liftweb.util.Helpers._
import net.liftweb.http._
import net.liftweb.common._

trait Controller {
    def getDefaultLang() : Long
    def getRequestContent() : String
    def process() : Box[LiftResponse]
    def processAction(action : String) : Box[LiftResponse]
}

trait BaseController extends Controller {
    def getDefaultLang() = {
        MetaModels.metaLanguage.findAllInstances.head.getID
//        22
    }
//    def getDefaultLangCode() = "cn"
    def getDefaultLangCode() = {
        MetaModels.metaLanguage.findAllInstances.head.getCode
//        "cn"
    }
    def getRequestContent() : String = {
        /*
         *  从Request对象内读出请求信息
         *  [{cat_id : id_value}, {lang_id : id_value}]
         *  如果没有cat_id，表示请求所有顶层的category
         *  如果美欧lang_id， 表示使用默认语言
         */
        val req = S.request.open_!
        val reqbody : Array[Byte] = req.body.openOr(Array())
        val reqstr = new String(reqbody, "UTF-8")
        urlDecode(reqstr)
    }
    def process() : Box[LiftResponse] = {
        println("module is " + reqInfo.is.module + ", controller is " + reqInfo.is.application + ", action is " + reqInfo.is.action)

        processAction(reqInfo.is.action)
    }
}

abstract class ModelController [A <: Model] extends BaseController {
    type T = MetaModel[A]
    def metaModel : T
}
