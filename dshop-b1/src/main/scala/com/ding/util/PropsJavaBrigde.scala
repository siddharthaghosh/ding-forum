/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.util
import net.liftweb.util.Props

class PropsJavaBridge {
    def getProperty(key : String) : String = Props.get(key).open_!
}
