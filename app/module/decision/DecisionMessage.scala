package module.decision

import com.pharbers.bmmessages.CommonMessage
import play.api.libs.json.JsValue

abstract class MsgDecisionCommand extends CommonMessage("decision", DecisionModule)
object DecisionMessage {
	case class MsgOutSumPromoBudgetExcelWithHtml(data: JsValue) extends MsgDecisionCommand
	case class MsgOutHospitalExcelWithHtml(data: JsValue) extends MsgDecisionCommand
}
