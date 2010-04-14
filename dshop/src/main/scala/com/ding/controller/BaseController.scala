/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.controller

import net.liftweb.http._
import net.liftweb.common._

abstract class BaseController {
    def process() : Box[LiftResponse] = {
        println("request url is " + S.request.open_!.uri)
//        println("param random value is " + S.request.open_!.params("drandom"))
        
        println("module is " + reqInfo.is.module + ", controller is " + reqInfo.is.application + ", action is " + reqInfo.is.action)

        processAction(reqInfo.is.action)
    }
    def processAction(action : String) : Box[LiftResponse]
}
