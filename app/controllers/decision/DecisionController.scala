package controllers.decision

import javax.inject.Inject

import module.readexcel.alReadExcelMessage.alReadExcel
import play.api.libs.json.Json.toJson
import akka.actor.ActorSystem
import com.pharbers.bmmessages.{CommonModules, MessageRoutes}
import com.pharbers.dbManagerTrait.dbInstanceManager
import com.pharbers.token.AuthTokenTrait
import com.pharbers.bmpattern.LogMessage.{common_log, msg_log}
import com.pharbers.bmpattern.ResultMessage.{common_result, msg_CommonResultMessage}
import controllers.common.requestArgsQuery
import module.decision.DecisionMessage._
import play.api.mvc.Action

class DecisionController @Inject()(as_inject: ActorSystem, dbt: dbInstanceManager, att: AuthTokenTrait) {
	implicit val as: ActorSystem = as_inject
	
	def alOutSumPromoBudgetExcelValueWithHtml = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
		MessageRoutes(msg_log(toJson(Map("method" -> toJson("alOutSumPromoBudgetExcelValueWithHtml"))), jv)
			:: alReadExcel(jv)
			:: MsgOutSumPromoBudgetExcelWithHtml(jv)
			:: MsgOutHospitalExcelWithHtml(jv)
			:: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
	})
	
	def alOutManagementExcelValueWithHtml = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
		MessageRoutes(msg_log(toJson(Map("method" -> toJson("alOutManagementExcelValueWithHtml"))), jv)
			:: alReadExcel(jv)
			:: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
	})
}
