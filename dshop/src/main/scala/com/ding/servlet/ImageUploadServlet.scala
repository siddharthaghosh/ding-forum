/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.servlet

import gwtupload.server._
import java.util.Hashtable
import java.io.File
import java.io.FileInputStream
import javax.servlet.http._
import org.apache.commons.fileupload.FileItem

class ImageUploadServlet extends UploadAction {

    val receivedFile : Hashtable[String, File] = new Hashtable[String, File]()
    val receivedContentType : Hashtable[String, String] = new Hashtable[String, String]()

    def executeAction(request : HttpServletRequest, sessionFiles : List[FileItem]) : String = {
        sessionFiles.foreach {
            item => {
                if(false == item.isFormField()) {
                    try {
                        val file = File.createTempFile("upload-", ".bin")
                        item.write(file)
                        this.receivedFile.put(item.getFieldName, file)
                        this.receivedContentType.put(item.getFieldName, item.getContentType)
                    } catch {
                        case ex : Exception => {}
                    }
                }
            }
        }
        null
    }

    override def removeItem(req : HttpServletRequest, fieldName : String) {
        val file : File = this.receivedFile.get(fieldName)
        this.receivedFile.remove(fieldName)
        this.receivedContentType.remove(fieldName)
        if(file != null) file.delete()
    }

    override def getUploadedFile(request : HttpServletRequest, response : HttpServletResponse) {
        val fieldName : String = request.getParameter("PARAM_SHOW")
        val f : File = receivedFile.get(fieldName)
        if(f != null) {
            response.setContentType(receivedContentType.get(fieldName))
            val is = new FileInputStream(f)

            UploadServlet.copyFromInputStreamToOutputStream(is, response.getOutputStream())
//            copyFromInputStreamToOutputStream(is, response.getOutputStream())
            ImageUploadServlet
        } else {
//            UploadServlet.renderXmlResponse(request, response, UploadServlet.ERROR_ITEM_NOT_FOUND)
        }
    }
}

object ImageUploadServlet extends UploadServlet
