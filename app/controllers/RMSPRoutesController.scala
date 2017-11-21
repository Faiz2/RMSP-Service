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
	def getRequestCookie(request: Request[AnyContent]): Option[String] = request.cookies.get("user_token").map(x => x.value)

	def forward(page: String)(implicit att: AuthTokenTrait, request: Request[AnyContent]): Result = {
		page match {
			case "home" => Ok(views.html.Home.home())
			case "market_info" => Ok(views.html.Module.MarketInfo.index())
			case "brd_info" => Ok(views.html.Module.Brd.index())
			case "product_info" => Ok(views.html.Module.Product.index())
			case "report" => Ok(views.html.Module.Report.report_home())
			case _ => ???
		}
	}
}

class RMSPRoutesController @Inject()(as_inject: ActorSystem, dbt: dbInstanceManager, att: AuthTokenTrait) extends Controller with RoutesFilter {

	implicit val as: ActorSystem = as_inject
	implicit val db_basic : DBTrait = dbt.queryDBInstance("stp").get
	implicit val attoken: AuthTokenTrait = att

	def page(link: String) = Action { implicit request =>
//			println(link)
//			println(parameter)
			forward(link)
	}
	
	def index = Action {
		Ok(views.html.Home.home())
//		Redirect("/page")
	}
	
	def marketInfo = Action {
		Ok(views.html.Module.MarketInfo.index())
	}

	def brdInfo = Action {
		Ok(views.html.Module.Brd.index())
	}

	def productInfo = Action {
		Ok(views.html.Module.Product.index())
	}
	
	def businessDecision = Action {
		Ok(views.html.Module.Decision.BusinessDecision.index())
	}

	def report = Action {
		Ok(views.html.Module.Report.report_home())
	}
}
