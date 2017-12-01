package module.report.ReportData

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json._

trait ReportData {

	def conditionAllot(data: JsValue): DBObject  = {
		val builder = MongoDBObject.newBuilder
		(data \ "condition" \ "").asOpt[String].map(x => builder += "" -> x).getOrElse(Unit)
		builder.result
	}

	val d2mAllot: DBObject => String Map JsValue = { obj =>

		Map("" -> toJson(""))
	}

	def conditionSales(data: JsValue): DBObject = {
		val builder = MongoDBObject.newBuilder
		(data \ "condition" \ "").asOpt[String].map(x => builder += "" -> x).getOrElse(Unit)
		builder.result
	}

	val d2mSales: DBObject => String Map JsValue = { obj =>

		Map("" -> toJson(""))
	}


	val d2mMarketSales: DBObject => String Map JsValue = { obj =>
		val reValLst = obj.asInstanceOf[MongoDBList]
		println(reValLst)
		Map("" -> toJson(""))
	}

	val d2mDelegate: DBObject => String Map JsValue = { obj =>
		Map("" -> toJson(""))
	}

	val d2mManager: DBObject => String Map JsValue = { obj =>
		Map("" -> toJson(""))
	}
}
