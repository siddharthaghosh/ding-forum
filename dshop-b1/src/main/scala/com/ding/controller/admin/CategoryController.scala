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

import java.io.File

object CategoryController extends ModelController[Category]{

    def metaModel : MetaCategory = MetaModels.metaCategory

//    def process() : Box[LiftResponse] = {
//        ShopLogger.debug("category controller works")
//        processAction(reqInfo.is.action)
//    }

    override def processAction(action : String) : Box[LiftResponse] = {
        action match {
            case "save" => {
                    categorySave()
                }
//            case "remove" => {
//                    remove()
//                }
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
            case "categoryname" => {
                    queryCategoryName()
                }
            case "productname" => {
                    queryProductName()
                }
            case "productimage" => {
                    queryProductImage()
                }
            case "producttype" => {
                    queryProductType()
                }
            case "productparameter" => {
                    queryProductParameter()
                }
            case "productextensionproperty" => {
                    queryProductExtensionProperty()
                }
            case "productoption" => {
                    queryProductOption()
                }
//            case "subcategory" => {
//                    categoryExplore()
//                }
            case "savecategory" => {
                    categorySave()
                }
            case "saveproduct" => {
                    productSave()
                }
            case "saveproductdisplayorder" => {
                    productSaveDisplayOrder()
                }
            case "saveproductparameter" => {
                    productSaveParameter()
                }
            case "saveproductextensionproperty" => {
                    productSaveExtensionProperty()
                }
            case "saveproductoption" => {
                    productSaveOption()
                }
            case "openproductoption" => {
                    productOpenOption()
                }
            case "closeproductoption" => {
                    productCloseOption()
                }
            case "addproductoptiongroup" => {
                    productAddOptionGroup()
                }
            case "removeproductoptiongroup" => {
                    productRemoveOptionGroup()
                }
            case "addproductoptionvalue" => {
                    productAddOptionValue()
                }
            case "removeproductoptionvalue" => {
                    productRemoveOptionValue()
                }
            case "saveproductgoods" => {
                    productSaveGoods()
                }
            case "removeproductgoods" => {
                    productRemoveGoods()
                }
            case "removecategory" => {
                    categoryRemove()
                }
            case "removeproduct" => {
                    productRemove()
                }
//            case "temp" => {
//                    tempProc()
//                }
            case _ => categoryExplore()
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
                        cat_item.setName(general._1, general._2/* , general._3 */)
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
            val defaultParent : Long= -1
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
        try {
            val defaultLang : Long = this.getDefaultLang()
            val defaultParent : Long= -1
            val jobj = this.getJsonObjectFromRequest()
            val catId = if(jobj.values.keySet.contains("id")) jobj.values("id").asInstanceOf[BigInt].toLong else defaultParent
            val langId = if(jobj.values.keySet.contains("langId")) jobj.values("langId").asInstanceOf[BigInt].toLong else defaultLang
            getAllSubCategories(catId, langId)
        }
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

    private def queryCategoryName() = {
        val reqstr = this.getRequestContent()
//        val reqstr = "[{\"id\":1}]"
        try {
            val jsonList = JsonParser.parse(reqstr).asInstanceOf[JArray].arr
            val jsonObj = jsonList.head.asInstanceOf[JObject]
            val id = jsonObj.values("id").asInstanceOf[BigInt].toLong
            val item = if(id == -1)
                metaModel.newInstance()
            else
                metaModel.findOneInstance(id)
            if(item != null) {
                val supportedLangs : List[Language] = MetaModels.metaLanguage.findAllInstances
                val name : List[JObject] = supportedLangs.flatMap {
                    lang => {
                        val langId = lang.getID
                        val header = lang.getName
                        val code = lang.getCode
                        val name = item.getName(langId)
//                        val desc = item.getDescription(langId)
                        JObject(JField("langId", JInt(langId))
                                ::
                                JField("header", JString(header))
                                ::
                                JField("code", JString(code))
                                ::
                                JField("name", JString(name))::Nil
//                             ++
//                             JField("description", JString(desc))
                        )::Nil
                    }
                }
                Full(JsonResponse(JObject(JField("name", JArray(name)) :: Nil)))
            }else {
                Full(NotFoundResponse())
            }
        }
//        Full(NotFoundResponse())
    }

    private def queryProductName() = {
        val reqstr = this.getRequestContent()
//        val reqstr = "[{\"id\":-1}]"
        try {
            val jsonList = JsonParser.parse(reqstr).asInstanceOf[JArray].arr
            val jsonObj = jsonList.head.asInstanceOf[JObject]
            val id = jsonObj.values("id").asInstanceOf[BigInt].toLong
            val item = if(id == -1)
                MetaModels.metaProduct.newInstance()
            else
                MetaModels.metaProduct.findOneInstance(id)
            if(item != null) {
                val supportedLangs : List[Language] = MetaModels.metaLanguage.findAllInstances
                val name : List[JObject] = supportedLangs.flatMap {
                    lang => {
                        val langId = lang.getID
                        val header = lang.getName
                        val code = lang.getCode
                        val name = item.getName(langId)
//                        val desc = item.getDescription(langId)
                        JObject(JField("langId", JInt(langId))
                                ::
                                JField("header", JString(header))
                                ::
                                JField("code", JString(code))
                                ::
                                JField("name", JString(name))::Nil
//                             ++
//                             JField("description", JString(desc))
                        )::Nil
                    }
                }
                Full(JsonResponse(JObject(JField("name", JArray(name)) :: Nil)))
            }else {
                Full(NotFoundResponse())
            }
        }
//        Full(NotFoundResponse())
    }

    private def queryProductImage() = {
        try{
            val id = this.getIdFromResquest()
//            val id = 2
            val item = if(id == -1)
                MetaModels.metaProduct.newInstance()
            else
                MetaModels.metaProduct.findOneInstance(id)
            if(item != null){
                val jsonStr = item.getImage
                val imageArr = try {
                    JsonParser.parse(jsonStr).asInstanceOf[JArray]
                } catch {
                    case ex : Exception => {
                            JsonParser.parse("[\"/nopic.gif\"]").asInstanceOf[JArray]
                        }
                }
                val resultArr = JArray(imageArr.arr.flatMap {
                        imageItem => {
                            val fn = imageItem.values.asInstanceOf[String]
                            val f = new File(fn)
                            val realName = f.getName
                            val uf = MetaModels.metaUploadFile.findByRealName(realName)
                            val displayName = if(uf!=null) {
                                uf.getDisplayName
                            } else {
                                "nopic.gif"
                            }
                            JObject(JField("name", JString(displayName))::JField("image", imageItem)::Nil) :: Nil
                        }
                    })
                val result = JObject(JField("image", resultArr) :: Nil)
                Full(JsonResponse(result))
            } else {
                Full(NotFoundResponse())
            }
        }
//        Full(NotFoundResponse())
    }

    private def queryProductType() = {
        try {
            val id = this.getIdFromResquest
            val p = MetaModels.metaProduct.findOneInstance(id)
            if(p != null) {
                val tid = p.categories.head.getType
                Full(JsonResponse(JObject(JField("typeId", JInt(tid))::Nil)))
            } else {
                Full(NotFoundResponse())
            }
        }
    }

    private def queryProductParameter() = {
        try {
            val id = this.getIdFromResquest
            val p = MetaModels.metaProduct.findOneInstance(id)
            if(p != null) {
                val code = this.getDefaultLangCode
                val pstr = if(p.getParameter != null && p.getParameter.length > 0) p.getParameter else "[]"
                val result = JsonParser.parse(pstr)
                val resultj = JArray(JString(code) :: result :: Nil)
                Full(JsonResponse(resultj))
//                Full(JsonResponse(JObject(JField("typeId", JString(p.getParameter))::Nil)))
            } else {
                Full(NotFoundResponse())
            }
        }
    }

    private def queryProductExtensionProperty() = {
        try {
            val id = this.getIdFromResquest
            val p = MetaModels.metaProduct.findOneInstance(id)
            if(p != null) {
                val eps = p.getExtensionProperties.flatMap {
                    ep => {
                        JInt(ep) :: Nil
                    }
                }
                Full(JsonResponse(JObject(JField("extensionProperty", JArray(eps.toList))::Nil)))
            } else {
                Full(NotFoundResponse())
            }
        }
    }

    private def queryProductOption() = {
        try {
            val jobj = this.getJsonObjectFromRequest()
            val id = jobj.values("id").asInstanceOf[BigInt].toLong
//            val id = this.getIdFromResquest
            val p = MetaModels.metaProduct.findOneInstance(id)
            if(p != null) {
                val usingOption = p.getUsingOption
                val tmpOptions = JsonParser.parse(p.getOptions).asInstanceOf[JArray].arr
                val options = JArray(tmpOptions.flatMap {
                        option => {
                            val gid = option.asInstanceOf[JObject].values("groupId").asInstanceOf[BigInt].toLong
                            val gitem = MetaModels.metaOptionGroup.findOneInstance(gid)
                            val gname = if(gitem == null) "NoSuchGroup" else gitem.getName(this.getDefaultLang)
                            val valueArr = option.asInstanceOf[JObject].values("value").asInstanceOf[List[BigInt]]
                            val values = valueArr.flatMap {
                                value => {
                                    val name = MetaModels.metaOptionValue.findOneInstance(value.toLong).getName(this.getDefaultLang)
                                    JObject(JField("id", JInt(value)) ::
                                            JField("name", JString(name)) ::
                                            Nil) :: Nil
                                }
                            }
                            JObject(JField("group", JObject(JField("id", JInt(gid)) :: JField("name", JString(gname)) :: Nil)) ::
                                    JField("value", JArray(values)) ::
                                    Nil) :: Nil
                        }
                    })
//                val options = JsonParser.parse(p.getOptions).asInstanceOf[JArray]

                val allGoods = p.Goods
                val goodsArr = JArray(allGoods.flatMap {
                        goods => {
                            val id = JField("id", JInt(goods.getID))
                            val store = JField("store", JInt(goods.getStore))
                            val bn = JField("bn", JString(goods.getBn))
                            val storep = JField("storePlace", JString(goods.getStorePlace))
                            val gw = goods.getWeight
                            println(gw)
                            val weight = JField("weight", if(goods.getWeight == null) { JNull } else {JDouble(goods.getWeight)})
                            val cost = JField("cost", if(goods.getCost == null) { JNull } else {JDouble(goods.getCost)})
                            val marketp = JField("marketPrice", if(goods.getMarketPrice == null) { JNull } else {JDouble(goods.getMarketPrice)})
                            val price = JField("price", JDouble(goods.getPrice))
                            val optionList = JsonParser.parse(if(goods.getOption == null) "[]" else goods.getOption).asInstanceOf[JArray].arr
                            val option = JField("option", JArray(optionList.flatMap {
                                        option => {
                                            val optionItem = option.asInstanceOf[JObject]
                                            val gid = optionItem.values("groupId").asInstanceOf[BigInt]
                                            val vid = optionItem.values("valueId").asInstanceOf[BigInt]
                                            val gname = MetaModels.metaOptionGroup.findOneInstance(gid.toLong).getName(this.getDefaultLang)
                                            val vname = MetaModels.metaOptionValue.findOneInstance(vid.toLong).getName(this.getDefaultLang)
                                            JObject(JField("group", JObject(JField("id", JInt(gid)) :: JField("name", JString(gname)) :: Nil)) ::
                                                    JField("value", JObject(JField("id", JInt(vid)) :: JField("name", JString(vname)) :: Nil)) ::
                                                    Nil) ::
                                            Nil
                                        }
                                    }))
                            JObject(id :: store :: bn :: storep :: weight :: cost :: marketp :: price :: option :: Nil) :: Nil
                        }
                    })
                val resultobj = JObject(JField("isOptionUsing", JBool(usingOption)) :: JField("option", options) :: JField("goods", goodsArr) :: Nil)
                Full(JsonResponse(resultobj))
            } else {
                Full(NotFoundResponse())
            }
        }
    }

//    private def tempProc() = {
//        try {
//            val jobj = this.getJsonObjectFromRequest()
//            val id = jobj.values("id").asInstanceOf[BigInt].toLong
//            val item = metaModel.findOneInstance(id)
//            if(item != null) {
//                item.addProduct(2)
//                item.saveInstance
//            }
//            Full(OkResponse())
//        }
//    }

    private def categorySave() = {
        try {
            val jobj = this.getJsonObjectFromRequest()
            val id = jobj.values("id").asInstanceOf[BigInt].toLong
            val item = if(id == -1){
                if(jobj.values.keySet.contains("parentId")) {
                    val pid = jobj.values("parentId").asInstanceOf[BigInt].toLong
                    val pitem = metaModel.findOneInstance(pid)
                    if(pitem != null) {
                        if((pitem.getType == -1) && (pitem.products.isEmpty) || (pitem.getID == 0)) {
                            metaModel.newInstance()
                        }else {
                            null
                        }
                    } else {
                        null
                    }
                }
                else {
                    null
                }
            }
            else
                metaModel.findOneInstance(id)
            if(item != null) {
                if(item.getID == -1) {
                    this.saveCategoryParent(item, jobj)
                }
                this.saveCategoryActive(item, jobj)
                this.saveCategoryName(item, jobj)
                this.saveCategoryImage(item, jobj)
                this.saveCategoryDisplayOrder(item, jobj)
                this.saveCategoryType(item, jobj)
                item.saveInstance
            }
//            val pid = if(item.getParentID >= 0) item.getParentID else 0
            val pids = urlDecode(S.param("id").openOr("0")).toDouble.toInt
            val pid = if(pids >= 0) pids else 0
            this.getAllSubCategories(-1, this.getDefaultLang)
        }
    }

    private def getProductItemById(id : Long) = {
        val item = if (id == -1) {
            null
        } else {
            MetaModels.metaProduct.findOneInstance(id)
        }
        item
    }

    private def getProductItemByIdNotExistCreate(id : Long, cid : Long) = {
        val item = if(id == -1){
            val citem = metaModel.findOneInstance(cid)
            if(citem != null) {
                if((citem.children.isEmpty) || (citem.getID == 0)) {
                    val i = MetaModels.metaProduct.newInstance()
                    i.saveInstance
                    i
                }else {
                    null
                }
            }else {
                null
            }
        }
        else
            MetaModels.metaProduct.findOneInstance(id)
        item
    }

    private def productSave() = {
        try {
            val jobj = this.getJsonObjectFromRequest()
            val id = jobj.values("id").asInstanceOf[BigInt].toLong
            val cid = jobj.values("categoryId").asInstanceOf[BigInt].toLong
            val item = if(id == -1){
                val citem = metaModel.findOneInstance(cid)
                if(citem != null) {
                    if((citem.children.isEmpty) || (citem.getID == 0)) {
                        val i = MetaModels.metaProduct.newInstance()
                        i.saveInstance
                        i
                    }else {
                        null
                    }
                }else {
                    null
                }               
            }
            else
                MetaModels.metaProduct.findOneInstance(id)
            if(item != null) {
//                if(item.getID == -1) {
//                    this.saveCategoryParent(item, jobj)
//                }
                this.saveProductCategory(item, jobj)
                this.saveProductActive(item, jobj)
                this.saveProductName(item, jobj)
                this.saveProductImage(item, jobj)
//                this.saveProductExtensionProperty(item, jobj)
//                this.saveProductParameter(item, jobj)
//                this.saveProductGoods(item, jobj)
//                this.saveCategoryDisplayOrder(item, jobj)
                if(id == -1) {
                    val newGoods = MetaModels.metaGoods.newInstance
                    newGoods.saveInstance
                    item.addGoods(newGoods.getID)

                }
                item.saveInstance
            }
//            val pid = if(item.getParentID >= 0) item.getParentID else 0

            getAllSubProducts(cid, this.getDefaultLang)
        }
    }

    private def productSaveDisplayOrder() = {
        val jobj = this.getJsonObjectFromRequest()
        val cid = jobj.values("categoryId").asInstanceOf[BigInt].toLong
        if(jobj.values.keySet.contains("displayOrder")){
            val products : List[Product] = metaModel.getProducts(cid)
            val displayArr = jobj.values("displayOrder").asInstanceOf[List[Map[String, _]]]
            products.foreach {
                product => {
                    val displayitem = displayArr.find(
                        item => {
                            item("id").asInstanceOf[BigInt].toLong == product.getID
                        }
                    )
                    if(!displayitem.isEmpty){
                        val order = (displayitem.get)("displayOrder").asInstanceOf[BigInt].toInt
                        product.setDisplayOrder(cid, order)
                        product.saveInstance
                    }
                    
//               product.setDisplayOrder(cid, order)
                }
            }
        }
        getAllSubProducts(cid, this.getDefaultLang)
    }

    private def productSaveParameter() = {
        try {
            val jobj = this.getJsonObjectFromRequest()
            val id = jobj.values("id").asInstanceOf[BigInt].toLong
            val cid = jobj.values("categoryId").asInstanceOf[BigInt].toLong
            val item = if(id == -1){
                val citem = metaModel.findOneInstance(cid)
                if(citem != null) {
                    if((citem.children.isEmpty) || (citem.getID == 0)) {
                        val i = MetaModels.metaProduct.newInstance()
                        i.saveInstance
                        i
                    }else {
                        null
                    }
                }else {
                    null
                }
            }
            else
                MetaModels.metaProduct.findOneInstance(id)
            
            if(item != null) {
                this.saveProductParameter(item, jobj)
                item.saveInstance
            }

        }
        queryProductParameter()
    }

    private def productAddOptionGroup() = {
        try {
            val jobj = this.getJsonObjectFromRequest()
            val id = jobj.values("id").asInstanceOf[BigInt].toLong
            val item = this.getProductItemById(id)
            if(item != null) {
                val resultId = this.addProductOptionGroup(item, jobj)
                if(resultId <=0 || !item.saveInstance) {
                    throw new Exception
                }else {
                    val gname = MetaModels.metaOptionGroup.findOneInstance(resultId).getName(this.getDefaultLang)
                    Full(JsonResponse(JObject(JField("id", JInt(resultId)) :: JField("name", JString(gname)) :: JField("error", JString("")) :: Nil)))
                }

            }else {
                throw new Exception
            }
        }
        catch {
            case e : Exception => {
                    val errMsg = "please delete all goods"
                    Full(JsonResponse(JObject(JField("id", JInt(-1)) :: JField("name", JString("")) :: JField("error", JString(errMsg)) :: Nil)))
                }
        }
    }

    private def productRemoveOptionGroup() = {
        try {
            val jobj = this.getJsonObjectFromRequest()
            val id = jobj.values("id").asInstanceOf[BigInt].toLong
            val item = this.getProductItemById(id)
            if(item != null) {
                val resultId = this.removeProductOptionGroup(item, jobj)
                if(resultId <=0 || !item.saveInstance) {
                    throw new Exception
                }else {
                    val gname = MetaModels.metaOptionGroup.findOneInstance(resultId).getName(this.getDefaultLang)
                    Full(JsonResponse(JObject(JField("id", JInt(resultId)) :: JField("name", JString(gname)) :: JField("error", JString("")) :: Nil)))
                }

            }else {
                throw new Exception
            }
        }
        catch {
            case e : Exception => {
                    val errMsg = "please delete all goods"
                    Full(JsonResponse(JObject(JField("id", JInt(-1)) :: JField("name", JString("")) :: JField("error", JString(errMsg)) :: Nil)))
                }
        }
    }

    private def productAddOptionValue() = {
        try {
            val time1 = System.currentTimeMillis()
            val jobj = this.getJsonObjectFromRequest()
            val id = jobj.values("id").asInstanceOf[BigInt].toLong
            val item = this.getProductItemById(id)
            if(item != null) {
                val resultId = this.addProductOptionValue(item, jobj)
                if(resultId <=0 || !item.saveInstance) {
                    throw new Exception
                }else {
                    val vname = MetaModels.metaOptionValue.findOneInstance(resultId).getName(this.getDefaultLang)
                    val jresponse = JsonResponse(JObject(JField("id", JInt(resultId)) :: JField("name", JString(vname)) :: JField("error", JString("")) :: Nil))
                    val time2 = System.currentTimeMillis()
                    println("outer : " + (time2-time1).toString + "ms")
                    Full(jresponse)
                }

            }else {
                throw new Exception
            }
        }
        catch {
            case e : Exception => {
                    val errMsg = "add value error"
                    Full(JsonResponse(JObject(JField("id", JInt(-1)) :: JField("name", JString("")) :: JField("error", JString(errMsg)) :: Nil)))
                }
        }
    }

    private def productRemoveOptionValue() = {
        try {
            val time1 = System.currentTimeMillis()
            val jobj = this.getJsonObjectFromRequest()
            val id = jobj.values("id").asInstanceOf[BigInt].toLong
            val item = this.getProductItemById(id)
            if(item != null) {
                val resultId = this.removeProductOptionValue(item, jobj)
                if(resultId <=0 || !item.saveInstance) {
                    throw new Exception
                }else {
                    val vname = MetaModels.metaOptionValue.findOneInstance(resultId).getName(this.getDefaultLang)
                    val jresponse = JsonResponse(JObject(JField("id", JInt(resultId)) :: JField("name", JString(vname)) :: JField("error", JString("")) :: Nil))
                    val time2 = System.currentTimeMillis()
                    println("outer : " + (time2-time1).toString + "ms")
                    Full(jresponse)
                }

            }else {
                throw new Exception
            }
        }
        catch {
            case e : Exception => {
                    val errMsg = "remove value error"
                    Full(JsonResponse(JObject(JField("id", JInt(-1)) :: JField("name", JString("")) :: JField("error", JString(errMsg)) :: Nil)))
                }
        }
    }

    private def productSaveGoods() = {
        try {
            val jobj = this.getJsonObjectFromRequest()
            val id = jobj.values("id").asInstanceOf[BigInt].toLong
            val item = this.getProductItemById(id)
            if(item != null) {
                saveProductGoods(item, jobj)
                item.saveInstance
            }
            queryProductOption()
        }catch {
            case e : Exception => {
                    e.printStackTrace
                    queryProductOption()
                }
        }
        finally {
            queryProductOption()
        }      
    }

    private def productRemoveGoods() = {
        try {
            val jobj = this.getJsonObjectFromRequest()
            val id = jobj.values("id").asInstanceOf[BigInt].toLong
            val item = this.getProductItemById(id)
            if(item != null) {
                removeProductGoods(item, jobj)
                item.saveInstance
            }
            queryProductOption()
        }catch {
            case e : Exception => {
                    e.printStackTrace
                    queryProductOption()
                }
        }
        finally {
            queryProductOption()
        }  
    }

    private def productOpenOption() = {
        try {
            val jobj = this.getJsonObjectFromRequest()
            val id = jobj.values("id").asInstanceOf[BigInt].toLong
//            val cid = jobj.values("categoryId").asInstanceOf[BigInt].toLong
            val item = this.getProductItemById(id)
            if(item != null) {
                this.openProductOption(item, jobj)
                item.saveInstance
            }
        }
        queryProductOption()
    }

    private def productCloseOption() = {
        try {
            val jobj = this.getJsonObjectFromRequest()
            val id = jobj.values("id").asInstanceOf[BigInt].toLong
//            val cid = jobj.values("categoryId").asInstanceOf[BigInt].toLong
            val item = this.getProductItemById(id)
            if(item != null) {
                this.closeProductOption(item, jobj)
                item.saveInstance
            }
        }
        queryProductOption()
    }

    private def productSaveOption() = {
        try {
            val jobj = this.getJsonObjectFromRequest()
            val id = jobj.values("id").asInstanceOf[BigInt].toLong
            val cid = jobj.values("categoryId").asInstanceOf[BigInt].toLong
            val item = if(id == -1){
                val citem = metaModel.findOneInstance(cid)
                if(citem != null) {
                    if((citem.children.isEmpty) || (citem.getID == 0)) {
                        val i = MetaModels.metaProduct.newInstance()
                        i.saveInstance
                        i
                    }else {
                        null
                    }
                }else {
                    null
                }
            }
            else
                MetaModels.metaProduct.findOneInstance(id)

            if(item != null) {
                this.saveProductOption(item, jobj)
                item.saveInstance
            }

        }
        queryProductOption()
    }

    private def productSaveExtensionProperty() = {
        try {
            val jobj = this.getJsonObjectFromRequest()
            val id = jobj.values("id").asInstanceOf[BigInt].toLong
            val cid = jobj.values("categoryId").asInstanceOf[BigInt].toLong
            val item = if(id == -1){
                val citem = metaModel.findOneInstance(cid)
                if(citem != null) {
                    if((citem.children.isEmpty) || (citem.getID == 0)) {
                        val i = MetaModels.metaProduct.newInstance()
                        i.saveInstance
                        i
                    }else {
                        null
                    }
                }else {
                    null
                }
            }
            else
                MetaModels.metaProduct.findOneInstance(id)

            if(item != null) {
                this.saveProductExtensionProperty(item, jobj)
                item.saveInstance
            }

        }
        queryProductExtensionProperty()
    }

    private def categoryRemove() = {
        try {
            val jobj = this.getJsonObjectFromRequest()
//            val ids = urlDecode(S.param("id").openOr("-5"))
//            val id = ids.toDouble.toLong
            val id = jobj.values("id").asInstanceOf[BigInt].toLong
            val item = metaModel.findOneInstance(id)
            if(item != null) {
                val pid = item.getParentID
                val ps = metaModel.getProducts(item.getID)
                ps.foreach {
                    pr => {
                        for(i <- (1 to pr.ExtensionPropertyNum)) {
                            pr.setExtensionProperty(i, -1)
                        }
                        pr.saveInstance
                    }
                }
                item.deleteInstance()
                this.getAllSubCategories(-1, this.getDefaultLang)
            } else {
                Full(NotFoundResponse())
            }
//            Full(NotFoundResponse())
        }
    }

    private def productRemove() = {
        val jobj = this.getJsonObjectFromRequest()
        val cid = jobj.values("id").asInstanceOf[BigInt].toLong
        val citem = metaModel.findOneInstance(cid)
        if(citem != null) {
            val reqstr = this.getRequestContent
            val ps = JsonParser.parse(reqstr).asInstanceOf[JArray].arr(1).asInstanceOf[JArray].arr
            ps.foreach {
                p => {
                    val pid = p.asInstanceOf[JObject].values("id").asInstanceOf[BigInt].toLong
                    citem.removeProduct(pid)
                }
            }
            this.getAllSubProducts(cid, this.getDefaultLang)
        } else {
            Full(NotFoundResponse())
        }
    }

    private def saveCategoryParent(item : Category, jobj : JObject) {
        if(jobj.values.keySet.contains("parentId") && jobj.values("parentId") != null ) {
            val parentId = jobj.values("parentId").asInstanceOf[BigInt].toLong
            if(item.getParentID != parentId)
                item.setParentID(parentId)
        }
    }

    private def saveProductCategory(item : Product, jobj : JObject) {
        if(jobj.values.keySet.contains("categoryId")) {
            val categoryId = jobj.values("categoryId").asInstanceOf[BigInt].toLong
            val catItem = metaModel.findOneInstance(categoryId)
            if(catItem != null) {
                catItem.addProduct(item.getID)
            }
        }
    }

    private def saveCategoryActive(item : Category, jobj : JObject) {
        if(jobj.values.keySet.contains("active")  && jobj.values("active") != null ) {
            val active = jobj.values("active").asInstanceOf[Boolean]
            if(item.getActive != active) {
                item.setActive(active)
            }
        }
    }

    private def saveCategoryType(item : Category, jobj : JObject) {
        if(jobj.values.keySet.contains("typeId") && jobj.values("typeId") != null ) {
            val t = jobj.values("typeId").asInstanceOf[BigInt]
            item.setType(t.toLong)
        }
    }

    private def saveProductActive(item : Product, jobj : JObject) {
        if(jobj.values.keySet.contains("active")) {
            val active = jobj.values("active").asInstanceOf[Boolean]
            if(item.getActive != active) {
                item.setActive(active)
            }
        }
    }

    private def saveProductParameter(item : Product, jobj : JObject) {
        if(jobj.values.keySet.contains("parameter")) {
            val param = jobj.values("parameter").asInstanceOf[String]
            item.setParameter(param)
        }
    }

    private def saveProductExtensionProperty(item : Product, jobj : JObject) {
        if(jobj.values.keySet.contains("extensionProperty")) {
            val eps = jobj.values("extensionProperty").asInstanceOf[List[BigInt]]
            for(i <- (1 to item.ExtensionPropertyNum)) {
                if((i - 1) < eps.length) {
                    item.setExtensionProperty(i, eps(i-1).toInt)
                }
            }
        }
    }

    private def saveProductOption(item : Product, jobj : JObject) {
        if(jobj.values.keySet.contains("isOptionUsing")) {
            val optUsing = jobj.values("isOptionUsing").asInstanceOf[Boolean]
            if(optUsing) {
                item.setUsingOption(true)
                val opt = jobj.children.find(
                    f => {
                        val itemf = f.asInstanceOf[JField]
                        itemf.name == "option"
                    }
                )
                val valueArr = opt.get.asInstanceOf[JField].value
                val valueStr = Printer.pretty(JsonAST.render(valueArr))
                item.setOptions(valueStr)
            }else {
                item.setUsingOption(false)
                item.setOptions("[]")
                item.removeAllGoods()
                val g = MetaModels.metaGoods.newInstance
                g.saveInstance
                item.addGoods(g.getID)
            }
        }
    }

    private def openProductOption(item : Product, jobj : JObject) {
        val openBefore = item.getUsingOption
        if(!openBefore) {
            item.setUsingOption(true)
            item.setOptions("[]")
            item.removeAllGoods()
        }
    }

    private def closeProductOption(item : Product, jobj : JObject) {
        val openBefore = item.getUsingOption
        if(openBefore) {
            item.setUsingOption(false)
            item.setOptions("[]")
            item.removeAllGoods()
            val g = MetaModels.metaGoods.newInstance
            g.saveInstance
            item.addGoods(g.getID)
        }
    }

    private def addProductOptionGroup(item : Product, jobj : JObject) : Long = {
        val failedId = -1
        //商品开启option控制否, 未开启不能添加规格
        if(!item.getUsingOption) {
            return failedId
        }
        //商品在开启规格控制的情况下，如果已经具有货品，不能更改规格
        if(!item.Goods.isEmpty) {
            return failedId
        }
        try {
            val addGroupId = jobj.values("groupId").asInstanceOf[BigInt].toLong
            val groupList = JsonParser.parse(item.getOptions).asInstanceOf[JArray].arr
            //商品当前的规格配置中是否已经存在此规格
            val oldGroup = groupList.find(
                group => {
                    val gid = group.asInstanceOf[JObject].values("groupId").asInstanceOf[BigInt].toLong
                    gid == addGroupId
                }
            )
            //已经存在此规格，不能重复添加
            if(!oldGroup.isEmpty)
                return failedId
            //该规格是否在商品所属类型配置中
            val tid = item.categories.head.getType
            val typeItem = MetaModels.metaType.findOneInstance(tid)
            if(typeItem == null)
                return failedId
            val supportGroups = typeItem.getAllSupportOptionGroup
            val existGroup = supportGroups.find(
                group => {
                    group.getID == addGroupId
                }
            )
            //所属类型没有配置此选项组，不可添加
            if(existGroup.isEmpty) {
                return failedId
            }
            val jgroup = JObject(JField("groupId", JInt(addGroupId)) :: JField("value", JArray(Nil)) :: Nil)
            val newGroupList = JArray(groupList ::: (jgroup :: Nil))
            item.setOptions(Printer.pretty(JsonAST.render(newGroupList)))
            addGroupId
        }
        catch {
            case e : Exception => {
                    failedId
                }
        }

    }

    private def removeProductOptionGroup(item : Product, jobj : JObject) : Long = {
        val failedId = -1
        //商品开启option控制否, 未开启不能删除规格
        if(!item.getUsingOption) {
            return failedId
        }
        //商品在开启规格控制的情况下，如果已经具有货品，不能更改规格
        if(!item.Goods.isEmpty) {
            return failedId
        }
        try {
            val removeGroupId = jobj.values("groupId").asInstanceOf[BigInt].toLong
            val groupList = JsonParser.parse(item.getOptions).asInstanceOf[JArray].arr
            //商品当前的规格配置中是否已经存在此规格
            val oldGroup = groupList.find(
                group => {
                    val gid = group.asInstanceOf[JObject].values("groupId").asInstanceOf[BigInt].toLong
                    gid == removeGroupId
                }
            )
            //不存在此规格，不能删除
            if(oldGroup.isEmpty)
                return failedId
//            val pos = groupList.indexOf(oldGroup.get)
            val nl = groupList.filterNot(
                group => {
                    val gid = group.asInstanceOf[JObject].values("groupId").asInstanceOf[BigInt].toLong
                    gid == removeGroupId
                }
            )
            val newGroupList = JArray(nl)
//            val jgroup = JObject(JField("groupId", JInt(removeGroupId)) :: JField("value", JArray(Nil)) :: Nil)
//            val newGroupList = JArray(groupList ::: (jgroup :: Nil))
            item.setOptions(Printer.pretty(JsonAST.render(newGroupList)))
            removeGroupId
        }
        catch {
            case e : Exception => {
                    failedId
                }
        }

    }

    private def addProductOptionValue(item : Product, jobj : JObject) : Long = {
        val time1 = System.currentTimeMillis()
        val failedId = -1
        //商品开启option控制否, 未开启不能增加规格值
        if(!item.getUsingOption) {
            return failedId
        }
        try{
            val groupId = jobj.values("groupId").asInstanceOf[BigInt].toLong
            val valueId = jobj.values("valueId").asInstanceOf[BigInt]

            //该规格是否在商品所属类型配置中
            val tid = item.categories.head.getType
            val typeItem = MetaModels.metaType.findOneInstance(tid)
            if(typeItem == null)
                return failedId
            val supportGroups = typeItem.getAllSupportOptionGroup
            val existGroup = supportGroups.find(
                group => {
                    group.getID == groupId
                }
            )
            //所属类型没有配置此选项组，不可添加
            if(existGroup.isEmpty) {
                return failedId
            }
            //在选项组里查找是否有该Value
            val existValue = existGroup.get.allValues.find(
                value => {
                    value.getID == valueId.toLong
                }
            )
            //选项组配置中没有该Value, 不可添加
            if(existValue.isEmpty) {
                return failedId
            }

            val groupList = JsonParser.parse(item.getOptions).asInstanceOf[JArray].arr
            //商品当前的规格配置中是否已经存在此规格
            val groupBox = groupList.find(
                group => {
                    val gid = group.asInstanceOf[JObject].values("groupId").asInstanceOf[BigInt].toLong
                    gid == groupId
                }
            )
            //不存在该规格组, 就不能添加规格值
            if(groupBox.isEmpty)
                return failedId
            val groupItem = groupBox.get.asInstanceOf[JObject]
            val valueList = groupItem.values("value").asInstanceOf[List[BigInt]]
            val valueExist = valueList.contains(valueId)
            //商品已经配置过该规格值
            if(valueExist)
                return failedId
            val newValueList = (valueList ::: (valueId :: Nil)).flatMap {
                value => {
                    JInt(value) :: Nil
                }
            }
            val newGroupItem = JObject(JField("groupId", JInt(groupId)) :: JField("value", JArray(newValueList)) :: Nil)
            val pos = groupList.indexOf(groupItem)
            val newGroupList = groupList.dropRight(groupList.length - pos) ::: (newGroupItem :: Nil) ::: groupList.drop(pos + 1)
            item.setOptions(Printer.pretty(JsonAST.render(JArray(newGroupList))))
            val time2 = System.currentTimeMillis()
            print(time2 - time1)
            println("ms")
            valueId.toLong
        } catch {
            case e : Exception => {
                    failedId
                }
        }
    }

    private def removeProductOptionValue(item : Product, jobj : JObject) : Long = {
        val failedId = -1
        //商品开启option控制否, 未开启不能增加规格值
        if(!item.getUsingOption) {
            return failedId
        }
        try {
            val groupId = jobj.values("groupId").asInstanceOf[BigInt].toLong
            val valueId = jobj.values("valueId").asInstanceOf[BigInt]
            val groupList = JsonParser.parse(item.getOptions).asInstanceOf[JArray].arr
            //商品当前的规格配置中是否已经存在此规格
            val groupBox = groupList.find(
                group => {
                    val gid = group.asInstanceOf[JObject].values("groupId").asInstanceOf[BigInt].toLong
                    gid == groupId
                }
            )
            //不存在该规格组, 就不能删除规格值
            if(groupBox.isEmpty)
                return failedId
            val groupItem = groupBox.get.asInstanceOf[JObject]
            val valueList = groupItem.values("value").asInstanceOf[List[BigInt]]
            val valueExist = valueList.contains(valueId)
            //商品未配置过该规格值, 不能删除
            if(!valueExist)
                return failedId
            //该商品是否有货品使用此规格值
            val vusing = item.valueUsingByGoods(valueId.toLong)
            //规格值有被使用，不能删除
            if(vusing)
                return failedId
            val posv = valueList.indexOf(valueId)
            val newValueList = (valueList.dropRight(valueList.length - posv) ::: valueList.drop(posv + 1)).flatMap {
                value => {
                    JInt(value) :: Nil
                }
            }
            val newGroupItem = JObject(JField("groupId", JInt(groupId)) :: JField("value", JArray(newValueList)) :: Nil)
            val pos = groupList.indexOf(groupItem)
            val newGroupList = groupList.dropRight(groupList.length - pos) ::: (newGroupItem :: Nil) ::: groupList.drop(pos + 1)
            item.setOptions(Printer.pretty(JsonAST.render(JArray(newGroupList))))
            valueId.toLong
        } catch {
            case e : Exception => {
                    failedId
                }
        }
    }

    private def saveProductGoods(item : Product, jobj : JObject) {
        
        def saveGoodsInfo(gitem : Goods, goods : Map[String, _]) {
            
            def ifNegativeGotZero(num : Double) : Double = {
                if(num < 0)
                    0
                else
                    num
            }

            if(goods.keySet.contains("store")) {
                if(goods("store") == null)
                    throw new Exception
                val store = goods("store").asInstanceOf[Number].intValue
                if(store != gitem.getStore)
                    gitem.setStore(ifNegativeGotZero(store).toInt)
            }
            if(goods.keySet.contains("bn")) {
                if(goods("bn") == null)
                    throw new Exception
                val bn = goods("bn").asInstanceOf[String]
                if(bn != gitem.getBn)
                    gitem.setBn(bn)
            }
            if(goods.keySet.contains("storePlace")) {
                if(goods("storePlace") == null) {
                    gitem.setStorePlace("")
                }else{
                    val sp = goods("storePlace").asInstanceOf[String]
                    if(sp != gitem.getStorePlace)
                        gitem.setStorePlace(sp)
                }
            }
            if(goods.keySet.contains("weight")) {
                val w = if(goods("weight") == null){ 0 } else { goods("weight").asInstanceOf[Number].doubleValue }
                if(w != gitem.getWeight)
                    gitem.setWeight(ifNegativeGotZero(w))
            }
            if(goods.keySet.contains("cost")) {
                val c = if(goods("cost") == null){ 0 } else { goods("cost").asInstanceOf[Number].doubleValue }
                if(c != gitem.getCost)
                    gitem.setCost(ifNegativeGotZero(c))
            }
            if(goods.keySet.contains("price")) {
                if(goods("price") == null)
                    throw new Exception
                val p = goods("price").asInstanceOf[Number].doubleValue
                if(p != gitem.getPrice)
                    gitem.setPrice(ifNegativeGotZero(p))
            }
            if(goods.keySet.contains("marketPrice")) {
                val mp = if(goods("marketPrice") == null){ 0 } else { goods("marketPrice").asInstanceOf[Number].doubleValue }
                if(mp != gitem.getMarketPrice)
                    gitem.setMarketPrice(ifNegativeGotZero(mp))
            }
        }

        def checkGoodsOptionAndSet(gitem : Goods, oplst : List[Map[String, BigInt]]) {
            val opjlst = JArray(oplst.flatMap{
                    opitem => {
                        val gid = opitem("groupId")
                        val vid = opitem("valueId")
                        JObject(JField("groupId", JInt(gid))::JField("valueId",JInt(vid))::Nil) ::
                        Nil
                    }
                })
            val op = Printer.pretty(JsonAST.render(opjlst))
            if(op == gitem.getOption)
                return
            if(item.isOptionSettingUsed(op))
                throw new Exception
            gitem.setOption(op)
        }

        if(jobj.values.keySet.contains("goods")) {
            val goods = jobj.values("goods").asInstanceOf[Map[String, _]]
            if(item.getUsingOption) {
                val gid = goods("id").asInstanceOf[BigInt].toLong
                val gitem = if(gid == -1)
                    MetaModels.metaGoods.newInstance
                else
                    MetaModels.metaGoods.findOneInstance(gid)
                if(gitem != null) {
                    saveGoodsInfo(gitem, goods)
                    if(gid == -1) {
                        if(!goods.keySet.contains("option"))
                            throw new Exception
                        val oplst = goods("option").asInstanceOf[List[Map[String, BigInt]]]
                        checkGoodsOptionAndSet(gitem, oplst)
                    } else {
                        if(goods.keySet.contains("option")) {
                            val oplst = goods("option").asInstanceOf[List[Map[String, BigInt]]]
                            checkGoodsOptionAndSet(gitem, oplst)
                        }
                    }
                    
                    gitem.saveInstance
                    if(gid == -1)
                        item.addGoods(gitem.getID)
                }

            } else {
                val gid = goods("id").asInstanceOf[BigInt].toLong
                val gitem = MetaModels.metaGoods.findOneInstance(gid)
                if(gitem != null) {
                    saveGoodsInfo(gitem, goods)
                    gitem.setOption("[]")
                    gitem.saveInstance
                }
            }
        }
    }

    private def removeProductGoods(item : Product, jobj : JObject) {
        if(jobj.values.keySet.contains("goods")) {
            val goodsList = jobj.values("goods").asInstanceOf[List[Map[String, BigInt]]]
            goodsList.foreach {
                goods => {
                    val gid = goods("id").toLong
                    val gitem = MetaModels.metaGoods.findOneInstance(gid)
                    gitem.deleteInstance
                }
            }
        }
    }

    private def saveCategoryName(item : Category, jobj : JObject) {
        if (jobj.values.keySet.contains("name") && jobj.values("name") != null ) {
            val nameArr = jobj.values("name").asInstanceOf[List[Map[String, _]]]
            nameArr foreach {
                nameItem => {
                    val langId = nameItem("langId").asInstanceOf[BigInt].toLong
                    val name = nameItem("name").asInstanceOf[String]
                    if(item.getName(langId) != name) {
                        item.setName(langId, name)
                    }
                }
            }
        }
    }

    private def saveProductName(item : Product, jobj : JObject) {
        if (jobj.values.keySet.contains("name")) {
            val nameArr = jobj.values("name").asInstanceOf[List[Map[String, _]]]
            nameArr foreach {
                nameItem => {
                    val langId = nameItem("langId").asInstanceOf[BigInt].toLong
                    val name = nameItem("name").asInstanceOf[String]
                    if(item.getName(langId) != name) {
                        item.setName(langId, name)
                    }
                }
            }
        }
    }

    private def saveCategoryImage(item : Category, jobj : JObject) {
        if(jobj.values.keySet.contains("image") && jobj.values("image") != null ) {
            val imageArr = jobj.values("image").asInstanceOf[List[String]]
            val jsonStr = JsonUtils.StringListToJsonStringArrayStr(imageArr)
            if(item.getImage != jsonStr) {
                item.setImage(jsonStr)
            }
        }
    }

    private def saveProductImage(item : Product, jobj : JObject) {
        if(jobj.values.keySet.contains("image")) {
            val imageArr = jobj.values("image").asInstanceOf[List[String]]
            val jsonStr = JsonUtils.StringListToJsonStringArrayStr(imageArr)
            if(item.getImage != jsonStr) {
                item.setImage(jsonStr)
            }
        }
    }

    private def saveCategoryDisplayOrder(item : Category, jobj : JObject) {
        if(jobj.values.keySet.contains("displayOrder") && jobj.values("displayOrder") != null ){
            val displayArr = jobj.values("displayOrder").asInstanceOf[List[Map[String, _]]]
            displayArr.foreach {
                displayItem => {
                    val id : Long = displayItem("id").asInstanceOf[BigInt].toLong
                    val displayOrder : Int = displayItem("displayOrder").asInstanceOf[BigInt].toInt
                    val sub = metaModel.findOneInstance(id)
                    if( sub != null ){
                        sub.setDisplayOrder(displayOrder)
                        sub.saveInstance()
                    }
                }
            }
        }
//        println(displayArr)
    }

//    private def remove() = {
//        val reqstr = this.getRequestContent()
//        ShopLogger.logger.debug(reqstr)
//        try {
//            val jsonList : List[JsonAST.JValue] = JsonParser.parse(reqstr).asInstanceOf[JsonAST.JArray].arr
//            val item = metaModel.findOneInstance(jsonList.head.asInstanceOf[JObject].values("id").asInstanceOf[BigInt].toLong)
//            val parent_id =
//                if (item != null)
//                    item.getParentID
//            else
//                -1
//            jsonList.foreach(
//                item => {
//                    val cat_id = item.asInstanceOf[JObject].values("id").asInstanceOf[BigInt].toLong
//                    val cat_item = metaModel.findOneInstance(cat_id)
//                    if(cat_item != null) {
//                        cat_item.deleteInstance()
//                    }
//                }
//            )
//            this.getAllSubCategories(parent_id, this.getDefaultLang())
//        }
//
//    }

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

    private def getAllSubCategories(categoryId : Long, languageId : Long) = {
        val cat_children : List[Category] = metaModel.getChildren(categoryId)
        val resultList = cat_children.flatMap {
            case  item : Category => {
                    val id = item.getID
                    val name = item.getName(languageId)
                    val active = item.getActive()
                    val addTime = item.getAddTime().toString
                    val updateTime = item.getUpdateTime().toString
                    val parentId = item.getParentID
                    val image = urlEncode(item.getFirstImageFileLocation)
                    val ptype = item.getType
                    val hasSubCat = !metaModel.getChildren(id).isEmpty
                    val hasProduct = !metaModel.getProducts(id).isEmpty
                    val leaf = (!hasSubCat) && (!hasProduct)
//                    val cat_desc = item.getDescription(languageId)
                    List(
                        JsonAST.JField("id", JsonAST.JInt(id))
                        ++
                        JsonAST.JField("parentId", JsonAST.JInt(parentId))
                        ++
                        JsonAST.JField("name", JsonAST.JString(name))
                        ++
                        JsonAST.JField("image", JsonAST.JString(image))
                        ++
                        JsonAST.JField("addTime", JsonAST.JString(addTime))
                        ++
                        JsonAST.JField("updateTime", JsonAST.JString(updateTime))
                        ++
                        JsonAST.JField("active", JsonAST.JBool(active))
                        ++
                        JsonAST.JField("typeId", JsonAST.JInt(ptype))
                        ++
                        JsonAST.JField("changeType", JsonAST.JBool(leaf))
                        ++
                        JsonAST.JField("hasSubCategory", JsonAST.JBool(hasSubCat))
                        ++
                        JsonAST.JField("hasProduct", JsonAST.JBool(hasProduct))
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
                product.getExtensionProperties()
                val product_id = product.getID()
                val product_name = product.getName(languageId)
                val image = product.getImage
                val active = product.getActive
                val isOptionUsing = product.getUsingOption
                val imageArr = try {
                    JsonParser.parse(image).asInstanceOf[JArray]
                } catch {
                    case ex : Exception => {
                            JsonParser.parse("[\"/nopic.gif\"]").asInstanceOf[JArray]
                        }
                }
                List(
                    JsonAST.JField("id", JsonAST.JInt(product_id))
                    ++
                    JsonAST.JField("name", JsonAST.JString(product_name))
                    ++
                    JsonAST.JField("active", JsonAST.JBool(active))
                    ++
                    JsonAST.JField("isOptionUsing", JsonAST.JBool(isOptionUsing))
                )
            }
        }
        Full(JsonResponse(
                JsonAST.JObject(JsonAST.JField("product", JsonAST.JArray(resultList)) :: Nil)
            ))
    }

    private def getJsonObjectFromRequest() : JObject = {
        val reqstr = this.getRequestContent
//        val reqstr = "[{\"id\":13, \"groupId\":3, \"valueId\":11, \"categoryId\":56, \"isOptionUsing\": true,\"option\": [],\"name\":[{\"langId\":22, \"name\":\"p2\"},{\"langId\":23, \"name\":\"p2\"},{\"langId\":24, \"name\":\"p2\"}], \"active\":true}]"
//        val reqstr = "[{\"id\":13,\"goods\":{\"id\":14,\"option\":[{\"groupId\":2,\"valueId\":10}, {\"groupId\":3,\"valueId\":12}]}}]"
//        val reqstr = "[{\"id\":13,\"goods\":[{\"id\":14}]}]"
        try {
            val jsonList = JsonParser.parse(reqstr).asInstanceOf[JArray].arr
            val jsonObj = jsonList.head.asInstanceOf[JObject]
            jsonObj
        } catch {
            case _ => {
                    JObject(Nil)
                }
        }
    }

    private def getIdFromResquest() : Long = {
        val reqstr = this.getRequestContent()
//         val reqstr = "[{\"id\":2 }]"
        try {
            val jsonList = JsonParser.parse(reqstr).asInstanceOf[JArray].arr
            val jsonObj = jsonList.head.asInstanceOf[JObject]
            val id = jsonObj.values("id").asInstanceOf[BigInt].toLong
            id
        }
    }
    
    override def getRequestContent() = {
        urlDecode(S.param("json").openOr(""))
//        val reqstr = "[{\"id\":2, \"categoryId\":56, \"isOptionUsing\": true,\"option\": [111,222],\"name\":[{\"langId\":22, \"name\":\"p2\"},{\"langId\":23, \"name\":\"p2\"},{\"langId\":24, \"name\":\"p2\"}], \"active\":true}]"
//        reqstr
    }
}
