/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import net.liftweb.mapper._
import com.ding.model._

class LiftLanguage extends LiftModel[LiftLanguage] with Language {

    override def getSingleton = LiftLanguage
    override def primaryKeyField = lang_id

    object lang_id extends MappedLongIndex(this)
    object name extends MappedString(this, 50)
    object code extends MappedString(this, 2)
    object image extends MappedString(this, 128)
    object directory extends MappedString(this, 128)
    object display_order extends MappedInt(this)

    override def updateInstance(name : String, code : String, image : String, dir : String, display_order : Int) {
        this.name(name).code(code).image(image).directory(dir).display_order(display_order)
    }

    override def getID() : Long = this.lang_id.is
    override def getName() : String = this.name.is
    override def getImage() : String = this.image.is
    override def getDirectory() : String = this.directory.is
    override def getCode() : String = this.code.is
    override def getDisplayOrder() : Int = this.display_order.is

    override def setName(name : String) {
        this.name(name)
    }
    override def setImage(image : String) {
        this.image(image)
    }
    override def setDirectory(dir : String) {
        this.directory(dir)
    }
    override def setCode(code : String) {
        this.code(code)
    }
    override def setDisplayOrder(order : Int) {
        this.display_order(order)
    }

}

object LiftLanguage extends LiftLanguage with LiftMetaModel[LiftLanguage] with MetaLanguage {

    override def dbTableName = "dshop_language"
    override def findOneInstance(id : Long) = {
        LiftLanguage.find(By(LiftLanguage.lang_id, id)).openOr(null)
    }
    override def findAllInstances() = {
        LiftLanguage.findAll(OrderBy(LiftLanguage.display_order, Ascending))
    }
    override def isLanguageExist(name : String) : Boolean = {
        !(LiftLanguage.findAll(By(LiftLanguage.name, name)).isEmpty)       
    }
    override def isLanguageExist(id : Long) : Boolean = {
        !(LiftLanguage.findAll(By(LiftLanguage.lang_id, id)).isEmpty)
    }
}
