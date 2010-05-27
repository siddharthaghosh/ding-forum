/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import com.ding.model._
import net.liftweb.mapper._

class LiftType extends LiftBaseModel[LiftType] with Type with LiftMultiLanguageName[LiftType, LiftTypeName] with ManyToMany {
    override def getSingleton = LiftType
    override def primaryKeyField = type_id
    override def multiLangNameObject() = LiftTypeName
    object type_id extends MappedLongIndex(this)
    object option_groups extends MappedManyToMany(LiftTypeOptionGroup, LiftTypeOptionGroup.type_id, LiftTypeOptionGroup.option_group_id, LiftOptionGroup)
    override def getAllSupportOptionGroup() : List[OptionGroup] = {
        option_groups.all
    }
    def removeSupportOptionGroup(og_id : Long) {
        val type_og_objs = LiftTypeOptionGroup.findAll(By(LiftTypeOptionGroup.type_id, this.type_id), By(LiftTypeOptionGroup.option_group_id, og_id))
        type_og_objs.foreach {
            type_og_obj => {
                type_og_obj.delete_!
            }
        }
    }
    def addSupportOptionGroup(og_id : Long) {
        val og = LiftOptionGroup.findOneInstance(og_id)
        if(og != null) {
            val exists = this.option_groups.contains(og)
            if(!exists) {
                this.option_groups += og
                this.option_groups.save
            }
        }
    }
}

object LiftType extends LiftType with LiftMetaModel[LiftType] with MetaType {
    override def dbTableName = "dshop_type"
    override def findOneInstance(id : Long) = {
        LiftType.find(By(LiftType.type_id, id)).openOr(null)
    }
}