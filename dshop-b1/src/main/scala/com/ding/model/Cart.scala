/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model

import net.liftweb.http._

class Cart {

    private var itemList : List[Long] = Nil
    
    def addItem(gid : Long) {
        if(!existItem(gid)) {
            itemList = itemList ::: (gid :: Nil)
        }
    }

    def removeItem(gid : Long) {
        if(existItem(gid)) {
            itemList = itemList.filterNot({
                    item => item == gid
                })
        }
    }

    def allItems() : List[Long] = {
        itemList.toList
    }

    def existItem(gid : Long) : Boolean = {
        itemList.contains(gid)
    }
    
}

object CartInfo extends SessionVar[Cart]({new Cart})
