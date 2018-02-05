package module.defaultvalues

import com.mongodb.casbah.Imports._
import com.pharbers.ErrorCode
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.dbManagerTrait.dbInstanceManager
import module.defaultvalues.DefaultValuesData._
import module.defaultvalues.DefaultValuesMessages._
import play.api.libs.json.{JsObject, JsValue}
import play.api.libs.json.Json.toJson

object mrHelper {
    def mapfunc(phrase : Int) =
        s"""
          | function() {
          |     emit ({ hc : this.hosp_code }, { pc : this.prod_code, p0 : this.phrase_$phrase })
          | }
        """.stripMargin

    def reducefunc(phrase : Int) =
        s"""
          | function(k, v) {
          |     var reducedVal = new Object();
          |     for (var idx = 0; idx < v.length; ++idx) {
          |         reducedVal[v[idx].pc] = v[idx].p0;
          |     }
          |     return reducedVal;
          | }
        """.stripMargin

    def outColl(phrase : Int) = s"phrase$phrase"
}

object DefaultValuesModule extends ModuleTrait {
    def dispatchMsg(msg: MessageDefines)
                   (pr: Option[String Map JsValue])
                   (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = msg match {

        case salesMenInProposal(data) => salesMenInProposal(data)
        case productInProposal(data) => productInProposal(data)
        case hospitalPotentialInProposal(data) => hospitalPotentialInProposal(data)
        case perResultInProposal(data) => perResultInProposal(data)

        case budgetInProposal(data) => budgetInProposal(data)

        case _ => throw new Exception("function is not impl")
    }

    object inner_trait extends salesmenData with productsData with hospitalData with preResultData with budgetData

    def salesMenInProposal(data: JsValue)
                          (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("stp").get

            import inner_trait.salesmen_d2m

            val reVal = db.queryMultipleObject(DBObject(), "salesmen")

            (Some(Map(
                "salesmen" -> toJson(reVal)
            )), None)

        } catch {
            case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def productInProposal(data: JsValue)
                         (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {

            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("stp").get

            import inner_trait.product_m2d

            val reVal = db.queryMultipleObject(DBObject(), "products")

            (Some(Map(
                "products" -> toJson(reVal)
            )), None)
        } catch {
            case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def hospitalPotentialInProposal(data: JsValue)
                         (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {

            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("stp").get
            val ci = conn.queryDBConnection("stp").get

            val phrases = (data \ "phrases").asOpt[List[Int]].get
            val tmp =
            phrases.map { phrase =>
                val m = mrHelper.mapfunc(phrase)
                val r = mrHelper.reducefunc(phrase)
                val o = mrHelper.outColl(phrase)
                if (!ci.isExisted(o)) {
                    db.mapReduceJob("constrats", m, r, None, o)
                }

                import inner_trait.hospital_potential_d2m
                val tmp = db.queryMultipleObject(DBObject(), o)

                (phrase.toString, toJson(tmp))
            }.toMap

            val hospitals = {
                import inner_trait.hospital_d2m
                db.queryMultipleObject(DBObject(), "hospitals")
            }

            val products = {
                import inner_trait.product_m2d
                db.queryMultipleObject(DBObject(), "products")
            }

            val reVal = tmp map { m =>
                m._1 -> m._2.asOpt[List[JsValue]].get.map { iter =>
                    val hosp_code = (iter \ "hosp_code").asOpt[String].get
                    toJson(
                        iter.as[JsObject].value.toMap.map { m =>
                            products.find (p => p.get("prod_code").get.asOpt[String].get == m._1).
                                map { x =>
                                    x.get("prod_name").get.asOpt[String].get -> m._2
                                }.getOrElse(m)
                        } ++
                        hospitals.find (p => p.get("hosp_code").get.asOpt[String].get == hosp_code).
                            map { x =>
                                Map("hosp_name" -> x.get("hosp_name").get,
                                    "hosp_area" -> x.get("hosp_area").get,
                                    "hosp_cat" -> x.get("hosp_cat").get)
                            }.getOrElse(throw new Exception())
                    )
                }
            }

            (Some(Map(
                "hospital_potential" -> toJson(reVal)
            )), None)
        } catch {
            case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def perResultInProposal(data: JsValue)
                         (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {

            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("stp").get

            val phrases = (data \ "phrases").asOpt[List[Int]].get
            val tmp =
                phrases.map { phrase =>

                    val tmp =
                    if (phrase == 1) {
                        import inner_trait.d2m_stage_1
                        db.queryMultipleObject(DBObject(), "preresult")
                    } else {
                        import inner_trait.d2m_stage_1
                        db.queryMultipleObject(DBObject(), "preresult")
                    }

                    (phrase.toString, toJson(tmp))
                }.toMap

            val products = {
                import inner_trait.product_m2d
                db.queryMultipleObject(DBObject(), "products")
            }

            val hospitals = {
                import inner_trait.hospital_d2m
                db.queryMultipleObject(DBObject(), "hospitals")
            }

            val reVal = tmp map { m =>
                m._1 -> m._2.asOpt[List[JsValue]].get.map { iter =>
                    val hosp_code = (iter \ "hosp_code").asOpt[String].get
                    toJson(
                        iter.as[JsObject].value.toMap.map { m =>
                            products.find (p => p.get("prod_code").get.asOpt[String].get == m._1).
                                map { x =>
                                    x.get("prod_name").get.asOpt[String].get -> m._2
                                }.getOrElse(m)
                        } ++
                            hospitals.find (p => p.get("hosp_code").get.asOpt[String].get == hosp_code).
                                map { x =>
                                    Map("hosp_name" -> x.get("hosp_name").get,
                                        "hosp_area" -> x.get("hosp_area").get,
                                        "hosp_cat" -> x.get("hosp_cat").get)
                                }.getOrElse(throw new Exception())
                    )
                }
            }

            (Some(Map(
                "preresult" -> toJson(reVal)
            )), None)
        } catch {
            case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def budgetInProposal(data : JsValue)
                        (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {

            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("stp").get

            import inner_trait.budget_d2m

            val phrases = (data \ "phrases").asOpt[List[Int]].get
            val reVal =
                phrases.map { phrase =>
                    val tmp = db.queryObject(DBObject("phrase" -> phrase.toString), "budget")

                    (phrase.toString, toJson(tmp))
                }.toMap

            (Some(Map(
                "budget" -> toJson(reVal)
            )), None)
        } catch {
            case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }
}
