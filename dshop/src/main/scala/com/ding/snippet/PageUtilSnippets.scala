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
import _root_.net.liftweb.common._

class PageUtilSnippets {

    def setDHTML4DocType(kids: NodeSeq): NodeSeq = {
        S.setDocType(Full("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">"))
        kids
    }

    def setContextToJS(in: NodeSeq) : NodeSeq = {
        import js._
        import JsCmds._
        import JE._
        val context = "\"" + S.contextPath + "\""
        val node = JsCmds.Script(JsCrVar("applicationContextPath", JsRaw(context)))
        node
    }

    def title(in : NodeSeq) : NodeSeq = {
        val titleContent = "title -- unfinish here"
        <title>
            {titleContent}
        </title>
    }
 }
