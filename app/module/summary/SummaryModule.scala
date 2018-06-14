package module.summary

import com.pharbers.ErrorCode
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.dbManagerTrait.dbInstanceManager
import module.summary.SummaryData.SummaryData
import module.summary.SummaryMessage.{MsgGenerateSummary, MsgSummaryQuery}
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

object SummaryModule extends ModuleTrait with SummaryData{
	def dispatchMsg(msg: MessageDefines)
	               (pr: Option[String Map JsValue])
	               (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = msg match {
		case MsgSummaryQuery(data) => summaryQuery(data)
		case MsgGenerateSummary(data) => generateSummaryData(data)(pr)
		case _ => throw new Exception("function is not impl")
	}
	
	def summaryQuery(data: JsValue)(implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {

			val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).
				getOrElse(throw new Exception("no db connection"))
			val db = conn.queryDBInstance("stp").get
			val o = d2m(data)
			val reVal = db.queryObject(o,"assessment")(s2d(_, data))
//			println(reVal)
			(Some(Map("data" -> toJson(Map("flag" -> toJson(true) )))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	def generateSummaryData(data: JsValue)
	                       (pr: Option[String Map JsValue])
	                       (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			
			(Some(Map("data" -> toJson(Map("flag" -> toJson(true) )))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
}
