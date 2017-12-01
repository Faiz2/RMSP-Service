package module.report.ReportData

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json._

trait ReportData {

	type resultType = String Map JsValue

	def conditionAllot(data: JsValue): DBObject  = {
		val builder = MongoDBObject.newBuilder
		(data \ "condition" \ "").asOpt[String].map(x => builder += "" -> x).getOrElse(Unit)
		builder.result
	}

	def conditionSalesCustomer(data: JsValue): DBObject = {
		val builder = MongoDBObject.newBuilder
		(data \ "condition" \ "").asOpt[String].map(x => builder += "" -> x).getOrElse(Unit)
		builder.result
	}

	def conditionSalesDeputy(data: JsValue): DBObject = {
		val builder = MongoDBObject.newBuilder
		(data \ "condition" \ "").asOpt[String].map(x => builder += "" -> x).getOrElse(Unit)
		builder.result
	}


	val d2mMarketSalesCommercialValue: DBObject => resultType = { obj =>
		Map("" -> toJson(""))
	}

	val d2mMarketSalesPerformance: DBObject => resultType = { obj =>
		Map("" -> toJson(""))
	}

	///////////////////////////////////////////////

	val d2mDeputyTimerAllot: DBObject => resultType = { obj =>
		Map("" -> toJson(""))
	}

	val d2mDeputyProductInformation: DBObject => resultType = { obj =>
		Map("" -> toJson(""))
	}

	val d2mDeputyEmpiric: DBObject => resultType = { obj =>
		Map("" -> toJson(""))
	}

	val d2mDeputySalesSkills: DBObject => resultType = { obj =>
		Map("" -> toJson(""))
	}

	val d2mDeputyWorkAttitude: DBObject => resultType = { obj =>
		Map("" -> toJson(""))
	}

	///////////////////////////////////////////////

	val d2mManagerCost: DBObject => resultType = { obj =>
		Map("" -> toJson(""))
	}

	val d2ManagerTimerAllot: DBObject => resultType = { obj =>
		Map("" -> toJson(""))
	}

	///////////////////////////////////////////////

	val d2mAllot: DBObject => resultType = { obj =>
		Map("" -> toJson(""))
	}

	val d2mSalesCustomer: DBObject => resultType = { obj =>
		Map("" -> toJson(""))
	}

	val d2mSalesDeputy: DBObject => resultType = { obj =>
		Map("" -> toJson(""))
	}

	val d2mSalesProduct: DBObject => resultType = { obj =>
		Map("" -> toJson(""))
	}
}
