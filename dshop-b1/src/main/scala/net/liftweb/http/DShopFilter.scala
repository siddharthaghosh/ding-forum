/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.liftweb.http

import net.liftweb.http._
import net.liftweb.http.provider.servlet._
import _root_.javax.servlet._
import _root_.javax.servlet.http._
import _root_.net.liftweb.common._
import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import Helpers._

import com.ding.util._
import com.ding.model.lift.Administrator

class DShopFilter extends ServletFilterProvider {
    override def doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) = {
        TransientRequestVarHandler(Empty,
                                   RequestVarHandler(Empty,
                                                     (req, res) match {
                    case (httpReq: HttpServletRequest, httpRes: HttpServletResponse) =>
                        val httpRequest = new HTTPRequestServlet(httpReq)
                        val httpResponse = new HTTPResponseServlet(httpRes)
                        if(httpRequest.uri.startsWith(httpRequest.contextPath + "/gwtcall")
//                           || httpRequest.uri.startsWith(httpRequest.contextPath + "/gwtclient")
                        ) {
                            chain.doFilter(req, res)
                        }else {
                            service(httpRequest, httpResponse) {
                                chain.doFilter(req, res)
                            }
                        }
//                        service(httpRequest, httpResponse) {
//                            chain.doFilter(req, res)
//                        }
                        
                    case _ => chain.doFilter(req, res)
                }))
    }
}
