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
package au.org.theark.core.model.study.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;




/**
 * @author nivedann
 *
 */

@Entity
@Table(name = "CUSTOM_FIELD", schema = Constants.STUDY_SCHEMA)
public class CustomField implements Serializable {
	

	private Long id;
	private String	name;
	private String	description;
	private FieldType fieldType;
	private Study study;
	private ArkFunction arkFunction;
	private UnitType unitType;
	private String	minValue;
	private String	maxValue;
	private String	encodedValues;
	private String	missingValue;
	private Boolean customFieldHasData;
	private String fieldLabel;

	private Set<CustomFieldDisplay> customFieldDisplay = new HashSet<CustomFieldDisplay>();

	/**
	 * Constructor
	 */
	public CustomField(){
		
	}

	@Id
	@SequenceGenerator(name = "custom_field_seq_gen", sequenceName = "CUSTOM_FIELD_SEQ_GEN")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "custom_field_seq_gen")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FIELD_TYPE_ID", nullable = false)
	public FieldType getFieldType() {
		return fieldType;
	}

	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}

	@Column(name = "NAME", nullable = false, length = 255)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	@Column(name = "DESCRIPTION", length = 1024)
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "MIN_VALUE", length = 100)
	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	@Column(name = "MAX_VALUE", length = 100)
	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	@Column(name = "ENCODED_VALUES")
	public String getEncodedValues() {
		return encodedValues;
	}

	public void setEncodedValues(String encodedValues) {
		this.encodedValues = encodedValues;
	}

	@Column(name = "MISSING_VALUE")
	public String getMissingValue() {
		return missingValue;
	}

	public void setMissingValue(String missingValue) {
		this.missingValue = missingValue;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UNIT_TYPE_ID")
	public UnitType getUnitType() {
		return unitType;
	}

	public void setUnitType(UnitType unitType) {
		this.unitType = unitType;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ARK_FUNCTION_ID", nullable = false)
	public ArkFunction getArkFunction() {
		return arkFunction;
	}

	public void setArkFunction(ArkFunction arkFunction) {
		this.arkFunction = arkFunction;
	}

	@Column(name = "HAS_DATA", precision = 1, scale = 0)
	public Boolean getCustomFieldHasData() {
		return customFieldHasData;
	}

	public void setCustomFieldHasData(Boolean customFieldHasData) {
		this.customFieldHasData = customFieldHasData;
	}

	@Column(name = "CUSTOM_FIELD_LABEL",length=255)
	public String getFieldLabel() {
		return fieldLabel;
	}


	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}

	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customField")
	public Set<CustomFieldDisplay> getCustomFieldDisplay() {
		return customFieldDisplay;
	}

	public void setCustomFieldDisplay(Set<CustomFieldDisplay> customFieldDisplay) {
		this.customFieldDisplay = customFieldDisplay;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomField other = (CustomField) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	
}
