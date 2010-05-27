/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model

trait Type extends BaseModel with MultiLanguageName{
    def getAllSupportOptionGroup() : List[OptionGroup]
    def removeSupportOptionGroup(og_id : Long)
    def addSupportOptionGroup(og_id : Long)
}

trait MetaType extends MetaModel[Type]