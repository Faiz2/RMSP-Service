package module.productinfo

import play.api.libs.json.JsValue
import play.api.libs.json.Json._

import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.ErrorCode

import module.productinfo.ProductInfoData.ProductInfoData
import module.productinfo.ProductInfoMessage.alOutProductInfoExcelValueWithHtml

object ProductInfoModule extends ModuleTrait with ProductInfoData{
	def dispatchMsg(msg: MessageDefines)
				   (pr: Option[String Map JsValue])
				   (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = msg match {

		case alOutProductInfoExcelValueWithHtml(data) => outExcelValueWithHtml(data)(pr)
		case _ => throw new Exception("fucntion is not impl")
	}


	def outExcelValueWithHtml(data: JsValue)(pr: Option[String Map JsValue])(implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
//			val cycle = (data \ "cycle").asOpt[String].getOrElse("")
			val reValProductInfoData = ""

			(Some(Map("" -> toJson(""))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
}
