package controllers

import java.util.UUID
import javax.inject.Inject

import com.pharbers.cliTraits.DBTrait
import akka.actor.ActorSystem
import com.pharbers.bmmessages.{CommonModules, MessageRoutes}
import com.pharbers.dbManagerTrait.dbInstanceManager
import com.pharbers.token.AuthTokenTrait
import controllers.common.requestArgsQuery
import com.pharbers.bmpattern.LogMessage.{common_log, msg_log}
import com.pharbers.bmpattern.ResultMessage.{common_result, msg_CommonResultMessage}
import module.auth.AuthMessage.MsgUserWithPassword
import module.inputs.userInputMessages.{forceCreateDefaultInputInOpPhase, updateUserInputInOpPhase, userHasLastOp}
//import module.decision.DecisionMessage.{MsgOutHospitalExcelWithHtml, MsgOutSumPromoBudgetExcelWithHtml}
import module.defaultvalues.DefaultValuesMessages._
//import module.readexcel.alReadExcelMessage.alReadExcel
import play.api.libs.json.JsValue
import play.api.mvc.Action
import play.api.libs.json.Json.toJson
import play.api.mvc._

trait TokenCheck {

}

trait RoutesFilter extends TokenCheck { this: Controller =>
	// TODO: 错误的逻辑
//	def getRequestCookie(request: Request[AnyContent]): Option[String] = request.cookies.get("user_token").map(x => x.value)
//
//	def forward(page: String)(implicit att: AuthTokenTrait, request: Request[AnyContent]): Result = {
//		page match {
//			case "home" => Ok(views.html.Home.home())
//			case "market_info" => Ok(views.html.Module.MarketInfo.index())
//			case "brd_info" => Ok(views.html.Module.Brd.index())
//			case "product_info" => Ok(views.html.Module.Product.index())
//			case "report" => Ok(views.html.Module.Report.index())
//			case _ => ???
//		}
//	}
	
	// TODO : 前端只存登入用户名，后续有权限再去掉，新增权限过滤，与整个的filter
	def getUserCookie(request: Request[AnyContent])(page: Result): Result = {
		request.cookies.get("user").map(x => x.value) match {
			case None => Redirect("/")
			case _ => page
		}
	}
}

class RMSPRoutesController @Inject()(as_inject: ActorSystem, dbt: dbInstanceManager, att: AuthTokenTrait) extends Controller with RoutesFilter {

	implicit val as: ActorSystem = as_inject
	implicit val db_basic : DBTrait = dbt.queryDBInstance("stp").get
	implicit val attoken: AuthTokenTrait = att

	//TODO: 失败错误的跳转，得认真想想
//	def page(link: String) = Action { implicit request =>
//			forward(link)
//	}

	def login = Action {
		Ok(views.html.Login.login())
	}
	
	def register = Action {
		Ok(views.html.Login.register())
	}
	
