package au.org.theark.core.model.report.entity;

import au.org.theark.core.model.study.entity.FieldType;

public class DemographicField {
	//the alternative being to hit the mysql system tables somehow with a defined list of which fields in certain tables
	
	private Long id;
	private String entity;
	private String underlyingFieldName;
	//could potentially store table name and table field name too.  But first attempt will be at using hql and entities.
	private String publicFieldName;
	private FieldType fieldType;
	
	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}
	
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public String getEntity() {
		return entity;
	}
	
	public void setUnderlyingFieldName(String underlyingFieldName) {
		this.underlyingFieldName = underlyingFieldName;
	}
	public String getUnderlyingFieldName() {
		return underlyingFieldName;
	}
	
	public void setPublicFieldName(String publicFieldName) {
		this.publicFieldName = publicFieldName;
	}
	public String getPublicFieldName() {
		return publicFieldName;
	}
	
	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}
	public FieldType getFieldType() {
		return fieldType;
	}
	
}
