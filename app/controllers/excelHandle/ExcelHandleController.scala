package controllers.excelHandle

import javax.inject.Inject

import akka.actor.ActorSystem
import com.pharbers.bmmessages.{CommonModules, MessageRoutes}
import com.pharbers.dbManagerTrait.dbInstanceManager
import com.pharbers.token.AuthTokenTrait
import controllers.common.requestArgsQuery
import module.excelHandle.ExcelHandleMessage.MsgReadDecisionExcel
import play.api.mvc.Action
import play.api.libs.json.Json.toJson
import com.pharbers.bmpattern.LogMessage.{common_log, msg_log}
import com.pharbers.bmpattern.ResultMessage.{common_result, msg_CommonResultMessage}
/**
  * Created by yym on 12/25/17.
  */
class ExcelHandleController @Inject()(as_inject: ActorSystem, dbt: dbInstanceManager, att: AuthTokenTrait) {
    implicit val as: ActorSystem = as_inject
    
    def read_decision_excel= Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("read_decision_excel"))), jv)
            ::MsgReadDecisionExcel(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })
}
