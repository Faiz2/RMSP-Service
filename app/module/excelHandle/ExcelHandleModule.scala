package module.excelHandle

import com.pharbers.ErrorCode
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import module.excelHandle.ExcelHandleMessage.MsgReadDecisionExcel
import module.excelHandle.excelData.DecisionExcel
import play.api.libs.json.JsValue

/**
  * Created by yym on 12/25/17.
  */
object ExcelHandleModule extends ModuleTrait with DecisionExcel{
    def dispatchMsg(msg: MessageDefines)
                   (pr: Option[String Map JsValue])
                   (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = msg match {
        case MsgReadDecisionExcel(data) => read_decision_excel(data)(pr)
        case _ => throw new Exception("function is not impl")
    }
    def read_decision_excel(data : JsValue)
                           (pr: Option[String Map JsValue])
                           (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
        try{
            val file = (data \ "file").getOrElse(throw new Exception("have no file path")).as[String]
            var result :Map[String , JsValue] = Map()
            (1 to 10).foreach{num =>
                result = result ++ decision_excel_read(file , "Decision1", "1" , num)
            }
            (Some(result),None)
        }catch {
            case ex : Exception =>
                println(ex)
                (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
        
    }
}
