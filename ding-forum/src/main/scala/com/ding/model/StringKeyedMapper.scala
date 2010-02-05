/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.model

import net.liftweb.mapper._

trait BaseStringKeyedMapper extends BaseKeyedMapper{
    override type TheKeyType = String
}

trait StringKeyedMapper[OwnerType <: StringKeyedMapper[OwnerType]] extends KeyedMapper[String, OwnerType] with BaseStringKeyedMapper {
    self: OwnerType =>
}

trait StringKeyedMetaMapper[A <: StringKeyedMapper[A]] extends KeyedMetaMapper[String, A] {
    self: A =>
}