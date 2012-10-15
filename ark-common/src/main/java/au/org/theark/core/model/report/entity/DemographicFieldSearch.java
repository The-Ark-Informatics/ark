package au.org.theark.core.model.report.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;

@Entity
@Table(name = "demographic_field_search", schema = Constants.REPORT_SCHEMA)
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
