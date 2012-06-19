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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;

/**
 * @author nivedann
 *
 */
@Entity
@Table(name = "UNIT_TYPE", schema = Constants.STUDY_SCHEMA)
public class UnitType implements Serializable{
	

	private static final long serialVersionUID = 1L;
	private Long		id;
	private ArkFunction	arkFunction;
	private String		name;
	private String		description;
	private MeasurementType measurementType;
	private Long		displayOrder;
	
	public UnitType(){
		arkFunction = new ArkFunction();
	}
	
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ARK_FUNCTION_ID")
	public ArkFunction getArkFunction() {
		return arkFunction;
	}

	public void setArkFunction(ArkFunction arkFunction) {
		this.arkFunction = arkFunction;
	}
	
	@Column(name = "NAME", length = 45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION", length=255)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEASUREMENT_TYPE_ID")
	public MeasurementType getMeasurementType() {
		return this.measurementType;
	}

	public void setMeasurementType(MeasurementType measurementType) {
		this.measurementType = measurementType;
	}
	
	@Column(name = "DISPLAY_ORDER")
	public Long getDisplayOrder() {
		return this.displayOrder;
	}

	public void setDisplayOrder(Long displayOrder) {
		this.displayOrder = displayOrder;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((arkFunction == null) ? 0 : arkFunction.hashCode());
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
		UnitType other = (UnitType) obj;
		if (arkFunction == null) {
			if (other.arkFunction != null)
				return false;
		} else if (!arkFunction.equals(other.arkFunction))
			return false;
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
