package com.ding.snippet

import _root_.scala.xml.NodeSeq
import _root_.net.liftweb.util.Helpers
import Helpers._
import net.liftweb.http.S

class HelloWorld {
  def howdy(in: NodeSeq): NodeSeq =
    Helpers.bind("b", in, "time" -> (new _root_.java.util.Date).toString)

    def testAttr(in: NodeSeq): NodeSeq = {
        <b>{S.attr.~("tattr").getOrElse("noattr")}</b>
    }
}

