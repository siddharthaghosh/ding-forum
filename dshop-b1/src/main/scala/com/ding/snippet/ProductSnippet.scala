/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.snippet

import scala.xml.NodeSeq
import com.ding.model._
import com.ding.util.MetaModels
import com.ding.util.LanguageUtils
import net.liftweb.http.S
import net.liftweb.http.SHtml
import scala.xml.Text

class ProductSnippet {

    def productDetail(kid : NodeSeq) : NodeSeq = {
        val pid = S.param("productid").openOr("-1").toLong
        val pitem = MetaModels.metaProduct.findOneInstance(pid)
        if(pitem != null) {
            val pname = pitem.getName(LanguageUtils.getDefaultLang)
            <div>{"Product Name: "}{pname}</div>
        } else {
            <div></div>
        }
    }

    def productGoodsList(kid : NodeSeq) : NodeSeq = {
        val pid = S.param("productid").openOr("-1").toLong
        val pitem = MetaModels.metaProduct.findOneInstance(pid)
        if(pitem != null) {
            val pname = pitem.getName(LanguageUtils.getDefaultLang)
            val usingOption = pitem.getUsingOption
            val allGoods = pitem.Goods
            if(usingOption) {
                <div>
                    {
                        allGoods.flatMap {
                            gitem => {
                                val gid = gitem.getID
                                val gprice = gitem.getPrice
                                val gweight = gitem.getWeight
                                val gstore = gitem.getStore
                                val options = gitem.getOption
                                <div>
                                    GoodsID: {gid.toString}<br/>
                                    Price: {gprice.toString}<br/>
                                    Weight: {gweight.toString}<br/>
                                    Stroe: {gstore.toString}{
                                        if(gstore > 0) {
                                            SHtml.link(makeBuyLink(gitem), () => {CartInfo.addItem(gid)}, Text("Buy"))
//                                            <a href={makeBuyLink(gitem)}>Buy</a>
                                        }
                                    }<br/>
                                    options: {options}<br/>
                                </div>
                            }
                        }
                    }
                </div>
            } else {
                val gitem = allGoods.head
                val gid = gitem.getID
                val gprice = gitem.getPrice
                val gweight = gitem.getWeight
                val gstore = gitem.getStore
                <div>
                    GoodsID: {gid.toString}<br/>
                    Price: {gprice.toString}<br/>
                    Weight: {gweight.toString}<br/>
                    Stroe: {gstore.toString}{
                        if(gstore > 0) {
                            SHtml.link(makeBuyLink(gitem), () => {CartInfo.addItem(gid)}, Text("Buy"))
//                            <a href={makeBuyLink(gitem)}>Buy</a>
                        }
                    }<br/>
                </div>
            }
        } else {
            <div></div>
        }
    }

    private def makeBuyLink(goods : Goods) : String = {

        val params = S.request.open_!.params.keys.flatMap {
            key => {
                val pvalue = S.param(key).openOr("")
                val pstr = key + "=" + pvalue
                pstr :: Nil
            }
        }
        var pstrs : String = "?"
//        params.foreach {
//            param => {
//                pstrs = pstrs + param + "&"
//            }
//        }
//        pstrs = pstrs.substring(0, pstrs.length - 1)
        pstrs = pstrs + "productid=" + S.param("productid").openOr("")
        S.request.open_!.uri + pstrs
    }
}
