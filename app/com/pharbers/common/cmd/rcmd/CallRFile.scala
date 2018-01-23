package com.pharbers.common.cmd.rcmd

import com.pharbers.aqll.common.alCmd.alShellOtherCmdExce

case class CallRFile(rfile : String, rDataPath: String, fileKey: String, reportPath: String) extends alShellOtherCmdExce{
	override def cmd: String = s"Rscript $rfile $rDataPath $fileKey $reportPath"
}
