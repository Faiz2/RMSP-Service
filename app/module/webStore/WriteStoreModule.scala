package module.webStore

import java.io.PrintWriter

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
            val user = (data \ "user" ).asOpt[String].getOrElse(new Exception("no user"))
            val phase =(data \ "phase").get.head.asOpt[String].getOrElse(new Exception("no phase"))
            val key = s"$user:$phase"
            println(key+" store_input")
            val maps = data.asInstanceOf[JsObject].value.toMap - "phase" - "user"
            println(maps.size)
            if(maps.size>30&&maps.size<40){
                rdt.addString(key , toJson(maps).toString())
            }else if(maps.size>180&&maps.size<195) {
                rdt.addString(key , toJson(maps).toString())
            }else {
//                val out = new PrintWriter("/Users/apple/Desktop/out.txt")
//                out.println(data)
//                out.close()
                println(maps.size)
            }
            (Some(Map("store" -> toJson(key))), None)
        } catch {
            case ex: Exception =>
                println("store _input"+ex)
//                println(data)
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
            val phase = (data \ "phase").get.head.asOpt[String].getOrElse(new Exception("no phase")).toString
            val key = s"$user:$phase"
            println(key)
            val maps= Json.parse(rdt.getString(key))
            val tmp =maps.asInstanceOf[JsObject].value.toMap
            if(tmp.size<30 ){
                println("fetch_input"+maps.asInstanceOf[JsObject].value.toMap)
            }else{
                println("fetch_input"+tmp.size)
                println(tmp)
            }
            (Some(Map("input"->(toJson(maps)))), None)
        }catch {
            case ex: Exception =>
                println("fetch_input"+ex)
                (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
        
    }
    
    
}
