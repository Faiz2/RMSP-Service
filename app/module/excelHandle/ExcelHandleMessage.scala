package module.excelHandle

import com.pharbers.bmmessages.CommonMessage
import play.api.libs.json.JsValue

/**
  * Created by yym on 12/25/17.
  */
abstract class MsgExcelHandleCommand extends CommonMessage("excelHandleModule", ExcelHandleModule)
object ExcelHandleMessage {
    case class MsgReadDecisionExcel(data : JsValue) extends MsgExcelHandleCommand
}
