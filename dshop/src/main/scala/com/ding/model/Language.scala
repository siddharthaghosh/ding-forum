/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model

trait Language {

    def saveInstance() : Boolean
    def updateInstance(name : String, code : String, image : String, dir : String, display_order : Int)
    def deleteInstance() : Boolean

    def getID() : Long
    def getName() : String
    def getImage() : String
    def getDirectory() : String
    def getCode() : String
    def getDisplayOrder() : Int

    def setName(name : String)
    def setImage(image : String)
    def setDirectory(dir : String)
    def setCode(code : String)
    def setDisplayOrder(order : Int)

}

trait MetaLanguage {

    def newInstance() : Language
    def findOneInstance(id : Int) : Language
    def findAllInstances() : List[Language]
    
}
