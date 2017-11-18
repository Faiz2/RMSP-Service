package controllers.brdinfo

import javax.inject.Inject

import akka.actor.ActorSystem
import com.pharbers.bmmessages.{CommonModules, MessageRoutes}
import com.pharbers.dbManagerTrait.dbInstanceManager
import com.pharbers.token.AuthTokenTrait
import controllers.common.requestArgsQuery
import com.pharbers.bmpattern.LogMessage.{common_log, msg_log}
import com.pharbers.bmpattern.ResultMessage.{common_result, msg_CommonResultMessage}
import module.brdinfo.BrdInfoMessage.alOutBrdInfoExcelValueWithHtml
import module.readexcel.alReadExcelMessage.alReadExcel
import play.api.libs.json.Json.toJson
import play.api.mvc.Action

class BrdInfoController @Inject()(as_inject: ActorSystem, dbt: dbInstanceManager, att: AuthTokenTrait) {
	implicit val as: ActorSystem = as_inject
	
	def alOutExcelValueWithHtml = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
		MessageRoutes(msg_log(toJson(Map("method" -> toJson("alOutExcelVcalueWithHtml"))), jv)
			:: alReadExcel(jv)
			:: alOutBrdInfoExcelValueWithHtml(jv)
			:: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
	})
}
