/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.controller.admin

import net.liftweb.http._
import net.liftweb.common._
import net.liftweb.json._
import net.liftweb.mapper._
import com.ding.model._
import com.ding.model.lift._
import com.ding.controller._
import com.ding.util._

object CategoryController {

    def metaModel : MetaCategory = LiftCategory

    def process() : Box[LiftResponse] = {
        ShopLogger.debug("category controller works")
        processAction(reqInfo.is.action)
    }

    private def processAction(action : String) : Box[LiftResponse] = {
        action match {
            case "add" => {
                    Full(OkResponse())
                }
            case "save" => {

                    Full(OkResponse())
                }
            case "delete" => {
                    Full(OkResponse())
                }
            case "list" => {
                    val cat = LiftCategory.find(By(LiftCategory.cat_id, 1)).open_!
//                    val dlist = cat.findDescriptions
//                    dlist.foreach(
//                        desc => {
//                            ShopLogger.logger.debug("%s %s", desc.name.is, desc.description.is)
//                            //ShopLogger.debug("%s %s", desc.name.is, desc.description.is)
//                        }
//                    )
                    val desc = cat.findDescriptionByLang(LiftLanguage.find(By(LiftLanguage.lang_id, 24)).openOr(null))
                    if(desc == null)
                        ShopLogger.logger.debug("description not found")
                    else
                        ShopLogger.logger.debug("%s %s", desc.name.is, desc.description.is)
                    Full(OkResponse())
            }
            case _ => Full(NotFoundResponse())
        }
    }

    private def save() = {
        Full(OkResponse())
    }

    private def list() = {
        Full(OkResponse())
    }
}
