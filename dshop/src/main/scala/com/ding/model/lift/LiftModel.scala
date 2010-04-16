/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import net.liftweb.mapper._
import com.ding.model._

trait LiftModel[A<:LiftModel[A]] extends LongKeyedMapper[A] with Model {
    self : A =>
    def saveInstance() : Boolean = this.save
    def deleteInstance() : Boolean = {
        this.delete_!
    }
}

trait LiftBaseModel[A<:LiftBaseModel[A]] extends LiftModel[A] with BaseModel {
    self : A =>
    def getID() : Long = {
        this.primaryKeyField.is
    }
}

trait LiftMetaModel[A<:LiftModel[A]] extends LongKeyedMetaMapper[A] with MetaModel[A] {
    self : A =>
    def newInstance() = this.create
    def findAllInstances() = {
        self.findAll
    }
}
