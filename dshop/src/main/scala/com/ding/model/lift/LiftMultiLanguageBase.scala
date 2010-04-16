/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import net.liftweb.mapper._

trait LiftMultiLanguageNameBase[A <: LiftMultiLanguageNameBase[A, R], R <: LiftBaseModel[R]]
extends LiftBaseModel[A] {
    self : A =>
    def getReferenceObject() : LiftMetaModel[R]
    def getReferenceObjectIDName() : String
    object lang_id extends MappedLongForeignKey(this, LiftLanguage)
    object ref_id extends MappedLongForeignKey(this, this.getReferenceObject()) {
        override def dbColumnName = getReferenceObjectIDName()
    }
    object name extends MappedString(this, 128)

}

trait MetaLiftMultiLanguageNameBase[A<:LiftMultiLanguageNameBase[A, R], R <: LiftBaseModel[R]]
extends LiftMultiLanguageNameBase[A,R]
   with LiftMetaModel[A]{
    self : A =>
}


trait LiftMultiLanguageDescriptionBase[A <: LiftMultiLanguageDescriptionBase[A, R], R <: LiftBaseModel[R]]
extends LiftBaseModel[A]{
    self : A =>
    def getReferenceObject() : LiftMetaModel[R]
    def getReferenceObjectIDName() : String
    object lang_id extends MappedLongForeignKey(this, LiftLanguage)
    object ref_id extends MappedLongForeignKey(this, this.getReferenceObject()) {
        override def dbColumnName = getReferenceObjectIDName()
    }
    object description extends MappedString(this, 255)
}

trait MetaLiftMultiLanguageDescriptionBase[A <: LiftMultiLanguageDescriptionBase[A, R], R <: LiftBaseModel[R]]
extends LiftMultiLanguageDescriptionBase[A,R]
   with LiftMetaModel[A]{
    self : A =>
}

trait LiftMultiLanguageNameDescriptionBase[A <: LiftMultiLanguageNameDescriptionBase[A, R], R <: LiftBaseModel[R]]
extends LiftBaseModel[A]{
    self : A =>
    def getReferenceObject() : LiftMetaModel[R]
    def getReferenceObjectIDName() : String
    object lang_id extends MappedLongForeignKey(this, LiftLanguage)
    object ref_id extends MappedLongForeignKey(this, this.getReferenceObject()) {
        override def dbColumnName = getReferenceObjectIDName()
    }
    object name extends MappedString(this, 128)
    object description extends MappedString(this, 255)
}

trait MetaLiftMultiLanguageNameDescriptionBase[A <: LiftMultiLanguageNameDescriptionBase[A, R], R <: LiftBaseModel[R]]
extends LiftMultiLanguageNameDescriptionBase[A,R]
   with LiftMetaModel[A]{
    self : A =>
}