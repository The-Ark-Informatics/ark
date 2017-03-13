package au.org.theark.arkcalendar.data;

import java.io.Serializable;
import java.util.Date;

public class CalendarCustomFieldData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//SubjectCustomFieldData properties
	private Long id;
	private Long linkSubjectStudyId;
	private Long customFieldDisplayId;
	private String textDataValue;
	private Date dateDataValue;
	private Double numberDataValue;
	private String errorDataValue;
	private String unitType;
	
	//SubjectCustomFieldDisplay properties
	private Long customFieldId;
	private Long customFieldGroupId;
	private Boolean required;
	private String requiredMessage;
	private Boolean allowMultiselect = Boolean.FALSE;
	private Long sequence;
	private String descriptiveNameIncludingCFGName;
	
	//SubjectCustomField properties
	private String name;
	private Long customFieldCategoryId;
	private String description;
	private String fieldType;//Character,Number,Date,Look up.
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


	public Long getLinkSubjectStudyId() {
		return linkSubjectStudyId;
	}

	public void setLinkSubjectStudyId(Long linkSubjectStudyId) {
		this.linkSubjectStudyId = linkSubjectStudyId;
	}

	public Long getCustomFieldDisplayId() {
		return customFieldDisplayId;
	}

	public void setCustomFieldDisplayId(Long customFieldDisplayId) {
		this.customFieldDisplayId = customFieldDisplayId;
	}

	public String getTextDataValue() {
		return textDataValue;
	}

	public void setTextDataValue(String textDataValue) {
		this.textDataValue = textDataValue;
	}

	public Date getDateDataValue() {
		return dateDataValue;
	}

	public void setDateDataValue(Date dateDataValue) {
		this.dateDataValue = dateDataValue;
	}

	public Double getNumberDataValue() {
		return numberDataValue;
	}

	public void setNumberDataValue(Double numberDataValue) {
		this.numberDataValue = numberDataValue;
	}

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public String getErrorDataValue() {
		return errorDataValue;
	}

	public void setErrorDataValue(String errorDataValue) {
		this.errorDataValue = errorDataValue;
	}

	public Long getCustomFieldId() {
		return customFieldId;
	}

	public void setCustomFieldId(Long customFieldId) {
		this.customFieldId = customFieldId;
	}

	public Long getCustomFieldGroupId() {
		return customFieldGroupId;
	}

	public void setCustomFieldGroupId(Long customFieldGroupId) {
		this.customFieldGroupId = customFieldGroupId;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public String getRequiredMessage() {
		return requiredMessage;
	}

	public void setRequiredMessage(String requiredMessage) {
		this.requiredMessage = requiredMessage;
	}

	public Boolean getAllowMultiselect() {
		return allowMultiselect;
	}

	public void setAllowMultiselect(Boolean allowMultiselect) {
		this.allowMultiselect = allowMultiselect;
	}

	public Long getSequence() {
		return sequence;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	public String getDescriptiveNameIncludingCFGName() {
		return descriptiveNameIncludingCFGName;
	}

	public void setDescriptiveNameIncludingCFGName(String descriptiveNameIncludingCFGName) {
		this.descriptiveNameIncludingCFGName = descriptiveNameIncludingCFGName;
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
