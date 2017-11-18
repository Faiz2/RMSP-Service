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
		Ok(views.html.Module.MarketInfo.index())
	}
	
	def brdInfo = Action {
		Ok(views.html.Module.Brd.index())
	}
	
	def productInfo = Action {
		Ok(views.html.Module.Product.product_home())
	}
	
	def report = Action {
		Ok(views.html.Module.Report.report_home())
	}
}
