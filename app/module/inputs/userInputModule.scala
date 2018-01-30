package module.inputs

import com.mongodb.casbah.Imports._
import com.pharbers.ErrorCode
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.dbManagerTrait.dbInstanceManager
import module.inputs.userInputData.{userInputCondition, userInputCreate, userInputData}
import module.inputs.userInputMessages._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

object userInputModule extends ModuleTrait {
    def dispatchMsg(msg: MessageDefines)
                   (pr: Option[String Map JsValue])
                   (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = msg match {

        case userHasLastOp(data) => userHasLastOp(data)
        case userPhaseCountInOp(data) => userPhaseCountInOp(data)
        case queryUserInputInOpPhase(data) => queryUserInputInOpPhase(data)
        case updateUserInputInOpPhase(data) => updateUserInputInOpPhase(data)
        case forceCreateDefaultInputInOpPhase(data) => forceCreateDefaultInputInOpPhase(data)

        case _ => throw new Exception("function is not impl")
    }

    object inner_trait extends userInputData
                          with userInputCondition
                          with userInputCreate

    def userHasLastOp(data : JsValue)
                     (implicit cm: CommonModules) : (Option[String Map JsValue], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("stp").get

            import inner_trait.dc
            import inner_trait.d2m

            val reVal = db.queryMultipleObject(data, "inputs", sort = "date", take = 1)
            val tmp = reVal.map { x =>
                x.get("uuid").get.asOpt[String].get
            }

            if (tmp.isEmpty) (Some(Map("hasLastOp" -> toJson(0), "uuid" -> toJson(""))), None)
            else (Some(Map("hasLastOp" -> toJson(1), "uuid" -> toJson(tmp.head))), None)

        } catch {
            case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def userPhaseCountInOp(data : JsValue)
                          (implicit cm: CommonModules) : (Option[String Map JsValue], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("stp").get

            import inner_trait.dc
            import inner_trait.d2m

            val reVal = db.queryObject(data, "inputs").get

            val tmp =
            reVal.get("decision").get.asOpt[List[JsValue]].get.map { x =>
                (x \ "phase").asOpt[Int].get
            }.distinct

            (Some(Map("phase_count" -> toJson(tmp.length))), None)

        } catch {
            case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def queryUserInputInOpPhase(data : JsValue)
                               (implicit cm: CommonModules) : (Option[String Map JsValue], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("stp").get

            import inner_trait.dc
            import inner_trait.d2m

            val reVal = db.queryObject(data, "inputs").get

            val tmp =
                reVal.get("decision").get.asOpt[List[JsValue]].get.filter { x =>
                    (x \ "phase").asOpt[Int].get == (data \ "phase").asOpt[Int].get
                }

            val fr = reVal + ("decision" -> toJson(tmp))

            (Some(Map("input" -> toJson(fr))), None)

        } catch {
            case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def updateUserInputInOpPhase(data : JsValue)
                                (implicit cm: CommonModules) : (Option[String Map JsValue], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("stp").get

            import inner_trait.dc

            val phase = (data \ "phase").asOpt[Int].get

            db.queryObject(data, "inputs") { obj =>
                val o : DBObject = inner_trait.m2d(data)
                val decision_new = o.getAs[MongoDBList]("decision").get.toList.asInstanceOf[List[BasicDBObject]]

                val decision = obj.getAs[MongoDBList]("decision").get.toList.asInstanceOf[List[BasicDBObject]]

                obj += "decision" -> (decision.filterNot(p => p.getAs[Number]("phase").get.intValue == phase) ++ decision_new)

                db.updateObject(obj, "inputs", "uuid")

                Map("a" -> toJson(""))
            }

            (Some(Map("input_update" -> toJson("success"))), None)

        } catch {
            case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def forceCreateDefaultInputInOpPhase(data : JsValue)
                                        (implicit cm: CommonModules) : (Option[String Map JsValue], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("stp").get

            import inner_trait.c2d

            db.insertObject(data, "inputs", "uuid")

            (Some(Map("input_create" -> toJson("success"))), None)

        } catch {
            case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }
}