/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model.lift

import net.liftweb.mapper._
import com.ding.model._

class LiftUploadFile
extends LiftBaseModel[LiftUploadFile] 
   with UploadFile
   with IdPK {

    override def getSingleton = LiftUploadFile
    object real_name extends MappedString(this, 128)
    object display_name extends MappedString(this, 128)

    override def getRealName() : String = {
        this.real_name.is
    }
    override def getDisplayName() : String = {
        this.display_name.is
    }
    override def setRealName(name : String) {
        this.real_name(name)
    }
    override def setDisplayName(name : String) {
        this.display_name(name)
    }
}

object LiftUploadFile
extends LiftUploadFile
   with LiftMetaModel[LiftUploadFile]
   with MetaUploadFile {

    override def dbTableName = "dshop_upload_file"

    override def findOneInstance(id : Long) = {
        LiftUploadFile.find(By(LiftUploadFile.id, id)).openOr(null)
    }

    override def findByRealName(name : String) : UploadFile = {
        LiftUploadFile.find(By(LiftUploadFile.real_name, name)).openOr(null)
    }

    override def isRealNameExist(name : String) : Boolean = {
        this.findByRealName(name) != null
    }
}
