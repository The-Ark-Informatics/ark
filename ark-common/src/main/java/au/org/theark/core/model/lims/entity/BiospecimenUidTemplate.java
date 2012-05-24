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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;
import au.org.theark.core.model.study.entity.Study;

/**
 * 
 * @author cellis
 * 
 */
@Entity
@Table(name = "BIOSPECIMENUID_TEMPLATE", schema = Constants.LIMS_TABLE_SCHEMA)
public class BiospecimenUidTemplate implements java.io.Serializable {


	private static final long		serialVersionUID	= -8619498549995379154L;

	private Long						id;
	private Study						study;
	private BiospecimenUidToken	biospecimenUidToken;
	private String						biospecimenUidPrefix;
	private BiospecimenUidPadChar	biospecimenUidPadChar;

	// Constructors
	/** default constructor */
	public BiospecimenUidTemplate() {
	}

	// Property accessors
	@Id
	@SequenceGenerator(name = "biospecimenUid_template", sequenceName = "biospecimenUid_template_sequence")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "biospecimenUid_template")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return this.study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	@Column(name = "BIOSPECIMENUID_PREFIX")
	public String getBiospecimenUidPrefix() {
		return biospecimenUidPrefix;
	}

	public void setBiospecimenUidPrefix(String biospecimenUidPrefix) {
		this.biospecimenUidPrefix = biospecimenUidPrefix;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BIOSPECIMENUID_TOKEN_ID")
	public BiospecimenUidToken getBiospecimenUidToken() {
		return biospecimenUidToken;
	}

	public void setBiospecimenUidToken(BiospecimenUidToken biospecimenUidToken) {
		this.biospecimenUidToken = biospecimenUidToken;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BIOSPECIMENUID_PADCHAR_ID")
	public BiospecimenUidPadChar getBiospecimenUidPadChar() {
		return biospecimenUidPadChar;
	}

	public void setBiospecimenUidPadChar(BiospecimenUidPadChar biospecimenUidPadChar) {
		this.biospecimenUidPadChar = biospecimenUidPadChar;
	}
}
