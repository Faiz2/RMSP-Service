package controllers.webStore

import javax.inject.Inject

import akka.actor.ActorSystem
import com.pharbers.bmmessages.{CommonModules, MessageRoutes}
import com.pharbers.bmpattern.LogMessage.{common_log, msg_log}
import com.pharbers.bmpattern.ResultMessage.{common_result, msg_CommonResultMessage}
import com.pharbers.dbManagerTrait.dbInstanceManager
import com.pharbers.token.AuthTokenTrait
import controllers.common.requestArgsQuery
import module.auth.AuthMessage.{MsgAuthTokenExpire, MsgAuthTokenParser}
import module.webStore.WebStoreMessage.{MsgInputFetch, MsgInputStore}
import play.api.libs.json.Json._
import play.api.mvc.Action

/**
  * Created by yym on 12/19/17.
  */
class WebStoreController  @Inject()(as_inject: ActorSystem, dbt: dbInstanceManager, att: AuthTokenTrait){
    
    implicit val as: ActorSystem = as_inject
   
    def input_store = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("input_store"))), jv)
            ::MsgAuthTokenParser(jv)
            ::MsgAuthTokenExpire(jv)
            ::MsgInputStore(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })
    
    def input_fetch = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("input_store"))), jv)
            ::MsgAuthTokenParser(jv)
            ::MsgAuthTokenExpire(jv)
            ::MsgInputFetch(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })
}
