package module.writejson.WriteJsonData

import java.io.{File, PrintWriter}
import java.util.UUID

import play.api.libs.json.JsValue

trait WriteJsonData {

	object HandleImpl {
		val j2s: JsValue => String = {_.toString}
	}

	def wirteJson(jv: JsValue)(implicit func: JsValue => String): String = {
		// TODO： 配置文件
		val uuid = UUID.randomUUID().toString
		val path = s"resource/json/$uuid.json"
		try {
			val w = new PrintWriter(new File(path))
			w.write(func(jv))
			w.close()
			uuid
		} catch {
			case _: Exception => throw new Exception("write file error")
		}
	}
}
