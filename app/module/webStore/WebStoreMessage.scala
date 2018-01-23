package module.webStore

import com.pharbers.bmmessages.CommonMessage
import play.api.libs.json.JsValue

/**
  * Created by yym on 12/17/17.
  */
abstract class WebStoreCommand  extends CommonMessage("webStore", WebStoreModule)

object WebStoreMessage{
    case class MsgInputStore(data : JsValue) extends WebStoreCommand
    case class MsgInputFetch(data : JsValue) extends WebStoreCommand
}
