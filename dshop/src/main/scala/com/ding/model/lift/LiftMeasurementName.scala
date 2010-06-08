/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import com.ding.model._
import net.liftweb.mapper._

class LiftMeasurementName
extends LiftMultiLanguageNameBase[LiftMeasurementName, LiftMeasurement]
   with IdPK
   with MeasurementName {
    override def getSingleton = LiftMeasurementName
    override def getReferenceObject() = LiftMeasurement
    override def getReferenceObjectIDName = "measurement_id"
}

object LiftMeasurementName
extends LiftMeasurementName 
   with MetaLiftMultiLanguageNameBase[LiftMeasurementName, LiftMeasurement]
   with MetaMeasurementName {
    override def dbTableName = "dshop_measurement_name"
    override def findOneInstance(id : Long) = {
        LiftMeasurementName.find(By(LiftMeasurementName.id, id)).openOr(null)
    }
}
