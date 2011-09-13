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
package au.org.theark.core.model.lims.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;

/**
 * Unit entity
 */
@Entity
@Table(name = "unit", schema = Constants.LIMS_TABLE_SCHEMA)
public class Unit implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8076302336716748287L;
	private Long					id;
	private String					name;
	private String					description;

	public Unit() {
	}

	public Unit(Long id) {
		this.id = id;
	}

	public Unit(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Unit(Long id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	@Id
	@SequenceGenerator(name = "unit_generator", sequenceName = "UNIT_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "unit_generator")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
