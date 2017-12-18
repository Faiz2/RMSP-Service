package module.webStore

import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.common.MergeStepResult
import com.pharbers.driver.redis.phRedisDriver

import play.api.libs.json.JsValue

/**
  * Created by yym on 12/17/17.
  */
object WebStoreModule extends ModuleTrait{
    def dispatchMsg(msg: MessageDefines)
                   (pr: Option[String Map JsValue])
                   (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = msg match {
        case _  => ???
    }
    def store_input(data : JsValue)
                   (pr: Option[String Map JsValue])
                   (implicit cm : CommonModules) : (Option[String Map JsValue] , Option[JsValue])={
        val user = (MergeStepResult(data, pr) \ "user_token" \ "user").asOpt[JsValue].getOrElse(new Exception("no user"))
        val redisDriver = phRedisDriver().commonDriver
        (None,None)
    }
    
    
}
