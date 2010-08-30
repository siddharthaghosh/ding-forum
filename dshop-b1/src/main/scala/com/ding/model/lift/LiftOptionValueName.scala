/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import net.liftweb.mapper._
import com.ding.model._

class LiftOptionValueName extends LiftModel[LiftOptionValueName] with IdPK with OptionValueName {

    override def getSingleton = LiftOptionValueName

    object option_value_id extends MappedLongForeignKey(this, LiftOptionValue)
    object lang_id extends MappedLongForeignKey(this, LiftLanguage)
    object name extends MappedString(this, 128)
}

object LiftOptionValueName extends LiftOptionValueName with LiftMetaModel[LiftOptionValueName] with MetaOptionValueName {
    override def dbTableName = "dshop_option_value_name"
    override def findOneInstance(id : Long) = {
        LiftOptionValueName.find(By(LiftOptionValueName.id, id)).openOr(null)
    }
}
