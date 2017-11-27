package module.report

import com.pharbers.bmmessages.CommonMessage
import play.api.libs.json.JsValue

abstract class MsgReportCommand extends CommonMessage("report", ReportModule)
object ReportMessage {
	case class MsgReport(data: JsValue) extends MsgReportCommand
}
