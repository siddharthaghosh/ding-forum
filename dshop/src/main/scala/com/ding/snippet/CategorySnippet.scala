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

class CategorySnippet {

    def metaModel = MetaModels.metaCategory
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
                <li>
                    <a href={makeProductURL(product)}>{product.getName(LanguageUtils.getDefaultLang)}</a>
                </li>
            }
        }
        <div>
            <div>CategoryId: {cid.toString}</div>
            <div>
                Products:&nbsp;
                {prolist}
            </div>
        </div>
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
}
