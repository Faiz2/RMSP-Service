package com.pharbers.common

import java.io.File

import scala.xml.XML

/**
  * Created by yym on 1/3/18.
  */
class RConfig {
    val file = (new File("")).getAbsolutePath
    val  config_path = file+"/pharbers_config/r_config.xml"
    lazy val xml_file = XML.loadFile(config_path)
    def rfile() : String = {
        println(config_path)
        val path = (xml_file \ "r-path" \ "rfile" \ "@value").text
        path
    }
    
    def rdata() : String = {
        println(config_path)
        val path = (xml_file \ "r-path" \ "rdata" \ "@value").text
        path
    }
    
    def report_path : String = {
        println(config_path)
        val path = (xml_file \ "excel-path" \ "report" \"@value").text
        path
    }
    def report_download_path : String = {
        println(config_path)
        val path = (xml_file \ "excel-path" \ "report_download" \"@value").text
        path
    }
    
    def pre_info_new : String = {
        println(config_path)
        val path = (xml_file \  "excel-path" \ "pre_info_new" \"@value").text
        path
    }
}
