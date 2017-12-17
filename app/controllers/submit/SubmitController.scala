package controllers.submit

import javax.inject.Inject

import akka.actor.ActorSystem
import com.pharbers.bmmessages.{CommonModules, MessageRoutes}
import com.pharbers.bmpattern.LogMessage.{common_log, msg_log}
import com.pharbers.bmpattern.ResultMessage.{common_result, msg_CommonResultMessage}
import com.pharbers.dbManagerTrait.dbInstanceManager
import com.pharbers.token.AuthTokenTrait
import controllers.common.requestArgsQuery
import module.callsystem.CallRLanguageMessage.MsgCallRLanguage
import module.writejson.WriteJsonMessage.WriterJsonData
import play.api.libs.json.Json.toJson
import play.api.mvc.Action

class SubmitController @Inject()(as_inject: ActorSystem, dbt: dbInstanceManager, att: AuthTokenTrait) {
	implicit val as: ActorSystem = as_inject

	def submitDataCallR = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
		MessageRoutes(msg_log(toJson(Map("method" -> toJson("submitDataCallR"))), jv)
		  :: WriterJsonData(jv)
		  :: MsgCallRLanguage(jv)
		  :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
	})
}
