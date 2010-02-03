/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model

import net.liftweb.mapper._
import net.liftweb.mapper.MappedText

class LiftUser extends LongKeyedMapper[LiftUser] {

    def getSingleton = LiftUser
    override def primaryKeyField = user_id

    object user_id extends MappedLongIndex(this)
    object user_active extends MappedBoolean(this)
    object username extends MappedString(this, 50)
    object user_password extends MappedString(this, 32)
    object user_session_time extends MappedLong(this)
    object user_session_page extends MappedInt(this)
    object user_lastvisit extends MappedDateTime(this)
    object user_regdate extends MappedDateTime(this)
    object user_level extends MappedInt(this)
    object user_posts extends MappedInt(this)
    object user_timezone extends MappedString(this, 5)
    object user_style extends MappedInt(this)
    object user_lang extends MappedString(this, 255)
    object user_dateformat extends MappedString(this, 20)
    object user_new_primsg extends MappedInt(this)
    object user_unread_primsg extends MappedInt(this)
    object user_last_primsg extends MappedDateTime(this)
    object user_emailtime extends MappedDateTime(this)
    object user_viewmail extends MappedBoolean(this)
    object user_attachsig extends MappedBoolean(this)
    object user_allowhtml extends MappedBoolean(this)
    object user_allowbbcode extends MappedBoolean(this)
    object user_allowsmilies extends MappedBoolean(this)
    object user_allowavatar extends MappedBoolean(this)
    object user_allow_pm extends MappedBoolean(this)
    object user_allow_viewonline extends MappedBoolean(this)
    object user_notify extends MappedBoolean(this)
    object user_notify_always extends MappedBoolean(this)
    object user_notify_text extends MappedBoolean(this)
    object user_notify_pm extends MappedBoolean(this)
    object user_popup_pm extends MappedBoolean(this)
    object rank_id extends MappedInt(this)
    object user_avatar extends MappedString(this, 100)
    object user_avatar_type extends MappedInt(this)
    object user_email extends MappedString(this, 255)
    object user_qq extends MappedString(this, 15)
    object user_website extends MappedString(this, 255)
    object user_from extends MappedString(this, 100)
    object user_sig extends MappedText(this)
    object user_sig_bbcode_uid extends MappedString(this, 10)
    object user_aim extends MappedString(this, 255)
    object user_yim extends MappedString(this, 255)
    object user_msn extends MappedString(this, 255)
    object user_occ extends MappedString(this, 100)
    object user_interests extends MappedString(this, 255)
    object user_biography extends MappedText(this)
    object user_actkey extends MappedString(this, 32)
    object user_gender extends MappedString(this, 1)
    object themes_id extends MappedInt(this)
    object deleted extends MappedBoolean(this)
    object user_viewonline extends MappedBoolean(this)
    object security_hash extends MappedString(this, 32)
    object user_karma extends MappedDouble(this)
    object user_authhash extends MappedString(this, 32)

}

object LiftUser extends LiftUser with LongKeyedMetaMapper[LiftUser] {

    override def dbTableName = "dforum_users"

}
