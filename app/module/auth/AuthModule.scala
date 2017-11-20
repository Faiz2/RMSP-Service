package module.auth

import java.io.{InputStreamReader, LineNumberReader}
import java.util.Date

import com.pharbers.ErrorCode
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.dbManagerTrait.dbInstanceManager
import com.pharbers.token.AuthTokenTrait
import module.auth.AuthData.AuthData
import module.auth.AuthMessage._
import play.api.libs.json.JsValue
import play.api.libs.json.Json._

object AuthModule extends ModuleTrait with AuthData {
	def dispatchMsg(msg: MessageDefines)
	               (pr: Option[String Map JsValue])
	               (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = msg match {
		case MsgUserWithPassword(data) => authWithPassword(data)
		case _ => throw new Exception("function is not impl")
	}
	
	def authWithPassword(data: JsValue)(implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
			val db = conn.queryDBInstance("stp").get
			val att = cm.modules.get.get("att").map(x => x.asInstanceOf[AuthTokenTrait]).getOrElse(throw new Exception("no encrypt impl"))
			val date = new Date().getTime
			val o = m2d(data)
			db.queryObject(o, "register") match {
				case None => throw new Exception("data not exist")
				case Some(d) =>
					val reVal = d + ("expire_in" -> toJson(date + 60 * 60 * 1000 * 24))
					val auth_token = att.encrypt2Token(toJson(reVal))
					(Some(Map("user_token" -> toJson(auth_token))), None)
			}
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
}

object Test extends App {
	try {
		val cmd = "Rscript /Users/qianpeng/Desktop/interface_test.R /Users/qianpeng/Desktop/test.json"
		println(s"cmd=$cmd")
		
		val process = new ProcessBuilder("/bin/bash", "-c", cmd).start()
		val input = new LineNumberReader(new InputStreamReader(process.getInputStream()))
		var line,result: String = ""
		process.waitFor()
		do {
			line = input.readLine()
			if(line != null) result = line
		} while (line != null)
		println(result)
	} catch {
		case e : Exception =>
			println(e.getMessage)
	}
}