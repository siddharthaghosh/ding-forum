/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model

trait Type extends BaseModel with MultiLanguageName{
    def getAllSupportOptionGroup() : List[OptionGroup]
    def removeSupportOptionGroup(og_id : Int)
    def addSupportOptionGroup(og_id : Int)
}

trait MetaType extends MetaModel[Type]