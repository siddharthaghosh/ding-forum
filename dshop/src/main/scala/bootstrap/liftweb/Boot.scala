package bootstrap.liftweb

import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.provider._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import _root_.net.liftweb.mapper.{DB, ConnectionManager, Schemifier, DefaultConnectionIdentifier, ConnectionIdentifier, BaseMetaMapper}
import _root_.java.sql.{Connection, DriverManager}
import _root_.com.ding.model._
import _root_.com.ding.model.lift._
import _root_.javax.servlet.http.{HttpServletRequest}
import com.ding.controller._
import com.ding.util._
import _root_.scala.xml.{NodeSeq, Text}

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
    def boot {
        if (!DB.jndiJdbcConnAvailable_?)
            DB.defineConnectionManager(DefaultConnectionIdentifier, DBVendor)

        LiftRules.passNotFoundToChain = true
        LiftRules.useXhtmlMimeType = false
        LiftRules.enableLiftGC = false
        LiftRules.autoIncludeAjax = {
            session => {
                false
            }
        }
//        LiftRules.liftRequest.append {
////            case Req("client"::"admin_client"::_, _, _) => true
////            case Req("client"::"certificate_cilent"::_,_,_) => true
//        }

// where to search snippet
        LiftRules.addToPackages("com.ding")
        Schemifier.schemify(true, Log.infoF _, schemify_arr : _*)

//        LiftRules.dispatch.append({
//            case Req(List("doc","add"), _, _) => () => addDocContent()
//            })
        FrontController.setupController()
        // Build SiteMap
        val adminMenu = Menu(
            Loc("Admin",
                ("client"::"admin_client"::"eshop"::Nil),
                "Admin",
                If(
                    ()=>Administrator.loggedIn_?, ()=> ForbiddenResponse()
                )
            )
        )
//        val myLink = MyLink.strPairToMyLink("client"::"certificate_client"::Nil,true)
        val certLoc = Loc("certificate",
                          ("client"::"certificate_client"::"admin"::"login"::Nil),
                          "Certificate"
        )
        val certificateMenu = Menu(
            certLoc
        )
        val testLoc = Loc("TestMatchHead", ("test"::Nil), "TestMatchHead")
        val testMenu = Menu(testLoc)
        val entries = Menu(Loc("Home", List("index"), "Home")) :: testMenu::Menu(Loc("Form", List("form"),"Form"))::  adminMenu :: certificateMenu :: Administrator.sitemap
        
        LiftRules.setSiteMap(SiteMap(entries:_*))

//        /*
//         * Show the spinny image when an Ajax call starts
//         */
//        LiftRules.ajaxStart =
//            Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)
//
//        /*
//         * Make the spinny image go away when it ends
//         */
//        LiftRules.ajaxEnd =
//            Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

        LiftRules.early.append(makeUtf8)

        LiftRules.loggedInTest = Full(() => Administrator.loggedIn_?)

        S.addAround(DB.buildLoanWrapper)

//        ShopLogger.debug(LangProps.langPropList.toString)

    }

    private val schemify_arr : Array[BaseMetaMapper] = Array(User, LiftDocument, LiftLanguage,
                                                             LiftCategory, LiftCategoryNameDescription, LiftOptionGroup,
                                                             LiftOptionGroupName, LiftOptionValue, LiftOptionValueName,
                                                             LiftManufacturer, LiftProduct, LiftProductNameDescription,LiftGoods,
                                                             LiftProductCategory, LiftUploadFile, LiftType, LiftTypeName, LiftTypeOptionGroup,
                                                             LiftMeasurement, LiftMeasurementName, Administrator)
    /**
     * Force the request to be UTF-8
     */
    /**
     * Force the request to be UTF-8
     */
    private def makeUtf8(req: HTTPRequest) {
        req.setCharacterEncoding("UTF-8")
    }

}

/**
 * Database connection calculation
 */
object DBVendor extends ConnectionManager {
    private var pool: List[Connection] = Nil
    private var poolSize = 0
    private val maxPoolSize = 4

    private def createOne: Box[Connection] = try {
        val driverName: String = Props.get("db.driver") openOr
        "org.apache.derby.jdbc.EmbeddedDriver"

        val dbUrl: String = Props.get("db.url") openOr
        "jdbc:derby:lift_example;create=true"

        Class.forName(driverName)

        val dm = (Props.get("db.user"), Props.get("db.password")) match {
            case (Full(user), Full(pwd)) =>{
//                    val url = dbUrl + "?user=" + user + "&password=" + pwd + "&characterEncoding=utf-8"
//                    val url = dbUrl + "?characterEncoding=utf-8"
//                    println(url)
//                  val conn = DriverManager.getConnection(url)
//                  val conn = DriverManager.getConnection(url, user, pwd)
                    val conn = DriverManager.getConnection(dbUrl, user, pwd)
                    conn
//                    DriverManager.getConnection(url)
                }

            case _ => DriverManager.getConnection(dbUrl)
        }

        Full(dm)
    } catch {
        case e: Exception => e.printStackTrace; Empty
    }

    def newConnection(name: ConnectionIdentifier): Box[Connection] =
        synchronized {
            pool match {
                case Nil if poolSize < maxPoolSize =>
                    val ret = createOne
                    poolSize = poolSize + 1
                    ret.foreach(c => pool = c :: pool)
                    ret

                case Nil => wait(1000L); newConnection(name)
                case x :: xs => try {
                        x.setAutoCommit(false)
                        Full(x)
                    } catch {
                        case e => try {
                                pool = xs
                                poolSize = poolSize - 1
                                x.close
                                newConnection(name)
                            } catch {
                                case e => newConnection(name)
                            }
                    }
            }
        }

    def releaseConnection(conn: Connection): Unit = synchronized {
        pool = conn :: pool
        notify
    }
}

class MyLink[-T](override val uriList: List[String], override val matchHead_? : Boolean) extends Link(uriList, matchHead_?) {
    def this(b: List[String]) = this(b, false)

    override def isDefinedAt(req: Req): Boolean = {
        println("link matchHead_? : " + this.matchHead_?)
        if (matchHead_?) {
            println("head matched")
            req.path.partPath.take(uriList.length) == uriList
        }
        else {
            println("head no match")
            uriList == req.path.partPath
        }
    }

//    def createLink(value: T): Box[NodeSeq] = Full(Text(createPath(value)))
}
object MyLink {
    def apply(urlLst: List[String], matchHead_? : Boolean, url: String) = {
        new MyLink[Unit](urlLst, matchHead_?) {
            /* override */ def createLink(value: Unit): Box[NodeSeq] = Full(Text(url))
        }
    }

    implicit def strLstToMyLink(in: Seq[String]): MyLink[Unit] = new MyLink[Unit](in.toList)
    implicit def strPairToMyLink(in: (Seq[String], Boolean)): MyLink[Unit] = new MyLink[Unit](in._1.toList, in._2)
}



