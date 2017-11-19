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
			val reValProductInfoData =  pr.getOrElse(throw new Exception("pr data not exist"))("data").
			  						as[List[String Map List[String Map String]]].map(x => x.get("product")).
			  						filterNot(f => f.isEmpty).flatMap(x => x.get)


			(Some(Map("data" -> toJson(setProductInfoData(reValProductInfoData).toString))), None)

		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
}