	def index(uuid : String) = Action { request =>
        getUserCookie(request) {
            val user = request.cookies.get("user").get.value

            val jv = toJson(Map("condition" -> toJson(Map("user_id" -> user))))
            val reVal = {
                requestArgsQuery().commonExcution(
                    MessageRoutes(msg_log(toJson(Map("method" -> toJson("query user last op"))), jv)
                        :: userHasLastOp(jv)
                        :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
                )
            }

            if ((reVal \ "status").asOpt[String].get == "ok") {
                val hasOp = (reVal \ "result" \ "hasLastOp").asOpt[Int].get
                val uuid = (reVal \ "result" \ "uuid").asOpt[String].get

                if (hasOp == 1) Ok(views.html.uuid_index(uuid))
                else {
                    // TODO : create new uuid
                    val uid = UUID.randomUUID().toString
                    val jv = toJson(Map("user_id" -> user, "uuid" -> uid))
                    val reVal = {
                        requestArgsQuery().commonExcution(
                            MessageRoutes(msg_log(toJson(Map("method" -> toJson("force create op"))), jv)
                                :: forceCreateDefaultInputInOpPhase(jv)
                                :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
                        )
                    }

                    if ((reVal \ "status").asOpt[String].get == "ok") Redirect("/brd/" + uid)
                    else Redirect("/login")
                }
            } else Redirect("/login")
        }
	}

	def brd(uuid : String) = Action { request =>
        getUserCookie(request) {
            val jv = toJson("")
            val reVal =
            requestArgsQuery().commonExcution(
                MessageRoutes(msg_log(toJson(Map("method" -> toJson("sales man proposal"))), jv)
                    :: salesMenInProposal(jv)
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
            )

            if ((reVal \ "status").asOpt[String].get == "ok") {
                Ok(views.html.Module.Brd.brd_index(
                    (reVal \ "result" \ "salesmen").asOpt[List[JsValue]].get
                )(uuid))
            } else Redirect("/login")
        }
    }

    def product(uuid : String) = Action { request =>
        getUserCookie(request) {
            val jv = toJson("")
            val reVal =
            requestArgsQuery().commonExcution(
                MessageRoutes(msg_log(toJson(Map("method" -> toJson("products proposal"))), jv)
                    :: productInProposal(jv)
                    :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
            )

            if ((reVal \ "status").asOpt[String].get == "ok") {
                Ok(views.html.Module.Product.product_index(
                    (reVal \ "result" \ "products").asOpt[List[JsValue]].get
                )(uuid))
            } else Redirect("/login")
        }
    }

    def market(uuid : String, phrase : String) = Action { request =>
        val p = if (phrase == "") "1"
                else phrase

        getUserCookie(request) {
            val jv = toJson(Map("phrases" -> toJson(1 :: 2 :: Nil)))
            val reVal1 = {
                 requestArgsQuery().commonExcution(
                    MessageRoutes(msg_log(toJson(Map("method" -> toJson("alOutExcelVcalueWithHtml"))), jv)
                        :: hospitalPotentialInProposal(jv)
                        :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
                )
            }

            val reVal2 = {
                requestArgsQuery().commonExcution(
                    MessageRoutes(msg_log(toJson(Map("method" -> toJson("alOutExcelVcalueWithHtml"))), jv)
                        :: perResultInProposal(jv)
                        :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
                )
            }

            if ((reVal1 \ "status").asOpt[String].get == "ok" &&
                (reVal2 \ "status").asOpt[String].get == "ok") {
                Ok(views.html.Module.MarketInfo.market_index(
                    (reVal2 \ "result" \ "preresult").asOpt[JsValue].get,
                    (reVal1 \ "result" \ "hospital_potential").asOpt[JsValue].get
                )(uuid)(p))
            } else Redirect("/login")
        }
    }

    def decision(uuid : String, phrase : String) = Action { request =>
        val p = if (phrase == "") "1"
        else phrase

        getUserCookie(request) {

            val jv1 = toJson(Map("phrases" -> toJson(1 :: Nil)))
            val reVal1 = {
                requestArgsQuery().commonExcution(
                    MessageRoutes(msg_log(toJson(Map("method" -> toJson("alOutExcelVcalueWithHtml"))), jv1)
                        :: budgetInProposal(jv1)
                        :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
                )
            }

            val reVal2 = {
                requestArgsQuery().commonExcution(
                    MessageRoutes(msg_log(toJson(Map("method" -> toJson("alOutExcelVcalueWithHtml"))), jv1)
                        :: hospitalPotentialInProposal(jv1)
                        :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
                )
            }

            val reVal3 = {
                requestArgsQuery().commonExcution(
                    MessageRoutes(msg_log(toJson(Map("method" -> toJson("alOutExcelVcalueWithHtml"))), jv1)
                        :: perResultInProposal(jv1)
                        :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
                )
            }

            val jv = toJson("")
            val reVal =
                requestArgsQuery().commonExcution(
                    MessageRoutes(msg_log(toJson(Map("method" -> toJson("sales man proposal"))), jv)
                        :: salesMenInProposal(jv)
                        :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
                )

            if ((reVal  \ "status").asOpt[String].get == "ok" &&
                (reVal1 \ "status").asOpt[String].get == "ok" &&
                (reVal2 \ "status").asOpt[String].get == "ok" &&
                (reVal3 \ "status").asOpt[String].get == "ok") {

                val budget = (reVal1 \ "result" \ "budget").asOpt[JsValue].get
                val preresult = (reVal3 \ "result" \ "preresult").asOpt[JsValue].get
                val hosp_potential = (reVal2 \ "result" \ "hospital_potential").asOpt[JsValue].get
                val sales_men = (reVal \ "result" \ "salesmen").asOpt[List[JsValue]].get

                val tmp1 = (preresult \ "1").asOpt[List[JsValue]].get.sortBy(s => (s \ "hosp_code").asOpt[String].get.toInt)
                val tmp2 = (hosp_potential \ "1").asOpt[List[JsValue]].get.sortBy(s => (s \ "hosp_code").asOpt[String].get.toInt)

                val tmp =
                tmp1 zip tmp2 map { x =>
                    toJson(Map(
                        "hosp_code" -> toJson((x._1 \ "hosp_code").asOpt[String].get),
                        "hosp_name" -> toJson((x._1 \ "hosp_name").asOpt[String].get),
                        "hosp_cat" -> toJson((x._1 \ "hosp_cat").asOpt[String].get),
                        "口服抗生素" -> toJson((x._1 \ "口服抗生素").asOpt[String].get :: ((x._2) \ "口服抗生素").asOpt[String].get :: Nil),
                        "一代降糖药" -> toJson((x._1 \ "一代降糖药").asOpt[String].get :: ((x._2) \ "一代降糖药").asOpt[String].get :: Nil),
                        "三代降糖药" -> toJson((x._1 \ "三代降糖药").asOpt[String].get :: ((x._2) \ "三代降糖药").asOpt[String].get :: Nil),
                        "皮肤药" -> toJson((x._1 \ "皮肤药").asOpt[String].get :: ((x._2) \ "皮肤药").asOpt[String].get :: Nil)
                    ))
                }

                Ok(views.html.Module.Decision.BusinessDecision.bus_index(budget)(tmp)(sales_men)(uuid)(p))
            } else Redirect("/login")
        }
    }

    def management(uuid : String, phrase : String) = Action { request =>
        getUserCookie(request) {
            Ok(views.html.Module.Decision.ManagementDecision.mag_index())
        }
    }

	def report(uuid : String, phrase : String) = Action { request =>
        getUserCookie(request) {
            Ok(views.html.Module.Report.report_index())
        }
	}

    def takelast(uuid : String) = Action { request =>
        getUserCookie(request) {
            Redirect("/brd/" + uuid)
        }
    }

    def takenew = Action { request =>
        getUserCookie(request) {
            Ok(views.html.Module.Report.report_index())
        }
    }

    def next = Action(request => requestArgsQuery().requestArgsV2(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("decision next"))), jv)
            :: updateUserInputInOpPhase(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
        })
}
