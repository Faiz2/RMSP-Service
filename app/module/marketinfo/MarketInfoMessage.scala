package module.marketinfo

import com.pharbers.bmmessages.CommonMessage
import play.api.libs.json.JsValue


abstract class MsgMarketInfoCommand extends CommonMessage("marketinfo", MarketInfoModule)
object alMarketInfoMessage {
	case class alOutMarketInfoExcelValueWithHtml(data: JsValue) extends MsgMarketInfoCommand
}
