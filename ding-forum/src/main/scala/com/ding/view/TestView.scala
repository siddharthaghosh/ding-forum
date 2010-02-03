/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.view

import net.liftweb.http.LiftView
import scala.xml.NodeSeq

class TestView extends LiftView{
    override def dispatch = {
        case "test" => doTest _
    }

    def doTest() : NodeSeq = {
        <lift:surround with="default" at="content">
            <lift:embed what="forum/index"/>
        </lift:surround>
    }
}
