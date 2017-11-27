package module.report

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
		case MsgReport(data) => report(data)
	  	case _ => throw new Exception("function is not impl")
	 }

	def report(data: JsValue)
			  (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
			val db = conn.queryDBInstance("stp").get

			
			(Some(Map("data" -> toJson(""))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
}
