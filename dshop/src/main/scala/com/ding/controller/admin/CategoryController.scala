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

    def processAction(action : String) : Box[LiftResponse] = {
        action match {
            case "add" => {
                    Full(OkResponse())
                }
            case "edit" => {

                    Full(OkResponse())
                }
            case "delete" => {
                    Full(OkResponse())
                }
            case "list" => {
                    val cat = LiftCategory.find(By(LiftCategory.cat_id, 1)).open_!
                    val dlist = cat.findDescriptions
                    dlist.foreach(
                        desc => {
                            ShopLogger.debug(desc.name.is)
                            ShopLogger.debug(desc.description.is)
                        }
                    )
                    Full(OkResponse())
            }
            case _ => Full(NotFoundResponse())
        }
    }

}
