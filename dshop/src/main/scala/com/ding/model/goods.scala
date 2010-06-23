/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model

trait Goods extends BaseModel{
    def getProductID() : Long

    def getStore() : Int
    def setStore(store : Int)

    def getBn() : String
    def setBn(bn : String)

    def getStorePlace() : String
    def setStorePlace(sp : String)

    def getWeight() : Double
    def setWeight(w : Double)

    def getCost() : Double
    def setCost(cost : Double)

    def getMarketPrice() : Double
    def setMarketPrice(mp : Double)

    def getPrice() : Double
    def setPrice(price : Double)

    def getOption() : String
    def setOption(op : String)
}

trait MetaGoods extends MetaModel[Goods] {
    
}
