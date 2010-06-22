/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model

trait Product
extends ImageBaseModel
   with MultiLanguageName
   with MultiLanguageDescription
//   with DisplayOrder
   with Active {

    def categories() : List[Category]
    def addToCategory(categoryId : Long)
    def getDisplayOrder(categoryId : Long) : Int
    def setDisplayOrder(categoryId : Long, order : Int)
    def getParameter() : String
    def setParameter(param : String)
    def getExtensionProperties() : Array[Int]
    def setExtensionProperties(resultArr : Array[Int])
    def setExtensionProperty(index : Int, value : Int)
    def getOptions() : String
    def setOptions(opt : String)
    def getUsingOption() : Boolean
    def setUsingOption(using : Boolean)
    def Goods() : List[Goods]
    def addGoods(gid : Long)
    def removeGoods(gid : Long)
    def removeAllGoods()
    val ExtensionPropertyNum : Int

}

trait MetaProduct extends MetaModel[Product] {
    
}
