package module.marketinfo.MarketInfoData

trait MarketInfoData {
	
	def setHospitalHtmlData(lst: List[String Map String]): play.twirl.api.HtmlFormat.Appendable = {
		views.html.Module.MarketInfo.market_info_news_content(lst)
	}
	
	def setClientInfoHtmlData(map: String Map List[String Map String]): play.twirl.api.HtmlFormat.Appendable = {
		views.html.Module.MarketInfo.market_info_client_info_content(map)
	}
}
