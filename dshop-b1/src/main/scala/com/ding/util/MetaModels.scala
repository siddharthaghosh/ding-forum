/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.util
import com.ding.model._
import com.ding.model.lift._

object MetaModels {
    lazy val metaLanguage : MetaLanguage = LiftLanguage
    lazy val metaCategory : MetaCategory = LiftCategory
    lazy val metaOptionGroup : MetaOptionGroup = LiftOptionGroup
    lazy val metaOptionValue : MetaOptionValue = LiftOptionValue
    lazy val metaManufacturer : MetaManufacturer = LiftManufacturer
    lazy val metaProduct : MetaProduct = LiftProduct
    lazy val metaGoods : MetaGoods = LiftGoods
    lazy val metaUploadFile : MetaUploadFile = LiftUploadFile
    lazy val metaType : MetaType = LiftType
    lazy val metaMeasurement : MetaMeasurement = LiftMeasurement
}
