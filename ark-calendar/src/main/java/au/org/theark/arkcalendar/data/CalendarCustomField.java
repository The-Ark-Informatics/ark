package au.org.theark.arkcalendar.data;

import java.io.Serializable;

public class CalendarCustomField implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private Long customFieldCategoryId;
	private String description;
	private String fieldType;//Charactor,Number,Date,Look up.
	private Long studyId;
	private Long unitTypeId;
	private String minValue;
	private String maxValue;
	private String encodedValues;
	private String missingValue;
	private Boolean customFieldHasData;
	private String fieldLabel;
	private String defaultValue;
	private String unitTypeInText;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getCustomFieldCategoryId() {
		return customFieldCategoryId;
	}
	public void setCustomFieldCategoryId(Long customFieldCategoryId) {
		this.customFieldCategoryId = customFieldCategoryId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public Long getStudyId() {
		return studyId;
	}
	public void setStudyId(Long studyId) {
		this.studyId = studyId;
	}
	public Long getUnitTypeId() {
		return unitTypeId;
	}
	public void setUnitTypeId(Long unitTypeId) {
		this.unitTypeId = unitTypeId;
	}
	public String getMinValue() {
		return minValue;
	}
	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}
	public String getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}
	public String getEncodedValues() {
		return encodedValues;
	}
	public void setEncodedValues(String encodedValues) {
		this.encodedValues = encodedValues;
	}
	public String getMissingValue() {
		return missingValue;
	}
	public void setMissingValue(String missingValue) {
		this.missingValue = missingValue;
	}
	public Boolean getCustomFieldHasData() {
		return customFieldHasData;
	}
	public void setCustomFieldHasData(Boolean customFieldHasData) {
		this.customFieldHasData = customFieldHasData;
	}
	public String getFieldLabel() {
		return fieldLabel;
	}
	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getUnitTypeInText() {
		return unitTypeInText;
	}
	public void setUnitTypeInText(String unitTypeInText) {
		this.unitTypeInText = unitTypeInText;
	}

	
	
}
