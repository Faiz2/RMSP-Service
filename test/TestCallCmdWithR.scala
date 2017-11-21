import java.io.{InputStreamReader, LineNumberReader}

import org.scalatestplus.play.PlaySpec

class TestCallCmdWithR extends PlaySpec {
	"Test Call R" must {
		"Run Call R With Scala And Paly" in{
			try {
				val cmd = "Rscript /Users/qianpeng/Desktop/R/interface_test.R /Users/qianpeng/Desktop/R/test.json"
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
	}
}
