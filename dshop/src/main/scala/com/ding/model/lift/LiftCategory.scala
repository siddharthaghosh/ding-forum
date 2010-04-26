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

class LiftCategory
extends LiftBaseModel[LiftCategory]
   with Category
   with LiftImage[LiftCategory]
   with LiftDisplayOrder[LiftCategory]
   with LiftMultiLanguageNameDescription[LiftCategory, LiftCategoryNameDescription]
   with OneToMany[Long, LiftCategory]
   with ManyToMany {

    override def getSingleton = LiftCategory
    override def primaryKeyField = cat_id
    override def multiLangNameDescriptionObject() = LiftCategoryNameDescription

    object cat_id extends MappedLongIndex(this)
    object parent_id extends MappedLong(this) {
        override def defaultValue : Long = 0
    }
    object active extends MappedBoolean(this)
//    object image extends MappedString(this, 128)
//    object display_order extends MappedInt(this)
    object add_time extends MappedDateTime(this)
    object update_time extends MappedDateTime(this)

//    object descriptions extends MappedOneToMany(LiftCategoryNameDescription, LiftCategoryNameDescription.category_id)
    object product extends MappedManyToMany(LiftProductCategory,
                                            LiftProductCategory.category_id,
                                            LiftProductCategory.product_id,
                                            LiftProduct)

    override def updateInstance(parent_id : Long, image : String, active : Boolean, display_order : Int, descriptions : Tuple3[Long, String, String]*) {
        this.parent_id(parent_id).image(image).active(active).display_order(display_order)
        descriptions.foreach {
            desc_item => {
                this.setName(desc_item._1, desc_item._2)
                this.setDescription(desc_item._1, desc_item._3)
            }
        }
    }

    override def products : List[Product] = this.product.all

    override def children() : List[LiftCategory] = {
        LiftCategory.findAll(By(LiftCategory.parent_id, this.cat_id), OrderBy(LiftCategory.display_order, Ascending))
    }
    override def getID() : Long = this.cat_id.is
    override def getParentID() : Long = this.parent_id.is
    override def getUpdateTime() : Date = this.update_time.is
    override def getAddTime() : Date = this.add_time.is
//    override def getName(lang_id : Long) : String = {
//        val desc_item = this.findDescriptionByLang(LiftLanguage.find(By(LiftLanguage.lang_id, lang_id)).openOr(null))
//        if(desc_item == null)
//            ""
//        else
//            desc_item.name.is
//    }
//    override def getDescription(lang_id : Long) : String = {
//        val desc_item = this.findDescriptionByLang(LiftLanguage.find(By(LiftLanguage.lang_id, lang_id)).openOr(null))
//        if(desc_item == null)
//            ""
//        else
//            desc_item.description.is
//    }
    override def getActive() : Boolean = this.active.is
//    override def getImage() : String = this.image.is
//    override def getDisplayOrder() : Int = this.display_order.is

    override def setParentID(id : Long) {
        this.parent_id(id)
    }
    override def setUpdateTime(date : Date) {
        this.update_time(date)
    }
    override def setAddTime(date : Date) {
        this.add_time(date)
    }
//    override def setName(lang_id : Long, name : String, desc : String*) {
//        val desc_item = this.findDescriptionByLang(LiftLanguage.find(By(LiftLanguage.lang_id, lang_id)).openOr(null))
//        if(desc_item != null) {
//            desc_item.name(name)
//            if(desc.length > 0)
//            {
//                desc_item.description(desc.head)
//            }
//        }
//        else {
//            val des = LiftCategoryNameDescription.newInstance
//            des.lang_id(lang_id)
//            des.name(name)
//            if(desc.length > 0)
//            {
//                des.description(desc.head)
//            }
//            else {
//                des.description("")
//            }
//
//            this.descriptions.append(des)
//        }
//    }
//    override def setDescription(lang_id : Long, desc : String) {
//        val desc_item = this.findDescriptionByLang(LiftLanguage.find(By(LiftLanguage.lang_id, lang_id)).openOr(null))
//        if(desc_item != null)
//            desc_item.description(desc)
//    }
    override def setActive(active : Boolean) {
        this.active(active)
    }
//    override def setImage(image : String) {
//        this.image(image)
//    }
//    override def setDisplayOrder(order : Int) {
//        this.display_order(order)
//    }
    override def saveInstance() : Boolean = {
        this.setUpdateTime(new Date())
        this.save
    }
    override def deleteInstance() : Boolean = {
        this.children.foreach {
            child => {
                child.deleteInstance()
            }
        }
        this.descriptions.all.foreach {
            desc => {
                desc.deleteInstance()
            }
        }
        this.delete_!
    }

//    def findDescriptions : List[LiftCategoryNameDescription] = {
//        descriptions.all
//        //LiftCategoryDescription.findAll(By(LiftCategoryDescription.category_id, this.cat_id))
//    }
//
//    def findDescriptionByLang(lang : LiftLanguage) : LiftCategoryNameDescription = {
//        if(lang == null)
//            null
//        else
//            descriptions.all.find(
//                descitem => {
//                    if(descitem.lang_id.is == lang.lang_id.is)
//                        true
//                    else
//                        false
//                }
//            ).orNull
//    }
}

object LiftCategory extends LiftCategory with LiftMetaModel[LiftCategory] with MetaCategory {
    override def dbTableName = "dshop_category"
    override def findOneInstance(id : Long) = {
        LiftCategory.find(By(LiftCategory.cat_id, id)).openOr(null)
    }
    override def getChildren(parentId : Long) : List[LiftCategory] = {
        LiftCategory.findAll(By(LiftCategory.parent_id, parentId), OrderBy(LiftCategory.display_order, Ascending))
    }
    override def getProducts(parentId : Long) : List[Product] = {
        val cat = LiftCategory.findOneInstance(parentId)
        if(cat != null) {
            cat.products()
        } else {
            Nil
        }
    }
    override def getAllAncestor(categoryId : Long) : List[Category] = {
        val cat = LiftCategory.findOneInstance(categoryId)
        if(cat == null) {
            Nil
        } else {
            val parentCat = LiftCategory.find(By(LiftCategory.cat_id, cat.parent_id)).openOr(null)
            if(parentCat == null) {
                cat :: Nil
            } else {
                LiftCategory.getAllAncestor(parentCat.cat_id) ++ (cat :: Nil)
            }
        }
    }

    override def newInstance() = {
        val ni = this.create
        ni.setAddTime(new Date())
        ni
    }
}