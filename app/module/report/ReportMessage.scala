package module.report

import com.pharbers.bmmessages.CommonMessage
import play.api.libs.json.JsValue

abstract class MsgReportCommand extends CommonMessage("report", ReportModule)
object ReportMessage {
	case class MsgReportMarketSalesCommercialValue(data: JsValue) extends MsgReportCommand // 市场销售-商业价值
	case class MsgReportMarketSalesPerformance(data: JsValue) extends MsgReportCommand // 市场销售-销售业绩
	
	case class MsgReportDeputyTimerAllot(data: JsValue) extends MsgReportCommand // 代表报告-时间分配
	case class MsgReportDeputyProductInformation(data: JsValue) extends MsgReportCommand // 代表报告-产品知识
	case class MsgReportDeputyManage(data: JsValue) extends MsgReportCommand // 代表报告-经理报告
	
	
	case class MsgReportManager(data: JsValue) extends MsgReportCommand // 经理报告
	case class MsgReportAllot(data: JsValue) extends MsgReportCommand // 分配报告
	case class MsgReportSales(data: JsValue) extends MsgReportCommand // 销售报告
}
