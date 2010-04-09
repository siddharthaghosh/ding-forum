/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.filter

import javax.servlet._
import javax.servlet.http._
import com.ding.util._


class GWTFilter extends Filter {

    def init(config : FilterConfig) {
//        ShopLogger.logger.debug("GWTFilter init")
    }

    def doFilter(request : ServletRequest, response : ServletResponse, chain : FilterChain) {
//        ShopLogger.logger.debug("GWTFilter doFilter")
        chain.doFilter(request, response)
    }

    def destroy() {
//        ShopLogger.logger.debug("GWTFilter destroy")
    }
    
}
