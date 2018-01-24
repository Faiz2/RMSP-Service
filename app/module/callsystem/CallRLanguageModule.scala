package module.callsystem

import com.pharbers.ErrorCode
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.dbManagerTrait.dbInstanceManager
import module.callsystem.CallRData.{CallRLanguageData, ReportExcelData}
import module.callsystem.CallRLanguageMessage.{MsgAfterCallR, MsgCallRLanguage}
import play.api.libs.json.JsValue
import play.api.libs.json.Json._

object CallRLanguageModule extends ModuleTrait with CallRLanguageData with ReportExcelData{
	def dispatchMsg(msg: MessageDefines)
				   (pr: Option[String Map JsValue])
				   (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = msg match {
		case MsgCallRLanguage(data) => callRLanuage(data)(pr)
		case MsgAfterCallR(data) =>afterCallR(data)(pr)
		case _ => throw new Exception("function is not impl")
	}

	def callRLanuage(data: JsValue)
					(pr: Option[String Map JsValue])
					(implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {

		try {
//			val reportFileName = (pr.get("data") \ "reportfilename").as[String]
            val reportFileName = "0d452ca4-ef2f-43a1-a5ef-eeb41c69c91b.xlsx" // (pr.get("data") \ "reportfilename").as[String]
			(Some(Map("user"->toJson(pr.get.get("user")),"phase"-> toJson(pr.get.get("phase")),"reportFile" -> toJson(reportFileName),
				"data" -> toJson(Map("flag" -> toJson(callR(pr)), "reportname" -> toJson(reportFileName) )))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	def afterCallR(data : JsValue)
				  (pr: Option[String Map JsValue])
				  (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try{
			val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
			val db = conn.queryDBInstance("stp").get
			val obj = toJson(pr.get)
			db.insertObject(obj,"report","reportFile")
			(pr,None)
		}catch {
			case ex: Exception =>
				println("afterCallR"+ex)
				(None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
}
