/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import com.ding.model._
import net.liftweb.mapper._

class LiftProductNameDescription extends LiftMultiLanguageNameDescriptionBase[LiftProductNameDescription, LiftProduct]
                                    with IdPK
                                    with ProductNameDescription {

    override def getSingleton = LiftProductNameDescription
    override def getReferenceObject() = LiftProduct
    override def getReferenceObjectIDName = "product_id"

}

object LiftProductNameDescription extends LiftProductNameDescription
                                     with MetaLiftMultiLanguageNameDescriptionBase[LiftProductNameDescription, LiftProduct]
                                     with MetaProductNameDescription {
    override def dbTableName = "dshop_product_name_description"
    override def findOneInstance(id : Long) = {
        LiftProductNameDescription.find(By(LiftProductNameDescription.id, id)).openOr(null)
    }
}
