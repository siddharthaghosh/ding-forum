/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model

trait UploadFile extends BaseModel {
    def getRealName() : String
    def getDisplayName() : String
    def setRealName(name : String)
    def setDisplayName(name : String)
}

trait MetaUploadFile extends MetaModel[UploadFile] {
    def findByRealName(name : String) : UploadFile
    def isRealNameExist(name : String) : Boolean
}