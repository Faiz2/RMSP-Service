package module.summary

import com.pharbers.bmmessages.CommonMessage
import play.api.libs.json.JsValue

abstract class SummaryMessageCommand extends CommonMessage("summary", SummaryModule)
object SummaryMessage {
	case class MsgSummaryQuery(data: JsValue) extends SummaryMessageCommand
	case class MsgGenerateSummary(data: JsValue) extends SummaryMessageCommand
}
