package module.rsinformation.RsInformationData

import play.api.libs.json.JsValue
import com.mongodb.casbah.Imports._
import play.api.libs.json.Json.toJson
import java.text.NumberFormat
import java.util.UUID

import com.pharbers.panel.util.excel.phHandleExcel

trait RsInformationData {
	
	def thousandsConvert(str: Option[String]): String =
		str.map(x => if (x.toString.isEmpty) "" else NumberFormat.getInstance().format(x.toDouble)).getOrElse("")
	
	implicit val m2d: JsValue => DBObject = { jv =>
		val builder = MongoDBObject.newBuilder
		(jv \ "condition" \ "uuid").asOpt[String].map(x => builder += "uuid" -> x)
			.getOrElse(throw new Exception("wrong input"))
		(jv \ "condition" \ "phase").asOpt[Int].map(x => builder += "phase" -> x)
			.getOrElse(throw new Exception("wrong input"))
		builder.result
	}
	
	def hospitalConvertMap(o: DBObject): String Map JsValue = {
		Map("hospitalName" -> toJson(o.getAs[String]("hosp_name")),
			"hospitalCode" -> toJson(o.getAs[String]("hosp_code")),
			"hospitalArea" -> toJson(o.getAs[String]("hosp_area")),
			"hospitalCat" -> toJson(o.getAs[String]("hosp_cat"))
		)
	}
	
	def phraseConvertMap(o: DBObject): String Map JsValue = {
		val values = o.getAs[DBObject]("value").get
		Map("hospitalCode" -> toJson(o.getAs[DBObject]("_id").get.getAs[String]("hc")),
			"oralAntibiotics" -> toJson(thousandsConvert(values.getAs[String]("1"))),
			"aHypoglycemicAgent" -> toJson(thousandsConvert(values.getAs[String]("2"))),
			"threeHypoglycemicAgent" -> toJson(thousandsConvert(values.getAs[String]("3"))),
			"skinMedicine" -> toJson(thousandsConvert(values.getAs[String]("4")))
		)
	}
	
	def previousResultConvertMap(o: DBObject): String Map JsValue = {
		val result = o.getAs[MongoDBList]("pre_result").get.toList.asInstanceOf[List[BasicDBObject]]
		
		val preresult = result.map { x =>
			val num = x.getAs[Number]("stage").get.intValue()
			"phrase_"+num -> toJson(x.getAs[String]("phrase_"+num))
		}.toMap
		
		Map("hospitalCode" ->  toJson(o.getAs[String]("hosp_code")),
			"phrase_1" -> preresult("phrase_1"),
			"phrase_2" -> preresult("phrase_2"),
			"phrase_3" -> preresult("phrase_3"),
			"phrase_4" -> preresult("phrase_4")
		)
	}
	
	def generateFile(data: JsValue, phase: Int): String = {
		val file =  UUID.randomUUID().toString + ".xlsx"
		val path = "./files/" + file
		val describe = (data \ "describe").as[List[String Map String]]
		val phrase = (data \ "phrase").as[List[String Map String]]
		val hospitals = (data \ "hospitals").as[List[String Map String]]
		val content = hospitals.map { x =>
			Map("hospitalName" -> x("hospitalName"),
				"survey" ->  describe.find(f =>  f("hospitalCode") == x("hospitalCode")).get("phrase_" + phase),
				"oralAntibiotics" -> phrase.find(f =>  f("hospitalCode") == x("hospitalCode")).get("oralAntibiotics"),
				"aHypoglycemicAgent" -> phrase.find(f =>  f("hospitalCode") == x("hospitalCode")).get("aHypoglycemicAgent"),
				"threeHypoglycemicAgent" -> phrase.find(f =>  f("hospitalCode") == x("hospitalCode")).get("threeHypoglycemicAgent"),
				"hospitalCode" -> phrase.find(f =>  f("hospitalCode") == x("hospitalCode")).get("hospitalCode")
			)
		}
		phHandleExcel().writeByList(content, path)
		file
	}
}