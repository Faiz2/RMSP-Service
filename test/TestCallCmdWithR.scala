import java.io.{File, InputStreamReader, LineNumberReader, PrintWriter}

import play.api.libs.json.Json._
import org.scalatestplus.play.PlaySpec

class TestCallCmdWithR extends PlaySpec {
	
	
	
//	"Test Call R" must {
//		"Run Call R With Scala And Paly" in{
//
//		}
//	}
	
	
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
	
	def testWithWriteJson(): Unit = {
		try {
			val json = toJson(Map("name" -> "钱鹏", "age" -> "22", "gender" -> "Man")).toString
			val w = new PrintWriter(new File("/Users/qianpeng/Desktop/t.json"))
			w.write(json)
			w.close()
			println(json)
		} catch {
			case e: Exception =>
				println(e.getMessage)
		}
	}

}
