package com.pharbers.common.cmd.rcmd

import com.pharbers.aqll.common.alCmd.alShellOtherCmdExce

case class CallRFile(rDataPath: String, jsonDataPath: String, reportPath: String) extends alShellOtherCmdExce{
	override def cmd: String = s"Rscript $rDataPath $jsonDataPath $reportPath"
}
