/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import net.liftweb.mapper._
import com.ding.model._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._

class Administrator extends MegaProtoUser[Administrator] {
    def getSingleton = Administrator

}

object Administrator extends Administrator with MetaMegaProtoUser[Administrator] {
    override def dbTableName = "dshop_administrator"
    override def screenWrap = Full(<lift:surround with="default" at="content">
            <lift:bind /></lift:surround>)
    // define the order fields will appear in forms and output
    override def fieldOrder = List(id, firstName, lastName, email,
                                   locale, timezone, password)

    // comment this line out to require email validations
    override def skipEmailValidation = true

    override def homePage = "/gwtclient/eshop.jsp"
}