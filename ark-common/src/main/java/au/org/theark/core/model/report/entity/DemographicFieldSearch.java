package au.org.theark.core.model.report.entity;

public class DemographicFieldSearch {
	private String entityName;
	private String fieldName;
	private Search search;
	
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setSearch(Search search) {
		this.search = search;
	}
	public Search getSearch() {
		return search;
	}
}
