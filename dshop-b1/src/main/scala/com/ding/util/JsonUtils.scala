/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.util

import net.liftweb.json._
import net.liftweb.json.JsonAST._

object JsonUtils {
    def StringListToJsonStringArrayStr(list : List[String]) : String = {
        val jsonFileNameArr = JArray(list.flatMap {
                item => {
                    JString(item)::Nil
                }
            })
        val jsonStr = Printer.pretty(JsonAST.render(jsonFileNameArr))
        jsonStr
    }
}
