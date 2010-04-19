/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model

trait Product
extends BaseModel
   with MultiLanguageName
   with MultiLanguageDescription
   with DisplayOrder{

    def categories() : List[Category]

}

trait MetaProduct extends MetaModel[Product] {
    
}
