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
import java.awt.image.BufferedImage
import java.io._
import javax.imageio._
import javax.imageio.stream._
import com.mortennobel.imagescaling.ThumpnailRescaleOp
//import com.mortennobel.imagescaling.ImageUtils


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
//        processThumbNail

        val bi = ImageIO.read(imageFile)
        val srcWidth = bi.getWidth()
        val srcHeight = bi.getHeight()
        val destWidth : Float = 1022
        val destHeight : Float = 766

        val tnBuffer = if(srcWidth <= destWidth && srcHeight <=destHeight){
            bi
        }else {
            val scaleX = srcWidth / destWidth
            val scaleY = srcHeight / destHeight
            val scale = if (scaleX > scaleY) scaleX else scaleY
            val tnWidth = srcWidth / scale
            val tnHeight = srcHeight / scale
            val tn = new ThumpnailRescaleOp(tnWidth.toInt, tnHeight.toInt)
            tn.filter(bi, null)
        }
        val destBuffer = new BufferedImage(destWidth.toInt, destHeight.toInt, tnBuffer.getType)

        var destY : Int = 0
        var destX : Int = 0

        while(destY < destHeight.toInt) {
            destX = 0
            while (destX < destWidth.toInt) {
                val r = 255
                val g = 255
                val b = 255
                val a = 1
                val rgb = /* (a<<24) +  */(r<<16) + (g<<8) + b
                destBuffer.setRGB(destX, destY, rgb)
                destX = destX + 1
            }
            destY = destY + 1
        }

        destX = 0
        destY = 0
        val startX : Int = (destWidth/2).toInt - tnBuffer.getWidth/2
        val startY  : Int = (destHeight/2).toInt - tnBuffer.getHeight/2
        var tnX = 0
        var tnY = 0
        while(tnY < tnBuffer.getHeight) {
            tnX = 0
            while(tnX < tnBuffer.getWidth){
                val rgb = tnBuffer.getRGB(tnX, tnY)
                destX = startX + tnX
                destY = startY + tnY
                destBuffer.setRGB(destX, destY, rgb)
                tnX = tnX + 1
            }
            tnY = tnY + 1
        }
        
        val bs = new ByteArrayOutputStream()
        val imOut = ImageIO.createImageOutputStream(bs)
        ImageIO.write(destBuffer, "jpg", imOut)
        val is = new ByteArrayInputStream(bs.toByteArray())
        
        Full(StreamingResponse(is, ()=>is.close, bs.toByteArray().length, ("Content-Type" -> mimeType)::Nil, Nil, 200))
    }

}
