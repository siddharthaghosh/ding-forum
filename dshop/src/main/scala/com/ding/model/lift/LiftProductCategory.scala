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
}

object LiftProductCategory
extends LiftProductCategory
   with LiftMetaModel[LiftProductCategory] {
    override def dbTableName = "dshop_product_category"
    override def findOneInstance(id : Long) = {
        LiftProductCategory.find(By(LiftProductCategory.id, id)).openOr(null)
    }
}
