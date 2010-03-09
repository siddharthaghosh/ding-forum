/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.mapper._
import com.ding.model._

class LiftCategory extends LongKeyedMapper[LiftCategory] with Category with OneToMany[Long, LiftCategory] {

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

    object descriptions extends MappedOneToMany(LiftCategoryDescription, LiftCategoryDescription.category_id)
    
    override def saveInstance() : Boolean = this.save
    override def deleteInstance() : Boolean = {
        this.delete_!
    }

    def findDescriptions : List[LiftCategoryDescription] = {
        descriptions.all
        //LiftCategoryDescription.findAll(By(LiftCategoryDescription.category_id, this.cat_id))
    }

    def findDescriptionByLang(lang : LiftLanguage) : LiftCategoryDescription = {
        if(lang == null)
            null
        else
            descriptions.all.find(
                descitem => {
                    if(descitem.lang_id.is == lang.lang_id.is)
                        true
                    else
                        false
                }
            ).orNull
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