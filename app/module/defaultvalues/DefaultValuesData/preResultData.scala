package module.defaultvalues.DefaultValuesData

import com.mongodb.casbah.Imports._
import org.bson.types.ObjectId
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

trait preResultData {
    implicit val d2m_stage_1 : DBObject => Map[String, JsValue] = { obj =>

        val stages = obj.getAs[MongoDBList]("pre_result").get.toList.asInstanceOf[List[BasicDBObject]]
        val stage = stages.find(p => p.get("stage") == 1).get

        Map(
            "_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "hosp_code" -> toJson(obj.getAs[String]("hosp_code").get.toString),
            "phrase" -> toJson(stage.getAs[String]("phrase_1").get.toString),
            "total" -> toJson(stage.getAs[String]("total").get.toString),
            "1" -> toJson(stage.getAs[String]("1").get.toString),
            "2" -> toJson(stage.getAs[String]("2").get.toString),
            "3" -> toJson(stage.getAs[String]("3").get.toString),
            "4" -> toJson(stage.getAs[String]("4").get.toString),
            "date" -> toJson(obj.getAs[Number]("date").get.longValue)
        )
    }

    implicit val d2m_stage_2 : DBObject => Map[String, JsValue] = { obj =>

        val stages = obj.getAs[MongoDBList]("pre_result").get.toList.asInstanceOf[List[BasicDBObject]]
        val stage = stages.find(p => p.get("stage") == 2).get

        Map(
            "_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "hosp_code" -> toJson(obj.getAs[String]("hosp_code").get.toString),
            "phrase" -> toJson(stage.getAs[String]("phrase_2").get.toString),
            "date" -> toJson(obj.getAs[Number]("date").get.longValue)
        )
    }
}
