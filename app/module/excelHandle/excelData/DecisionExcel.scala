package module.excelHandle.excelData

import com.pharbers.common.Book
import com.pharbers.common.excel2D.Excel2D
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by yym on 12/27/17.
  */
trait DecisionExcel {
    def decision_excel_read(file_path : String ,sheet_name : String , period :String, hospital_num : Int):  Map[String, JsValue] ={
        try{
            val n= 9*(hospital_num-1)
            val book = Book(file_path)
            val sheet = book.sheet(sheet_name)
            val excel2D= new Excel2D(sheet)
            var res : Map[String, JsValue] = Map()
            val name = sheet.string("A"+(n+1))
            res = res ++ Map(s"hospital_$hospital_num" -> toJson(name))
            val  potential_sales = excel2D.read_row(n+2, 'B', 'E')(excel2D.parser.useIntOpt)
            potential_sales.foreach{x => x._2.asInstanceOf[Option[Int]] match {
                case Some(v) =>
                    val hosp = hospital_num
                    val tmp = x._1.split("_")(0).map(_.toByte.asInstanceOf[Int])
                    val num = tmp.head - 65
                    val k = s"p$period"+s"_potential_sales_hosp$hosp"+s"_$num"
                    res = res ++ Map(k -> toJson(v.toString))
                case None => ???
            }
            }
            val  promotional_budget = excel2D.read_row(n+3, 'B', 'B')(excel2D.parser.useIntOpt)
            promotional_budget.foreach{x => x._2.asInstanceOf[Option[Int]] match {
                case Some(v) =>
                    val hosp = hospital_num
                    val k = s"p$period"+s"_promotional_budget_hosp$hosp"
                    res = res ++ Map(k -> toJson(v.toString))
                case None => ???
            }
            }
            val  current_sales = excel2D.read_row(n+4, 'B', 'E')(excel2D.parser.useIntOpt)
            current_sales.foreach{x => x._2.asInstanceOf[Option[Int]] match {
                case Some(v) =>
                    val hosp = hospital_num
                    val tmp = x._1.split("_")(0).map(_.toByte.asInstanceOf[Int])
                    val num = tmp.head - 65
                    val k = s"p$period"+s"_current_sales_hosp$hosp"+s"_$num"
                    res = res ++ Map(k -> toJson(v.toString))
                case None => ???
            }
            }
            val  sales_target = excel2D.read_row(n+5, 'B', 'E')(excel2D.parser.useIntOpt)
            sales_target.foreach{x => x._2.asInstanceOf[Option[Int]] match {
                case Some(v) =>
                    val hosp = hospital_num
                    val tmp = x._1.split("_")(0).map(_.toByte.asInstanceOf[Int])
                    val num = tmp.head - 65
                    val k = s"p$period"+s"_hosp$hosp"+s"_sales_target"+s"_$num"
                    res = res ++ Map(k -> toJson(v.toString))
                case None => ???
            }
            }
            val  sr = sheet.string("B"+(n+6))
            if(sr!=""){
                val hosp = hospital_num
                val k = s"p$period"+s"_sr_hosp$hosp"
                res = res ++ Map(k -> toJson(sr))
            }
            val  worktime = excel2D.read_row(n+7, 'B', 'E')(excel2D.parser.useIntOpt)
            worktime.foreach{x => x._2.asInstanceOf[Option[Int]] match {
                case Some(v) =>
                    val hosp = hospital_num
                    val tmp = x._1.split("_")(0).map(_.toByte.asInstanceOf[Int])
                    val num = tmp.head - 65
                    val k = s"p$period"+s"_hosp$hosp"+s"_worktime"+s"_$num"
                    res = res ++ Map(k -> toJson(v.toString))
                case None => ???
                }
            }
            res
        }catch {
            case ex : Exception =>
                println(ex)
                throw new Exception("decision excel have wrong style")
        }
    }
}
