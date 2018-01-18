package module.webStore

import com.pharbers.ErrorCode
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.common.MergeStepResult
import com.pharbers.driver.PhRedisDriver
import com.pharbers.driver.util.PhRedisTrait
import module.webStore.WebStoreMessage.{MsgInputFetch, MsgInputStore}
import play.api.libs.json._
import play.api.libs.json.Json._

/**
  * Created by yym on 12/17/17.
  */
object WebStoreModule extends ModuleTrait {
    def dispatchMsg(msg: MessageDefines)
                   (pr: Option[String Map JsValue])
                   (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = msg match {
        case MsgInputStore(data) => store_input(data)(pr)
        case MsgInputFetch(data) => fetch_input(data)(pr)
        case _ => ???
    }
    
    def store_input(data: JsValue)
                   (pr: Option[String Map JsValue])
                   (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
        try {
            val rdt = cm.modules.get.get("rdt").map(x => x.asInstanceOf[PhRedisTrait]).getOrElse(throw new Exception("no encrypt impl"))
//            val user = (MergeStepResult(data, pr) \ "user_token" \ "account").asOpt[String].getOrElse(new Exception("no user"))
            val user = (MergeStepResult(data, pr) \ "user" ).asOpt[String].getOrElse(new Exception("no user"))
            val phase =(MergeStepResult(data, pr) \ "phase").asOpt[JsValue].getOrElse(new Exception("no phase")).toString
            val key = s"$user:$phase"
            val maps = data.asInstanceOf[JsObject].value.toMap - "phase" - "user_token"
//            println(maps.size)
            if(maps.size>10){
                rdt.addString(key , toJson(maps).toString())
            }
            (Some(Map("store" -> toJson(key))), None)
        } catch {
            case ex: Exception =>
                println(ex)
                (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }
    
    def fetch_input(data: JsValue)
                   (pr:  Option[String Map JsValue])
                   (implicit cm: CommonModules) : (Option[String Map JsValue], Option[JsValue]) = {
        try {
            val rdt = cm.modules.get.get("rdt").map(x => x.asInstanceOf[PhRedisTrait]).getOrElse(throw new Exception("no encrypt impl"))
//            val user = (MergeStepResult(data, pr) \ "user_token" \ "account").asOpt[String].getOrElse(new Exception("no user"))
            val user = (MergeStepResult(data, pr) \ "user" ).asOpt[String].getOrElse(new Exception("no user"))
            val phase = (data \ "phase").asOpt[JsValue].getOrElse(new Exception("no phase")).toString
            val key = s"$user:$phase"
            val maps= Json.parse(rdt.getString(key))
            (Some(Map("input"->(toJson(maps)))), None)
        }catch {
            case ex: Exception =>
                println(ex)
                (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
        
    }
    
    //TODO:redis hash 删除
//    def delete_input(data: JsValue)
//                   (pr:  Option[String Map JsValue])
//                   (implicit cm: CommonModules) : (Option[String Map JsValue], Option[JsValue]) = {
//        try {
//            val user = (MergeStepResult(data, pr) \ "user_token" \ "user").asOpt[String].getOrElse(new Exception("no user"))
//            val phase = (data \ "phase").asOpt[String].getOrElse(new Exception("no phase"))
//            val redis_driver = new PhRedisDriver()
//            val key = s"$user:$phase"
//            val maps = redis_driver
//            (Some(Map("input" -> toJson(maps))), None)
//        }catch {
//            case ex: Exception =>
//                println(ex)
//                (None, Some(ErrorCode.errorToJson(ex.getMessage)))
//        }
//
//    }
    
}
