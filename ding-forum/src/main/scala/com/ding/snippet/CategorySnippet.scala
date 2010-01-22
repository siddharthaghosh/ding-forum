/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.snippet

import com.ding.model._
import scala.xml._
import net.liftweb.common._
import net.liftweb.util.Helpers._
import net.liftweb.http._
import net.liftweb.http.SHtml._
//import net.liftweb.http.S

class CategorySnippet {

    object categoryReqVar extends RequestVar[LiftCategory]({
        LiftCategory.create
    })

    def listAll(in : NodeSeq) : NodeSeq = {
        val allCates = LiftCategory.findAll
        allCates.flatMap(cat => {bind("cat", chooseTemplate("category", "entry", in),
                                    "title" -> Text(cat.title.is),
                                    "action" -> {
                                                    link("/admin/category/index", () => cat.delete_!, Text("Delete")) ++  
                                                    Text(" ") ++
                                                    link("/admin/category/edit", () => categoryReqVar(cat), Text("Edit"))

                                                })})
    }

    def manage(in : NodeSeq) : NodeSeq = {
        categoryReqVar.is.toForm(Full("Save"), { _.save })
    }
}
