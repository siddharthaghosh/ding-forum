/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model

trait OptionGroup extends Model {
    
    def allValues() : List[OptionValue]

    def getID() : Long
    def getName(lang_id : Long) : String
    def getDisplayOrder() : Int

    def setName(lang_id : Long, name : String)
    def setDisplayOrder(order : Int)
}

trait MetaOptionGroup extends MetaModel[OptionGroup] {
    
}
