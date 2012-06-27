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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.Constants;

@Entity
@Table(name = "ARK_FUNCTION", schema = Constants.STUDY_SCHEMA)
public class ArkFunction implements Serializable {


	private static final long serialVersionUID = 1L;
	private Long				id;
	private String				name;
	private String				description;
	private String				resourceKey;
	private ArkFunctionType		arkFunctionType;
//	private ArkModuleFunction	arkModuleFunction;

	@Id
	@SequenceGenerator(name = "ark_function_generator", sequenceName = "ARK_FUNCTION_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "ark_function_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "RESOURCE_KEY")
	public String getResourceKey() {
		return resourceKey;
	}

	public void setResourceKey(String resourceKey) {
		this.resourceKey = resourceKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ARK_FUNCTION_TYPE_ID")
	public ArkFunctionType getArkFunctionType() {
		return arkFunctionType;
	}

	public void setArkFunctionType(ArkFunctionType arkFunctionType) {
		this.arkFunctionType = arkFunctionType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((arkFunctionType == null) ? 0 : arkFunctionType.hashCode());
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
		ArkFunction other = (ArkFunction) obj;
		if (arkFunctionType == null) {
			if (other.arkFunctionType != null)
				return false;
		} else if (!arkFunctionType.equals(other.arkFunctionType))
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

/*
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "arkFunction")
	public void setArkModuleFunction(ArkModuleFunction arkModuleFunction) {
		this.arkModuleFunction = arkModuleFunction;
	}

	public ArkModuleFunction getArkModuleFunction() {
		return arkModuleFunction;
	}
*/
}
