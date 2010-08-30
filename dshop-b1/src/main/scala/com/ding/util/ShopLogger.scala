/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.util

import net.lag.configgy._
import net.lag.logging._
import net.liftweb.util._

object ShopLogger {
    val config = Configgy.configure(getClass.getResource("/conf/logger/").getPath, "logger.conf")
    val logger = Logger.get

    def debug(msg : String, items : Any*) {
        logger.debug(msg, items)
    }
    def error(msg : String, items : Any*) {
        logger.error(msg, items)
    }
    def info(msg : String, items : Any*) {
        logger.info(msg, items)
    }
    def warning(msg : String, items : Any*) {
        logger.warning(msg, items)
    }
}
