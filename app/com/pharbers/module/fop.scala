package com.pharbers.module

import java.io.{File, FileInputStream}

import com.pharbers.common.RConfig

object fop {
	def downloadFile(name: String) : Array[Byte] = {
		val f = (new File("")).getAbsoluteFile
		println(f)
		val config = new RConfig()
		val filepath = config.report_download_path
		val reValPath = f+"/"+filepath+name
		val file = new File(reValPath)
		val reVal : Array[Byte] = new Array[Byte](file.length.intValue)
		new FileInputStream(file).read(reVal)
		reVal
	}
}
