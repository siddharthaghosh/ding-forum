/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model
import net.liftweb.mapper._
import net.liftweb.common._
import net.liftweb.http._
import scala.xml._
import scala.collection.mutable._

class LiftForum extends LongKeyedMapper[LiftForum] {

    def getSingleton = LiftForum
    override def primaryKeyField = forum_id

    object forum_id extends MappedLongIndex(this){
        override def dbIndexed_? = false
    }
    object category_id extends MappedLongForeignKey(this, LiftCategory) {
        override def defaultValue: Long = 2L
        override def dbIndexed_? = true
        override def validSelectValues = Full(LiftCategory.findAll.map(cat => {(cat.category_id.is, cat.title.is)}))
    }
    object name extends MappedString(this, 150)
    object description extends MappedString(this, 255)
    object display_order extends MappedInt(this)
    object moderated extends MappedBoolean(this)
    object last_post_id extends MappedLong(this)
    object topics_sum extends MappedLong(this)

}

object LiftForum extends LiftForum with LongKeyedMetaMapper[LiftForum] {
    override def dbTableName = "dforum_forms"
}
