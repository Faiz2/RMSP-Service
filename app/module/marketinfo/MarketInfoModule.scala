package module.marketinfo

import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import play.api.libs.json.JsValue
import play.api.libs.json.Json._
import alMarketInfoMessage._
import com.pharbers.ErrorCode
import module.marketinfo.MarketInfoData._

object MarketInfoModule extends ModuleTrait with MarketInfoData {
    def dispatchMsg(msg: MessageDefines)
                   (pr: Option[String Map JsValue])
                   (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = msg match {
        case alOutMarketInfoExcelValueWithHtml(data) => outExcelValueWithHtml(data)(pr)
        case _ => throw new Exception("function is not impl")
    }
    
    def outExcelValueWithHtml(data: JsValue)
                             (pr: Option[String Map JsValue])
                             (implicit cm: CommonModules): (Option[String Map JsValue], Option[JsValue]) = {
        
        try {
            val cycle = (data \ "condition" \ "cycle").asOpt[String].getOrElse("周期1")
            val reValHostitalData = pr.getOrElse(throw new Exception("pr data not exist"))("data").
                as[List[String Map List[String Map String]]].map(x => x.get("news")).
                filterNot(f => f.isEmpty).flatMap(x => x.get).map(x => Map("letters" -> x(cycle),
                "hospname" -> x("医院"),
                "periodsales" -> x("上期总销售额(元)"),
                "口服抗生素" -> x("口服抗生素"),
                "一代降糖药" -> x("一代降糖药"),
                "三代降糖药" -> x("三代降糖药"),
                "皮肤药" -> x("皮肤药")
            
            ))
            val tmp = pr.getOrElse(throw new Exception("pr data not exist"))("data").
                as[List[String Map List[String Map String]]].map(x => x.get("hospital")).
                filterNot { f =>
                    f.isEmpty
                }.flatMap { x =>
                x.get
            }.map { x =>
                 x("名称")
            }.distinct
            val reValClientInfoData = pr.getOrElse(throw new Exception("pr data not exist"))("data").
                as[List[String Map List[String Map String]]].map(x => x.get("hospital")).
                filterNot(f => f.isEmpty).flatMap(x => x.get).
                map{x =>
                    Map("hospname" -> x("名称"), x("产品") -> x(cycle), "area" -> x("区域"), "type" -> x("类型"))}.
                groupBy { g =>
                    val host_name = g("hospname").distinct
                    host_name
                }
            var m : Map[String,List[Map[String,String]]]= Map.empty
            var i = 1
            for(t <- tmp){
//                println(i+t)
                val info = reValClientInfoData(t)
                m = m ++ Map(i+"" -> info)
                i = i + 1
//                println(m)
            }

            val rt = m.toList.sortBy(p => p._1).map (x => x._2).map { lst =>
                var v = Map[String, JsValue]()
                lst.map { iter =>
                    v += ("hospname" -> toJson(iter.get("hospname").get))
                    v += ("area" -> toJson(iter.get("area").get))
                    v += ("type" -> toJson(iter.get("type").get))
                    iter.get("口服抗生素").map { x =>
                        v += "口服抗生素" -> toJson(x)
                    }.getOrElse(Unit)
                    iter.get("一代降糖药").map { x =>
                        v += "一代降糖药" -> toJson(x)
                    }.getOrElse(Unit)
                    iter.get("一代降糖药").map { x =>
                        v += "一代降糖药" -> toJson(x)
                    }.getOrElse(Unit)
                    iter.get("皮肤药").map { x =>
                        v += "皮肤药" -> toJson(x)
                    }.getOrElse(Unit)
                }
                toJson(v)
            }

//            val condition = Map("news" -> toJson(setHospitalHtmlData(reValHostitalData).toString), "clientInfo" -> toJson(setClientInfoHtmlData(reValClientInfoData).toString))
//            val condition = Map("news" -> toJson(setHospitalHtmlData(reValHostitalData).toString), "clientInfo" -> toJson(setClientInfoHtmlData(m).toString))
            val condition = Map("news" -> toJson(reValHostitalData), "clientInfo" -> toJson(rt))

            (Some(Map("data" -> toJson(condition))), None)
        } catch {
            case ex: Exception =>
                println(ex)
                (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }
}
