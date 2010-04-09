/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.servlet

import javax.servlet._
import javax.servlet.http._

class HelloWorldServlet extends HttpServlet {

    override def doGet(request : HttpServletRequest, response : HttpServletResponse) {
        response.getWriter().println("hello gwt world")
    }
    
}
