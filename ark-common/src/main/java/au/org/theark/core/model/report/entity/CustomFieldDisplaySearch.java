package au.org.theark.core.model.report.entity;

import au.org.theark.core.model.study.entity.CustomFieldDisplay;

public class CustomFieldDisplaySearch {
	private Long id;
	private CustomFieldDisplay customFieldDisplay;
	private Search search;
	
	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}
	
	public void setCustomFieldDisplay(CustomFieldDisplay customFieldDisplay) {
		this.customFieldDisplay = customFieldDisplay;
	}
	public CustomFieldDisplay getCustomFieldDisplay() {
		return customFieldDisplay;
	}
	
	public void setSearch(Search search) {
		this.search = search;
	}
	public Search getSearch() {
		return search;
	}
	
}
