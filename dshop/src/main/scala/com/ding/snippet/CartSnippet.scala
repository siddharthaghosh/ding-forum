/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.snippet

import scala.xml.NodeSeq
import scala.xml.Text
import com.ding.model.CartInfo

class CartSnippet {
    def cartBrief(kids: NodeSeq): NodeSeq = {
        val itemNum = CartInfo.allItems.length
        Text("Cart now has " + itemNum.toString + " items.")
    }
}
