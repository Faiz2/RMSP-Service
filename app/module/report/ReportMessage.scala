package module.report

import com.pharbers.bmmessages.CommonMessage
import play.api.libs.json.JsValue

abstract class MsgReportCommand extends CommonMessage("report", ReportModule)
object ReportMessage {
	case class MsgReportMarketSalesCommercialValue(data: JsValue) extends MsgReportCommand // 市场销售-商业价值
	case class MsgReportMarketSalesPerformance(data: JsValue) extends MsgReportCommand // 市场销售-销售业绩
	case class MsgReportMarketSales(data: JsValue) extends MsgReportCommand //市场销售，汇总输出view
	
	case class MsgReportDeputyTimerAllot(data: JsValue) extends MsgReportCommand // 代表报告-时间分配
	case class MsgReportDeputyProductInformation(data: JsValue) extends MsgReportCommand // 代表报告-产品知识
	case class MsgReportDeputyEmpiric(data: JsValue) extends MsgReportCommand // 代表报告-经验
	case class MsgReportDeputySalesSkills(data: JsValue) extends MsgReportCommand // 代表报告-销售技巧
	case class MsgReportDeputyWorkAttitude(data: JsValue) extends MsgReportCommand // 代表报告-工作积极性
	case class MsgReportDeputy(data: JsValue) extends MsgReportCommand //代表报告，汇总输出view

	case class MsgReportManagerCost(data: JsValue) extends MsgReportCommand // 经理报告-职员成本
	case class MsgReportManagerTimerAllot(data: JsValue) extends MsgReportCommand // 经理报告-时间分配
	case class MsgReportManager(data: JsValue) extends MsgReportCommand // 经理报告，汇总输出view

	case class MsgReportAllot(data: JsValue) extends MsgReportCommand // 分配报告
	case class MsgReportAllotView(data: JsValue) extends MsgReportCommand // 分配报告，汇总输出view

	case class MsgReportSalesCustomer(data: JsValue) extends MsgReportCommand // 销售报告-销售额/顾客
	case class MsgReportSalesCustomerMerge(data: JsValue) extends MsgReportCommand // 销售报告，汇总输出view
	
	case class MsgReportSalesDeputy(data: JsValue) extends MsgReportCommand // 销售报告-销售额/代表
	case class MsgReportSalesDeputyMerge(data: JsValue) extends MsgReportCommand // 销售额/代表，汇总输出view
	
	case class MsgReportSalesProduct(data: JsValue) extends MsgReportCommand // 销售报告-销售额/产品
	case class MsgReportSalesProductMerge(data: JsValue) extends MsgReportCommand // 销售额/产品，汇总输出view
	
	case class MsgDownReport(data: JsValue) extends  MsgReportCommand// 报告生成
}
