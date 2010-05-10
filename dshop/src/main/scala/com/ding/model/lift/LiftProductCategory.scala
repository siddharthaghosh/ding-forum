/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import net.liftweb.mapper._

class LiftProductCategory
extends LiftModel[LiftProductCategory]
   with IdPK {

    def getSingleton = LiftProductCategory

    object product_id extends MappedLongForeignKey(this, LiftProduct)
    object category_id extends MappedLongForeignKey(this, LiftCategory)
    object display_order extends MappedInt(this){
        override def defaultValue = Int.MaxValue
    }

    override def delete_! = {
        println("call PCRelation delete cat_ID: " + this.category_id.is + ", prod_id: " + this.product_id.is)
//        val result = super.delete_!
//        result
        val otherRef = !(LiftProductCategory.find(By(LiftProductCategory.product_id, this.product_id),
                                                  NotBy(LiftProductCategory.category_id, this.category_id)
            ).isEmpty)
        if(otherRef || this.category_id.is == 0) {
            println("has other ref or cat id is 0")
            val ret = super.delete_!
            println("call PCRelation delete end!")
            ret
        } else {
            val ret = super.delete_!
            val rootcat = LiftCategory.findOneInstance(0)
            rootcat.addProduct(this.product_id.is)
            val ret2 = rootcat.save
            println("call PCRelation delete end!")
            ret && ret2
        }
    }
}
object LiftProductCategory
extends LiftProductCategory
   with LiftMetaModel[LiftProductCategory] {
    override def dbTableName = "dshop_product_category"
    override def findOneInstance(id : Long) = {
        LiftProductCategory.find(By(LiftProductCategory.id, id)).openOr(null)
    }
}
