package module.report.ReportData

import com.mongodb.casbah.Imports._
import play.api.libs.json.{JsObject, JsValue}
import play.api.libs.json.Json._

import collection.JavaConversions._

trait ReportData {
	type resultType = String Map JsValue

	def MergeStepReportResult(data : JsValue, pr : Option[Map[String, JsValue]]) : JsValue = {
		val reVal = pr.map(x => x.getOrElse("data", throw new Exception("data not exist"))).
						getOrElse(throw new Exception("data not exist")).as[JsObject]
		data.as[JsObject] ++ reVal
	}


	def condition(data: JsValue): DBObject = {
		val builder = MongoDBObject.newBuilder
		(data \ "condition" \ "cycle").asOpt[String].map(x => builder += "phase" -> List(x)).getOrElse(Unit)
		builder.result
	}

	// TODO: 郁闷死，为啥他们R用中文作为key
	def conditionAllot(data: JsValue): DBObject  = {
		val builder = MongoDBObject.newBuilder
		(data \ "condition" \ "cycle").asOpt[String].map(x => builder += "phase" -> List(x)).getOrElse(Unit)
		(data \ "condition" \ "hospital").asOpt[String].map(x => builder += "p1_report4_mod1.医院" -> x).getOrElse(Unit)
		(data \ "condition" \ "dimension").asOpt[String].map(x => builder += "p1_report4_mod1.因素" -> x).getOrElse(Unit)
		builder.result
	}

	def conditionSalesCustomer(data: JsValue): DBObject = {
		val builder = MongoDBObject.newBuilder
		(data \ "condition" \ "cycle").asOpt[String].map(x => builder += "phase" -> List(x)).getOrElse(Unit)
		(data \ "condition" \ "hospital").asOpt[String].map(x => builder += "p1_report5_mod1.医院" -> x).getOrElse(Unit)
		(data \ "condition" \ "product").asOpt[String].map(x => builder += "p1_report5_mod1.产品" -> x).getOrElse(Unit)
		builder.result
	}

	def conditionSalesDeputy(data: JsValue): DBObject = {
		val builder = MongoDBObject.newBuilder
		(data \ "condition" \ "cycle").asOpt[String].map(x => builder += "phase" -> List(x)).getOrElse(Unit)
		(data \ "condition" \ "people").asOpt[String].map(x => builder += "p1_report5_mod2.销售代表" -> x).getOrElse(Unit)
		(data \ "condition" \ "product").asOpt[String].map(x => builder += "p1_report5_mod2.产品" -> x).getOrElse(Unit)
		builder.result
	}


	val d2mMarketSalesCommercialValue: DBObject => resultType = { obj =>

//		val phase = obj.getAs[List[String]]("phase").getOrElse("")
		val reDocObj = obj.getAs[List[BasicDBObject]]("p1_report1_mod1").getOrElse(Nil).
						map(x => x.toMap.toMap.map(f => f._1.toString -> f._2.toString))

		Map("result" -> toJson(reDocObj))
	}

	val d2mMarketSalesPerformance: DBObject => resultType = { obj =>
		val reDocObj = obj.getAs[List[BasicDBObject]]("p1_report1_mod2").getOrElse(Nil).
						map(x => x.toMap.toMap.map(f => f._1.toString -> f._2.toString))
		Map("result" -> toJson(reDocObj))
	}

	///////////////////////////////////////////////

	val d2mDeputyTimerAllot: DBObject => resultType = { obj =>
		val reDocObj = obj.getAs[List[BasicDBObject]]("p1_report2_mod1").getOrElse(Nil).
						map(x => x.toMap.toMap.map(f => f._1.toString -> f._2.toString))
		Map("result" -> toJson(reDocObj))
	}

	val d2mDeputyProductInformation: DBObject => resultType = { obj =>
		val reDocObj = obj.getAs[List[BasicDBObject]]("p1_report2_mod2").getOrElse(Nil).
						map(x => x.toMap.toMap.map(f => f._1.toString -> f._2.toString))
		Map("result" -> toJson(reDocObj))
	}

	val d2mDeputyEmpiric: DBObject => resultType = { obj =>
		val reDocObj = obj.getAs[List[BasicDBObject]]("p1_report2_mod3").getOrElse(Nil).
			map(x => x.toMap.toMap.map(f => f._1.toString -> f._2.toString))
		Map("result" -> toJson(reDocObj))
	}

	val d2mDeputySalesSkills: DBObject => resultType = { obj =>
		val reDocObj = obj.getAs[List[BasicDBObject]]("p1_report2_mod4").getOrElse(Nil).
			map(x => x.toMap.toMap.map(f => f._1.toString -> f._2.toString))
		Map("result" -> toJson(reDocObj))
	}

