package module.inputs

import com.pharbers.bmmessages.CommonMessage
import module.defaultvalues.DefaultValuesModule
import play.api.libs.json.JsValue

abstract class MsgUserInputCommand extends CommonMessage("user input values", userInputModule)

object userInputMessages {
    case class userHasLastOp(data : JsValue) extends MsgUserInputCommand
    case class userPhaseCountInOp(data : JsValue) extends MsgUserInputCommand
    case class queryUserInputInOpPhase(data : JsValue) extends MsgUserInputCommand
    case class updateUserInputInOpPhase(data : JsValue) extends MsgUserInputCommand
    case class forceCreateDefaultInputInOpPhase(date : JsValue) extends MsgUserInputCommand
}