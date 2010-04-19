/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import com.ding.model._
import net.liftweb.mapper._

trait LiftImage[A <: LiftImage[A]]
extends LiftBaseModel[A]
   with Image {
    self : A =>

    object image extends MappedString(this, 128)

    def getImage() : String = {
        this.image.is
    }

    def setImage(image : String) {
        this.image(image)
    }
}
