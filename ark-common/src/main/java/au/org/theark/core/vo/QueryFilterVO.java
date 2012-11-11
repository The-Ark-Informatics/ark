package au.org.theark.core.vo;

import au.org.theark.core.model.report.entity.FieldCategory;
import au.org.theark.core.model.report.entity.QueryFilter;

public class QueryFilterVO {

	private FieldCategory fieldCategory;
	private QueryFilter queryFilter;
	
	public FieldCategory getFieldCategory() {
		return fieldCategory;
	}
	public void setFieldCategory(FieldCategory fieldCategory) {
		this.fieldCategory = fieldCategory;
	}
	public QueryFilter getQueryFilter() {
		return queryFilter;
	}
	public void setQueryFilter(QueryFilter queryFilter) {
		this.queryFilter = queryFilter;
	}
	
}
