package module.summary.SummaryData

import play.api.libs.json.JsValue
import com.mongodb.casbah.Imports._
import play.api.libs.json.Json.toJson

trait SummaryData {
	implicit val d2m: JsValue => DBObject = { jv =>
		val builder = MongoDBObject.newBuilder
		(jv \ "condition" \ "uuid").asOpt[String].map(x => builder += "uuid" -> x)
		builder.result
	}
	
	val m2d: DBObject => JsValue = { db =>
		
		null
	}
}
