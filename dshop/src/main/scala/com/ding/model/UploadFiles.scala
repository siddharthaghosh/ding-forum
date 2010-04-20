/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model

trait UploadFilesBaseModel extends BaseModel {
    def getFiles() : String
    def setFiles(files : String)
}
