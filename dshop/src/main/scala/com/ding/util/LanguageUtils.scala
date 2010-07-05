/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.util

object LanguageUtils {
    def getDefaultLang() = {
        MetaModels.metaLanguage.findAllInstances.head.getID
//        22
    }
//    def getDefaultLangCode() = "cn"
    def getDefaultLangCode() = {
        MetaModels.metaLanguage.findAllInstances.head.getCode
//        "cn"
    }
}
