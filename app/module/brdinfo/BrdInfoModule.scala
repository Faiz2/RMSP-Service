package module.brdinfo

import com.pharbers.ErrorCode
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import module.brdinfo.BrdInfoData.BrdInfoData
import module.brdinfo.BrdInfoMessage.alOutBrdInfoExcelValueWithHtml
import play.api.libs.json.JsValue
import play.api.libs.json.Json._

object BrdInfoModule extends ModuleTrait with BrdInfoData {
	def dispatchMsg(msg: MessageDefines)
	               (pr: Option[String Map JsValue])
	               (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = msg match {

		case alOutBrdInfoExcelValueWithHtml(data) => outExcelValueWithHtml(data)(pr)
		case _ => throw new Exception("function is not impl")
	}
	
	def outExcelValueWithHtml(data : JsValue)
							 (pr : Option[String Map JsValue])
							 (implicit cm : CommonModules) : (Option[String Map JsValue], Option[JsValue]) = {
		try {
//			val cycle = (data \ "cycle").asOpt[String].getOrElse("")
			val reValBrdData = pr.getOrElse(throw new Exception("pr data not exist"))("data").
			  						as[List[String Map List[String Map String]]].map(x => x.get("sales_rep")).
			  						filterNot(f => f.isEmpty).flatMap(x => x.get).map(x => x - "pp_sr_acc_revenue" - "pp_sr_acc_field_work" -
			  																					"pp_target_revenue_realization_by_sr" - "pp_motivation_index" -
			  																					"pp_real_revenue_by_sr" - "sales_level" -
			  																					"pp_real_volume_by_sr" - "pp_sales_skills_index" -
																								"pp_product_knowledge_index")

//			(Some(Map("data" -> toJson(setBrdHtmlData(reValBrdData).toString))), None)
            (Some(Map("data" -> toJson(reValBrdData))), None)

		} catch  {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
}