	val d2mDeputyWorkAttitude: DBObject => resultType = { obj =>
		val reDocObj = obj.getAs[List[BasicDBObject]]("p1_report2_mod5").getOrElse(Nil).
			map(x => x.toMap.toMap.map(f => f._1.toString -> f._2.toString))
		Map("result" -> toJson(reDocObj))
	}

	///////////////////////////////////////////////

	val d2mManagerCost: DBObject => resultType = { obj =>
		val reDocObj = obj.getAs[List[BasicDBObject]]("p1_report3_mod1").getOrElse(Nil).
			map(x => x.toMap.toMap.map(f => f._1.toString -> f._2.toString))
		Map("result" -> toJson(reDocObj))
	}

	val d2ManagerTimerAllot: DBObject => resultType = { obj =>
		val reDocObj = obj.getAs[List[BasicDBObject]]("p1_report3_mod2").getOrElse(Nil).
			map(x => x.toMap.toMap.map(f => f._1.toString -> f._2.toString))
		Map("result" -> toJson(reDocObj))
	}

	///////////////////////////////////////////////

	val d2mAllot: DBObject => resultType = { obj =>
		val reDocObj = obj.getAs[List[BasicDBObject]]("p1_report4_mod1").getOrElse(Nil).
			map(x => x.toMap.toMap.map(f => f._1.toString -> f._2.toString))
		Map("result" -> toJson(reDocObj))
	}

	///////////////////////////////////////////////

	val d2mSalesCustomer: DBObject => resultType = { obj =>
		val reDocObj = obj.getAs[List[BasicDBObject]]("p1_report5_mod1").getOrElse(Nil).
			map(x => x.toMap.toMap.map(f => f._1.toString -> f._2.toString))
		Map("result" -> toJson(reDocObj))
	}

	val d2mSalesDeputy: DBObject => resultType = { obj =>
		val reDocObj = obj.getAs[List[BasicDBObject]]("p1_report5_mod2").getOrElse(Nil).
			map(x => x.toMap.toMap.map(f => f._1.toString -> f._2.toString))
		Map("result" -> toJson(reDocObj))
	}

	val d2mSalesProduct: DBObject => resultType = { obj =>
		val reDocObj = obj.getAs[List[BasicDBObject]]("p1_report5_mod3").getOrElse(Nil).
			map(x => x.toMap.toMap.map(f => f._1.toString -> f._2.toString))
		Map("result" -> toJson(reDocObj))
	}
}


trait ReportViewData {

	def marketSalesReportView(m: String Map List[String Map String], cycle: String): String = cycle match {
		case "周期1" => views.html.Module.Report.ReportCycle1Content.market_sales_report(m).toString
		case "周期2" => views.html.Module.Report.ReportCycle2Content.market_sales_report(m).toString
		case _ => ???
	}

	def deputyReportView(m: String Map List[String Map String], cycle: String): String = cycle match {
		case "周期1" => views.html.Module.Report.ReportCycle1Content.delegate_report(m).toString
		case "周期2" => views.html.Module.Report.ReportCycle2Content.delegate_report(m).toString
		case _ => ???
	}

	def managerReportView(m: String Map List[String Map String], cycle: String): String = cycle match {
		case "周期1" => views.html.Module.Report.ReportCycle1Content.manager_report(m).toString
		case "周期2" => views.html.Module.Report.ReportCycle2Content.manager_report(m).toString
		case _ => ???
	}

	def allotReportView(m: String Map List[String Map String], cycle: String): String = cycle match {
		case "周期1" => views.html.Module.Report.ReportCycle1Content.allot_report(m).toString
		case "周期2" => views.html.Module.Report.ReportCycle2Content.allot_report(m).toString
		case _ => ???
	}

	def salesCustomerReportView(m: String Map List[String Map String], cycle: String): String = cycle match {
		case "周期1" => views.html.Module.Report.ReportCycle1Content.sales_customer_report(m).toString
		case "周期2" => views.html.Module.Report.ReportCycle2Content.sales_customer_report(m).toString
		case _ => ???
	}

	def salesDeputyReportView(m: String Map List[String Map String], cycle: String): String = cycle match {
		case "周期1" => views.html.Module.Report.ReportCycle1Content.sales_deputy_report(m).toString
		case "周期2" => views.html.Module.Report.ReportCycle2Content.sales_deputy_report(m).toString
		case _ => ???
	}

	def salesProductReportView(m: String Map List[String Map String], cycle: String): String = cycle match {
		case "周期1" => views.html.Module.Report.ReportCycle1Content.sales_product_report(m).toString
		case "周期2" => views.html.Module.Report.ReportCycle2Content.sales_product_report(m).toString
		case _ => ???
	}


}