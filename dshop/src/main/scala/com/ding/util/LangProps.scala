/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.util

import java.io.{InputStreamReader, FileInputStream, File, FilenameFilter}
import java.util.Properties


class LangPropertyFileFilter extends FilenameFilter {

    private val suffix : String = "xml"

    override def accept(dir : File, fileName : String) : Boolean ={
        return fileName.endsWith(suffix)
    }
}

case class LangProperty(val name : String, val code : String, val directory : String, val image : String) {
    override def toString = "Object LangProperty [name : " + name + ", code : " + code + ", directory : " + directory + ", image : " + image + "]"
}

object LangProps {

    private val lang_prop_dir = getClass.getResource("/lang/")
    private val supportLanguages = new File(lang_prop_dir.getPath)
    private val propFileList : List[File] = supportLanguages.listFiles(new LangPropertyFileFilter).toList

//    propFileList.foreach(
//        propFile => {
//            val ret : Properties = new Properties
//            ret.load(new FileInputStream(propFile))
//            ret.entrySet
//        }
//    )
    lazy val langPropList : List[LangProperty] = propFileList.flatMap {
        case propFile : File => {
                val ret : Properties = new Properties
                ret.loadFromXML(new FileInputStream(propFile))
                //ret.load(new FileInputStream(propFile))
                List(LangProperty(ret.getProperty("name"), ret.getProperty("code"), ret.getProperty("directory"), ret.getProperty("image")))
            }
    }
}
