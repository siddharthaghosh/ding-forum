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
        val allproducts = getAllProduct(citem)
        val prolist = allproducts.flatMap {
            product => {
                makeProductNode(product)
            }
        }
//        <div>
//            <div>CategoryId: {cid.toString}</div>
//            <div>
//                Products:&nbsp;
//                {prolist}
//            </div>
//        </div>
        <div class="productShowcase">
            {prolist}
        </div>
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
        val titleNode = <div class="ding-topCategory-titlebar">{titleLink}</div>
        val childrenNode = category.children.flatMap {
            child => {
                make2ndLevelCategoryNode(child)
            }
        }
        val contentNode = <div class="ding-topCategory-content">{childrenNode}</div>
        val innerNode = titleNode ++ contentNode
        <div class="ding-topCategory">{innerNode}</div>
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
        val priceNode = <span>{price.toString}</span>
        val priceDivNode = <div class="price">{mpriceNode ++ priceNode}</div>
//        //按钮显示
//        val buyBtnNode : NodeSeq = <a target="_blank" href="#">{"buy"}</a>
        val buyBtnNode : NodeSeq = SHtml.link("/client/cart.html",
                                              () => {CartInfo.addItem(gid)},
                                              Text("Buy"),
                                              ("target", "_blank"))
        val btnDivNode : NodeSeq = <div class="toolbar">{buyBtnNode}</div>
        val resultNode = <div class="productCase">
            {imageDivNode ++ descDivNode ++ priceDivNode ++ btnDivNode}
                         </div>
        resultNode
    }
}
