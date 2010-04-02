/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.controller

import net.liftweb.http._
import net.liftweb.mapper._
import net.liftweb.common._
import net.liftweb.util.Helpers._
import com.ding.model._
import com.ding.model.lift._

object reqInfo extends RequestVar[RequestInfo]({new RequestInfo})

object FrontController {

    //requestPathProcess()

    val controller_func : LiftRules.DispatchPF = {
        case Req(List("admin", _*), _, _) => {
                () => dispathProcess()
            }
    }

    def setupController() {
        LiftRules.dispatch.append(controller_func)
    }

    private def requestPathProcess() {

        val req = S.request.open_!
        val path = req.path
        val partpath = path.partPath
        if(partpath.length < 2){
            reqInfo.is.module = ""
            reqInfo.is.application = ""
            reqInfo.is.action = ""
        }
        else{
            reqInfo.is.module = partpath(0)
            reqInfo.is.application = partpath(1)
            if(partpath.length < 3) {
                reqInfo.is.action = urlDecode(S.param("action").openOr(""))

            }else {
                reqInfo.is.action = partpath(2)
            }
        }
    }

    private def dispathProcess() : Box[LiftResponse] = {

        requestPathProcess()

        /*
         * authorization code here, next step
         */

        reqInfo.is.module match {
            case "admin" => adminProcess()
            case _ => Full(NotFoundResponse())
        }
        /*
         * authorization code here, next step
         */
//        val req = S.request.open_!
//        val path = req.path
//        val partpath = path.partPath
//
//        partpath(1) match {
//            case "language" => admin.LanguageController.process()
//            case _ => Full(NotFoundResponse())
//        }
    }

    private def adminProcess() : Box[LiftResponse] = {
        /*
         * authorization code here, next step
         */

        reqInfo.is.application match {
            case "localization" => admin.LanguageController.process()
            case "category" => admin.CategoryController.process()
            case "optiongroup" => admin.OptionGroupController.process()
            case "optionvalue" => admin.OptionValueController.process()
            case _ => Full(NotFoundResponse())
        }
    }

    private def addDocContent() : Box[LiftResponse] = {
        val reqbox = S.request
        val req = reqbox.open_!
        val reqbody : Array[Byte] = req.body.openOr(Array())
        val responsemsg = if (reqbody.isEmpty){
            "reqbody empty"
        }
        else{
            val doc_entry = LiftDocument.create.content(reqbody)
            doc_entry.save
            doc_entry.doc_id.toString
        }
        Full(InMemoryResponse((responsemsg.getBytes("UTF-8")), "Content-Type" -> "text/plain; charset=utf-8" :: Nil, Nil, 200))
    }
}
