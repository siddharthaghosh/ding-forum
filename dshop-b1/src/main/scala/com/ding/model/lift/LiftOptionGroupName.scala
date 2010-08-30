/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import net.liftweb.mapper._
import com.ding.model._

class LiftOptionGroupName extends LiftModel[LiftOptionGroupName] with IdPK with OptionGroupName {

    override def getSingleton = LiftOptionGroupName

    object option_group_id extends MappedLongForeignKey(this, LiftOptionGroup)
    object lang_id extends MappedLongForeignKey(this, LiftLanguage)
    object name extends MappedString(this, 128)
}

object LiftOptionGroupName extends LiftOptionGroupName with LiftMetaModel[LiftOptionGroupName] with MetaOptionGroupName {
    override def dbTableName = "dshop_option_group_name"
    override def findOneInstance(id : Long) = {
        LiftOptionGroupName.find(By(LiftOptionGroupName.id, id)).openOr(null)
    }
}
