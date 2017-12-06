package com.pharbers.module

import java.io.{File, FileInputStream}

object fop {
	def downloadFile(name: String) : Array[Byte] = {
		val reValPath = s"resource/report/$name"
		val file = new File(reValPath)
		val reVal : Array[Byte] = new Array[Byte](file.length.intValue)
		new FileInputStream(file).read(reVal)
		reVal
	}
}
