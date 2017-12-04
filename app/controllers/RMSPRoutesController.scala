package controllers

import javax.inject.Inject

import akka.actor.ActorSystem
import com.pharbers.cliTraits.DBTrait
import com.pharbers.dbManagerTrait.dbInstanceManager
import com.pharbers.token.AuthTokenTrait
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
		getUserCookie(request)(Ok(views.html.Home.home()))
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
