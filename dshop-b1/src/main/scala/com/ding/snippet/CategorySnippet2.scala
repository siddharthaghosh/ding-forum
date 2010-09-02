/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.snippet

import scala.xml.NodeSeq
import scala.xml.UnprefixedAttribute
import scala.xml.Null
import scala.xml.Text
import scala.xml.Elem
import scala.xml.MetaData
import com.ding.model._
import com.ding.util.MetaModels
import com.ding.util.LanguageUtils
import net.liftweb.http.S
import net.liftweb.http.SHtml
import net.liftweb.util.Helpers._
import net.liftweb.util.BindHelpers
import net.liftweb.util.Props
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

        //上一页的链接与节点
        val prePageUrl = selfURLWithoutPageInfo() + "&page=" + (prePage).toString
        //下一页的链接与节点
        val nextPageUrl = selfURLWithoutPageInfo() + "&page=" + (currentPage + 1).toString
        
        val currentPageNode = <div>{Text(currentPage.toString + "/" + totalPages.toString)}</div>
        
        def getPrePageLink(in : NodeSeq) : Box[Elem] = {
            if(prePage < 1) Empty else Full(SHtml.link(prePageUrl, ()=>{}, in))
        }
        def getNextPageLink(in : NodeSeq) : Box[Elem] = {
            if(nextPage > totalPages) Empty else Full(SHtml.link(nextPageUrl, ()=>{}, in))
        }

        bind("top-pager", kids,
             FuncBindParam("prevlink", kid => getPrePageLink(kid).map(_ % (BindHelpers.currentNode.map(_.attributes) openOr Null)) openOr NodeSeq.Empty),
             "curpage" -%> currentPageNode,
             FuncBindParam("nextlink", kid => getNextPageLink(kid).map(_ % (BindHelpers.currentNode.map(_.attributes) openOr Null)) openOr NodeSeq.Empty)
        )
    }

    def bottomPager(kids : NodeSeq) : NodeSeq = {
        
        //获取in节点的html属性
        def getNodeSeqAttr(in : NodeSeq) : MetaData = {
            in.headOption.map(_.attributes).getOrElse(Null)
        }

        val prevAttr = getNodeSeqAttr(kids \\ "prevlink")
        val nextAttr = getNodeSeqAttr(kids \\ "nextlink")
        val numAttr = getNodeSeqAttr(kids \\ "pagenumber")
        val ellipsisAttr = getNodeSeqAttr(kids \\ "ellipsis")

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

        //上一页的链接与节点
        val prePageUrl = selfURLWithoutPageInfo() + "&page=" + (prePage).toString
        //下一页的链接与节点
        val nextPageUrl = selfURLWithoutPageInfo() + "&page=" + (currentPage + 1).toString
        //当前页的链接与节点
        val currentPageUrl = selfURLWithoutPageInfo() + "&page=" + currentPage.toString
        //第一页的链接与节点
        val firstPageUrl = selfURLWithoutPageInfo() + "&page=1"
        //最后页的链接与节点
        val lastPageUrl = selfURLWithoutPageInfo() + "&page=" + totalPages.toString

        def getPrePageLink(in : NodeSeq) : Box[Elem] = {
            if(prePage < 1) Empty else Full(SHtml.link(prePageUrl, ()=>{}, in) % prevAttr)
        }
        def getNextPageLink(in : NodeSeq) : Box[Elem] = {
            if(nextPage > totalPages) Empty else Full(SHtml.link(nextPageUrl, ()=>{}, in) % nextAttr)
        }

        if(currentPage == 0) {
            <div />
        } else {


            def createNumberLink() : NodeSeq = {

                val currentPageNode = SHtml.link(currentPageUrl, ()=>{}, Text(currentPage.toString)) % numAttr
                val ellipsisDiv = (<div>{"..."}</div>) % ellipsisAttr
                val emptyNode = NodeSeq.Empty
                val firstPageLink = SHtml.link(firstPageUrl, ()=>{}, Text("1")) % numAttr
                val lastPageLink = SHtml.link(lastPageUrl, ()=>{}, Text(totalPages.toString)) % numAttr
                val prePageNumLink = SHtml.link(prePageUrl, ()=>{}, Text(prePage.toString)) % numAttr
                val nextPageNumLink = SHtml.link(nextPageUrl, ()=>{}, Text(nextPage.toString)) % numAttr
                
                val firstPageNode : NodeSeq = if(currentPage == 1 || prePage == 1) {
                    emptyNode
                } else {
                    firstPageLink
                }

                val lastPageNode : NodeSeq = if(currentPage == totalPages || nextPage == totalPages) {
                    emptyNode
                } else {
                    lastPageLink
                }

                val prePageNumNode : NodeSeq = if(currentPage == 1) {
                    emptyNode
                } else {
                    prePageNumLink
                }

                val nextPageNumNode : NodeSeq = if(currentPage == totalPages) {
                    emptyNode
                } else {
                    nextPageNumLink
                }

                val frontEllipsisNode : NodeSeq = if(prePage < 3) {
                    emptyNode
                } else {
                    ellipsisDiv
                }

                val endEllipsisNode : NodeSeq = if(nextPage > totalPages -2) {
                    emptyNode
                } else {
                    ellipsisDiv
                }

                firstPageNode ++
                frontEllipsisNode ++
                prePageNumNode ++ currentPageNode ++ nextPageNumNode ++
                endEllipsisNode ++
                lastPageNode
            }

            bind("bottom-pager", kids,
                 FuncBindParam("prevlink", kid => getPrePageLink(kid) openOr NodeSeq.Empty),
                 FuncBindParam("nextlink", kid => getNextPageLink(kid) openOr NodeSeq.Empty),
                 "ellipsis" -> NodeSeq.Empty,
                 "pagenumber" -> createNumberLink()
            )
        }
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
