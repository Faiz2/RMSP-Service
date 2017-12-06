package module.report

import com.mongodb.casbah.Imports._
import com.pharbers.ErrorCode
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.common.Book
import com.pharbers.dbManagerTrait.dbInstanceManager
import com.pharbers.panel.util.excel.handle.phWriteExcelHandle
import com.pharbers.panel.util.excel.phHandleExcel
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
		case MsgReportMarketSales(data) => marketSalesReport(data)(pr)
		
		case MsgReportDeputyTimerAllot(data) => deputyTimerAllotReport(data)(pr)
		case MsgReportDeputyProductInformation(data) => deputyProductInformationReport(data)(pr)
		case MsgReportDeputyEmpiric(data) => deputyEmpiricReport(data)(pr)
		case MsgReportDeputySalesSkills(data) => deputySalesSkillsReport(data)(pr)
		case MsgReportDeputyWorkAttitude(data) => deputyWorkAttitudeReport(data)(pr)
		case MsgReportDeputy(data) => deputyReport(data)(pr)

		case MsgReportManagerCost(data) => managerCostReport(data)(pr)
		case MsgReportManagerTimerAllot(data) => managerTimerAllotReport(data)(pr)
		case MsgReportManager(data) => managerReport(data)(pr)

		case MsgReportAllot(data) => allotReport(data)(pr)
		case MsgReportAllotView(data) => allotReportViews(data)(pr)

		case MsgReportSalesCustomer(data) => salesCustomerReport(data)(pr)
		case MsgReportSalesCustomerMerge(data) => salesCustomerReportMerge(data)(pr)
		
		case MsgReportSalesDeputy(data) => salesDeputyReport(data)(pr)
		case MsgReportSalesDeputyMerge(data) => salesDeputyReportMerge(data)(pr)
		
		case MsgReportSalesProduct(data) => salesProductReport(data)(pr)
		case MsgReportSalesProductMerge(data) => salesProductReportMerge(data)(pr)

		case MsgDownReport(data) => downReport(data)(pr)
			
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
			
			(Some(Map("data" -> toJson(Map("commercialvalue" -> result.get("result").asOpt[List[String Map String]].map(x => x).getOrElse(Nil) )))), None)
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

			val result = db.queryObject(o, user)(d2mMarketSalesPerformance) match {
				case None => throw new Exception("data not exist")
				case Some(one) =>
					one("result").asOpt[List[String Map String]].map(x => x).getOrElse(Nil)
			}

			val reValJv = MergeStepReportResult(toJson(Map("performance" -> result)), pr)
			
			(Some(Map("data" -> toJson(reValJv))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
		
	}
	
	def marketSalesReport(data: JsValue)
	                     (pr: Option[String Map JsValue])
	                     (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			val reValMap = pr.get("data").as[String Map List[String Map String]]
			val o = condition(data)
			
			val view = Map("marketsalesreport" -> toJson(marketSalesReportView(reValMap, o.getAs[List[String]]("phase").
				map(x => x.head).getOrElse(throw new Exception("wrong input")) )))
			
			(Some(Map("data" -> toJson(view))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	/////////////////////////////////////
	def deputyTimerAllotReport(data: JsValue)
	                          (pr: Option[String Map JsValue])
							  (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
			val db = conn.queryDBInstance("stp").get
			val user = (data \ "condition" \ "user").asOpt[String].getOrElse(throw new Exception("wrong input"))
			val o = condition(data)

			val result= db.queryObject(o, user)(d2mDeputyTimerAllot)
			
			pr match {
				case None =>
					(Some(Map("data" -> toJson(Map("timerallot" -> result.get("result").asOpt[List[String Map String]].map(x => x).getOrElse(Nil))))), None)
				case Some(one) =>
					if(one.getOrElse("status", toJson("no")).as[String] == "ok") {
						(Some(Map("data" -> toJson(Map("timerallot" -> result.get("result").asOpt[List[String Map String]].map(x => x).getOrElse(Nil))))), None)
					} else {
						val reValJv = MergeStepReportResult(toJson(Map("timerallot" -> result.get("result").asOpt[List[String Map String]].map(x => x).getOrElse(Nil))), pr)
						(Some(Map("data" -> toJson(reValJv))), None)
					}
			}
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

			val result = db.queryObject(o, user)(d2mDeputyProductInformation).
							map(x => x.getOrElse("result", throw new Exception("data not exist"))).getOrElse(throw new Exception("data not exist"))

			val reValJv = MergeStepReportResult(toJson(Map("productinformation" -> result)), pr)
			
			(Some(Map("data" -> toJson(reValJv))), None)
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

			val reValJv = MergeStepReportResult(toJson(Map("empiric" -> result)), pr)

			(Some(Map("data" -> toJson(reValJv))), None)
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

			val reValJv = MergeStepReportResult(toJson(Map("salesskills" -> result)), pr)

			(Some(Map("data" -> toJson(reValJv))), None)
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

			val reValJv = MergeStepReportResult(toJson(Map("workattitude" -> result)), pr)

			(Some(Map("data" -> toJson(reValJv))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	def deputyReport(data: JsValue)
	                (pr: Option[String Map JsValue])
	                (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			
			val reValMap = pr.get("data").as[String Map List[String Map String]]
			
			val o = condition(data)
			
			val view = Map("deputyreport" -> toJson(deputyReportView(reValMap, o.getAs[List[String]]("phase").
				map(x => x.head).getOrElse(throw new Exception("wrong input")))))
			
			(Some(Map("data" -> toJson(view))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	/////////////////////////////////////
	def managerCostReport(data: JsValue)
	                     (pr: Option[String Map JsValue])
						 (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
			val db = conn.queryDBInstance("stp").get
			val user = (data \ "condition" \ "user").asOpt[String].getOrElse(throw new Exception("wrong input"))
			val o = condition(data)

			val result= db.queryObject(o, user)(d2mManagerCost)
			
			pr match {
				case None =>
					(Some(Map("data" -> toJson(Map("cost" -> result.get("result").asOpt[List[String Map String]].map(x => x).getOrElse(Nil))))), None)
				case Some(one) =>
					if(one.getOrElse("status", toJson("no")).as[String] == "ok") {
						(Some(Map("data" -> toJson(Map("cost" -> result.get("result").asOpt[List[String Map String]].map(x => x).getOrElse(Nil))))), None)
					} else {
						val reValJv = MergeStepReportResult(toJson(Map("cost" -> result.get("result").asOpt[List[String Map String]].map(x => x).getOrElse(Nil))), pr)
						(Some(Map("data" -> toJson(reValJv))), None)
					}
			}

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
			
			val result = db.queryObject(o, user)(d2ManagerTimerAllot) match {
				case None => throw new Exception("data not exist")
				case Some(one) =>
					one("result").asOpt[List[String Map String]].map(x => x).getOrElse(Nil)
			}

			val reValJv = MergeStepReportResult(toJson(Map("timerallot" -> result)), pr)
			
			(Some(Map("data" -> toJson(reValJv))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	def managerReport(data: JsValue)
	                 (pr: Option[String Map JsValue])
	                 (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			val reValMap = pr.get("data").as[String Map List[String Map String]]
			
			val o = condition(data)
			
			val view = Map("managerreport" -> toJson(managerReportView(reValMap, o.getAs[List[String]]("phase").
				map(x => x.head).getOrElse(throw new Exception("wrong input")))))
			
			(Some(Map("data" -> toJson(view))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}

	/////////////////////////////////////
	def allotReport(data: JsValue)
	               (pr: Option[String Map JsValue])
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
			
			pr match {
				case None =>
					(Some(Map("data" -> toJson(Map("timerallot" -> result)))), None)
				case Some(one) =>
					if(one.getOrElse("status", toJson("no")).as[String] == "ok") {
						(Some(Map("data" -> toJson(Map("timerallot" -> result)))), None)
					} else {
						val reValJv = MergeStepReportResult(toJson(Map("timerallot" -> result)), pr)
						(Some(Map("data" -> toJson(reValJv))), None)
					}
			}
			
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	def allotReportViews(data: JsValue)
	                    (pr: Option[String Map JsValue])
	                    (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			val reValMap = pr.get("data").as[String Map List[String Map String]]
			
			val o = condition(data)
			
			val view = Map("allotreport" -> toJson(allotReportView(reValMap, o.getAs[List[String]]("phase").
				map(x => x.head).getOrElse(throw new Exception("wrong input") ))))
			
			(Some(Map("data" -> toJson(view))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}

	/////////////////////////////////////
	def salesCustomerReport(data: JsValue)
	                       (pr: Option[String Map JsValue])
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
			
			pr match {
				case None =>
					(Some(Map("data" -> toJson(Map("salescustomer" -> result)))), None)
				case Some(one) =>
					if(one.getOrElse("status", toJson("no")).as[String] == "ok") {
						(Some(Map("data" -> toJson(Map("salescustomer" -> result)))), None)
					} else {
						val reValJv = MergeStepReportResult(toJson(Map("salescustomer" -> result)), pr)
						(Some(Map("data" -> toJson(reValJv))), None)
					}
			}
			
			
//			val view = Map("salescustomerreport" -> toJson(salesCustomerReportView(Map("salescustomer" -> result), o.getAs[List[String]]("phase").
//				map(x => x.head).getOrElse(throw new Exception("wrong input") ))))

//			(Some(Map("data" -> toJson(view))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	def salesCustomerReportMerge(data: JsValue)
	                            (pr: Option[String Map JsValue])
	                            (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			
			val reValMap = pr.get("data").as[String Map List[String Map String]]
			
			val o = condition(data)
			
			val view = Map("salescustomerreport" -> toJson(salesCustomerReportView(reValMap, o.getAs[List[String]]("phase").
				map(x => x.head).getOrElse(throw new Exception("wrong input") ))))
			
			(Some(Map("data" -> toJson(view))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	
	/////////////////////////////////////
	def salesDeputyReport(data: JsValue)
	                     (pr: Option[String Map JsValue])
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
			
			pr match {
				case None =>
					(Some(Map("data" -> toJson(Map("salesdeputy" -> result)))), None)
				case Some(one) =>
					if(one.getOrElse("status", toJson("no")).as[String] == "ok") {
						(Some(Map("data" -> toJson(Map("salesdeputy" -> result)))), None)
					} else {
						val reValJv = MergeStepReportResult(toJson(Map("salesdeputy" -> result)), pr)
						(Some(Map("data" -> toJson(reValJv))), None)
					}
			}
			
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	def salesDeputyReportMerge(data: JsValue)
	                          (pr: Option[String Map JsValue])
	                          (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) ={
		try {
			
			val reValMap = pr.get("data").as[String Map List[String Map String]]
			
			val o = condition(data)
			
			val view = Map("salesdeputyreport" -> toJson(salesDeputyReportView(reValMap, o.getAs[List[String]]("phase").
				map(x => x.head).getOrElse(throw new Exception("wrong input") ))))
			
			(Some(Map("data" -> toJson(view))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	/////////////////////////////////////
	def salesProductReport(data: JsValue)
	                      (pr: Option[String Map JsValue])
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
			
			pr match {
				case None =>
					(Some(Map("data" -> toJson(Map("salesproduct" -> result)))), None)
				case Some(one) =>
					if(one.getOrElse("status", toJson("no")).as[String] == "ok") {
						(Some(Map("data" -> toJson(Map("salesproduct" -> result)))), None)
					} else {
						val reValJv = MergeStepReportResult(toJson(Map("salesproduct" -> result)), pr)
						(Some(Map("data" -> toJson(reValJv))), None)
					}
			}
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
	
	
	def salesProductReportMerge(data: JsValue)
	                           (pr: Option[String Map JsValue])
	                           (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			val reValMap = pr.get("data").as[String Map List[String Map String]]
			
			val o = condition(data)
			
			val view = Map("salesproductreport" -> toJson(salesProductReportView(reValMap, o.getAs[List[String]]("phase").
				map(x => x.head).getOrElse(throw new Exception("wrong input") ))))
			
			(Some(Map("data" -> toJson(view))), None)
		} catch {
			case ex: Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}


	def downReport(data: JsValue)
	              (pr: Option[String Map JsValue])
	              (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
		try {
			
//			pr.get("data").as[String Map List[String Map String]].foreach(println(_))
//			val m = pr.get("data").as[String Map List[String Map String]]
			
			(Some(Map("" -> toJson(""))), None)
		} catch {
			case ex: Exception =>
				println(ex.getMessage)
				(None, Some(ErrorCode.errorToJson(ex.getMessage)))
		}
	}
}