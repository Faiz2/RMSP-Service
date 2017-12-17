package module.callsystem.CallRData

import com.pharbers.common.cmd.rcmd.CallRFile
import play.api.libs.json.JsValue

trait CallRLanguageData {

	def callR(jv: Option[String Map JsValue]): Boolean = {
		try {
//			val rfile = "resource/stp_handler.R"
			val rdata = "resource/pre_data_linux.RData"
			val fileKey = (jv.get("data") \ "fileKey").asOpt[String].getOrElse(throw new Exception("data not exist"))
			val reportFileName = (jv.get("data") \ "reportfilename").asOpt[String].getOrElse(throw new Exception("data not exist"))
			println(s"Rscript $rdata $fileKey $reportFileName")
			CallRFile(rdata, s"$fileKey", s"resource/report/$reportFileName").excute
			true
		} catch {
			case ex: Exception =>
				println(ex.getMessage)
				false
		}
	}
}
