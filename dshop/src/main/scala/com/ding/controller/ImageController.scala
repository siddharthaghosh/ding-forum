/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.controller

import net.liftweb.http._
import net.liftweb.common._
import com.ding.util._
import java.io._
import javax.imageio._
import javax.imageio.stream._

object ImageController extends BaseController{

    override def processAction(action : String) : Box[LiftResponse] = {
        
        val appdir = reqInfo.is.application + "/"
        val filename = action
        val imagedir = "d:/ws-netbeans/dshop/image/"
        val absFileName = imagedir + appdir + filename
        ShopLogger.logger.debug(absFileName)
        val imageFile = new File(absFileName)
        if(imageFile.exists) {
            val imageFileInput = new FileImageInputStream(imageFile)
            val imageBytes = new Array[Byte](imageFileInput.length.toInt)
            imageFileInput.readFully(imageBytes)
            imageFileInput.close()
            Full(InMemoryResponse(imageBytes, ("Cache-Control" -> "no-cache,must-revalidate")::Nil, Nil, 200))
//            Full(OkResponse())
        } else {
            Full(NotFoundResponse())
        }

    }

}
