/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model

import net.liftweb.mapper._

class LiftLanguage extends LongKeyedMapper[LiftLanguage] with Language{

    override def getSingleton = LiftLanguage
    override def primaryKeyField = lang_id

    object lang_id extends MappedLongIndex(this)
    object name extends MappedString(this, 50)
    object code extends MappedString(this, 2)
    object image extends MappedString(this, 128)
    object directory extends MappedString(this, 128)
    object display_order extends MappedInt(this)

    override def saveInstance() = this.save
    override def updateInstance(name : String, code : String, image : String, dir : String, display_order : Int) {
        this.name(name).code(code).image(image).directory(dir).display_order(display_order)
    }
}

object LiftLanguage extends LiftLanguage with LongKeyedMetaMapper[LiftLanguage] with MetaLanguage {

    override def dbTableName = "dshop_language"
    override def newInstance() = this.create
}
