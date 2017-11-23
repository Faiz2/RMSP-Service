package module.decision

import com.pharbers.ErrorCode
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import module.decision.DecisionData._
import module.decision.DecisionMessage._
import play.api.libs.json.JsValue
import play.api.libs.json.Json._

object DecisionModule extends ModuleTrait with DecisionData {
	def dispatchMsg(msg: MessageDefines)
	               (pr: Option[String Map JsValue])
	               (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = msg match {
		case MsgOutSumPromoBudgetExcelWithHtml(data) => outExcelValueWithSumPromoBudgetDecision(data)(pr)
		case MsgOutHospitalExcelWithHtml(data) => outExcelValueWithHospital(data)(pr)
		case _ => throw new Exception("function is not impl")
	}
	
	def outExcelValueWithSumPromoBudgetDecision(data: JsValue)(pr: Option[String Map JsValue])(implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			val cycle = (data \ "cycle").asOpt[String].getOrElse("周期1")
			
			val reValSumPrompBudgetData = pr.getOrElse(throw new Exception("pr data not exist"))("data").
										as[List[String Map List[String Map String]]].map(x => x.get("promotion_budget")).
										filterNot(f => f.isEmpty).flatMap(x => x.get).find(f => f("phase") == cycle)
			
			(Some(Map("data" -> toJson(reValSumPrompBudgetData), "pr" -> toJson(pr.get))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	def outExcelValueWithHospital(data: JsValue)(pr: Option[String Map JsValue])(implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			val cycle = (data \ "cycle").asOpt[String].getOrElse("周期1")
			val cycleNum = cycle match {
				case "周期1" => "1"
				case "周期2" => "2"
				case _ => "0"
			}
			def queryPrExcelData(key: String) = {
				pr.get("pr").as[String Map JsValue].getOrElse("data", throw new Exception("pr data not exist")).
					as[List[String Map List[String Map String]]].
					map(x => x.get(key)).filterNot(f => f.isEmpty)
			}
			val reValHospitalData = queryPrExcelData("hospital").flatMap(x => x.get).map(x => Map(x("产品")-> x(cycle), "hospital" -> x("名称"), "code" -> x("hosp_code"))).groupBy(g => g("hospital")).toList.sortBy(s => s._2.head("code").toInt)
			val reValPeopleData = queryPrExcelData("sales_rep").flatMap(x => x.get).map(x => Map("people" -> x("业务代表")))
			val reValSumPrompBudgetData = pr.get("data").as[String Map String].getOrElse("budget", "0")
			
			val reValSumPromotionHtml = setSumPromotionBudget(reValSumPrompBudgetData, cycleNum).toString
			val reValHospitalHtml = setHospitalTab(reValHospitalData, reValPeopleData, cycleNum).toString
			
			val map = Map("reValSumPrompBudgetHtml" -> toJson(reValSumPromotionHtml),
						  "reValHospitalHtml" -> toJson(reValHospitalHtml))
			(Some(Map("data" -> toJson(map))), None)
		} catch {
			case ex: Exception =>
				println(ex.getMessage)
				(None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
}
