package module.report

import com.mongodb.casbah.Imports._
import com.pharbers.ErrorCode
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.dbManagerTrait.dbInstanceManager
import module.report.ReportData._
import module.report.ReportMessage._
import play.api.libs.json.{JsObject, JsValue}
import play.api.libs.json.Json._


// TODO： 报告更适合并行
object ReportModule extends ModuleTrait with ReportData with ReportViewData {
	 def dispatchMsg(msg: MessageDefines)
					(pr: Option[String Map JsValue])
					(implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = msg match {
		case MsgReportMarketSalesCommercialValue(data) => marketSalesCommercialValueReport(data)
		case MsgReportMarketSalesPerformance(data) => marketSalesPerformanceReport(data)(pr)
		
		case MsgReportDeputyTimerAllot(data) => deputyTimerAllotReport(data)
		case MsgReportDeputyProductInformation(data) => deputyProductInformationReport(data)(pr)
		case MsgReportDeputyEmpiric(data) => deputyEmpiricReport(data)(pr)
		case MsgReportDeputySalesSkills(data) => deputySalesSkillsReport(data)(pr)
		case MsgReportDeputyWorkAttitude(data) => deputyWorkAttitudeReport(data)(pr)

		case MsgReportManagerCost(data) => managerCostReport(data)
		case MsgReportManagerTimerAllot(data) => managerTimerAllotReport(data)(pr)

		case MsgReportAllot(data) => allotReport(data)

		case MsgReportSalesCustomer(data) => salesCustomerReport(data)
		case MsgReportSalesDeputy(data) => salesDeputyReport(data)
		case MsgReportSalesProduct(data) => salesProductReport(data)


	  	case _ => throw new Exception("function is not impl")
	 }

	def marketSalesCommercialValueReport(data: JsValue)
			  			                (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
			val db = conn.queryDBInstance("stp").get
			val user = (data \ "condition" \ "user").asOpt[String].getOrElse(throw new Exception("wrong input"))
			val o = condition(data)

			val result= db.queryObject(o, user)(d2mMarketSalesCommercialValue)

			(Some(Map("data" -> toJson(result))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	def marketSalesPerformanceReport(data: JsValue)
	                                (pr: Option[String Map JsValue])
			  			            (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
			val db = conn.queryDBInstance("stp").get
			val user = (data \ "condition" \ "user").asOpt[String].getOrElse(throw new Exception("wrong input"))
			val o = condition(data)
			val prResult = pr match {
				case None => throw new Exception("pr data not exist")
				case Some(one) =>
					one("data").asOpt[String Map JsValue].get("result").asOpt[List[String Map String]].map(x => x).getOrElse(Nil)
			}

			val result = db.queryObject(o, user)(d2mMarketSalesPerformance) match {
				case None => throw new Exception("data not exist")
				case Some(one) =>
					one("result").asOpt[List[String Map String]].map(x => x).getOrElse(Nil)
			}

			val reValMap = Map("commercialvalue" -> prResult, "performance" -> result)

			val view = Map("marketsalesreport" -> toJson(marketSalesReportView(reValMap, o.getAs[List[String]]("phase").
															map(x => x.head).getOrElse(throw new Exception("wrong input")) )))

			(Some(Map("data" -> toJson(view))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
		
	}
	
	/////////////////////////////////////
	def deputyTimerAllotReport(data: JsValue)
							  (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
			val db = conn.queryDBInstance("stp").get
			val user = (data \ "condition" \ "user").asOpt[String].getOrElse(throw new Exception("wrong input"))
			val o = condition(data)

			val result= db.queryObject(o, user)(d2mDeputyTimerAllot)

			(Some(Map("data" -> toJson(result))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}


	def deputyProductInformationReport(data: JsValue)
									  (pr: Option[String Map JsValue])
									  (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
			val db = conn.queryDBInstance("stp").get
			val user = (data \ "condition" \ "user").asOpt[String].getOrElse(throw new Exception("wrong input"))
			val o = condition(data)

			val prResult = pr.map(x => x.getOrElse("data", throw new Exception("pr data not exist")).asOpt[String Map JsValue].get("result")).getOrElse(throw new Exception("data not exist"))
			val result = db.queryObject(o, user)(d2mDeputyProductInformation).
							map(x => x.getOrElse("result", throw new Exception("data not exist"))).getOrElse(throw new Exception("data not exist"))


			val reValMap = Map("timerallot" -> prResult, "productinformation" -> result)

			(Some(Map("data" -> toJson(reValMap))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}

	def deputyEmpiricReport(data: JsValue)
						   (pr: Option[String Map JsValue])
						   (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
			val db = conn.queryDBInstance("stp").get
			val user = (data \ "condition" \ "user").asOpt[String].getOrElse(throw new Exception("wrong input"))
			val o = condition(data)

			val result = db.queryObject(o, user)(d2mDeputyEmpiric).
				map(x => x.getOrElse("result", throw new Exception("data not exist"))).getOrElse(throw new Exception("data not exist"))

			val reValMap = MergeStepReportResult(toJson(Map("empiric" -> result)), pr)

			(Some(Map("data" -> toJson(reValMap))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}

	def deputySalesSkillsReport(data: JsValue)
							   (pr: Option[String Map JsValue])
							   (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
			val db = conn.queryDBInstance("stp").get
			val user = (data \ "condition" \ "user").asOpt[String].getOrElse(throw new Exception("wrong input"))
			val o = condition(data)

			val result = db.queryObject(o, user)(d2mDeputySalesSkills).
							map(x => x.getOrElse("result", throw new Exception("data not exist"))).getOrElse(throw new Exception("data not exist"))

			val reValMap = MergeStepReportResult(toJson(Map("salesskills" -> result)), pr)

			(Some(Map("data" -> toJson(reValMap))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}

	def deputyWorkAttitudeReport(data: JsValue)
								(pr: Option[String Map JsValue])
							   (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
			val db = conn.queryDBInstance("stp").get
			val user = (data \ "condition" \ "user").asOpt[String].getOrElse(throw new Exception("wrong input"))
			val o = condition(data)

			val result = db.queryObject(o, user)(d2mDeputyWorkAttitude).
				map(x => x.getOrElse("result", throw new Exception("data not exist"))).getOrElse(throw new Exception("data not exist"))

			val reValMap = MergeStepReportResult(toJson(Map("workattitude" -> result)), pr).as[String Map List[String Map String]]

			val view = Map("deputyreport" -> toJson(deputyReportView(reValMap, o.getAs[List[String]]("phase").
													map(x => x.head).getOrElse(throw new Exception("wrong input")))))

			(Some(Map("data" -> toJson(view))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}

	/////////////////////////////////////
	def managerCostReport(data: JsValue)
						 (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
			val db = conn.queryDBInstance("stp").get
			val user = (data \ "condition" \ "user").asOpt[String].getOrElse(throw new Exception("wrong input"))
			val o = condition(data)

			val result= db.queryObject(o, user)(d2mManagerCost)

			(Some(Map("data" -> toJson(result))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}

	def managerTimerAllotReport(data: JsValue)
							   (pr: Option[String Map JsValue])
							   (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
			val db = conn.queryDBInstance("stp").get
			val user = (data \ "condition" \ "user").asOpt[String].getOrElse(throw new Exception("wrong input"))
			val o = condition(data)
			val prResult = pr match {
				case None => throw new Exception("pr data not exist")
				case Some(one) =>
					one("data").asOpt[String Map JsValue].get("result").asOpt[List[String Map String]].map(x => x).getOrElse(Nil)
			}

			val result = db.queryObject(o, user)(d2ManagerTimerAllot) match {
				case None => throw new Exception("data not exist")
				case Some(one) =>
					one("result").asOpt[List[String Map String]].map(x => x).getOrElse(Nil)
			}

			val reValMap = Map("cost" -> prResult, "timerallot" -> result)

			val view = Map("managerreport" -> toJson(managerReportView(reValMap, o.getAs[List[String]]("phase").
				map(x => x.head).getOrElse(throw new Exception("wrong input") ))))

			(Some(Map("data" -> toJson(view))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}

	/////////////////////////////////////
	def allotReport(data: JsValue)
				   (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			def conditions(jv: JsValue)(implicit m: String Map String): Boolean = {
				val jvobj = (jv \ "condition").as[JsObject]
				val jvmap = jvobj.as[String Map String] - "user" - "cycle"
				val r = jvmap.map ( x => m.exists(e => e._2 == x._2) ).toList.distinct match {
					case Nil => true
					case lst if lst.size ==1 && lst.head => true
					case _ => false
				}
				r
			}

			val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
			val db = conn.queryDBInstance("stp").get
			val user = (data \ "condition" \ "user").asOpt[String].getOrElse(throw new Exception("wrong input"))

			val o = conditionAllot(data)
			val result = (db.queryObject(o, user)(d2mAllot) match {
				case None => throw new Exception("data not exist")
				case Some(one) => one("result").asOpt[List[String Map String]].map(x => x).getOrElse(Nil)
			}).filter(implicit f => conditions(data))

			val view = Map("allotreport" -> toJson(allotReportView(Map("timerallot" -> result), o.getAs[List[String]]("phase").
				map(x => x.head).getOrElse(throw new Exception("wrong input") ))))

			(Some(Map("data" -> toJson(view))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}

	/////////////////////////////////////
	def salesCustomerReport(data: JsValue)
						   (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			def conditions(jv: JsValue)(implicit m: String Map String): Boolean = {
				val jvobj = (jv \ "condition").as[JsObject]
				val jvmap = jvobj.as[String Map String] - "user" - "cycle"
				val r = jvmap.map ( x => m.exists(e => e._2 == x._2) ).toList.distinct match {
					case Nil => true
					case lst if lst.size ==1 && lst.head => true
					case _ => false
				}
				r
			}

			val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
			val db = conn.queryDBInstance("stp").get
			val user = (data \ "condition" \ "user").asOpt[String].getOrElse(throw new Exception("wrong input"))

			val o = conditionSalesCustomer(data)
			val result = (db.queryObject(o, user)(d2mSalesCustomer) match {
				case None => throw new Exception("data not exist")
				case Some(one) => one("result").asOpt[List[String Map String]].map(x => x).getOrElse(Nil)
			}).filter(implicit f => conditions(data))

			val view = Map("salescustomerreport" -> toJson(salesCustomerReportView(Map("salescustomer" -> result), o.getAs[List[String]]("phase").
				map(x => x.head).getOrElse(throw new Exception("wrong input") ))))

			(Some(Map("data" -> toJson(view))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}

	def salesDeputyReport(data: JsValue)
						 (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			def conditions(jv: JsValue)(implicit m: String Map String): Boolean = {
				val jvobj = (jv \ "condition").as[JsObject]
				val jvmap = jvobj.as[String Map String] - "user" - "cycle"
				val r = jvmap.map ( x => m.exists(e => e._2 == x._2) ).toList.distinct match {
					case Nil => true
					case lst if lst.size ==1 && lst.head => true
					case _ => false
				}
				r
			}

			val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
			val db = conn.queryDBInstance("stp").get
			val user = (data \ "condition" \ "user").asOpt[String].getOrElse(throw new Exception("wrong input"))

			val o = conditionSalesDeputy(data)
			val result = (db.queryObject(o, user)(d2mSalesDeputy) match {
				case None => throw new Exception("data not exist")
				case Some(one) => one("result").asOpt[List[String Map String]].map(x => x).getOrElse(Nil)
			}).filter(implicit f => conditions(data))

			val view = Map("salesdeputyreport" -> toJson(salesDeputyReportView(Map("salesdeputy" -> result), o.getAs[List[String]]("phase").
				map(x => x.head).getOrElse(throw new Exception("wrong input") ))))

			(Some(Map("data" -> toJson(view))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}

	def salesProductReport(data: JsValue)
						  (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
			val db = conn.queryDBInstance("stp").get
			val user = (data \ "condition" \ "user").asOpt[String].getOrElse(throw new Exception("wrong input"))
			val o = condition(data)

			val result= db.queryObject(o, user)(d2mSalesProduct) match {
				case None => throw new Exception("data not exist")
				case Some(one) => one("result").asOpt[List[String Map String]].map(x => x).getOrElse(Nil)
			}

			val view = Map("salesproductreport" -> toJson(salesProductReportView(Map("salesproduct" -> result), o.getAs[List[String]]("phase").
				map(x => x.head).getOrElse(throw new Exception("wrong input") ))))

			(Some(Map("data" -> toJson(view))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}

}