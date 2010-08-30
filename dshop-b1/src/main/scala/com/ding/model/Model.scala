/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model

trait Model {
    def saveInstance() : Boolean
    def deleteInstance() : Boolean
}

trait BaseModel extends Model {
    def getID() : Long
}

trait MetaModel[+T] {
    def newInstance() : T
    def findOneInstance(id : Long) : T
    def findAllInstances() : List[T]
}
