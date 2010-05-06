/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import com.ding.model._
import net.liftweb.mapper._

trait LiftActive[A <: LiftActive[A]]
extends LiftBaseModel[A]
   with Active {
    self : A=>

    object active extends MappedBoolean(this)

    def getActive() : Boolean = {
        this.active.is
    }

    def setActive(active : Boolean) = {
        this.active(active)
    }
}
