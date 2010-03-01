/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model

import net.liftweb.mapper._

class LiftLanguage extends LongKeyedMapper[LiftLanguage]{

    override def getSingleton = LiftLanguage
    override def primaryKeyField = lang_id

    object lang_id extends MappedLongIndex(this)
    object name extends MappedString(this, 50)
    object code extends MappedString(this, 2)
    object image extends MappedString(this, 128)
    object directory extends MappedString(this, 128)
    object display_order extends MappedInt(this)
}

object LiftLanguage extends LiftLanguage with LongKeyedMetaMapper[LiftLanguage] {

    override def dbTableName = "dshop_language"

}
