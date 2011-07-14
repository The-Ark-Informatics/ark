package au.org.theark.report.model.vo.report;

import java.io.Serializable;
import java.util.Date;

public class FieldDetailsDataRow implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected String collection;
	protected String fieldName;
	protected String description;
	protected String minValue;
	protected String maxValue;
	protected String encodedValues;
	protected String missingValue;
	protected String units;
	protected String type;
	
	public FieldDetailsDataRow() {
		
	}
	
	public FieldDetailsDataRow(String collection, String fieldName,
			String description, String minValue, String maxValue,
			String encodedValues, String missingValue, String units, String type) {
		super();
		this.collection = collection;
		this.fieldName = fieldName;
		this.description = description;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.encodedValues = encodedValues;
		this.missingValue = missingValue;
		this.units = units;
		this.type = type;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
