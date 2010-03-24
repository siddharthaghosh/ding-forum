/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.controller

import net.liftweb.http._
import com.ding.model._
import net.liftweb.util._
import net.liftweb.http._
import net.liftweb.common._

abstract class Controller [A <: Model] {
    type T = MetaModel[A]
    def metaModel : T
    private def getDefaultLang() = 22
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
        reqstr
    }
    def process() : Box[LiftResponse] = {
        processAction(reqInfo.is.action)
    }
    def processAction(action : String) : Box[LiftResponse]
}
