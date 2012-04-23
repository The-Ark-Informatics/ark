/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.report.model.vo.report;

import java.io.Serializable;

public class CustomFieldDetailsDataRow implements Serializable {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	protected String				questionnaire;
	protected String				fieldName;
	protected String				description;
	protected String				minValue;
	protected String				maxValue;
	protected String				encodedValues;
	protected String				missingValue;
	protected String				units;
	protected String				type;

	public CustomFieldDetailsDataRow() {

	}

	public CustomFieldDetailsDataRow(String questionnaire, String fieldName, String description, String minValue, String maxValue, String encodedValues, String missingValue, String units, String type) {
		super();
		this.questionnaire = questionnaire;
		this.fieldName = fieldName;
		this.description = description;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.encodedValues = encodedValues;
		this.missingValue = missingValue;
		this.units = units;
		this.type = type;
	}

	public String getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(String questionnaire) {
		this.questionnaire = questionnaire;
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
