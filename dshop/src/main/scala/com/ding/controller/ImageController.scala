/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.controller

import net.liftweb.http._
import net.liftweb.common._
import net.liftweb.util.Helpers._
import com.ding.util._
import java.io._
import javax.imageio._
import javax.imageio.stream._

object ImageController extends BaseController{

    override def processAction(action : String) : Box[LiftResponse] = {
        
        val appdir = reqInfo.is.application + "/"
//        ShopLogger.logger.debug(S.request.open_!.uri)
        val filename = urlDecode(if(S.param("filename").openOr("nopic.gif").length > 0) S.param("filename").openOr("nopic.gif") else "nopic.gif")
        val imagedir = "d:/ws-netbeans/dshop/image/"
        val absFileName = imagedir + appdir + filename
        ShopLogger.logger.debug(absFileName)
        val imageFile = new File(absFileName)

        val imageFileInput = new FileImageInputStream(if(imageFile.exists) imageFile else new File(imagedir + "nopic.gif"))
        val imageBytes = new Array[Byte](imageFileInput.length.toInt)
        imageFileInput.readFully(imageBytes)
        imageFileInput.close()
        Full(InMemoryResponse(imageBytes, ("Cache-Control" -> "no-cache,must-revalidate")::Nil, Nil, 200))

//        if(imageFile.exists) {
//            val imageFileInput = new FileImageInputStream(imageFile)
//            val imageBytes = new Array[Byte](imageFileInput.length.toInt)
//            imageFileInput.readFully(imageBytes)
//            imageFileInput.close()
//            Full(InMemoryResponse(imageBytes, ("Cache-Control" -> "no-cache,must-revalidate")::Nil, Nil, 200))
////            Full(OkResponse())
//        } else {
//            val nopigImageFile = new File(imagedir + "nopic.gif")
//
//            Full(NotFoundResponse())
//        }

    }

}
