package controllers

import play.api.mvc._

trait RoutesFilter {
	def getRequestCookie(request: Request[AnyContent]): Option[String] = {
		None
	}

	def forward(page: String) = {}


}

class RMSPRoutesController extends Controller {
	
	def index = Action {
		Ok(views.html.Home.home())
	}
	
	def marketInfo = Action {
		Ok(views.html.Module.MarketInfo.market_home())
	}
	
	
}
