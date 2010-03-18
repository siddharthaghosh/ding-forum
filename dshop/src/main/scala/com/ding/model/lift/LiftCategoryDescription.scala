/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import net.liftweb.mapper._
import com.ding.model._

class LiftCategoryDescription extends LiftModel[LiftCategoryDescription] with IdPK with CategoryDescription{

    override def getSingleton = LiftCategoryDescription

    object category_id extends MappedLongForeignKey(this, LiftCategory)
    object lang_id extends MappedLongForeignKey(this, LiftLanguage)
    object name extends MappedString(this, 128)
    object description extends MappedString(this, 255)
    
}

object LiftCategoryDescription extends LiftCategoryDescription with LiftMetaModel[LiftCategoryDescription] with MetaCategoryDescription{
    
    override def dbTableName = "dshop_category_description"
    override def findOneInstance(id : Long) = {
        LiftCategoryDescription.find(By(LiftCategoryDescription.id, id)).openOr(null)
    }
}
