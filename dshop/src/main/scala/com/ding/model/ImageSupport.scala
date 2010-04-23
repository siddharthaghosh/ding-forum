/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model

import net.liftweb.json._
import net.liftweb.json.JsonAST._

trait Image extends UploadFilesBaseModel {
    def getImage() : String
    def setImage(image : String)

    def getFiles() : String = this.getImage()
    def setFiles(files : String) {
        this.setImage(files)
    }

    def getAllImageFileLocation() : List[String] = {
        val jsonStr = this.getImage()
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

    def getFirstImageFileLocation() : String = {
        val allFiles = this.getAllImageFileLocation()
        if(allFiles.isEmpty) {
            ""
        }else {
            allFiles.head
        }
    }
}

trait ImageBaseModel extends BaseModel with Image
