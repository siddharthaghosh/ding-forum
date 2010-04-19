/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import com.ding.model._
import net.liftweb.mapper._

trait LiftDisplayOrder[A<:LiftDisplayOrder[A]]
extends LiftBaseModel[A]
   with DisplayOrder {
    self : A =>

    object display_order extends MappedInt(this)

    def getDisplayOrder() : Int = {
        this.display_order.is
    }

    def setDisplayOrder(order : Int) {
        this.display_order(order)
    }
    
}
