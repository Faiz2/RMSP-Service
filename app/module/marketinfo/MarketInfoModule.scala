package module.marketinfo

import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import play.api.libs.json.JsValue
import play.api.libs.json.Json._
import alMarketInfoMessage._
import com.pharbers.ErrorCode
import module.marketinfo.MarketInfoData._

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
			val reValHostitalData = pr.getOrElse(throw new Exception("pr data not exist"))("data").
			  						as[List[String Map List[String Map String]]].map(x => x.get("news")).
			  						filterNot(f => f.isEmpty).flatMap(x => x.get).map(x => Map("letters" -> x(cycle),
																								"hospname" -> x("医院"),
																								"periodsales" -> x("上期总销售额(元)"),
																								"口服抗生素" -> x("口服抗生素"),
																								"一代降糖药" -> x("一代降糖药"),
																								"三代降糖药" -> x("三代降糖药"),
																								"皮肤药" -> x("皮肤药")
			))
			val reValClientInfoData = pr.getOrElse(throw new Exception("pr data not exist"))("data").
			  						as[List[String Map List[String Map String]]].map(x => x.get("hospital")).
			  						filterNot(f => f.isEmpty).flatMap(x => x.get).map(x => Map("hospname" -> x("名称"), x("产品") -> x(cycle), "area" -> x("区域"), "type" -> x("类型"))).groupBy(g => g("hospname") )

			val condition = Map("news" -> toJson(setHospitalHtmlData(reValHostitalData).toString), "clientInfo" -> toJson(setClientInfoHtmlData(reValClientInfoData).toString))

			(Some(Map("data" -> toJson(condition))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
}
