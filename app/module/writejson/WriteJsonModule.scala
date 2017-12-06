package module.writejson

import java.util.UUID

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
			val reportFileName = s"${UUID.randomUUID().toString}.xlsx"
			(Some(Map("data" -> toJson(Map("flag" -> toJson(true), "jsonfilename" -> toJson(wirteJson(data)), "reportfilename" -> toJson(reportFileName) )))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
}
