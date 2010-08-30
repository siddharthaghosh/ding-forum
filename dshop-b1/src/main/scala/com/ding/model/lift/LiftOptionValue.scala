/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import net.liftweb.mapper._
import com.ding.model._

class LiftOptionValue extends LiftModel[LiftOptionValue] with OptionValue with OneToMany[Long, LiftOptionValue]{

    override def getSingleton = LiftOptionValue
    override def primaryKeyField = option_value_id

    object option_value_id extends MappedLongIndex(this)
    object option_group_id extends MappedLongForeignKey(this, LiftOptionGroup)
    object names extends MappedOneToMany(LiftOptionValueName, LiftOptionValueName.option_value_id)
    object display_order extends MappedInt(this)

    override def getID() : Long = this.option_value_id.is
    override def getName(lang_id : Long) : String = {
        val name_item = this.findNameByLang(LiftLanguage.find(By(LiftLanguage.lang_id, lang_id)).openOr(null))
        if (name_item == null)
            ""
        else
            name_item.name.is
    }
    override def getGroupID() : Long = {
        this.option_group_id.is
    }
    override def getDisplayOrder() : Int = {
        this.display_order.is
    }

    override def setName(lang_id : Long, name : String) {
        val name_item = this.findNameByLang(LiftLanguage.find(By(LiftLanguage.lang_id, lang_id)).openOr(null))
        if(name_item != null) {
            name_item.name(name)
        } else if(LiftLanguage.isLanguageExist(lang_id)){
            val n = LiftOptionValueName.newInstance
            n.lang_id(lang_id)
            n.name(name)
            this.names.append(n)
        }
    }
    override def setGroupID(id : Long) {
        this.option_group_id(id)
    }
    override def setDisplayOrder(order : Int) {
        this.display_order(order)
    }

    override def deleteInstance : Boolean = {
        this.names.all.foreach {
            name => {
                name.deleteInstance()
            }
        }
        this.delete_!
    }

    def findNameByLang(lang : LiftLanguage) : LiftOptionValueName = {
        if(lang == null)
            null
        else
            names.all.find(
                name => {
                    if(name.lang_id.is == lang.lang_id.is)
                        true
                    else
                        false
                }
            ).orNull
    }
}

object LiftOptionValue extends LiftOptionValue with LiftMetaModel[LiftOptionValue] with MetaOptionValue {
    override def dbTableName = "dshop_option_value"
    override def findOneInstance(id : Long) = {
        LiftOptionValue.find(By(LiftOptionValue.option_value_id, id)).openOr(null)
    }
}
