/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import net.liftweb.mapper._

class LiftTypeOptionGroup extends LiftBaseModel[LiftTypeOptionGroup] with IdPK {
    def getSingleton = LiftTypeOptionGroup
    object type_id extends MappedLongForeignKey(this, LiftType)
    object option_group_id extends MappedLongForeignKey(this, LiftOptionGroup)
}

object LiftTypeOptionGroup extends LiftTypeOptionGroup with LiftMetaModel[LiftTypeOptionGroup] {
    override def dbTableName = "dshop_type_option_group"
    override def findOneInstance(id : Long) = {
        LiftTypeOptionGroup.find(By(LiftTypeOptionGroup.id, id)).openOr(null)
    }
}