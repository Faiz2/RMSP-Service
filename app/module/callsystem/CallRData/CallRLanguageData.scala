package module.callsystem.CallRData

import com.pharbers.common.cmd.rcmd.CallRFile
import play.api.libs.json.JsValue

trait CallRLanguageData {

	def callR(jv: Option[String Map JsValue]): Boolean = {
		try {
			val rfile = "resource/stp_handler.R"
			val rdata = "resource/pre_data_linux.RData"
			val rjson = (jv.get("data") \ "filename").asOpt[String].getOrElse(throw new Exception("data not exist"))
			CallRFile(rfile, rdata, s"resource/json/$rjson.json").excute
			true
		} catch {
			case ex: Exception =>
				println(ex.getMessage)
				false
		}
	}
}
