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
package au.org.theark.core.model.geno.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;

/**
 * MarkerType entity. @author MyEclipse Persistence Tools
 */
@Entity(name = "au.org.theark.geno.model.entity.MarkerType")
@Table(name = "MARKER_TYPE", schema = Constants.GENO_TABLE_SCHEMA)
public class MarkerType implements java.io.Serializable {




	private static final long serialVersionUID = 1L;
	private Long					id;
	private String					name;
	private Set<MarkerGroup>	markerGroups	= new HashSet<MarkerGroup>(0);


	public MarkerType() {
	}

	public MarkerType(Long id) {
		this.id = id;
	}

	public MarkerType(Long id, String name, Set<MarkerGroup> markerGroups) {
		this.id = id;
		this.name = name;
		this.markerGroups = markerGroups;
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

	@Column(name = "NAME", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "markerType")
	public Set<MarkerGroup> getMarkerGroups() {
		return this.markerGroups;
	}

	public void setMarkerGroups(Set<MarkerGroup> markerGroups) {
		this.markerGroups = markerGroups;
	}

}
