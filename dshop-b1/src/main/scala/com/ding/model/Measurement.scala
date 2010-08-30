/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model

trait Measurement
extends BaseModel
   with MultiLanguageName {

}

trait MetaMeasurement extends MetaModel[Measurement] {
    
}