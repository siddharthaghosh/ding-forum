/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import com.ding.model._
import net.liftweb.common._
import net.liftweb.mapper._
import net.liftweb.util._
import java.util.Date

class LiftManufacturer extends LiftModel[LiftManufacturer] with Manufacturer {

    override def getSingleton = LiftManufacturer
    override def primaryKeyField = manufact_id

    object manufact_id extends MappedLongIndex(this)
    object name extends MappedString(this, 128)
    object image extends MappedString(this, 128)
    object add_time extends MappedDateTime(this)
    object update_time extends MappedDateTime(this)
    object url extends MappedString(this, 256)

    override def getID() : Long = this.manufact_id.is
    override def getName() : String = this.name.is
    override def getImage() : String = this.image.is
    override def getAddTime() : Date = this.add_time.is
    override def getUpdateTime() : Date = this.update_time.is
    override def getURL() : String = this.url.is

    override def setName(name : String) {
        this.name(name)
    }
    override def setImage(image : String) {
        this.image(image)
    }
    override def setAddTime(date : Date) {
        this.add_time(date)
    }
    override def setUpdateTime(date : Date) {
        this.update_time(date)
    }
    override def setURL(url : String) {
        this.url(url)
    }

    override def saveInstance() : Boolean = {
        this.setUpdateTime(new Date())
        this.save
    }
}

object LiftManufacturer extends LiftManufacturer with LiftMetaModel[LiftManufacturer] with MetaManufacturer {

    override def dbTableName = "dshop_manufacture"

    override def newInstance() = {
        val ni = this.create
        ni.setAddTime(new Date())
        ni
    }

    override def findOneInstance(id : Long) = {
        LiftManufacturer.find(By(LiftManufacturer.manufact_id, id)).openOr(null)
    }
}
