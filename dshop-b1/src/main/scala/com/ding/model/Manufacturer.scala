/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model

import java.util.Date

trait Manufacturer extends ImageBaseModel {

    def getID() : Long
    def getName() : String
    def getImage() : String
    def getAddTime() : Date
    def getUpdateTime() : Date
    def getURL() : String

    def setName(name : String)
    def setImage(image : String)
    def setAddTime(date : Date)
    def setUpdateTime(date : Date)
    def setURL(url : String)

}

trait MetaManufacturer extends MetaModel[Manufacturer]
