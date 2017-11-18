package module.brdinfo.BrdInfoData

trait BrdInfoData {

	def setBrdHtmlData(lst: List[String Map String]): play.twirl.api.HtmlFormat.Appendable = {
		views.html.Module.Brd.brd_content(lst)
	}
}
