package module.inputs.userInputData

import java.util.Date

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue

trait userInputCreate {
    implicit val c2d : JsValue => DBObject = { mm =>
//        val plst = "口服抗生素" :: "一代降糖药" :: "三代降糖药" :: "皮肤药" :: Nil

        val builder = MongoDBObject.newBuilder

        builder += "user_id" -> (mm \ "user_id").asOpt[String].get
        builder += "uuid" -> (mm \ "uuid").asOpt[String].get

//        val dd = (mm \ "decision").asOpt[List[JsValue]].get
        val dst = MongoDBList.newBuilder

        (1 to 10).map { d =>
            val tmp = MongoDBObject.newBuilder
            tmp += "hosp_code" -> d //(d \ "hosp_id").asOpt[String].get
            tmp += "hosp_name" -> ""
            tmp += "phase" -> 1
            tmp += "budget" -> 0.0
            tmp += "salesmen" -> ""

//            val ss = (d \ "sales").asOpt[List[JsValue]].get
            val sales_lst = MongoDBList.newBuilder
            (1 to 4) map { s =>
                val tmp_ss = MongoDBObject.newBuilder
                tmp_ss += "prod_name" -> s.toString // (s \ "prod_name").asOpt[String].get
                tmp_ss += "prod_value" -> 0.0 //(s \ "prod_value").asOpt[Double].get

                sales_lst += tmp_ss.result
            }
            tmp += "sales" -> sales_lst.result

//            val vv = (d \ "visit_hours").asOpt[List[JsValue]].get
            val vh_lst = MongoDBList.newBuilder
            (1 to 4) map { v =>
                val tmp_vv = MongoDBObject.newBuilder
                tmp_vv += "prod_name" -> v.toString // (v \ "prod_name").asOpt[String].get
                tmp_vv += "prod_hours" -> 0.0 //(v \ "prod_hours").asOpt[Double].get

                vh_lst += tmp_vv.result
            }
            tmp += "visit_hours" -> vh_lst.result

            dst += tmp.result
        }
        builder += "decision" -> dst.result

        builder += "date" -> new Date().getTime
        builder.result
    }
}
