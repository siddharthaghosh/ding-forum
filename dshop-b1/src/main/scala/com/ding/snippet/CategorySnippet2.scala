/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.snippet

import scala.xml.NodeSeq
import scala.xml.UnprefixedAttribute
import scala.xml.Null
import com.ding.model._
import com.ding.util.MetaModels
import com.ding.util.LanguageUtils
import net.liftweb.http.S
import net.liftweb.http.SHtml
import net.liftweb.util.Helpers._
import net.liftweb.util.BindHelpers
import net.liftweb.util.Props
import scala.xml.Text
import scala.xml.Elem
import net.liftweb.common.Empty
import net.liftweb.common.Full
import net.liftweb.common.Box

class CategorySnippet2 {

    def metaModel = MetaModels.metaCategory
    val rootCatId = 0
    val rootCategory = metaModel.findOneInstance(rootCatId)

    def topPager(kids : NodeSeq) : NodeSeq = {
        val cid = S.param("catid").openOr("0").toLong
        val citem = MetaModels.metaCategory.findOneInstance(cid)
        val allproducts = getAllProduct(citem)
        val itemsPerPage = Props.get("ProductNumberInOnePage").openOr("9").toInt
        //计算总页数
        val totalPages = (allproducts.length / itemsPerPage) + (if((allproducts.length % itemsPerPage) > 0) 1 else 0)
        //从请求参数中获得当前请求页
        val currentPage = if(totalPages == 0)
            0
        else {
            val cpage = S.param("page").openOr("1").toInt
            if(cpage < 1)
                1
            else
                cpage
        }
        
        val prePage = currentPage - 1
        val nextPage = currentPage + 1

        val emptyNode : NodeSeq = Text("")

        //上一页的链接与节点
        val prePageUrl = selfURLWithoutPageInfo() + "&page=" + (prePage).toString
        //下一页的链接与节点
        val nextPageUrl = selfURLWithoutPageInfo() + "&page=" + (currentPage + 1).toString
        
        val currentPageNode = <div>{Text(currentPage.toString + "/" + totalPages.toString)}</div>
        
        def getPrePageLink(in : NodeSeq) : Box[Elem] = {
            if(prePage < 1) Empty else Full(SHtml.link(prePageUrl, ()=>{}, in))
        }
        def getNextPageLink(in : NodeSeq) : Box[Elem] = {
            if(nextPage > totalPages) Empty else Full(SHtml.link(nextPageUrl, ()=>{}, Text("下一页")))
        }

        bind("top-pager", kids,
             FuncBindParam("prelink", kid => getPrePageLink(kid).map(_ % (BindHelpers.currentNode.map(_.attributes) openOr Null)) openOr NodeSeq.Empty),
             "curpage" -%> currentPageNode,
             FuncBindParam("nextlink", kid => getNextPageLink(kid).map(_ % (BindHelpers.currentNode.map(_.attributes) openOr Null)) openOr NodeSeq.Empty)
        )
    }

    def sortButton(kids : NodeSeq) : NodeSeq = {

        val cid = S.param("catid").openOr("0").toLong
        val citem = MetaModels.metaCategory.findOneInstance(cid)

        val priceOrderAscUrl = makeCategoryURL(citem) + "&orderby=price_asc"
        val priceOrderDescUrl = makeCategoryURL(citem) + "&orderby=price_desc"
        val orderby = S.param("orderby").openOr("default")

        val priceOrderClass = if(orderby == "price_asc") "price-desc" else "price-asc"
        val priceOrderUrl = if(orderby == "price_asc") priceOrderDescUrl else priceOrderAscUrl

        def getPriceLink(in : NodeSeq) : Box[Elem] = {
            Full(SHtml.link(priceOrderUrl, ()=>{}, in, ("class", priceOrderClass)))
        }

        val addOrderDescUrl = if(orderby == "add_desc") "" else (makeCategoryURL(citem) + "&orderby=add_desc")
        val addOrderClass = if(orderby == "add_desc") "addtime ding-button-helper-disabled" else "addtime"
        def getAddTimeLink(in : NodeSeq) : Box[Elem] = {
            Full(SHtml.link(addOrderDescUrl, ()=>{}, in, ("class", addOrderClass)))
        }

        bind("sort-button", kids,
             FuncBindParam("price", kid => getPriceLink(kid).map(_ % (BindHelpers.currentNode.map(_.attributes) openOr Null)) openOr NodeSeq.Empty),
             FuncBindParam("addtime", kid => getAddTimeLink(kid).map(_ % (BindHelpers.currentNode.map(_.attributes) openOr Null)) openOr NodeSeq.Empty)
        )
    }

    private def getAllProduct(category : Category) : List[Product] = {
        val resultList : List[Product] = if(category.children.length > 0) {
            category.children.flatMap(
                subcat => {
                    getAllProduct(subcat)
                }
            )
        } else {
            category.products
        }
        resultList
    }

    private def selfURLWithoutPageInfo() : String = {
        val catidparam = "catid=" + S.param("catid").openOr("0")
        val orderparam = if(S.param("orderby").isEmpty) "" else "&orderby=" + S.param("orderby").open_!
        S.request.open_!.uri + "?" + catidparam + orderparam
    }

    private def makeCategoryURL(category : Category) : String = {
        val params = "?catid=" + category.getID.toString
        val baseurl = "/client/category.html"
        baseurl + params
    }
}
