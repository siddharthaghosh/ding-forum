/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.controller

import com.ding.model._
import net.liftweb.http._
import net.liftweb.util._
import net.liftweb.json._
import net.liftweb.json.JsonAST._
import java.io.File

trait FilesUpload {

    type A <: UploadFilesBaseModel

    case class UploadMode()
    case object SingleUpload extends UploadMode
    case object MultiUpload extends UploadMode

    def storeUploadFiles(owner : A) {
        val req = S.request.open_!
        val tmpFileNames = req.request.session.attribute(Props.get("upload.sessionkey").open_!).asInstanceOf[java.util.List[String]]
        var tmpFileNamesList : List[String] = Nil
        if(tmpFileNames != null && tmpFileNames.size() > 0) {
            var i = tmpFileNames.iterator()
            while(i.hasNext()) {
                tmpFileNamesList = tmpFileNamesList ::: (i.next().asInstanceOf[String] :: Nil)
            }
        }
        val uploadFileNames : List[String] = tmpFileNamesList.flatMap {
            tmpFileName => {
                val tmpFile = new File(Props.get("upload.tmpdir").open_! + tmpFileName)
                if(tmpFile.exists){
                    val newFile = new File(getUploadDir + owner.getID().toString() + "-" + tmpFileName)
                    if(tmpFile.renameTo(newFile)) {
                        this.uploadMode() match {
                            case SingleUpload => {
                                    println("single upload")
                                    if(owner.getID() != -1) {
                                        if(owner.getFiles.length > 0) {
                                            val oldFile = new File(this.getUploadDir + this.getFirstUploadFile(owner))
                                            if(oldFile.isFile && oldFile.exists) {
                                                oldFile.delete()
                                                owner.setFiles("")
                                            }
                                        }
                                    }
                                    (owner.getID().toString() + "-" + tmpFileName) :: Nil
                                }
                            case MultiUpload => {
                                    (owner.getID().toString() + "-" + tmpFileName) :: Nil
                                }
                        }                        
                    } else {
                        Nil
                    }
                } else {
                    Nil
                }
            }
        }
        this.uploadMode() match {
            case SingleUpload => {
                    val jsonFileNames : String =
                        Printer.pretty(JsonAST.render(JsonAST.JArray(uploadFileNames.flatMap {
                                        fileName => {
                                            JsonAST.JString(fileName) :: Nil
                                        }
                                    }
                                )
                            )
                        )
                    if(uploadFileNames.length > 0 && jsonFileNames != null && jsonFileNames.length > 0) {
                        owner.setFiles(jsonFileNames)
                    } else if (owner.getID == -1){
                        val nopicname = Printer.pretty(JsonAST.render(JsonAST.JArray(JString(Props.get("image.notfound").open_!)::Nil
                                )
                            )
                        )
                        owner.setFiles(nopicname)
                    }
                }
            case MultiUpload => {
                    
                }
        }
        req.request.session.removeAttribute(Props.get("upload.sessionkey").open_!)
//        println(jsonFileNames)
    }

    def uploadMode() : UploadMode
    def getUploadDir() : String
    def getFirstUploadFile(item : A) : String
//    def getFileByName() : String
    def getAllUploadFiles(item : A) : List[String]

    def setUploadFiles(item : A, jsonFileLocations : String)
}

trait ImageSingleUpload extends FilesUpload {

//    trait ImageModel extends BaseModel with Image
    type A <: ImageBaseModel

    override def uploadMode() = SingleUpload
    def getUploadDir() : String = Props.get("image.dir").open_! + reqInfo.is.application + "/"
    def getFirstUploadFile(item : A) : String = {
        val allFiles = this.getAllUploadFiles(item)
        if(allFiles.isEmpty) {
            ""
        }else {
            allFiles.head
        }
    }
//    def getFileByName(filename : String) : String = ""

    def getAllUploadFiles(item : A) : List[String] = {
        val jsonStr = item.getImage()
        val resultList : List[String] = try {
            val jsonList : List[JValue] = JsonParser.parse(jsonStr).asInstanceOf[JsonAST.JArray].arr
            jsonList.flatMap {
                jitem => {
                    val jstr : JString = jitem.asInstanceOf[JString]
                    jstr.values :: Nil
                }
            }
        }catch {
            case ex : Exception => {
                    println(ex.getStackTraceString)
                    Nil
                }
        }
        resultList
    }
    def setUploadFiles(item : A, jsonFileLocations : String) {
        item.setImage(jsonFileLocations)
    }

}