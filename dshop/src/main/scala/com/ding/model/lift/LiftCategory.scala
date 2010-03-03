/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.mapper._

class LiftCategory extends LongKeyedMapper[LiftCategory] {

    override def getSingleton = LiftCategory
    override def primaryKeyField = cat_id

    object cat_id extends MappedLongIndex(this)
    object parent_id extends MappedLong(this)
    object active extends MappedBoolean(this)
    object image extends MappedString(this, 128)
    object display_order extends MappedInt(this)
    object add_time extends MappedDateTime(this)
    object update_time extends MappedDateTime(this)
    

}

object LiftCategory extends LiftCategory with LongKeyedMetaMapper[LiftCategory] {
    override def dbTableName = "dshop_category"
}