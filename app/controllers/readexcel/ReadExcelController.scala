package controllers.readexcel

import javax.inject.Inject

import akka.actor.ActorSystem

import play.api.mvc._
import play.api.libs.json.Json._

import com.pharbers.bmmessages.{CommonModules, MessageRoutes}
import com.pharbers.dbManagerTrait.dbInstanceManager
import com.pharbers.token.AuthTokenTrait
import com.pharbers.bmpattern.LogMessage.{common_log, msg_log}
import com.pharbers.bmpattern.ResultMessage.{common_result, msg_CommonResultMessage}

import controllers.common.requestArgsQuery

import module.readexcel.alReadExcelMessage._

class ReadExcelController @Inject()(as_inject: ActorSystem, dbt: dbInstanceManager, att: AuthTokenTrait) {
	implicit val as: ActorSystem = as_inject
	
	def readExcelController = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
		MessageRoutes(msg_log(toJson(Map("method" -> toJson("readExcelController"))), jv)
			:: alReadExcel(jv)
			:: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
	})
	
}