package module.summary.SummaryData

import play.api.libs.json.{JsObject, JsValue}
import com.mongodb.casbah.Imports._
import play.api.libs.json.Json.toJson
import play.libs.Json

trait SummaryData {
	val mapping = Map()
	implicit val d2m: JsValue => DBObject = { jv =>
		val builder = MongoDBObject.newBuilder
		(jv \ "condition" \ "uuid").asOpt[String].map(x => builder += "uuid" -> x)
		builder.result
	}
	
//	val m2d: DBObject => String Map JsValue = { db =>
//
//		null
//	}

	val s2d: (DBObject, JsValue) => String Map JsValue = (db, jv) => {
		val phase = (jv \ "condition" \ "phase").as[String]
		val result = db.getAs[DBObject]("result").map(_.getAs[DBObject](phase).get).get
		toJson(Json.parse(result.toString)).as[JsObject].value
		null
	}
}
