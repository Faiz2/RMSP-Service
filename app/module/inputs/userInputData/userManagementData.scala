package module.inputs.userInputData

import java.util.Date

import com.mongodb.casbah.Imports._
import org.bson.types.ObjectId
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

trait userManagementData {
    implicit val mag_m2d : JsValue => DBObject = { mm =>
        val builder = MongoDBObject.newBuilder

        builder += "user_id" -> (mm \ "user_id").asOpt[String].get
        builder += "uuid" -> (mm \ "uuid").asOpt[String].get

        val dd = (mm \ "decision").asOpt[List[JsValue]].get
        val dst = MongoDBList.newBuilder



        builder += "date" -> new Date().getTime
        builder.result
    }

    implicit val mag_d2m : DBObject => Map[String, JsValue] = { obj =>

        val decisions = obj.getAs[MongoDBList]("decision").get.toList.asInstanceOf[List[BasicDBObject]]

        Map(
            "_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "user_id" -> toJson(obj.getAs[String]("user_id").get),
            "uuid" -> toJson(obj.getAs[String]("uuid").get),
//            "decision" -> toJson(map_decision),
            "date" -> toJson(obj.getAs[Number]("date").get.longValue)
        )
    }
}
