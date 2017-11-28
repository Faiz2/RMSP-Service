package module.report

import com.pharbers.bmmessages.CommonMessage
import play.api.libs.json.JsValue

abstract class MsgReportCommand extends CommonMessage("report", ReportModule)
object ReportMessage {
	case class MsgReportMarketSales(data: JsValue) extends MsgReportCommand // 市场销售
	case class MsgReportDelegate(data: JsValue) extends MsgReportCommand // 代表报告
	case class MsgReportManager(data: JsValue) extends MsgReportCommand // 经理报告
	case class MsgReportAllot(data: JsValue) extends MsgReportCommand // 分配报告
	case class MsgReportSales(data: JsValue) extends MsgReportCommand // 销售报告
}
