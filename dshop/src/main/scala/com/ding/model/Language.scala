/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model

trait Language {

    def saveInstance() : Boolean
    def updateInstance(name : String, code : String, image : String, dir : String, display_order : Int)
}

trait MetaLanguage {

    def newInstance() : Language
}
