package module.report

import com.mongodb.casbah.Imports._
import com.pharbers.ErrorCode
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.dbManagerTrait.dbInstanceManager
import module.report.ReportData._
import module.report.ReportMessage._
import play.api.libs.json.JsValue
import play.api.libs.json.Json._

object ReportModule extends ModuleTrait with ReportData {
	 def dispatchMsg(msg: MessageDefines)
					(pr: Option[String Map JsValue])
					(implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = msg match {
		case MsgReportMarketSalesCommercialValue(data) => marketSalesCommercialValueReport(data)
		case MsgReportMarketSalesPerformance(data) => marketSalesPerformanceReport(data)(pr)
		
		case MsgReportDelegate(data) => delegateReport(data)
		case MsgReportManager(data) => managerReport(data)
		case MsgReportAllot(data) => allotReport(data)
		case MsgReportSales(data) => salesReport(data)
	  	case _ => throw new Exception("function is not impl")
	 }

	def marketSalesCommercialValueReport(data: JsValue)
			  			                (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
			val db = conn.queryDBInstance("stp").get
			val user = (data \ "condition" \ "user").asOpt[String].getOrElse(throw new Exception("wrong input"))
			val cycle = (data \ "condition" \ "cycle").asOpt[String].getOrElse(throw new Exception("wrong input"))
			
			db.queryObject(DBObject("phase" -> cycle :: Nil), user)(d2mMarketSales)


			(Some(Map("data" -> toJson(""))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	def marketSalesPerformanceReport(data: JsValue)
	                                (pr: Option[String Map JsValue])
			  			            (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
			val db = conn.queryDBInstance("stp").get
			val user = (data \ "condition" \ "user").asOpt[String].getOrElse(throw new Exception("wrong input"))
			val cycle = (data \ "condition" \ "cycle").asOpt[String].getOrElse(throw new Exception("wrong input"))
			
			val prRsult = pr match {
				case None => throw new Exception("")
				case Some(one) =>
					one("data").asOpt[List[String Map JsValue]]
			}
			
			db.queryObject(DBObject("phase" -> cycle :: Nil), user)(d2mMarketSales)
			
			(Some(Map("" -> toJson(""))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
		
	}
	
	
	def delegateReport(data: JsValue)
	                  (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			
			(Some(Map("data" -> toJson(""))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}

	def managerReport(data: JsValue)
					 (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			
			(Some(Map("" -> toJson(""))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	def allotReport(data: JsValue)
				   (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {

			(Some(Map("" -> toJson(""))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}

	def salesReport(data: JsValue)
				   (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {

			(Some(Map("" -> toJson(""))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}





}