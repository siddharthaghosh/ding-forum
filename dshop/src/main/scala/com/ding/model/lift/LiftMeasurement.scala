/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import com.ding.model._
import net.liftweb.mapper._

class LiftMeasurement
extends LiftBaseModel[LiftMeasurement] 
   with Measurement
   with LiftMultiLanguageName[LiftMeasurement, LiftMeasurementName]{
    
    override def  getSingleton = LiftMeasurement
    override def primaryKeyField = measurement_id
    override def multiLangNameObject() = LiftMeasurementName
    object measurement_id extends MappedLongIndex(this)
}

object LiftMeasurement extends LiftMeasurement with LiftMetaModel[LiftMeasurement] with MetaMeasurement {

    override def dbTableName = "dshop_measurement"
    override def findOneInstance(id : Long) = {
        LiftMeasurement.find(By(LiftMeasurement.measurement_id, id)).openOr(null)
    }

}