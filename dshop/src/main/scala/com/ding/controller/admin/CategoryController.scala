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
//import com.ding.model.lift._
import com.ding.controller._
import com.ding.util._
import net.liftweb.util.Helpers._
import net.liftweb.json._
import net.liftweb.json.JsonAST._

object CategoryController extends Controller[Category]{

    def metaModel : MetaCategory = MetaModels.metaCategory

//    def process() : Box[LiftResponse] = {
//        ShopLogger.debug("category controller works")
//        processAction(reqInfo.is.action)
//    }

    override def processAction(action : String) : Box[LiftResponse] = {
        action match {
            case "save" => {
                    save()
                }
            case "remove" => {
                    remove()
                }
            case "explore" => {
                    explore()
                }
            case "navigator" => {
                    navigator()
                }
            case "query" => {
                    query()
                }
            case "category" => {
                    categoryExplore()
                }
            case "product" => {
                    productExplore()
                }
            case _ => Full(NotFoundResponse())
        }
    }

    private def save() = {
        val reqstr = this.getRequestContent()
//        val reqstr = "[{\"id\":-1,\"parentId\":0,\"general\":[{\"langId\":22,\"name\":\"\",\"description\":\"\"},{\"langId\":23,\"name\":\"\",\"description\":\"\"}]}]"
        try {
            val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
//            ShopLogger.logger.debug(jsonList.toString)

            val (cat_id : Long, parent_id : Long, generals : Array[Tuple3[Long, String, String]]) =
                jsonList match {
                    case List(JObject(
                                JField("id", JInt(id)) ::
                                JField("parentId", JInt(parentId)) ::
                                JField("general", JArray(descriptions : List[JValue])
                                ) ::
                                Nil
                            )
                        ) => {
                            val descList = descriptions.flatMap {
                                description => {
                                    try {
                                        val desc_obj = description.asInstanceOf[JObject]
                                        val fields : Map[String, Any] = desc_obj.values
//                                        ShopLogger.logger.debug(fields.toString)
                                        val langId = fields("langId").asInstanceOf[BigInt].toLong
                                        val name = fields("name").asInstanceOf[String]
                                        val desc = fields("description").asInstanceOf[String]
                                        List((langId, name, desc))
//                                        List((1, "", ""))
                                    }
                                    catch {
                                        case ex : Exception => throw ex
                                    }
                                    
                                }
                            }
                            (id.toLong,parentId.toLong, descList.toArray)
                        }
                    case _ =>{
                            throw new Exception
                        }
                }


            val cat_item = if (cat_id  == -1) {
                metaModel.newInstance
            } else {
                metaModel.findOneInstance(cat_id)
            }
            if (cat_item == null) {
                Full(NotFoundResponse())
            } else {
                cat_item.setParentID(parent_id)
                cat_item.setActive(true)
                cat_item.setImage("no_image.png")
                if (cat_id == -1) {
                    cat_item.setDisplayOrder(metaModel.findOneInstance(cat_item.getParentID).children.length + 1)
                }
                generals.foreach {
                    general => {
                        cat_item.setName(general._1, general._2, general._3)
                    }
                }
                cat_item.saveInstance()
                this.getAllSubCategories(cat_item.getParentID, getDefaultLang)
            }
//            ShopLogger.logger.debug(cat_id.toString)
//            ShopLogger.logger.debug(parent_id.toString)
//            generals.foreach {
//                gen => {
//                    ShopLogger.logger.debug(gen._1.toString)
//                    ShopLogger.logger.debug(gen._2)
//                    ShopLogger.logger.debug(gen._3)
//                }
//            }
        }
        catch{
            case ex : Exception => {
                    ShopLogger.logger.debug(ex.getStackTraceString)
                    ShopLogger.logger.debug(ex.getMessage)
                    ex.getMessage match {
                        case "category not found" => {
                                Full(NotFoundResponse())
                            }
                        case _ => {
                                Full(BadResponse())
                            }
                    }
                }
            case _ => {
                    Full(BadResponse())
                }
        }
//        val cat = LiftCategory.find(By(LiftCategory.cat_id, 1)).open_!
//        cat.updateInstance(0, "cat", true, 11, (23, "CatChangeAgain", "cat ccccccccc"))
//        cat.save
        
    }

