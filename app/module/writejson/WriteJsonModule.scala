package module.writejson

import com.pharbers.ErrorCode
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import module.writejson.WriteJsonData._
import module.writejson.WriteJsonMessage._
import play.api.libs.json.JsValue
import play.api.libs.json.Json._

object WriteJsonModule extends ModuleTrait with WriteJsonData {
	def dispatchMsg(msg: MessageDefines)
				   (pr: Option[String Map JsValue])
				   (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = msg match {
		case WriterJsonData(data) => writeJsonWithFile(data)
		case _ => throw new Exception("function is not impl")
	}

	def writeJsonWithFile(data: JsValue)(implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			implicit val func = HandleImpl.j2s
			(Some(Map("data" -> toJson(Map("flag" -> toJson(true), "filename" -> toJson(wirteJson(data)) )))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
}
