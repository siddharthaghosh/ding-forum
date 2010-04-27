/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import net.liftweb.mapper._
import com.ding.model._

class LiftCategoryNameDescription
extends LiftMultiLanguageNameDescriptionBase[LiftCategoryNameDescription, LiftCategory]
   with IdPK
   with CategoryNameDescription {

    override def getSingleton = LiftCategoryNameDescription
    override def getReferenceObject() = LiftCategory
    override def getReferenceObjectIDName = "category_id"
    
}

object LiftCategoryNameDescription
extends LiftCategoryNameDescription 
   with MetaLiftMultiLanguageNameDescriptionBase[LiftCategoryNameDescription, LiftCategory]
   with MetaCategoryNameDescription{
    
    override def dbTableName = "dshop_category_name_description"
    override def findOneInstance(id : Long) = {
        LiftCategoryNameDescription.find(By(LiftCategoryNameDescription.id, id)).openOr(null)
    }
}
