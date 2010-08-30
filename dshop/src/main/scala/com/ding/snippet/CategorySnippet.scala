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

class CategorySnippet {

    def metaModel = MetaModels.metaCategory
    val rootCatId = 0
    val rootCategory = metaModel.findOneInstance(rootCatId)

    def categoryMenu(kids: NodeSeq): NodeSeq = {
        val rootId = 0
        val rootCat = metaModel.findOneInstance(rootId)
        val childrenCats = rootCat.children()
        val childrenNodes = childrenCats.flatMap {
            childCat => {
                makeCategoryNode(childCat)
            }
        }
        <ul>
            {childrenNodes}
        </ul>
    }

    def productList(kids: NodeSeq) : NodeSeq = {
        val cid = S.param("catid").openOr("0").toLong
        val citem = MetaModels.metaCategory.findOneInstance(cid)
//        val allproducts = getAllProduct(citem)

        val orderby = S.param("orderby").openOr("default")

        def price_asc(a : Product, b : Product) : Boolean = {
            a.Goods.head.getPrice < b.Goods.head.getPrice
        }

        def add_desc(a : Product, b : Product) : Boolean = {
            a.getAddTime.after(b.getAddTime)
        }

        val allproducts = orderby match {
//            case "add_asc" => {
//                    getAllProduct(citem).sortWith(add_asc(_, _))
//                }
            case "add_desc" => {
                    getAllProduct(citem).sortWith(add_desc(_, _))
                }
            case "price_asc" => {
                    getAllProduct(citem).sortWith(price_asc(_, _))
                }
            case "price_desc" => {
                    getAllProduct(citem).sortWith(!price_asc(_, _))
                }
            case _ => getAllProduct(citem)
        }

        //从配置文件中获取每页显示商品数
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

        //获取当前请求页的商品节点
        val end = 0 + currentPage*itemsPerPage
        val start = end - itemsPerPage
        val endIndex = if(end > allproducts.length) allproducts.length else end
        val startIndex = if(start < allproducts.length) start else allproducts.length
        val prolist = allproducts.slice(startIndex, endIndex).flatMap {
            product => {
                makeProductNode(product)
            }
        }
        
        def PageToolNodes(pageToolPosition : String) : NodeSeq = {

            val prePage = currentPage - 1
            val nextPage = currentPage + 1
            //上一页的链接与节点
            val prePageUrl = selfURLWithoutPageInfo() + "&page=" + (prePage).toString
            val prePageLink = SHtml.link(prePageUrl, ()=>{}, Text("上一页"),("class", "prev"))
            //下一页的链接与节点
            val nextPageUrl = selfURLWithoutPageInfo() + "&page=" + (currentPage + 1).toString
            val nextPageLink = SHtml.link(nextPageUrl, ()=>{}, Text("下一页"), ("class", "next"))

            val pageToolNodes = if(pageToolPosition == "top-toolbar") {
                //上工具条中的页面工具
                val currentPageNode = <div class="curpage">{Text(currentPage.toString + "/" + totalPages.toString)}</div>

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
                
            } else if(pageToolPosition == "bottom-toolbar") {
                //下工具条中的页面工具
                val numberPageClass = "page-n"
                val firstPageUrl = selfURLWithoutPageInfo() + "&page=1"
                val firstPageLink = SHtml.link(firstPageUrl, ()=>{}, Text("1"), ("class", numberPageClass))
                val lastPageUrl = selfURLWithoutPageInfo() + "&page=" + totalPages.toString
                val lastPageLink = SHtml.link(lastPageUrl, ()=>{}, Text(totalPages.toString), ("class", numberPageClass))
                val prePageNumLink = SHtml.link(prePageUrl, ()=>{}, Text(prePage.toString), ("class", numberPageClass))
                val nextPageNumLink = SHtml.link(nextPageUrl, ()=>{}, Text(nextPage.toString), ("class", numberPageClass))
                val currentPageNumDiv = <div class={numberPageClass}>{currentPage.toString}</div>
                val ellipsisDiv = <div class="ellipsis">{"..."}</div>
                val emptyNode = Text("")

                if(currentPage == 0) {
                    <div />
                } else {
                    val prePageNode : NodeSeq = if(currentPage == 1) {
                        emptyNode
                    } else {
                        prePageLink
                    }

                    val nextPageNode : NodeSeq = if(currentPage == totalPages) {
                        emptyNode
                    } else {
                        nextPageLink
                    }

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

                    val currentPageNode : NodeSeq = currentPageNumDiv

                    prePageNode ++ firstPageNode ++
                    frontEllipsisNode ++
                    prePageNumNode ++ currentPageNode ++ nextPageNumNode ++
                    endEllipsisNode ++
                    lastPageNode ++ nextPageNode
                }
//                {prePageLink ++ firstPageLink} ++ ellipsisDiv ++ {prePageNumLink ++ currentPageNumDiv ++ nextPageNumLink} ++ ellipsisDiv ++ {lastPageLink ++ nextPageLink}
            } else {
                //位置信息错误
                <div />
            }

            <div class="pager">{pageToolNodes}</div>
        }

        val priceOrderAscUrl = makeCategoryURL(citem) + "&orderby=price_asc"
        val priceOrderDescUrl = makeCategoryURL(citem) + "&orderby=price_desc"
//        val addOrderAscUrl = makeCategoryURL(citem) + "&orderby=add_asc"
        val addOrderDescUrl = if(orderby == "add_desc") "" else (makeCategoryURL(citem) + "&orderby=add_desc")

        val priceOrderUrl = if(orderby == "price_asc") priceOrderDescUrl else priceOrderAscUrl
        val priceOrderClass = if(orderby == "price_asc") "price-desc" else "price-asc"
        val addOrderClass = if(orderby == "add_desc") "addtime ding-button-helper-disabled" else "addtime"
//        val priceOrderAscLink = SHtml.link(priceOrderAscUrl, ()=>{}, Text("价格升序"))
//        val priceOrderDescLink = SHtml.link(priceOrderDescUrl, ()=>{}, Text("价格降序"))
        val priceOrderLink = SHtml.link(priceOrderUrl, ()=>{}, Text("价格"), ("class", priceOrderClass))
//        val addOrderAscLink = SHtml.link(addOrderAscUrl, ()=>{}, Text("上架升序"))
        val addOrderDescLink = SHtml.link(addOrderDescUrl, ()=>{}, Text("上架时间"), ("class", addOrderClass))
        val orderLinks = <div class="sort">
 {/*priceOrderAscLink ++ priceOrderDescLink  ++ addOrderAscLink */ priceOrderLink ++ addOrderDescLink}
                         </div>
        val topPageLinks = PageToolNodes("top-toolbar")
        val bottomPageLinks = PageToolNodes("bottom-toolbar")

        val topToolsNode = <div class="top-toolbar">{
                topPageLinks  ++ orderLinks /*  ++ Text(selfURLWithoutPageInfo()) */
            }</div>
        val bottomToolsNode = <div class="bottom-toolbar">{
                bottomPageLinks
            }</div>

        val prolistContainer = <div class="thumbcontainer">{prolist}</div>
        val showcaseNode = <div class="ding-product-showcase">
            { 
                topToolsNode ++  prolistContainer ++ bottomToolsNode
            }
                           </div>
        /* toolsNode ++  */showcaseNode
    }

