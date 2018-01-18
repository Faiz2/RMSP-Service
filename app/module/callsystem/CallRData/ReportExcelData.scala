package module.callsystem.CallRData

import com.mongodb.casbah.Imports.{DBObject, _}
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by yym on 1/17/18.
  */
trait ReportExcelData {
    implicit val m2d : JsValue => DBObject = {jv =>
        val builder = MongoDBObject.newBuilder
        builder += "user" -> (jv \ "user").asOpt[String].getOrElse(throw new Exception("wrong input user"))
        builder += "reportFile"-> (jv \ "reportFile").asOpt[String].getOrElse(throw new Exception("wrong input value")).toString()
        builder += "phase"-> (jv \ "phase").asOpt[String].getOrElse(throw new Exception("wrong input value")).toString()
        builder.result
    }
    
    implicit val d2m : DBObject => String Map JsValue = { obj =>
        val user = obj.getAs[String]("user").getOrElse("")
        val reportFile = obj.getAs[String]("reportFile").getOrElse("")
        val phase = obj.getAs[String]("phase").getOrElse("")
        Map("user"-> toJson(user) , "reportFile" -> toJson(reportFile) , "phase" -> toJson(phase))
        
    }
}
