package module.brdinfo

import com.pharbers.bmmessages.CommonMessage
import play.api.libs.json.JsValue


abstract class MsgBrdInfoCommand extends CommonMessage("brdinfo", BrdInfoModule)
object BrdInfoMessage {
	case class alOutBrdInfoExcelValueWithHtml(data: JsValue) extends MsgBrdInfoCommand
}
