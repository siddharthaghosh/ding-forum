/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import com.ding.model._
import net.liftweb.mapper._

class LiftGoods extends LiftBaseModel[LiftGoods] with Goods{

    override def getSingleton = LiftGoods
    override def primaryKeyField = goods_id
    
    object goods_id extends MappedLongIndex(this)
    object product_id extends MappedLongForeignKey(this, LiftProduct)
    object option extends MappedText(this)
    object bn extends MappedString(this, 255)
    object store extends MappedInt(this) {
        override def defaultValue = 0
    }
    object store_place extends MappedString(this, 255)
    object weight extends MappedDouble(this){
        override def defaultValue = 0
    }
    object cost extends MappedDouble(this){
        override def defaultValue = 0
    }
    object market_price extends MappedDouble(this){
        override def defaultValue = 0
    }
    object price extends MappedDouble(this){
        override def defaultValue = 0
    }

    override def getProductID() : Long = this.product_id.is
}

object LiftGoods extends LiftGoods with LiftMetaModel[LiftGoods] with MetaGoods {

    override def dbTableName = "dshop_goods"

    override def findOneInstance(id : Long) = {
        LiftGoods.find(By(LiftGoods.goods_id, id)).openOr(null)
    }
}
