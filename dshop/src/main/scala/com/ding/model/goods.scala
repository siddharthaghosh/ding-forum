/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model

trait Goods extends BaseModel{
    def getProductID() : Long
}

trait MetaGoods extends MetaModel[Goods] {
    
}
