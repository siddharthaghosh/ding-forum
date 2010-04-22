/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.controller

import net.liftweb.http._
import net.liftweb.util._
import com.ding.model._
import com.ding.util._
import java.util.concurrent.ConcurrentHashMap
import java.io.File

trait UploadAction[A <: MetaUploadFile] {

    def uploadFileModel : A
    def getUploadDir() : String

    def storeUploadFiles(relative_dir : String) {

        val req = S.request.open_!
        val uploadFileMap = req.request.session.attribute(Props.get("upload.sessionkey").open_!).asInstanceOf[ConcurrentHashMap[String, String]]
        val keys = uploadFileMap.keys
        while(keys.hasMoreElements) {

            val key = keys.nextElement()
            val realName = key
            val displayName = if(null == uploadFileMap.get(realName)) {
                realName
            } else {
                uploadFileMap.get(realName)
            }
            if(!uploadFileModel.isRealNameExist(realName)) {

                val tmpFile = new File(Props.get("upload.tmpdir").open_! + realName)
                if(tmpFile != null && tmpFile.exists) {

                    val relativeDir = if(relative_dir.endsWith("/")) relative_dir else relative_dir + "/"
                    val newFile = new File(getUploadDir + relativeDir + realName)
                    if(this.storeFile(tmpFile, newFile, realName, displayName)) {
                        processFile(newFile)
                    }

                }

            }

        }
        
    }

    def storeFile(tmpFile : File, newFile : File, realName : String, displayName : String) : Boolean = {
        if(tmpFile.renameTo(newFile)) {

            val fileItem = uploadFileModel.newInstance
            fileItem.setRealName(realName)
            fileItem.setDisplayName(displayName)
            fileItem.saveInstance()
        } else {
            false
        }
    }

    def processFile(file : File) {
        
    }
}

trait UploadImageAndGenerateThumb[A <: MetaUploadFile]
extends UploadAction[A] {

    override def processFile(file : File) {
        val location = new File(this.getThumbNailDir + "/" + file.getName)
        ImageUtils.writeThumbNail(file, Props.getInt("image.thumbnail.width").open_!, Props.getInt("image.thumbnail.height").open_!, location)
    }

    private def getThumbNailDir : String = Props.get("image.thumbnail.dir").open_!
}
