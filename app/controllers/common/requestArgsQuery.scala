package controllers.common

import javax.inject.Inject

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.pharbers.bmmessages.{MessageRoutes, excute}
import com.pharbers.bmpattern.RoutesActor
import play.api.libs.Files.TemporaryFile
import play.api.libs.json.JsValue
import play.api.mvc._

import scala.concurrent.Await
import scala.concurrent.duration._

object requestArgsQuery {
	def apply()(implicit akkasys : ActorSystem) = new requestArgsQuery()
}

class requestArgsQuery @Inject() (implicit akkasys : ActorSystem) extends Controller {
	implicit val t = Timeout(1 * 60 * 10 second)

	def requestArgs(request : Request[AnyContent])(func : JsValue => JsValue) : Result = {
		try {
			request.body.asJson.map { x =>
				Ok(func(x))
			}.getOrElse (BadRequest("Bad Request for input"))
		} catch {
			case _ : Exception => BadRequest("Bad Request for input")
		}
	}

	def requestArgsV2(request : Request[AnyContent])(func : JsValue => MessageRoutes) : Result = {
		try {
			request.body.asJson.map { x =>
				Ok(commonExcution(func(x)))
			}.getOrElse (BadRequest("Bad Request for input"))
		} catch {
			case _ : Exception => BadRequest("Bad Request for input")
		}
	}

	def commonExcution(msr : MessageRoutes) : JsValue = {
		val act = akkasys.actorOf(Props[RoutesActor])
		val r = act ? excute(msr)
		Await.result(r.mapTo[JsValue], t.duration)
	}

	def uploadRequestArgs(request : Request[AnyContent])(func : MultipartFormData[TemporaryFile] => JsValue) : Result = {
		try {
			request.body.asMultipartFormData.map { x =>
				Ok(func(x))
			}.getOrElse (BadRequest("Bad Request for input"))
		} catch {
			case _ : Exception => BadRequest("Bad Request for input")
		}
	}
	
	//测试
	def uploadRequestArgs2(request : Request[TemporaryFile]) : Result = {
		try {
//			println(request.body.file)
//			request.body.moveTo(new File("/Users/qianpeng/FileBase/fea9f203d4f593a96f0d6faa91ba24ba/Client/CPA/aa.xlsx"), true)
//			request.body.asMultipartFormData.map { x =>
//				Ok(func(x))
//			}.getOrElse (BadRequest("Bad Request for input"))
			
			Ok("File uploaded")
		} catch {
			case ex : Exception => BadRequest("Bad Request for input")
		}
	}
}