/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import com.ding.model._
import net.liftweb.mapper._

class LiftTypeName extends LiftMultiLanguageNameBase[LiftTypeName, LiftType] with IdPK with TypeName {
    override def getSingleton = LiftTypeName
    override def getReferenceObject() = LiftType
    override def getReferenceObjectIDName = "type_id"
}

object LiftTypeName extends LiftTypeName with MetaLiftMultiLanguageNameBase[LiftTypeName, LiftType] with MetaTypeName {
    override def dbTableName = "dshop_type_name"
    override def findOneInstance(id : Long) = {
        LiftTypeName.find(By(LiftTypeName.id, id)).openOr(null)
    }
}
