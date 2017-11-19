package module.productinfo.ProductInfoData

trait ProductInfoData {

	def setProductInfoData(lst: List[String Map String]): play.twirl.api.HtmlFormat.Appendable = {
		views.html.Module.Product.product_content(lst)
	}

}