    private def query() = {
        val reqstr : String = this.getRequestContent()
        try {
            val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
            ShopLogger.logger.debug(jsonList.toString)

            val (cat_id : Long, parent_id) = jsonList match {
                case List(JObject(JField("id", JInt(id)) :: JField("parentId", JInt(pid)) :: Nil)) => {
                        (id.toLong, pid.toLong)
                    }
                case _ => {
                        throw (new Exception("bad request format"))
                    }
            }
            //val cat_id = -1
            //val parent_id = 0
            val item = if(cat_id == -1)
                metaModel.newInstance()
            else
                metaModel.findOneInstance(cat_id)
            if(item != null) {
//                val (parentId : Long, addTime : String, updateTime : String, active : Boolean, displayOrder : Int) = /*if( cat_id == -1) {
//                    (parent_id, "", "", true, 0)
//                } else */{
//                    (item.getParentID, item.getAddTime.toString, item.getUpdateTime.toString, item.getActive, item.getDisplayOrder)
//                }
                val parentId : Long = if(cat_id == -1) parent_id else item.getParentID
                val addTime : String = if(cat_id == -1) "" else item.getAddTime.toString
                val updateTime : String = if(cat_id == -1) "" else item.getUpdateTime.toString
                val active : Boolean = if(cat_id == -1) true else item.getActive
                val image : String = if(cat_id == -1) "" else item.getImage
                val displayOrder : Int = if(cat_id == -1) 0 else item.getDisplayOrder

                val supportedLangs : List[Language] = MetaModels.metaLanguage.findAllInstances
                val general : List[JValue] = supportedLangs.flatMap {
                    lang => {
                        val langId = lang.getID
                        val header = lang.getName
                        val name = item.getName(langId)
                        val desc = item.getDescription(langId)
                        List(JField("langId", JInt(langId))
                             ++
                             JField("header", JString(header))
                             ++
                             JField("name", JString(name))
                             ++
                             JField("description", JString(desc))
                        )
                    }
                }
                val result = JObject(JField("id", JInt(cat_id))
                                     ::
                                     JField("parentId", JInt(parentId))
                                     ::
                                     JField("addTime", JString(addTime))
                                     ::
                                     JField("updateTime", JString(updateTime))
                                     ::
                                     JField("active", JBool(active))
                                     ::
                                     JField("image", JString(image))
                                     ::
                                     JField("displayOrder", JInt(displayOrder))
                                     ::
                                     JField("general", JArray(general))
                                     ::
                                     Nil)
                Full(JsonResponse(JArray(result :: Nil)))
            } else {
                throw (new Exception("category not found"))
            }
        } catch {
            case ex : Exception => {
                    ShopLogger.logger.debug(ex.getStackTraceString)
                    ShopLogger.logger.debug(ex.getMessage)
                    ex.getMessage match {
                        case "category not found" => {
                                Full(NotFoundResponse())
                            }
                        case _ => {
                                Full(BadResponse())
                            }
                    }
                }
            case _ => {
                    Full(BadResponse())
                }
        }    
    }
    private def explore() = {

        val reqstr = this.getRequestContent()
//        val reqstr = "[{\"id\":20}, {\"language\":20}]"
//        val reqstr = "[{\"id\":20}]"
//        val reqstr = "[{\"language\":20}]"
//        val reqstr = "[{\"id\":0}]"
//        ShopLogger.logger.debug(reqstr)

        try {
            val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
//            ShopLogger.logger.debug(jsonList.toString)
            /*
             * categoryId 表示要查找该节点下的所有子结点
             * languageId 表示要显示的语言种类
             */
            val defaultLang : Long = 22
            val defaultParent : Long= 0
            val (categoryId : Long, languageId : Long) = jsonList match {
                case List(JObject(JField("id", JInt(id)) :: Nil), JObject(JField("language", JInt(lang_id)) :: Nil)) => {
                        (id.toLong, lang_id.toLong)
                    }
                case List(JObject(JField("id", JInt(id)) :: Nil)) => {
                        (id.toLong, defaultLang)
                    }
                case List(JObject(JField("language", JInt(lang_id)) :: Nil)) => {
                        (defaultParent, lang_id.toLong)
                    }
                case _ => {
                        (defaultParent, defaultLang)
                    }
            }
//            val categoryId : Long = matched._1
//            val languageId : Long = matched._2
//            ShopLogger.logger.debug("(" + categoryId.toString + ", " + languageId.toString + ")")
//            val cat_item = if(categoryId > 0) {
//                metaModel.findOneInstance(categoryId)
//            } else {
//                metaModel.newInstance
//            }
            getAllSubCategories(categoryId, languageId)
        }
        catch {
            case ex : Exception => {
//                    ShopLogger.logger.error(ex.getMessage)
//                    ShopLogger.logger.error(ex.getStackTraceString)
                    Full(BadResponse())
                }
        }
    }

