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
    def getDisplayOrder(categoryId : Int) : Int
    def setDisplayOrder(categoryId : Int, order : Int)

}

trait MetaProduct extends MetaModel[Product] {
    
}
