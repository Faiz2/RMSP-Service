package module.decision.DecisionData

trait DecisionData {
	
	
	def setSumPromotionBudget(str: String, cycle: String): play.twirl.api.HtmlFormat.Appendable = {
		views.html.Module.Decision.BusinessDecision.sum_promotion_budget(str, cycle)
	}
	
	def setHospitalTab(hospital: List[(String, List[String Map String])] , people: List[String Map String], cycle: String): play.twirl.api.HtmlFormat.Appendable = {
		views.html.Module.Decision.BusinessDecision.hospital_tab(hospital, people, cycle)
	}
}
