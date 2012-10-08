package au.org.theark.core.model.report.entity;

import java.util.Set;

/**
 * 
 * The search object is used to save what criteria, name, etc is used in a search 
 * 
 * @author travis
 *
 */
public class Search {

	private Long id;
	private String name;
	private Set<CustomFieldDisplaySearch>  customFieldsToReturn;
	private Set<DemographicFieldSearch>  demographicFieldsToReturn;
	private Set<QueryFilter> queryFilters;
	

	public void setDemographicFieldsToReturn(Set<DemographicFieldSearch> demographicFieldsToReturn) {
		this.demographicFieldsToReturn = demographicFieldsToReturn;
	}
	public Set<DemographicFieldSearch> getDemographicFieldsToReturn() {
		return demographicFieldsToReturn;
	}
	public void setFieldsToReturn(Set<CustomFieldDisplaySearch> customFieldsToReturn) {
		this.customFieldsToReturn = customFieldsToReturn;
	}
	public Set<CustomFieldDisplaySearch> getCustomFieldsToReturn() {
		return customFieldsToReturn;
	}
	public void setQueryFilters(Set<QueryFilter> queryFilters) {
		this.queryFilters = queryFilters;
	}
	public Set<QueryFilter> getQueryFilters() {
		return queryFilters;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}
	
	
	
}
