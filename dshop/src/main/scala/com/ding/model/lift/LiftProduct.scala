/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import com.ding.model._
import net.liftweb.mapper._

class LiftProduct extends LiftBaseModel[LiftProduct]
                     with Product
                     with LiftMultiLanguageNameDescription[LiftProduct, LiftProductNameDescription]
//                     with LiftDisplayOrder[LiftProduct]
                     with LiftImage[LiftProduct]
                     with LiftActive[LiftProduct]
                     with ManyToMany {

    override def getSingleton = LiftProduct
    override def primaryKeyField = product_id
    override def multiLangNameDescriptionObject() = LiftProductNameDescription

    object product_id extends MappedLongIndex(this)
    object category extends MappedManyToMany(LiftProductCategory,
                                             LiftProductCategory.product_id,
                                             LiftProductCategory.category_id,
                                             LiftCategory)

    override def getDisplayOrder(categoryId : Long) : Int = {
        0
    }

    override def setDisplayOrder(categoryId : Long, order : Int) {

    }

    override def addToCategory(categoryId : Long) {
        
    }

    override def categories() : List[Category] = {
        category.all
    }


}

object LiftProduct extends LiftProduct with LiftMetaModel[LiftProduct] with MetaProduct {

    override def dbTableName = "dshop_product"
    override def findOneInstance(id : Long) = {
        LiftProduct.find(By(LiftProduct.product_id, id)).openOr(null)
    }

}