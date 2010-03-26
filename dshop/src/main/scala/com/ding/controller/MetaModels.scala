/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.controller
import com.ding.model._
import com.ding.model.lift._

object MetaModels {
    lazy val metaLanguage : MetaLanguage = LiftLanguage
    lazy val metaCategory : MetaCategory = LiftCategory
    lazy val metaOptionGroup : MetaOptionGroup = LiftOptionGroup
    lazy val metaOptionValue : MetaOptionValue = LiftOptionValue
}

