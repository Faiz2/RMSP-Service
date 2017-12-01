package controllers.report

import javax.inject.Inject

import akka.actor.ActorSystem
import com.pharbers.bmmessages.{CommonModules, MessageRoutes}
import com.pharbers.bmpattern.LogMessage.{common_log, msg_log}
import com.pharbers.bmpattern.ResultMessage.{common_result, msg_CommonResultMessage}
import com.pharbers.dbManagerTrait.dbInstanceManager
import com.pharbers.token.AuthTokenTrait
import controllers.common.requestArgsQuery
import play.api.libs.json.Json.toJson
import play.api.mvc.Action
import module.report.ReportMessage._

class ReportController @Inject()(as_inject: ActorSystem, dbt: dbInstanceManager, att: AuthTokenTrait){

	implicit val as: ActorSystem = as_inject

	def reportMarketSalesController = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
		MessageRoutes(msg_log(toJson(Map("method" -> toJson("reportMarketSalesController"))), jv)
		  :: MsgReportMarketSalesCommercialValue(jv)
		  :: MsgReportMarketSalesPerformance(jv)
		  :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
	})

	def reportDeputyController = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
		MessageRoutes(msg_log(toJson(Map("method" -> toJson("reportDeputyController"))), jv)
		  :: MsgReportDeputyTimerAllot(jv)
		  :: MsgReportDeputyProductInformation(jv)
		  :: MsgReportDeputyEmpiric(jv)
		  :: MsgReportDeputySalesSkills(jv)
		  :: MsgReportDeputyWorkAttitude(jv)
		  :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
	})

	def reportManagerController = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
		MessageRoutes(msg_log(toJson(Map("method" -> toJson("reportManagerController"))), jv)
		  :: MsgReportManagerCost(jv)
		  :: MsgReportManagerTimerAllot(jv)
		  :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
	})


	def reportAllotController = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
		MessageRoutes(msg_log(toJson(Map("method" -> toJson("reportAllotController"))), jv)
		  :: MsgReportAllot(jv)
		  :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
	})


	def reportSalesController = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
		MessageRoutes(msg_log(toJson(Map("method" -> toJson("reportSalesController"))), jv)
		  :: MsgReportSalesCustomer(jv)
		  :: MsgReportSalesDeputy(jv)
		  :: MsgReportSalesProduct(jv)
		  :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
	})
}
