/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.controller

//import com.ding.controller._
import net.liftweb.json._
import net.liftweb.json.JsonAST._
import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.util.Helpers._
import com.ding.model.lift.Administrator

object AdminCertificationController extends BaseController{

    override def processAction(action : String) : Box[LiftResponse] = {
        action match {
            case "login" => {
                    loginProcess()
            }
            case _ => {
                    Full(NotFoundResponse())
            }
        }
    }

    private def loginProcess() = {
        if(Administrator.notLoggedIn_?) {
            Administrator.login
//            println(Administrator.loggedIn_?)
        }
        Full(OkResponse())
    }
}
