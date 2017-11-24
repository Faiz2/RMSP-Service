package module.writejson.WriteJsonData

import java.io.{File, PrintWriter}
import java.util.UUID

import play.api.libs.json.JsValue

trait WriteJsonData {

	object HandleImpl {
		val j2s: JsValue => String = {_.toString}
	}

	def wirteJson(jv: JsValue)(implicit func: JsValue => String): (Boolean, String) = {
		// TODO： 配置文件
		val path = s"/Users/qianpeng/Desktop/json/${UUID.randomUUID().toString}.json"
		try {
			val w = new PrintWriter(new File(path))
			w.write(func(jv))
			w.close()
			(true, path)
		} catch {
			case _: Exception => throw new Exception("write file error")
		}
	}
}
