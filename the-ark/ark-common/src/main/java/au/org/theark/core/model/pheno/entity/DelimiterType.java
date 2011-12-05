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
package au.org.theark.core.model.pheno.entity;

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
 * DelimiterType entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity(name = "au.org.theark.core.model.pheno.entity.DelimiterType")
@Table(name = "DELIMITER_TYPE", schema = Constants.PHENO_TABLE_SCHEMA)
public class DelimiterType implements java.io.Serializable {

	// Fields

	private Long					id;
	private String					name;
	private char					delimiterCharacter;
	private Set<PhenoUpload>	collectionUploads	= new HashSet<PhenoUpload>(0);

	// Constructors

	/** default constructor */
	public DelimiterType() {
	}

	/** minimal constructor */
	public DelimiterType(Long id) {
		this.id = id;
	}

	/** full constructor */
	public DelimiterType(Long id, String name, Set<PhenoUpload> collectionUploads) {
		this.id = id;
		this.name = name;
		this.collectionUploads = collectionUploads;
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

	@Column(name = "DELIMITER_CHARACTER")
	public char getDelimiterCharacter() {
		return delimiterCharacter;
	}

	public void setDelimiterCharacter(char delimiterCharacter) {
		this.delimiterCharacter = delimiterCharacter;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "delimiterType")
	public Set<PhenoUpload> getCollectionImports() {
		return this.collectionUploads;
	}

	public void setCollectionImports(Set<PhenoUpload> collectionUploads) {
		this.collectionUploads = collectionUploads;
	}
}
