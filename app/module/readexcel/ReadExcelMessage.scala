package module.readexcel

import com.pharbers.bmmessages.CommonMessage
import play.api.libs.json.JsValue

abstract class MsgReadExcelCommnd extends CommonMessage("readexcel", ReadExcelModule)
object alReadExcelMessage {
	case class alReadExcel(data: JsValue) extends MsgReadExcelCommnd
}
