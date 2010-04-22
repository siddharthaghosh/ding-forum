/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.controller

import net.liftweb.util._
import net.liftweb.http._
import net.liftweb.common._
import net.liftweb.util.Helpers._
import net.liftweb.util.Props
import com.ding.util._
import java.awt.image.BufferedImage
import java.io._
import javax.imageio._
import javax.imageio.stream._

object ThumbNailController extends BaseController {

    val imageDir : String = Props.get("image.dir").open_!
    val originDir : String = Props.get("image.origin.dir").open_!
    val thumbDir : String = Props.get("image.thumbnail.dir").open_!
    val fileNameParam : String = Props.get("urlparam.filename").open_!
    val notFoundFileName : String = Props.get("image.notfound").open_!

    override def processAction(action : String) : Box[LiftResponse] = {

        val filename = urlDecode(if(S.param(fileNameParam).openOr(notFoundFileName).length > 0) S.param(fileNameParam).openOr(notFoundFileName) else notFoundFileName)
        val absFileName = originDir + filename
        ShopLogger.logger.debug("thumbnail origin "+absFileName)
        val imageFile = new File(absFileName)
        val outFile = if(imageFile != null && imageFile.exists) {
            val filename = imageFile.getName
            val thumbFile = new File(thumbDir + filename)
            if(thumbFile != null && thumbFile.exists){
                thumbFile
            }else{
                if(ImageUtils.writeThumbNail(imageFile,
                                             Props.getInt("image.thumbnail.width").open_!,
                                             Props.getInt("image.thumbnail.height").open_!,
                                             thumbFile)){
                    thumbFile
                }else{
                    new File(imageDir + notFoundFileName)
                }
                
            }
        }else{
            new File(imageDir + notFoundFileName)
        }
        val imageFileInput = new FileImageInputStream(outFile)
        Full(StreamingResponse(imageFileInput, ()=>imageFileInput.close, imageFileInput.length, ("Content-Type" -> "image/jpeg")::Nil, Nil, 200))
    }

}
