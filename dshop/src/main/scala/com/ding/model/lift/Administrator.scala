/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import net.liftweb.mapper._
import com.ding.model._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.net.liftweb.http._
import _root_.net.liftweb.util.Helpers._

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

    override def homePage = "/client/admin_client/eshop.html"

    override def login = {
        if (S.post_?) {
            S.param("username").
            flatMap(username => getSingleton.find(By(email, username))) match {
                case Full(user) if user.validated &&
                    user.password.match_?(S.param("password").openOr("*")) =>
                    S.notice(S.??("logged.in"))
                    logUserIn(user)
                    //S.redirectTo(homePage)
                    val redir = loginRedirect.is match {
                        case Full(url) =>
                            loginRedirect(Empty)
                            url
                        case _ =>
                            homePage
                    }
//                    S.redirectTo(redir)

                case Full(user) if !user.validated =>
                    S.error(S.??("account.validation.error"))

                case _ => S.error(S.??("invalid.credentials"))
            }
        }

        bind("user", loginXhtml,
             "email" -> ((<input type="text" name="username"/>)),
             "password" -> (<input type="password" name="password"/>),
             "submit" -> (<input type="submit" value={S.??("log.in")}/>))
    }
}