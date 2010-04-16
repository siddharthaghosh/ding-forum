/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import com.ding.model._
import net.liftweb.mapper._

trait LiftMultiLanguageName[A<:LiftMultiLanguageName[A, R], R<:LiftMultiLanguageNameBase[R, A]]
extends LiftBaseModel[A]
   with MultiLanguageName with OneToMany[Long, A]{

    self : A =>

    def multiLangNameObject() : MetaLiftMultiLanguageNameBase[R, A]


    object names extends MappedOneToMany(multiLangNameObject(), multiLangNameObject().ref_id)

    def getName(lang_id : Long) : String = {
        ""
    }
    def setName(lang_id : Long, name : String) {

    }
}

trait LiftMultiLanguageDescription {
    def getDescription(lang_id : Long) : String = {
        ""
    }
    def setDescription(lang_id : Long, name : String) = {
        
    }
}

trait  LiftMultiLanguageNameDescription[A<:LiftMultiLanguageNameDescription[A, R], R<:LiftMultiLanguageNameDescriptionBase[R, A]]
extends LiftBaseModel[A]
   with MultiLanguageName
   with MultiLanguageDescription
   with OneToMany[Long, A]{

    self : A =>

    def multiLangNameDescriptionObject() : MetaLiftMultiLanguageNameDescriptionBase[R, A]


    object names extends MappedOneToMany(multiLangNameDescriptionObject(), multiLangNameDescriptionObject().ref_id)
    object descriptions extends MappedOneToMany(multiLangNameDescriptionObject(), multiLangNameDescriptionObject().ref_id)

    def getName(lang_id : Long) : String = {
        ""
    }
    def setName(lang_id : Long, name : String) {

    }
    def getDescription(lng_id : Long) : String = {
        ""
    }
    def setDescription(lang_id : Long, description : String) {
        
    }
}
