/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model

import net.liftweb.mapper._

class LiftSessionDAO extends StringKeyedMapper[LiftSessionDAO] {

    def getSingleton = LiftSessionDAO

    //override type TheKeyType = String

    object session_id extends MappedStringIndex(this, 150)
    object session_start extends MappedDateTime(this)
    object session_time extends MappedLong(this)
    object session_ip extends MappedString(this, 15)
    object session_page extends MappedInt(this)
    object session_logged extends MappedBoolean(this)
    object session_user_id extends MappedLongForeignKey(this, LiftUser)
    
    override def primaryKeyField = session_id
}

object LiftSessionDAO extends LiftSessionDAO with StringKeyedMetaMapper[LiftSessionDAO] {

    override def dbTableName = "dforum_sessions"

}