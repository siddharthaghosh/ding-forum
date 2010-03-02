/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.controller.admin

import net.liftweb.http._
import net.liftweb.common._
import com.ding.model._

object LanguageController {

    val langFactory : MetaLanguage = LiftLanguage

    def process() : Box[LiftResponse] = {
        println("language controller works")
        add()
        Full(OkResponse())
    }

    def findAction() {
        
    }

    def add() {

        val addRecord : Language = langFactory.newInstance()
        addRecord.updateInstance("chinese", "cc", "cn2", "chinese2", 9)
        addRecord.saveInstance()
        
    }

    def edit() {

    }

    def delete() {
        
    }
}
