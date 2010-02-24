/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model
import net.liftweb.mapper._

class LiftDocument extends LongKeyedMapper[LiftDocument]{
    def getSingleton = LiftDocument
    override def primaryKeyField = doc_id
    // the primary key
    object doc_id extends MappedLongIndex(this)

    object content extends MappedBinary(this)
}

object LiftDocument extends LiftDocument with LongKeyedMetaMapper[LiftDocument] {
    override def dbTableName = "dcms_doc_content"
}
