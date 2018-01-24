package controllers

import javax.inject.Inject

import com.pharbers.cliTraits.DBTrait
import akka.actor.ActorSystem
import com.pharbers.bmmessages.{CommonModules, MessageRoutes}
import com.pharbers.dbManagerTrait.dbInstanceManager
import com.pharbers.token.AuthTokenTrait
import controllers.common.requestArgsQuery
import com.pharbers.bmpattern.LogMessage.{common_log, msg_log}
import com.pharbers.bmpattern.ResultMessage.{common_result, msg_CommonResultMessage}
import module.brdinfo.BrdInfoMessage.alOutBrdInfoExcelValueWithHtml
<<<<<<< HEAD
import module.marketinfo.alMarketInfoMessage.alOutMarketInfoExcelValueWithHtml
=======
>>>>>>> origin/alfred-180123
import module.productinfo.ProductInfoMessage.alOutProductInfoExcelValueWithHtml
import module.readexcel.alReadExcelMessage.alReadExcel
import play.api.libs.json.JsValue
import play.api.mvc.Action
import play.api.libs.json.Json.toJson
import play.api.mvc._

trait TokenCheck {

}

trait RoutesFilter extends TokenCheck { this: Controller =>
	// TODO: 错误的逻辑
//	def getRequestCookie(request: Request[AnyContent]): Option[String] = request.cookies.get("user_token").map(x => x.value)
//
//	def forward(page: String)(implicit att: AuthTokenTrait, request: Request[AnyContent]): Result = {
//		page match {
//			case "home" => Ok(views.html.Home.home())
//			case "market_info" => Ok(views.html.Module.MarketInfo.index())
//			case "brd_info" => Ok(views.html.Module.Brd.index())
//			case "product_info" => Ok(views.html.Module.Product.index())
//			case "report" => Ok(views.html.Module.Report.index())
//			case _ => ???
//		}
//	}
	
	// TODO : 前端只存登入用户名，后续有权限再去掉，新增权限过滤，与整个的filter
	def getUserCookie(request: Request[AnyContent])(page: Result): Result = {
		request.cookies.get("user").map(x => x.value) match {
			case None => Redirect("/")
			case _ => page
		}
	}
}

class RMSPRoutesController @Inject()(as_inject: ActorSystem, dbt: dbInstanceManager, att: AuthTokenTrait) extends Controller with RoutesFilter {

	implicit val as: ActorSystem = as_inject
	implicit val db_basic : DBTrait = dbt.queryDBInstance("stp").get
	implicit val attoken: AuthTokenTrait = att
	
	//TODO: 失败错误的跳转，得认真想想
//	def page(link: String) = Action { implicit request =>
//			forward(link)
//	}
	
	def login = Action {
		Ok(views.html.Login.login())
	}
	
	def register = Action {
		Ok(views.html.Login.register())
	}
	
	def index = Action { request =>
//		getUserCookie(request) (Ok(views.html.Home.home()))
        getUserCookie(request) (Ok(views.html.Module.Brd.brd_index(Nil)))
	}

	def brd = Action { request =>
        getUserCookie(request) {
            val jv = toJson("")
            val reVal =
            requestArgsQuery().commonExcution(
                MessageRoutes(msg_log(toJson(Map("method" -> toJson("alOutExcelVcalueWithHtml"))), jv)
                    :: alReadExcel(jv)
                    :: alOutBrdInfoExcelValueWithHtml(jv)
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
            )

            if ((reVal \ "status").asOpt[String].get == "ok") {
                Ok(views.html.Module.Brd.brd_index(
                    (reVal \ "result" \ "data").asOpt[List[JsValue]].get
                ))
            } else Redirect("/login")
        }
    }

<<<<<<< HEAD
	
	def market = Action { request =>
		getUserCookie(request){
			val jv = request.body.asJson.getOrElse(toJson(""))
			val reVal =
				requestArgsQuery().commonExcution(
					MessageRoutes(msg_log(toJson(Map("method" -> toJson("alOutExcelVcalueWithHtml"))), jv)
						:: alReadExcel(jv)
						:: alOutMarketInfoExcelValueWithHtml(jv)
						:: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
				)
			
			if ((reVal \ "status").asOpt[String].get == "ok") {
				println(reVal \ "result"  \ "data")
				Ok(views.html.Login.login())
			} else Redirect("/login")
		}
	}


=======
>>>>>>> origin/alfred-180123
    def product = Action { request =>
        getUserCookie(request) {
            val jv = toJson("")
            val reVal =
            requestArgsQuery().commonExcution(
                MessageRoutes(msg_log(toJson(Map("method" -> toJson("alOutExcelValueWithHtml"))), jv)
                    :: alReadExcel(jv)
                    :: alOutProductInfoExcelValueWithHtml(jv)
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
            )

            if ((reVal \ "status").asOpt[String].get == "ok") {
                Ok(views.html.Module.Product.product_index(
                    (reVal \ "result" \ "data").asOpt[List[JsValue]].get
                ))
            } else Redirect("/login")
        }
    }

	def marketInfo = Action { request =>
		getUserCookie(request)(Ok(views.html.Module.MarketInfo.index()))
	}

	def brdInfo = Action { request =>
		getUserCookie(request)(Ok(views.html.Module.Brd.index()))
	}

	def productInfo = Action { request =>
		getUserCookie(request)(Ok(views.html.Module.Product.index()))
	}
	
	def businessDecision = Action { request =>
		getUserCookie(request)(Ok(views.html.Module.Decision.BusinessDecision.index()))
	}
	
	def managementDecision = Action { request =>
		getUserCookie(request)(Ok(views.html.Module.Decision.ManagementDecision.index()))
	}

	def report = Action { request =>
		getUserCookie(request)(Ok(views.html.Module.Report.index()))
	}
	
	
}
