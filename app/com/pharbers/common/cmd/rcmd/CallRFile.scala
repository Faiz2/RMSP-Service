package com.pharbers.common.cmd.rcmd

import com.pharbers.aqll.common.alCmd.alShellOtherCmdExce

case class CallRFile(rDataPath: String, jsonDataPath: String) extends alShellOtherCmdExce{
	override def cmd: String = s"Rscript /Users/qianpeng/Desktop/R/new/stp_handler.R $rDataPath $jsonDataPath"
}
