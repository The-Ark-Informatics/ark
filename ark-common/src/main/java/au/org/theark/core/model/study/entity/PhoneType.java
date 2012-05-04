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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import au.org.theark.core.Constants;

/**
 * PhoneType entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PHONE_TYPE", schema = Constants.STUDY_SCHEMA, uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
public class PhoneType implements java.io.Serializable {

	// Fields


	private static final long serialVersionUID = 1L;
	private Long			id;
	private String			name;
	private String			description;
	private Set<Phone>	phones	= new HashSet<Phone>(0);

	// Constructors

	/** default constructor */
	public PhoneType() {
	}

	/** minimal constructor */
	public PhoneType(Long id) {
		this.id = id;
	}

	/** full constructor */
	public PhoneType(Long id, String name, String description, Set<Phone> phones) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.phones = phones;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", unique = true, nullable = false, length = 20)
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "phoneType")
	public Set<Phone> getPhones() {
		return this.phones;
	}

	public void setPhones(Set<Phone> phones) {
		this.phones = phones;
	}

}
