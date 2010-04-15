/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.util

import java.io.File
import javax.imageio.ImageIO
import javax.imageio.ImageReader
import javax.imageio.stream.ImageInputStream

object FileValidator {
    def isImageFile(file : File) : Boolean = {
        val fileType = this.getImageType(file)
        fileType match {
            case "JPEG" => {
                 true
            }
            case "PNG" => {
                 true
            }
            case "GIF" => {
                 true
            }
            case _ => {
                 false
            }
        }
    }
    def getImageType(file : File) : String = {
        val iis : ImageInputStream = ImageIO.createImageInputStream(file)
        val iter : java.util.Iterator[ImageReader] = ImageIO.getImageReaders(iis)
        val imageTypes : String = if(!iter.hasNext()) "" else {
            val reader : ImageReader = iter.next()
            iis.close()
            reader.getFormatName()
        }
        imageTypes
    }
    def getMIMEType(file : File) : String = {
        val imageTypes = this.getImageType(file)
        val mimeType = imageTypes match {
            case "JPEG" => {
                 "image/jpeg"
            }
            case "PNG" => {
                 "image/png"
            }
            case "GIF" => {
                 "image/gif"
            }
            case _ => {
                 "text/html"
            }
        }
        mimeType
    }
}
