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

    val ExtensionPropertyNum = 5

    override def getSingleton = LiftProduct
    override def primaryKeyField = product_id
    override def multiLangNameDescriptionObject() = LiftProductNameDescription

    object product_id extends MappedLongIndex(this)
    object category extends MappedManyToMany(LiftProductCategory,
                                             LiftProductCategory.product_id,
                                             LiftProductCategory.category_id,
                                             LiftCategory)
    object goods extends MappedOneToMany(LiftGoods, LiftGoods.product_id)
    object ep1 extends MappedInt(this){
        override def defaultValue = -1
        override def dbNotNull_? = true
    }
    object ep2 extends MappedInt(this){
        override def defaultValue = -1
        override def dbNotNull_? = true
    }
    object ep3 extends MappedInt(this){
        override def defaultValue = -1
        override def dbNotNull_? = true
    }
    object ep4 extends MappedInt(this){
        override def defaultValue = -1
        override def dbNotNull_? = true
    }
    object ep5 extends MappedInt(this){
        override def defaultValue = -1
        override def dbNotNull_? = true
    }

    object parameter extends MappedText(this)

    override def getDisplayOrder(categoryId : Long) : Int = {
        val join = category.joins.find(
            join => {
                join.category_id.is == categoryId
            }
        )
        join.map(_.display_order.is).getOrElse(-1)
    }

    override def setDisplayOrder(categoryId : Long, order : Int) {
        val join = category.joins.find(
            join => {
                join.category_id.is == categoryId
            }
        )
        join.map(_.display_order(order))
    }

    override def addToCategory(categoryId : Long) {
        
    }

    override def categories() : List[Category] = {
        category.all
    }

    override def addGoods(gid : Long){
        val goods = LiftGoods.findOneInstance(gid)
        if(goods != null) {
            this.goods += goods
            this.goods.save
        }
    }
    override def removeGoods(gid : Long){}
    override def removeAllGoods(){
        if(this.goods.all.length > 0){
            this.goods.all.foreach {
                g => {
                    g.delete_!
                }
            }
            this.goods.save
        }
    }

    def getExtensionProperties() : Array[Int] = {

        val resultarr = new Array[Int](this.ExtensionPropertyNum)

        for( i <- (1 to this.ExtensionPropertyNum)) {
            val oep = this.getClass.getMethod("ep" + i.toString).invoke(this).asInstanceOf[MappedInt[LiftProduct]]
            resultarr(i-1) = oep.is
        }
        resultarr
    }

    def setExtensionProperties(resultArr : Array[Int]) {
        val leng = if(resultArr.length > this.ExtensionPropertyNum) this.ExtensionPropertyNum else resultArr.length
        for(i <- (1 to leng)) {
            val oep = this.getClass.getMethod("ep" + i.toString).invoke(this).asInstanceOf[MappedInt[LiftProduct]]
            oep(resultArr(i - 1))
        }
    }

    def setExtensionProperty(index : Int, value : Int) {
        if(index <= this.ExtensionPropertyNum) {
            val oep = this.getClass.getMethod("ep" + index.toString).invoke(this).asInstanceOf[MappedInt[LiftProduct]]
            oep(value)
        }
    }

    def setParameter(param : String) {
        this.parameter(param)
    }
    def getParameter() : String = {
        this.parameter.is
    }
}

object LiftProduct extends LiftProduct with LiftMetaModel[LiftProduct] with MetaProduct {

    override def dbTableName = "dshop_product"
    override def findOneInstance(id : Long) = {
        LiftProduct.find(By(LiftProduct.product_id, id)).openOr(null)
    }

}