    private def categoryExplore() = {
        val reqstr = if(this.getRequestContent().length > 0) this.getRequestContent() else "[{\"id\":0}]"
//        val reqstr = "[{\"id\":0}]"
        try {
            val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
            val defaultLang : Long = this.getDefaultLang()
            val defaultParent : Long= 0
            val (categoryId : Long, languageId : Long) = jsonList match {
                case List(JObject(JField("id", JInt(id)) :: Nil), JObject(JField("language", JInt(lang_id)) :: Nil)) => {
                        (id.toLong, lang_id.toLong)
                    }
                case List(JObject(JField("id", JInt(id)) :: Nil)) => {
                        (id.toLong, defaultLang)
                    }
                case List(JObject(JField("language", JInt(lang_id)) :: Nil)) => {
                        (defaultParent, lang_id.toLong)
                    }
                case _ => {
                        (defaultParent, defaultLang)
                    }
            }
            getAllSubCategories(categoryId, languageId)
        }
//        Full(NotFoundResponse())
    }

    private def productExplore() = {
        val reqstr = if(this.getRequestContent().length > 0) this.getRequestContent() else "[{\"id\":0}]"
//        val reqstr = "[{\"id\":0}]"
        try {
            val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
            val defaultLang : Long = this.getDefaultLang()
            val defaultParent : Long= 0
            val (categoryId : Long, languageId : Long) = jsonList match {
                case List(JObject(JField("id", JInt(id)) :: Nil), JObject(JField("language", JInt(lang_id)) :: Nil)) => {
                        (id.toLong, lang_id.toLong)
                    }
                case List(JObject(JField("id", JInt(id)) :: Nil)) => {
                        (id.toLong, defaultLang)
                    }
                case List(JObject(JField("language", JInt(lang_id)) :: Nil)) => {
                        (defaultParent, lang_id.toLong)
                    }
                case _ => {
                        (defaultParent, defaultLang)
                    }
            }
            getAllSubProducts(categoryId, languageId)
        }
    }

    private def remove() = {
        val reqstr = this.getRequestContent()
        ShopLogger.logger.debug(reqstr)
        try {
            val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
            val item = metaModel.findOneInstance(jsonList.head.asInstanceOf[JObject].values("id").asInstanceOf[BigInt].toLong)
            val parent_id =
                if (item != null)
                    item.getParentID
            else
                -1
            jsonList.foreach(
                item => {
                    val cat_id = item.asInstanceOf[JObject].values("id").asInstanceOf[BigInt].toLong
                    val cat_item = metaModel.findOneInstance(cat_id)
                    if(cat_item != null) {
                        cat_item.deleteInstance()
                    }
                }
            )
            this.getAllSubCategories(parent_id, this.getDefaultLang())
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
    
//    def getRequestContent() : String = {
//        /*
//         *  从Request对象内读出请求信息
//         *  [{cat_id : id_value}, {lang_id : id_value}]
//         *  如果没有cat_id，表示请求所有顶层的category
//         *  如果美欧lang_id， 表示使用默认语言
//         */
//        val req = S.request.open_!
//        val reqbody : Array[Byte] = req.body.openOr(Array())
//        val reqstr = new String(reqbody, "UTF-8")
//        reqstr
//    }

    private def getAllSubCategories(categoryId : Long, languageId : Long) = {
        val cat_children : List[Category] = metaModel.getChildren(categoryId)
        val resultList = cat_children.flatMap {
            case  item : Category => {
                    val cat_name = item.getName(languageId)
//                    val cat_desc = item.getDescription(languageId)
                    List(
                        JsonAST.JField("id", JsonAST.JInt(item.getID()))
                        ++
                        JsonAST.JField("name", JsonAST.JString(cat_name))
                        ++
                        JsonAST.JField("leaf", JsonAST.JBool(item.children().isEmpty))
                    )
                }
        }
//        ShopLogger.logger.debug("finished")
//        val pitem = metaModel.findOneInstance(categoryId)
//        val pname = if(pitem != null) pitem.getName(languageId) else "root"
//        val parentCat = JsonAST.JField("id", JsonAST.JInt(categoryId)) ++ JsonAST.JField("name", JsonAST.JString(pname))

        Full(JsonResponse(
                JsonAST.JObject(JsonAST.JField("category", JsonAST.JArray(resultList)) :: Nil)
            ))
    }

    private def getAllSubProducts(categoryId : Long, languageId : Long) = {
        val products : List[Product] = metaModel.getProducts(categoryId)
        val resultList = products.flatMap {
            product => {
                val product_name = product.getName(languageId)
                List(
                    JsonAST.JField("id", JsonAST.JInt(product.getID()))
                    ++
                    JsonAST.JField("name", JsonAST.JString(product_name))
                )
            }
        }
        Full(JsonResponse(
                JsonAST.JObject(JsonAST.JField("product", JsonAST.JArray(resultList)) :: Nil)
            ))
    }

    override def getRequestContent() = {
        urlDecode(S.param("json").openOr(""))
    }
}
