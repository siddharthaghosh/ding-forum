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

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
    def boot {
        if (!DB.jndiJdbcConnAvailable_?)
            DB.defineConnectionManager(DefaultConnectionIdentifier, DBVendor)

        LiftRules.passNotFoundToChain = false
// where to search snippet
        LiftRules.addToPackages("com.ding")
        Schemifier.schemify(true, Log.infoF _, schemify_arr : _*)

//        LiftRules.dispatch.append({
//            case Req(List("doc","add"), _, _) => () => addDocContent()
//            })
        FrontController.setupController()
        // Build SiteMap
        val entries = Menu(Loc("Home", List("index"), "Home")) :: Menu(Loc("Test", List("test"), "Test")) :: Menu(Loc("Form", List("form"),"Form")):: User.sitemap
        LiftRules.setSiteMap(SiteMap(entries:_*))

        /*
         * Show the spinny image when an Ajax call starts
         */
        LiftRules.ajaxStart =
            Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

        /*
         * Make the spinny image go away when it ends
         */
        LiftRules.ajaxEnd =
            Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

        LiftRules.early.append(makeUtf8)

        LiftRules.loggedInTest = Full(() => User.loggedIn_?)

        S.addAround(DB.buildLoanWrapper)

//        ShopLogger.debug(LangProps.langPropList.toString)

    }

    private val schemify_arr : Array[BaseMetaMapper] = Array(User, LiftDocument, LiftLanguage,
                                                             LiftCategory, LiftCategoryNameDescription, LiftOptionGroup,
                                                             LiftOptionGroupName, LiftOptionValue, LiftOptionValueName,
                                                             LiftManufacturer, LiftProduct, LiftProductNameDescription,
                                                             LiftProductCategory, LiftUploadFile)
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
                    val url = dbUrl + "?characterEncoding=utf-8"
                    println(url)
//                  val conn = DriverManager.getConnection(url)
                  val conn = DriverManager.getConnection(url, user, pwd)
//                    val conn = DriverManager.getConnection(dbUrl, user, pwd)
                    conn.getClientInfo.list(System.out)
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


