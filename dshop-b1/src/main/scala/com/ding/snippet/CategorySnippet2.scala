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
import net.liftweb.util.Helpers._
import net.liftweb.util.Props
import scala.xml.Text

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

        val emptyNode = Text("")

        //上一页的链接与节点
        val prePageUrl = selfURLWithoutPageInfo() + "&page=" + (prePage).toString
        val prePageLink = if(prePage < 1) emptyNode else SHtml.link(prePageUrl, ()=>{}, Text("上一页"),("class", "prev"))
        //下一页的链接与节点
        val nextPageUrl = selfURLWithoutPageInfo() + "&page=" + (currentPage + 1).toString
        val nextPageLink = if(nextPage > totalPages) emptyNode else SHtml.link(nextPageUrl, ()=>{}, Text("下一页"), ("class", "next"))
        
        val currentPageNode = <div>{Text(currentPage.toString + "/" + totalPages.toString)}</div>
        val tmpNode1 = if(prePage < 1) {
            currentPageNode
        } else {
            prePageLink ++ currentPageNode
        }
        val tmpNode2 = if(nextPage > totalPages) {
            tmpNode1
        } else {
            tmpNode1 ++ nextPageLink
        }
        tmpNode2
        bind("top-pager", kids,
             "prelink" -> ((in : NodeSeq) => {prePageLink}),
             "curpage" -> ((in : NodeSeq) => {
                    currentPageNode
                }),
             "nextlink" -> ((in : NodeSeq) => {nextPageLink})
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
}