    def allCategory(kids : NodeSeq) : NodeSeq = {
        val childrenCats1stLev = rootCategory.children()
        val FirstLevNodes = childrenCats1stLev.flatMap {
            childCat => {
                make1stLevelCategoryNode(childCat)
            }
        }
        FirstLevNodes
    }

    private def make1stLevelCategoryNode(category : Category) : NodeSeq = {
    
        val title = category.getName(LanguageUtils.getDefaultLang())
        val titleUrl = makeCategoryURL(category)
        val titleLink = <a href={titleUrl}>{title}</a>
        val titleNode = <div class="titlebar">{titleLink}</div>
        val childrenNode = category.children.flatMap {
            child => {
                make2ndLevelCategoryNode(child)
            }
        }
        val contentNode = <div class="content">{childrenNode}</div>
        val innerNode = titleNode ++ contentNode
        <div class="topcategory">{innerNode}</div>
    }
    
    private def make2ndLevelCategoryNode(category : Category) : NodeSeq = {
        val title = category.getName(LanguageUtils.getDefaultLang())
        val titleUrl = makeCategoryURL(category)
        val titleLink = <a href={titleUrl}>{title}</a>
        val titleNode = <dt>{titleLink}</dt>
        val childrenNode = category.children.flatMap {
            child => {
                make3rdLevelCategoryNode(child)
            }
        }
        val contentNode = <dd>{childrenNode}</dd>
        val innerNode = titleNode ++ contentNode
        <dl>{innerNode}</dl>
    }

