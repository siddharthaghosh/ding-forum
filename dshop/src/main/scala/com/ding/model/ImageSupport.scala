/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model

trait Image extends UploadFilesBaseModel {
    def getImage() : String
    def setImage(image : String)

    def getFiles() : String = this.getImage()
    def setFiles(files : String) {
        this.setImage(files)
    }
}

trait ImageBaseModel extends BaseModel with Image
