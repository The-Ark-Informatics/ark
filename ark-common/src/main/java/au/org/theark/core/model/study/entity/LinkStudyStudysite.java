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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import au.org.theark.core.Constants;

/**
 * LinkStudyStudysite entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "LINK_STUDY_STUDYSITE", schema = Constants.STUDY_SCHEMA)
public class LinkStudyStudysite implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long		id;
	private Study		study;
	private StudySite	studySite;

	// Constructors

	/** default constructor */
	public LinkStudyStudysite() {
	}

	/** minimal constructor */
	public LinkStudyStudysite(Long id) {
		this.id = id;
	}

	/** full constructor */
	public LinkStudyStudysite(Long id, Study study, StudySite studySite) {
		this.id = id;
		this.study = study;
		this.studySite = studySite;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return this.study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_SITE_ID")
	public StudySite getStudySite() {
		return this.studySite;
	}

	public void setStudySite(StudySite studySite) {
		this.studySite = studySite;
	}

}
