/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.controller

import net.liftweb.http._
import net.liftweb.util._
import com.ding.model._
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
                    if(tmpFile.renameTo(newFile)) {

                        val fileItem = uploadFileModel.newInstance
                        fileItem.setRealName(realName)
                        fileItem.setDisplayName(displayName)
                        fileItem.saveInstance()

                    }

                }

            }

        }
        
    }

}
