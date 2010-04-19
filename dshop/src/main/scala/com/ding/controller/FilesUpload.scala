/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.controller

import com.ding.model._
import net.liftweb.http._
import net.liftweb.util._
import java.io.File

trait FilesUpload[A<:BaseModel] {

    def uploadFiles(owner : A) {
        val req = S.request.open_!
        val tmpFileNames = req.request.session.attribute(Props.get("upload.sessionkey").open_!).asInstanceOf[java.util.List[String]]
        var tmpFileNamesList : List[String] = Nil
        if(tmpFileNames != null && tmpFileNames.size() > 0) {
            var i = tmpFileNames.iterator()
            while(i.hasNext()) {
                tmpFileNamesList = tmpFileNamesList ::: (i.next().asInstanceOf[String] :: Nil)
            }
        }
        tmpFileNamesList.flatMap {
            tmpFileName => {
                val tmpFile = new File(Props.get("upload.tmpdir").open_! + tmpFileName)
                if(tmpFile.exists){
                    val newFile = new File(getUploadDir + owner.getID().toString() + "-" + tmpFileName)
                    if(tmpFile.renameTo(newFile)) {
                        (owner.getID().toString() + "-" + tmpFileName) :: Nil
                    } else {
                        Nil
                    }
                } else {
                    Nil
                }
            }
        }
    }

    def getUploadDir() : String
}
