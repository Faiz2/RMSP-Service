import java.io.{File, InputStreamReader, LineNumberReader, PrintWriter}

import module.writejson.WriteJsonData.WriteJsonData
import play.api.libs.json.Json._
import org.specs2.mutable.Specification

class TestCallCmdWithR extends Specification with WriteJsonData{
	
	
	
//	"Test Call R" must {
//		"Run Call R With Scala And Paly" in{
//
//		}
//	}

	override def is =
		s2"""
        This is a RMSP specification to check the 'conditionSearch' string

            The 'RMSP ' Test functions should
                testWithWriteJson result must be "true"!                                                       ${testWithWriteJson()}

			|This is a RMSP specification to check the 'Test' string
			|The 'PIC ' Test functions should
		  """
	
	def testCallR(): Unit = {
		try {
			val cmd = "Rscript /Users/qianpeng/Desktop/stp_handler.R /Users/qianpeng/Desktop/pre_data_linux.RData /Users/qianpeng/Desktop/test.json"
			println(s"cmd=$cmd")
			
			val process = new ProcessBuilder("/bin/bash", "-c", cmd).start()
			val input = new LineNumberReader(new InputStreamReader(process.getInputStream()))
			var line,result: String = ""
			process.waitFor()
			do {
				line = input.readLine()
				if(line != null) result = line
			} while (line != null)
			println(result)
		} catch {
			case e : Exception =>
				println(e.getMessage)
		}
	}
	
	def testWithWriteJson() = {
//		try {
//			val json = toJson(Map("name" -> "钱鹏", "age" -> "22", "gender" -> "Man")).toString
//			val w = new PrintWriter(new File("/Users/qianpeng/Desktop/t.json"))
//			w.write(json)
//			w.close()
//			println(json)
//			val result = "ok"
//			result must_== "ok"
//		} catch {
//			case e: Exception =>
//				println(e.getMessage)
//				val result = "no"
//				result must_== "ok"
//		}
		implicit val func = HandleImpl.j2s
		val json = toJson(Map("name" -> "钱鹏", "age" -> "22", "gender" -> "Man"))
		val r = wirteJson(json)
		r._1 must_== true
	}

}
