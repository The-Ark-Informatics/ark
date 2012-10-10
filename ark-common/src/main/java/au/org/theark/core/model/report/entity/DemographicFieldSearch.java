package au.org.theark.core.model.report.entity;

public class DemographicFieldSearch {

	private DemographicField demographicField;
	private Search search;
	
	public void setSearch(Search search) {
		this.search = search;
	}
	public Search getSearch() {
		return search;
	}
	
	public void setDemographicField(DemographicField demographicField) {
		this.demographicField = demographicField;
	}
	public DemographicField getDemographicField() {
		return demographicField;
	}
}
