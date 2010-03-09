/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.mapper._
import com.ding.model._

class LiftCategory extends LongKeyedMapper[LiftCategory] with Category {

    override def getSingleton = LiftCategory
    override def primaryKeyField = cat_id

    object cat_id extends MappedLongIndex(this)
    object parent_id extends MappedLong(this) {
        override def defaultValue : Long = 0
    }
    object active extends MappedBoolean(this)
    object image extends MappedString(this, 128)
    object display_order extends MappedInt(this)
    object add_time extends MappedDateTime(this)
    object update_time extends MappedDateTime(this)
    
    override def saveInstance() : Boolean = this.save
    override def deleteInstance() : Boolean = {
        this.delete_!
    }

    def findDescriptions : List[LiftCategoryDescription] = {
        LiftCategoryDescription.findAll(By(LiftCategoryDescription.category_id, this.cat_id))
    }

    def findDescriptionByLang(lang : LiftLanguage) : LiftCategoryDescription = {
        LiftCategoryDescription.find(By(LiftCategoryDescription.lang_id, lang.lang_id),
                                     By(LiftCategoryDescription.category_id, this.cat_id)).openOr( null )
    }
}

object LiftCategory extends LiftCategory with LongKeyedMetaMapper[LiftCategory] with MetaCategory {
    override def dbTableName = "dshop_category"
    override def newInstance() = this.create
    override def findOneInstance(id : Int) = {
        LiftCategory.find(By(LiftCategory.cat_id, id)).openOr(null)
    }
    override def findAllInstances() = {
        LiftCategory.findAll
    }
}