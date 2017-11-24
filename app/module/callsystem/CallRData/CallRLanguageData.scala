package module.callsystem.CallRData

import com.pharbers.common.cmd.rcmd.CallRFile

trait CallRLanguageData {

	def callR(): Boolean = {
		try {
			CallRFile("", "")
			true
		} catch {
			case ex: Exception => false
		}
	}
}
