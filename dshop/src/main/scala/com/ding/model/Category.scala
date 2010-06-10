/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model

import java.util.Date

trait Category
extends ImageBaseModel 
   with DisplayOrder
   with MultiLanguageName
   with MultiLanguageDescription{
    
    def updateInstance(parent_id : Long, image : String, active : Boolean, display_order : Int, descriptions : Tuple3[Long, String, String]*)

    def children() : List[Category]

    def products() : List[Product]

    def addProduct(pid : Long)

    def getID() : Long
    def getParentID() : Long
    def getUpdateTime() : Date
    def getAddTime() : Date
//    def getName(lang_id : Long) : String
//    def getDescription(lang_id : Long) : String
    def getActive() : Boolean
    def getType() : Long
//    def getImage() : String
//    def getDisplayOrder() : Int

    def setParentID(id : Long)
    def setUpdateTime(date : Date)
    def setAddTime(date : Date)
//    def setName(lang_id : Long, name : String, desc : String*)
//    def setDescription(lang_id : Long, desc : String)
    def setActive(active : Boolean)
//    def setImage(image : String)
//    def setDisplayOrder(order : Int)
    def setType(t : Long)
}

trait MetaCategory extends MetaModel[Category] {
    def getChildren(parentId : Long) : List[Category]
    def getProducts(parentId : Long) : List[Product]
    def getAllAncestor(categoryId : Long) : List[Category]
}
