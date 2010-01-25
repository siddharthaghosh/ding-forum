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

object categoryReqVar extends RequestVar[LiftCategory]({
        LiftCategory.create
})

class CategorySnippet {


    def listAll(in : NodeSeq) : NodeSeq = {
        val allCates = LiftCategory.findAll
        val processCatNodes = chooseTemplate("category", "entry", in)
        val processForNodes = chooseTemplate("forum", "entry", in)
        allCates.flatMap(cat => {bind("cat", processCatNodes,
                                      "title" -> Text(cat.title.is),
                                      "action" -> {
                        link("/admin/category/index", () => cat.delete_!, Text("Delete")) ++
                        Text(" ") ++
                        link("/admin/category/edit", () => categoryReqVar(cat), Text("Edit"))

                    }) ++ cat.forums.flatMap(forum => {bind("for", processForNodes,
                                                            "title" -> Text(forum.name.is),
                                                            "action" -> {
                                link("/admin/category/index", () => forum.delete_!, Text("Delete")) ++
                                Text(" ") ++
                                link("/admin/forum/edit", () => ForumReqVar(forum), Text("Edit"))

                            }
                        )
                    }
                )
            }
        )
    }

    def manage(in : NodeSeq) : NodeSeq = {
        categoryReqVar.is.toForm(Full("Save"), { _.save })
    }
}
