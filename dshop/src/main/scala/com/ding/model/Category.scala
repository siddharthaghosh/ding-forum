/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model

import java.util.Date

trait Category extends Model {
    
    def updateInstance(parent_id : Long, image : String, active : Boolean, display_order : Int, descriptions : Tuple3[Long, String, String]*)

    def children() : List[Category]

    def getID() : Long
    def getParentID() : Long
    def getUpdateTime() : Date
    def getAddTime() : Date
    def getName(lang_id : Long) : String
    def getDescription(lang_id : Long) : String
    def getActive() : Boolean
    def getImage() : String
    def getDisplayOrder() : Int

    def setParentID(id : Long)
    def setUpdateTime(date : Date)
    def setAddTime(date : Date)
    def setName(lang_id : Long, name : String)
    def setDescription(lang_id : Long, desc : String)
    def setActive(active : Boolean)
    def setImage(image : String)
    def setDisplayOrder(order : Int)
}

trait MetaCategory extends MetaModel[Category] {
    def getChildren(parentId : Long) : List[Category]
    def getAllAncestor(categoryId : Long) : List[Category]
}
