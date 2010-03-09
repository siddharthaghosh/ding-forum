/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.mapper._
import com.ding.model._
import java.util.Date

class LiftCategory extends LiftModel[LiftCategory] with Category with OneToMany[Long, LiftCategory] {

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

    override def updateInstance(parent_id : Long, image : String, active : Boolean, display_order : Int, descriptions : Tuple3[Long, String, String]*) {
        this.parent_id(parent_id).image(image).active(active).display_order(display_order)
        descriptions.foreach {
            desc_item => {
                this.setName(desc_item._1, desc_item._2)
                this.setDescription(desc_item._1, desc_item._3)
            }
        }
        this.update_time(new Date())
    }
    override def getID() : Long = this.cat_id.is
    override def getParentID() : Long = this.parent_id.is
    override def getUpdateTime() : Date = this.update_time.is
    override def getAddTime() : Date = this.add_time.is
    override def getName(lang_id : Long) : String = {
        val desc_item = this.findDescriptionByLang(LiftLanguage.find(By(LiftLanguage.lang_id, lang_id)).openOr(null))
        if(desc_item == null)
            null
        else
            desc_item.name.is
    }
    override def getDescription(lang_id : Long) : String = {
        val desc_item = this.findDescriptionByLang(LiftLanguage.find(By(LiftLanguage.lang_id, lang_id)).openOr(null))
        if(desc_item == null)
            null
        else
            desc_item.description.is
    }
    override def getActive() : Boolean = this.active.is
    override def getImage() : String = this.image.is
    override def getDisplayOrder() : Int = this.display_order.is

    override def setParentID(id : Long) {
        this.parent_id(id)
    }
    override def setUpdateTime(date : Date) {
        this.update_time(date)
    }
    override def setAddTime(date : Date) {
        this.add_time(date)
    }
    override def setName(lang_id : Long, name : String) {
        val desc_item = this.findDescriptionByLang(LiftLanguage.find(By(LiftLanguage.lang_id, lang_id)).openOr(null))
        if(desc_item != null)
            desc_item.name(name)
    }
    override def setDescription(lang_id : Long, desc : String) {
        val desc_item = this.findDescriptionByLang(LiftLanguage.find(By(LiftLanguage.lang_id, lang_id)).openOr(null))
        if(desc_item != null)
            desc_item.description(desc)
    }
    override def setActive(active : Boolean) {
        this.active(active)
    }
    override def setImage(image : String) {
        this.image(image)
    }
    override def setDisplayOrder(order : Int) {
        this.display_order(order)
    }
    
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

object LiftCategory extends LiftCategory with LiftMetaModel[LiftCategory] with MetaCategory {
    override def dbTableName = "dshop_category"
    override def findOneInstance(id : Long) = {
        LiftCategory.find(By(LiftCategory.cat_id, id)).openOr(null)
    }
}