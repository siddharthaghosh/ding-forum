/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model
import net.liftweb.mapper._

class LiftCategory extends LongKeyedMapper[LiftCategory]{
    def getSingleton = LiftCategory

    override def primaryKeyField = category_id
    // the primary key
    object category_id extends MappedLongIndex(this)

    object title extends MappedString(this, 100) {
//        override def dbIndexed_? = true
    }
    object display_oder extends MappedInt(this)
    object moderated extends MappedBoolean(this)

    def forums = LiftForum.findAll(By(LiftForum.category_id, this.category_id))
}

object LiftCategory extends LiftCategory with LongKeyedMetaMapper[LiftCategory] {
    override def dbTableName = "dforum_categories"
}