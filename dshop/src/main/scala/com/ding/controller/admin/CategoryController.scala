/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.controller.admin

import net.liftweb.http._
import net.liftweb.common._
import net.liftweb.json._
import net.liftweb.mapper._
import com.ding.model._
import com.ding.model.lift._
import com.ding.controller._
import com.ding.util._
import net.liftweb.json._
import net.liftweb.json.JsonAST._

object CategoryController {

    def metaModel : MetaCategory = LiftCategory

    def process() : Box[LiftResponse] = {
        ShopLogger.debug("category controller works")
        processAction(reqInfo.is.action)
    }

    private def processAction(action : String) : Box[LiftResponse] = {
        action match {
            case "add" => {
                    Full(OkResponse())
                }
            case "save" => {
                    save()
                }
            case "delete" => {
                    Full(OkResponse())
                }
            case "list" => {
                    val cat = LiftCategory.find(By(LiftCategory.cat_id, 1)).open_!
                    val desc = cat.findDescriptionByLang(LiftLanguage.find(By(LiftLanguage.lang_id, 24)).openOr(null))
                    if(desc == null)
                        ShopLogger.logger.debug("description not found")
                    else
                        ShopLogger.logger.debug("%s %s", desc.name.is, desc.description.is)
                    Full(OkResponse())
            }
            case "category" => {
                    categoryList()
            }
            case "navigator" => {
                    navigator()
            }
            case _ => Full(NotFoundResponse())
        }
    }

    private def save() = {
        val cat = LiftCategory.find(By(LiftCategory.cat_id, 1)).open_!
        cat.updateInstance(0, "cat", true, 11, (23, "CatChangeAgain", "cat ccccccccc"))
        cat.save
        Full(OkResponse())
    }

    private def categoryList() = {
        /*
         *  从Request对象内读出请求信息
         *  [{cat_id : id_value}, {lang_id : id_value}]
         *  如果没有cat_id，表示请求所有顶层的category
         *  如果美欧lang_id， 表示使用默认语言
         */
        val req = S.request.open_!
        val reqbody : Array[Byte] = req.body.openOr(Array())
//        val reqstr = new String(reqbody, "UTF-8")
//        val reqstr = "[{\"id\":20}, {\"language\":20}]"
//        val reqstr = "[{\"id\":20}]"
//        val reqstr = "[{\"language\":20}]"
        val reqstr = "[]"
        ShopLogger.logger.debug(reqstr)

        try {
            val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
            /*
             * categoryId 表示要查找该节点下的所有子结点
             * languageId 表示要显示的语言种类
             */
            val defaultLang : Long = 22
            val defaultParent : Long= 0
            val (categoryId : Long, languageId : Long) = jsonList match {
                case List(JObject(JField("id", JInt(id)) :: Nil), JObject(JField("language", JInt(lang_id)) :: Nil)) => {
                        (id, lang_id)
                }
                case List(JObject(JField("id", JInt(id)) :: Nil)) => {
                        (id, defaultLang)
                }
                case List(JObject(JField("language", JInt(lang_id)) :: Nil)) => {
                        (defaultParent, lang_id)
                }
                case _ => {
                        (defaultParent, defaultLang)
                }
            }
            ShopLogger.logger.debug("(" + categoryId.toString + ", " + languageId.toString + ")")
//            val cat_item = if(categoryId > 0) {
//                metaModel.findOneInstance(categoryId)
//            } else {
//                metaModel.newInstance
//            }

            val cat_children : List[Category] = metaModel.getChildren(categoryId)
            val resultList = cat_children.flatMap {
                case  item : Category => {
                        val cat_name = item.getName(languageId)
                        val cat_desc = item.getDescription(languageId)
                        List(
                            JsonAST.JField("id", JsonAST.JInt(item.getID()))
                            ++
                            JsonAST.JField("name", JsonAST.JString(cat_name))
                            ++
                            JsonAST.JField("description", JsonAST.JString(cat_desc))
                        )
                    }
            }
            Full(JsonResponse(
                    JsonAST.JArray(resultList)
                ))
        }
        catch {
            case ex : Exception => {
                    ShopLogger.error(ex.getMessage)
                    ShopLogger.error(ex.getStackTraceString)
                    Full(BadResponse())
                }
        }
    }

    private def navigator() = {
        val reqstr = "[]"
        ShopLogger.logger.debug(reqstr)
        try {
            val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
            /*
             * categoryId 表示要查找该节点下的所有子结点
             * languageId 表示要显示的语言种类
             */
            val defaultLang : Long = 22
            val defaultCat : Long= 0
            val (categoryId : Long, languageId : Long) = jsonList match {
                case List(JObject(JField("id", JInt(id)) :: Nil), JObject(JField("language", JInt(lang_id)) :: Nil)) => {
                        (id, lang_id)
                }
                case List(JObject(JField("id", JInt(id)) :: Nil)) => {
                        (id, defaultLang)
                }
                case List(JObject(JField("language", JInt(lang_id)) :: Nil)) => {
                        (defaultCat, lang_id)
                }
                case _ => {
                        (defaultCat, defaultLang)
                }
            }
            val ancestors = metaModel.getAllAncestor(categoryId)
            val resultList = ancestors.flatMap {
                case  item : Category => {
                        val cat_name = item.getName(languageId)
                        List(
                            JsonAST.JField("id", JsonAST.JInt(item.getID()))
                            ++
                            JsonAST.JField("name", JsonAST.JString(cat_name))
                        )
                    }
            }
            Full(JsonResponse(
                    JsonAST.JArray(resultList)
                ))
        } catch {
            case ex : Exception => {
                    ShopLogger.error(ex.getMessage)
                    ShopLogger.error(ex.getStackTraceString)
                    Full(BadResponse())
                }
        }
    }
}
