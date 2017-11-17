package module.marketinfo

import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import play.api.libs.json.JsValue
import play.api.libs.json.Json._
import alMarketInfoMessage._
import com.pharbers.ErrorCode
import module.marketinfo.MarketInfoData.MarketInfoData

object MarketInfoModule extends ModuleTrait with MarketInfoData {
	def dispatchMsg(msg: MessageDefines)
	               (pr: Option[String Map JsValue])
	               (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = msg match {
		case alOutMarketInfoExcelValueWithHtml(data) => outExcelValueWithHtml(data)(pr)
		case _ => throw new Exception("function is not impl")
	}
	
	def outExcelValueWithHtml(data: JsValue)
	                         (pr: Option[String Map JsValue])
	                         (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		
		try {
			val cycle = (data \ "cycle").asOpt[String].getOrElse("周期1")
			val reValHostitalData = pr.getOrElse(throw new Exception("pr data not exist"))("data").as[List[String Map List[String Map String]]].map(x => x.get("news")).filterNot(f => f.isEmpty).flatMap(x => x.get)
//			val reValClientInfoData = pr.getOrElse(throw new Exception("pr data not exist"))("data").as[List[String Map List[String Map String]]].map(x => x.get("sales_rep"))
			println(reValHostitalData)
//			setHospitalHtmlData(reValHostitalData)
//			setClientInfoHtmlData(reValClientInfoData)
			(Some(Map("" -> toJson(""))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
}
