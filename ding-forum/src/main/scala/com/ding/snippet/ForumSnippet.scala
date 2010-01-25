/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.snippet
import com.ding.model._
import net.liftweb.http._
import net.liftweb.common._
import scala.xml._

object ForumReqVar extends RequestVar[LiftForum]({
        LiftForum.create
})

class ForumSnippet {
    def manage(in : NodeSeq) : NodeSeq = {
        ForumReqVar.is.toForm(Full("Save"), { _.save })
    }
}
