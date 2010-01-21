/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.snippet

import com.ding.model._
import scala.xml.NodeSeq
import net.liftweb.common._
import net.liftweb.http.S

class CategorySnippet {
    def listAll : NodeSeq = {
        val firstcat = LiftCategory.findAll.head
        val content = firstcat.toForm(Full("save"), {cat => {println("save process");cat.save}})
        S.functionMap.foreach(m => {println(m._1)})
        <form>{content}</form>
    }
}
