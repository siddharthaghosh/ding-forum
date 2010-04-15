/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.controller

import net.liftweb.http._
import net.liftweb.common._
import net.liftweb.util.Helpers._
import net.liftweb.util.Props
import com.ding.util._
import java.io._
import javax.imageio._
import javax.imageio.stream._

object ImageController extends BaseController{

    val imagedir : String = Props.get("image.dir").open_!
    val fileNameParam : String = Props.get("urlparam.filename").open_!
    val notFoundFileName : String = Props.get("image.notfound").open_!

    override def processAction(action : String) : Box[LiftResponse] = {
        
        val appdir = reqInfo.is.application + "/"
//        ShopLogger.logger.debug(S.request.open_!.uri)
        val filename = urlDecode(if(S.param(fileNameParam).openOr(notFoundFileName).length > 0) S.param(fileNameParam).openOr(notFoundFileName) else notFoundFileName)
        val absFileName = imagedir + appdir + filename
        ShopLogger.logger.debug(absFileName)
        val imageFile = new File(absFileName)

        val mimeType = FileValidator.getMIMEType(imageFile)
        val imageFileInput = new FileImageInputStream(if(imageFile.exists) imageFile else new File(imagedir + notFoundFileName))
//        val imageBytes = new Array[Byte](imageFileInput.length.toInt)
//        imageFileInput.readFully(imageBytes)
//        imageFileInput.close()
        Full(StreamingResponse(imageFileInput, ()=>imageFileInput.close, imageFileInput.length, ("Content-Type" -> mimeType)::Nil, Nil, 200))
//        Full(InMemoryResponse(imageBytes, ("Cache-Control" -> "no-cache,must-revalidate")::Nil, Nil, 200))

    }

}
