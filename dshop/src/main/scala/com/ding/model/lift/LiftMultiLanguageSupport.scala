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

    object names extends MappedOneToMany(multiLangNameObject(), multiLangNameObject().ref_id) with Cascade[R]

    def findNameByLang(lang : LiftLanguage) : LiftMultiLanguageNameBase[R, A] = {
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

    def getName(lang_id : Long) : String = {
        val item = this.findNameByLang(LiftLanguage.find(By(LiftLanguage.lang_id, lang_id)).openOr(null))
        if(item == null)
            ""
        else
            item.name.is
    }
    def setName(lang_id : Long, name : String) {
        val item = this.findNameByLang(LiftLanguage.find(By(LiftLanguage.lang_id, lang_id)).openOr(null))
        if(item != null) {
            item.name(name)
        } else if(LiftLanguage.isLanguageExist(lang_id)){
            val n = multiLangNameObject().newInstance
            n.lang_id(lang_id)
            n.name(name)
            this.names.append(n)
        }
    }

    override def deleteInstance() : Boolean = {
        this.delete_!
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

    def findNameAndDescriptionByLang(lang : LiftLanguage) : LiftMultiLanguageNameDescriptionBase[R, A] = {
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

    def getName(lang_id : Long) : String = {
        val item = this.findNameAndDescriptionByLang(LiftLanguage.find(By(LiftLanguage.lang_id, lang_id)).openOr(null))
        if(item == null)
            ""
        else
            item.name.is
    }
    def setName(lang_id : Long, name : String) {
        val item = this.findNameAndDescriptionByLang(LiftLanguage.find(By(LiftLanguage.lang_id, lang_id)).openOr(null))
        if(item != null) {
            item.name(name)
        } else if(LiftLanguage.isLanguageExist(lang_id)){
            val n = multiLangNameDescriptionObject().newInstance
            n.lang_id(lang_id)
            n.name(name)
            this.names.append(n)
        }
        
    }
    def getDescription(lang_id : Long) : String = {
        val item = this.findNameAndDescriptionByLang(LiftLanguage.find(By(LiftLanguage.lang_id, lang_id)).openOr(null))
        if(item == null)
            ""
        else
            item.description.is
    }
    def setDescription(lang_id : Long, description : String) {
        val item = this.findNameAndDescriptionByLang(LiftLanguage.find(By(LiftLanguage.lang_id, lang_id)).openOr(null))
        if(item != null) {
            item.description(description)
        }
    }
}
