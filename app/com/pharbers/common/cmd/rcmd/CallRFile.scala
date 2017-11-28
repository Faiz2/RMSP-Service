package com.pharbers.common.cmd.rcmd

import com.pharbers.aqll.common.alCmd.alShellOtherCmdExce

case class CallRFile(rFilePath: String, rDataPath: String, jsonDataPath: String) extends alShellOtherCmdExce{
	override def cmd: String = s"Rscript $rFilePath $rDataPath $jsonDataPath"
}
