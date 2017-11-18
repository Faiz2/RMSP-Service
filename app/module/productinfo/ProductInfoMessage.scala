package module.productinfo


import play.api.libs.json.JsValue

import com.pharbers.bmmessages.CommonMessage


abstract class MsgProductInfoCommand extends CommonMessage("productinfo", ProductInfoModule)

object ProductInfoMessage {
	case class alOutProductInfoExcelValueWithHtml(data: JsValue) extends MsgProductInfoCommand
}
