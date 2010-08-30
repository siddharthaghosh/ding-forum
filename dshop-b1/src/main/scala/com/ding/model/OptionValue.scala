/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model

trait OptionValue extends Model {

    def getID() : Long
    def getName(lang_id : Long) : String
    def getGroupID() : Long
    def getDisplayOrder() : Int

    def setName(lang_id : Long, name : String)
    def setGroupID(id : Long)
    def setDisplayOrder(order : Int)
}

trait MetaOptionValue extends MetaModel[OptionValue]
