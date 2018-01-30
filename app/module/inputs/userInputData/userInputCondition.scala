package module.inputs.userInputData

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue

trait userInputCondition {
    implicit val dc : JsValue => DBObject = { data =>
        val user_id = (data \ "condition" \ "user_id").asOpt[String].get
        val uuid = (data \ "condition" \ "uuid").asOpt[String].get

        DBObject(
            "user_id" -> user_id,
            "uuid" -> uuid
        )
    }
}
