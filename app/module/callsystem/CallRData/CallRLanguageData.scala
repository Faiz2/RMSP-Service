package module.callsystem.CallRData

import java.io.File

import com.pharbers.common.RConfig
import com.pharbers.common.cmd.rcmd.CallRFile
import play.api.libs.json.JsValue

trait CallRLanguageData {

	def callR(jv: Option[String Map JsValue]): Boolean = {
		try {
			val r_config = new RConfig()
			val rfile = r_config.rfile()
			val rdata = r_config.rdata()
			val fileKey = (jv.get("data") \ "fileKey").asOpt[String].getOrElse(throw new Exception("data not exist"))
			val report_path = r_config.report_path
			val reportFileName = (jv.get("data") \ "reportfilename").asOpt[String].getOrElse(throw new Exception("data not exist"))
			println(s"Rscript $rfile $rdata $fileKey resource/report/$reportFileName")
			CallRFile(rfile, rdata, s"$fileKey", report_path+reportFileName).excute
			true
		} catch {
			case ex: Exception =>
				println(ex.getMessage)
				false
		}
	}
}
