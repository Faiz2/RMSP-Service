package module.user

import com.pharbers.ErrorCode
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.dbManagerTrait.dbInstanceManager
import module.user.RegisterData._
import module.user.RegisterMessage._
import play.api.libs.json.JsValue
import play.api.libs.json.Json._

object RegisterModule extends ModuleTrait with RegisterData {
	
	def dispatchMsg(msg: MessageDefines)
	               (pr: Option[String Map JsValue])
	               (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = msg match {
		case MsgCheckRepeatRegisterUser(data) => checkRegisterUser(data)
		case MsgRegisterUser(data) => registerUser(data)
		case _ => throw new Exception("function is not impl")
	}
	
	def checkRegisterUser(data: JsValue)(implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
			val db = conn.queryDBInstance("stp").get
			val o = validationUser(data)
			db.queryObject(o, "register") match {
				case None => (Some(Map("data" -> toJson(Map("flag" -> toJson(true) )))), None)
				case Some(_) => throw new Exception("user is repeat")
			}
			(Some(Map("data" -> toJson(Map("flag" -> toJson(true) )))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	def registerUser(data: JsValue)(implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
			val db = conn.queryDBInstance("stp").get
			val o = m2d(data)
			db.insertObject(o, "register", "user")
			(Some(Map("data" -> toJson(Map("flag" -> toJson(true) )))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
}
