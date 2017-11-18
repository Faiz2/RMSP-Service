package module.brdinfo

import com.pharbers.ErrorCode
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import module.brdinfo.BrdInfoMessage.alOutBrdInfoExcelValueWithHtml
import play.api.libs.json.JsValue
import play.api.libs.json.Json._

object BrdInfoModule extends ModuleTrait{
	def dispatchMsg(msg: MessageDefines)
	               (pr: Option[String Map JsValue])
	               (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = msg match {
		case alOutBrdInfoExcelValueWithHtml(data) => outExcelValueWithHtml(data)(pr)
		case _ => throw new Exception("function is not impl")
	}
	
	def outExcelValueWithHtml(data: JsValue)(pr: Option[String Map JsValue])(implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			
			(Some(Map("" -> toJson(""))), None)
		} catch  {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
}
