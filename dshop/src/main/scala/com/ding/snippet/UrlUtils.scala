/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.snippet

import _root_.scala.xml.NodeSeq
import _root_.net.liftweb.util.Helpers
import Helpers._
import _root_.net.liftweb.http.S
import _root_.net.liftweb.http.js

class UrlUtils {

    def context(in: NodeSeq): NodeSeq =
        Helpers.bind("context", in, "context" -> S.contextPath)

    def contextToJS(in: NodeSeq) : NodeSeq = {
        import js._
        import JsCmds._
        import JE._
        val context = "\"" + S.contextPath + "\""
        val node = JsCmds.Script(JsCrVar("applicationContextPath", JsRaw(context)))
        node
    }
}
