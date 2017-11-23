package module.callsystem

import com.pharbers.bmmessages.CommonMessage
import play.api.libs.json.JsValue

abstract class MsgCallRLanguageCommand extends CommonMessage("callr", CallRLanguageModule)
object CallRLanguageMessage {
	case class MsgCallRLanguage(data: JsValue) extends MsgCallRLanguageCommand
}
