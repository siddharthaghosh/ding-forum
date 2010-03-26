/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import net.liftweb.mapper._
import com.ding.model._

class LiftOptionGroup extends LiftModel[LiftOptionGroup] with OptionGroup with OneToMany[Long, LiftOptionGroup]{

    override def getSingleton = LiftOptionGroup
    override def primaryKeyField = option_group_id

    object option_group_id extends MappedLongIndex(this)
    object display_order extends MappedInt(this)
    object names extends MappedOneToMany(LiftOptionGroupName, LiftOptionGroupName.option_group_id)
    object values extends MappedOneToMany(LiftOptionValue, LiftOptionValue.option_group_id)

    override def allValues() : List[OptionValue] = {
        values.all
    }

    override def getID() : Long = this.option_group_id.is
    override def getName(lang_id : Long) : String = {
        val name_item = this.findNameByLang(LiftLanguage.find(By(LiftLanguage.lang_id, lang_id)).openOr(null))
        if (name_item == null)
            ""
        else
            name_item.name.is
    }
    override def getDisplayOrder() : Int = this.display_order.is

    override def setName(lang_id : Long, name : String) {
        val name_item = this.findNameByLang(LiftLanguage.find(By(LiftLanguage.lang_id, lang_id)).openOr(null))
        if(name_item != null) {
            name_item.name(name)
        } else if(LiftLanguage.isLanguageExist(lang_id)){
            val n = LiftOptionGroupName.newInstance
            n.lang_id(lang_id)
            n.name(name)
            this.names.append(n)
        }
    }
    override def setDisplayOrder(order : Int) {
        this.display_order(order)
    }

    def findNameByLang(lang : LiftLanguage) : LiftOptionGroupName = {
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

object LiftOptionGroup extends LiftOptionGroup with LiftMetaModel[LiftOptionGroup] with MetaOptionGroup {
    override def dbTableName = "dshop_option_group"
    override def findOneInstance(id : Long) = {
        LiftOptionGroup.find(By(LiftOptionGroup.option_group_id, id)).openOr(null)
    }
}
