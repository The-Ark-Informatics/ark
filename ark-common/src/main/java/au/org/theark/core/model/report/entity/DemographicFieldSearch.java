package au.org.theark.core.model.report.entity;

public class DemographicFieldSearch {

	private DemographicFieldSearch demographicField;
	private Search search;
	
	public void setSearch(Search search) {
		this.search = search;
	}
	public Search getSearch() {
		return search;
	}
	public void setDemographicField(DemographicFieldSearch demographicField) {
		this.demographicField = demographicField;
	}
	public DemographicFieldSearch getDemographicField() {
		return demographicField;
	}
}
