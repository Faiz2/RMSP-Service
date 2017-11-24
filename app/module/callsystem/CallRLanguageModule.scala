package module.callsystem

import com.pharbers.ErrorCode
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import module.callsystem.CallRData.CallRLanguageData
import module.callsystem.CallRLanguageMessage.MsgCallRLanguage
import play.api.libs.json.JsValue
import play.api.libs.json.Json._

object CallRLanguageModule extends ModuleTrait with CallRLanguageData {
	def dispatchMsg(msg: MessageDefines)
				   (pr: Option[String Map JsValue])
				   (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = msg match {
		case MsgCallRLanguage(data) => callRLanuage(data)(pr)
		case _ => throw new Exception("function is not impl")
	}

	def callRLanuage(data: JsValue)
					(pr: Option[String Map JsValue])
					(implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {

		try {
			(Some(Map("data" -> toJson(Map("flag" -> toJson(callR()))))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
}
