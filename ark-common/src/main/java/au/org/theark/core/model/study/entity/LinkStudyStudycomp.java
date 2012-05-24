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
 * LinkStudyStudycomp entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "LINK_STUDY_STUDYCOMP", schema = Constants.STUDY_SCHEMA)
public class LinkStudyStudycomp implements java.io.Serializable {




	private static final long serialVersionUID = 1L;
	private Long				id;
	private StudyComp			studyComp;
	private Study				study;
	private StudyCompStatus	studyCompStatus;


	public LinkStudyStudycomp() {
	}

	public LinkStudyStudycomp(Long id) {
		this.id = id;
	}

	public LinkStudyStudycomp(Long id, StudyComp studyComp, Study study, StudyCompStatus studyCompStatus) {
		this.id = id;
		this.studyComp = studyComp;
		this.study = study;
		this.studyCompStatus = studyCompStatus;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getid() {
		return this.id;
	}

	public void setid(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_COMP_ID")
	public StudyComp getStudyComp() {
		return this.studyComp;
	}

	public void setStudyComp(StudyComp studyComp) {
		this.studyComp = studyComp;
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
	@JoinColumn(name = "STUDY_COMP_STATUS_ID")
	public StudyCompStatus getStudyCompStatus() {
		return this.studyCompStatus;
	}

	public void setStudyCompStatus(StudyCompStatus studyCompStatus) {
		this.studyCompStatus = studyCompStatus;
	}

}
