/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model

trait MultiLanguageName {
    def getName(lang_id : Long) : String
    def setName(lang_id : Long, name : String)
}

trait MultiLanguageDescription {
    def getDescription(lang_id : Long) : String
    def setDescription(lang_id : Long, name : String)
}