    private def make3rdLevelCategoryNode(category : Category) : NodeSeq = {
        val title = category.getName(LanguageUtils.getDefaultLang())
        val titleUrl = makeCategoryURL(category)
        val titleLink : NodeSeq = <a href={titleUrl}>{title}</a>
        titleLink
        <span>{titleLink}</span>
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

    private def makeCategoryNode(category : Category) : NodeSeq = {
        val name = category.getName(LanguageUtils.getDefaultLang)
        val url = makeCategoryURL(category)
        val node1 = <li><a href={url}>{name}</a></li>
        val childrenCats = category.children()
        if(childrenCats.length > 0) {
            val childrenNodes = childrenCats.flatMap {
                childCat => {
                    makeCategoryNode(childCat)
                }
            }
//            <ul>{childrenNodes}</ul>
            val node2 : NodeSeq = <li><a href={url}>{name}</a><ul>{childrenNodes}</ul></li>
            node2
        } else {
            <li><a href={url}>{name}</a></li>
        }
//        node1
    }

    private def makeCategoryURL(category : Category) : String = {
        val params = "?catid=" + category.getID.toString
        val baseurl = "/client/category.html"
        baseurl + params
    }

    private def selfURLWithoutPageInfo() : String = {
//        S.request.open_!.request.url
//        val params = S.request.open_!.params
//        var paramstr = ""
//        params.foreach {
//            param => {
//                val pname = param._1
//                if(pname != "page") {
//                    val pvalue = param._2.head
//                    paramstr += (pname + "=" + pvalue + "&")
//                }
//            }
//        }
//        paramstr = paramstr.substring(0, paramstr.length - 1)
        val catidparam = "catid=" + S.param("catid").openOr("0")
        val orderparam = if(S.param("orderby").isEmpty) "" else "&orderby=" + S.param("orderby").open_!
        S.request.open_!.uri + "?" + catidparam + orderparam
    }

    private def makeProductURL(product : Product) : String = {
        val params = "?productid=" + product.getID.toString
        val baseurl = "/client/product.html"
        baseurl + params
    }

    private def makePictureURL(pic : String) : String = {

        val params = "?filename=" + urlEncode(pic)
        val baseurl = "/image/thumbnail/image"
        baseurl + params
    }

    private def makeProductNode(product : Product) : NodeSeq = {

        val desc = product.getName(LanguageUtils.getDefaultLang)
        val imageUrl = product.getFirstImageFileLocation
        val mprice = product.Goods.head.getMarketPrice
        val price = product.Goods.head.getPrice
        val gid = product.Goods.head.getID
        //图片显示
        val imageNode = <img src={makePictureURL(imageUrl)} alt={desc}></img>
        val imageLinkNode = <a target="_blank" href={makeProductURL(product)}>{imageNode}</a>
        val imageDivNode = <div class="picture">{imageLinkNode}</div>
        //描述显示
        val descLinkNode = <a target="_blank" href={makeProductURL(product)}>{desc}</a>
        val descDivNode = <div class="brief">{descLinkNode}</div>
        //价格显示
        val mpriceNode = <span>{mprice.toString}</span>
        val mpriceLabelNode = <label>市场价:</label>
        val mpriceDivNode = <div class="marketprice">{mpriceLabelNode ++ mpriceNode}</div>
        val vipPriceNode = <span>{price.toString}</span>
        val vipPriceLabelNode = <label>会员价:</label>
        val vipPriceDivNode = <div class="vipprice">{vipPriceLabelNode ++ vipPriceNode}</div>
        val priceDivNode = <div class="price">{mpriceDivNode ++ vipPriceDivNode}</div>
//        //按钮显示
//        val buyBtnNode : NodeSeq = <a target="_blank" href="#">{"buy"}</a>
//        val buyBtnNode : NodeSeq = SHtml.link("/client/cart.html",
//                                              () => {CartInfo.addItem(gid)},
//                                              Text("Buy"),
//                                              ("target", "_blank"))
//        val btnDivNode : NodeSeq = <div class="toolbar">{buyBtnNode}</div>
        val resultNode = <div class="productthumb">
            {imageDivNode ++ descDivNode ++ priceDivNode /* ++ btnDivNode */}
                         </div>
        resultNode
    }
}
