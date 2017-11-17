package module.readexcel.ReadExcelData

import com.pharbers.panel.util.excel.{phExcelData, phHandleExcel}
import play.api.libs.json.JsValue
import play.api.libs.json.Json._


trait ReadExcalFileData {
	
	def readSourceWithExcel: JsValue = {
		val file_local = "resource/pre_info_new.xlsx"
		implicit val postArg: String Map String => Option[String Map String] = com.pharbers.panel.util.excel.phHandleExcel.postFun
		implicit val filterArg: String Map String => Boolean = com.pharbers.panel.util.excel.phHandleExcel.filterFun
		val parse = phHandleExcel()
		val result = (parse.getSheetNames(file_local).zipWithIndex map ( x => Map(x._1 -> parse.readExcel(phExcelData(file_local,x._2 + 1)) )) ).toList
		toJson(result)
	}
	
}
