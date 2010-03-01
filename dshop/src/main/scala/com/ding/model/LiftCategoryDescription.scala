/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model

import net.liftweb.mapper._

class LiftCategoryDescription extends LongKeyedMapper[LiftCategoryDescription] with IdPK{

    override def getSingleton = LiftCategoryDescription

    object category_id extends MappedLongForeignKey(this, LiftCategory)
    object lang_id extends MappedLongForeignKey(this, LiftLanguage)
    object name extends MappedString(this, 128)
    object description extends MappedString(this, 255)
    
}

object LiftCategoryDescription extends LiftCategoryDescription with LongKeyedMetaMapper[LiftCategoryDescription] {
    
    override def dbTableName = "dshop_category_description"
    
}
