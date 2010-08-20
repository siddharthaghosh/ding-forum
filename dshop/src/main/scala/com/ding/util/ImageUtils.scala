/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.util

import com.mortennobel.imagescaling.ThumpnailRescaleOp
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import net.liftweb.imaging._

object ImageUtils {

    def getThumbNail(pic : File, width : Int, height : Int) : BufferedImage = {

        val bi = ImageIO.read(pic)
//        val srcWidth = bi.getWidth()
//        val srcHeight = bi.getHeight()
//        val destWidth : Float = width
//        val destHeight : Float = height
//
//        val tnBuffer = if(srcWidth <= destWidth && srcHeight <=destHeight){
//            bi
//        }else {
//            val scaleX = srcWidth / destWidth
//            val scaleY = srcHeight / destHeight
//            val scale = if (scaleX > scaleY) scaleX else scaleY
//            val tnWidth = srcWidth / scale
//            val tnHeight = srcHeight / scale
//            val tn = new ThumpnailRescaleOp(tnWidth.toInt, tnHeight.toInt)
//            tn.filter(bi, null)
//        }
//        val picType = /* if(this.getImageType(tnBuffer)==4) BufferedImage.TYPE_INT_ARGB else  */BufferedImage.TYPE_INT_RGB
//        val destBuffer = new BufferedImage(destWidth.toInt, destHeight.toInt, picType)
//
//        var destY : Int = 0
//        var destX : Int = 0
//
//        while(destY < destHeight.toInt) {
//            destX = 0
//            while (destX < destWidth.toInt) {
//                val r = 255
//                val g = 255
//                val b = 255
//                val a = 255
//                val rgb = /* (a<<24) +  */(r<<16) + (g<<8) + b
//                destBuffer.setRGB(destX, destY, rgb)
//                destX = destX + 1
//            }
//            destY = destY + 1
//        }
//
//        destX = 0
//        destY = 0
//        val startX : Int = (destWidth/2).toInt - tnBuffer.getWidth/2
//        val startY  : Int = (destHeight/2).toInt - tnBuffer.getHeight/2
//        var tnX = 0
//        var tnY = 0
//        while(tnY < tnBuffer.getHeight) {
//            tnX = 0
//            while(tnX < tnBuffer.getWidth){
//                val rgb = tnBuffer.getRGB(tnX, tnY)
//                destX = startX + tnX
//                destY = startY + tnY
//                destBuffer.setRGB(destX, destY, rgb)
//                tnX = tnX + 1
//            }
//            tnY = tnY + 1
//        }
        val destBuffer = ImageResizer.max(Some(ImageOrietation.ok), bi, width, height)
        destBuffer

    }

    def writeThumbNail(pic : File, width : Int, height : Int, location : File) : Boolean = {
        
        val btn = this.getThumbNail(pic, width, height)
        if(location != null) {
            if(location.exists)
                location.delete
            val ios = ImageIO.createImageOutputStream(location)
            ImageIO.write(btn, "jpg", ios)
        }else {
            false
        }
    }

    def getImageType(image : BufferedImage) : Int = {
        image.getType() match {
            case BufferedImage.TYPE_3BYTE_BGR => 3
            case BufferedImage.TYPE_4BYTE_ABGR => 4
            case BufferedImage.TYPE_BYTE_GRAY => 1
            case BufferedImage.TYPE_INT_BGR => 3
            case BufferedImage.TYPE_INT_ARGB => 4
            case BufferedImage.TYPE_INT_RGB => 3
            case BufferedImage.TYPE_CUSTOM => 4
            case BufferedImage.TYPE_4BYTE_ABGR_PRE => 4
            case BufferedImage.TYPE_INT_ARGB_PRE => 4
            case BufferedImage.TYPE_USHORT_555_RGB => 3
            case BufferedImage.TYPE_USHORT_565_RGB => 3
            case BufferedImage.TYPE_USHORT_GRAY => 1
            case _ => 0
        }
    }
}
