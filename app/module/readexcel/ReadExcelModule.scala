package module.readexcel

import play.api.libs.json.JsValue
import play.api.libs.json.Json._
import com.pharbers.ErrorCode
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import module.readexcel.ReadExcelData._
import module.readexcel.alReadExcelMessage._

object ReadExcelModule extends ModuleTrait with ReadExcalFileData {
	
	def dispatchMsg(msg: MessageDefines)
	               (pr: Option[String Map JsValue])
	               (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = msg match {
		case alReadExcel(data) => readExcel(data)
		case _ => throw new Exception("function is not impl")
	}
	
	def readExcel(data: JsValue)
	             (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			(Some(Map("data" -> readSourceWithExcel)), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
}