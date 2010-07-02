/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.controller

import net.liftweb.http._
import net.liftweb.mapper._
import net.liftweb.common._
import net.liftweb.util.Helpers._
import net.liftweb.json._
import net.liftweb.json.JsonAST._
import com.ding.model._
import com.ding.model.lift._
import com.ding.util._
import net.liftweb.sitemap._

object reqInfo extends RequestVar[RequestInfo]({new RequestInfo})
//object loginInfo extends SessionVar[String]({"notlogin"})

object FrontController {

    //requestPathProcess()

    val controller_func : LiftRules.DispatchPF = {

        case Req("client"::"admin_client"::"eshop"::Nil, _, _) if Administrator.notLoggedIn_? => {
                () => Full(RedirectResponse("/client/certificate_client/admin/login.html"))
            }
        
        case Req("client"::"admin_client"::_, _, _) if Administrator.notLoggedIn_? => {
                () => Full(ForbiddenResponse())
            }
//        case req @ Req("client"::"certificate_client"::_, _, _) => {
//                () => {
//                    val certLoc = Loc("certificate",
//                                      ("client"::"certificate_client"::Nil,true),
//                                      "Certificate"
//                    )
//                    val text = if(certLoc.link.isDefinedAt(req)) {"match"} else {"nomatch"}
//                    Full(InMemoryResponse(text.getBytes, ("Content-Type","text/html")::Nil, Nil,200))
//                }
//            }

        case Req(List("certificate", _*), _, _) => {
                () => dispathProcess()
            }

        case Req(List("admin", _*), _, _) => {
                () => dispathProcess()
            }

        case Req(List("image", _*), _, _) => {
                () => dispathProcess()
            }

        case Req(List("module", _*), _, _) => {
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
        if(partpath.length < 1) {
            reqInfo.is.module = ""
            reqInfo.is.application = ""
            reqInfo.is.action = ""
        }
        else if(partpath.length < 2){
            reqInfo.is.module = partpath(0)
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

//        if(loginInfo.is != "notlogin") {
//            Full(ForbiddenResponse("must login first"))
//        } else {
        requestPathProcess()

        /*
         * authorization code here, next step
         */

        reqInfo.is.module match {
            case "certificate" => certificateProcess()
            case "admin" => {
                    adminProcess()
                }
            case "image" => imageProcess()
//                case "module" => moduleProcess()
            case _ => Full(NotFoundResponse())
        }
//        }
    }

    private def adminProcess() : Box[LiftResponse] = {
        /*
         * authorization code here
         */

        reqInfo.is.application match {
            case "localization" => admin.LanguageController.process()
            case "product" => admin.CategoryController.process()
            case "optiongroup" => admin.OptionGroupController.process()
            case "optionvalue" => admin.OptionValueController.process()
            case "manufacturer" => admin.ManufacturerController.process()
            case "filemanager" => admin.FileManagerController.process()
            case "module" => admin.ClientModuleController.process()
            case "type" => admin.TypeController.process()
            case "measurement" => admin.MeasurementCoontroller.process()
            case _ => Full(NotFoundResponse())
        }
    }

    private def certificateProcess() : Box[LiftResponse] = {
        reqInfo.is.application match {
            case "admin" => AdminCertificationController.process()
            case _ => Full(NotFoundResponse())
        }
    }

    private def imageProcess() : Box[LiftResponse] = {
        ShopLogger.logger.debug("image process controller works!")
        reqInfo.is.application match {
            case "origin" => ImageController.process()
            case "thumbnail" => ThumbNailController.process()
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
