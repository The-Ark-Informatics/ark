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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.Constants;

/**
 * LinkSubjectStudycomp entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "LINK_SUBJECT_STUDYCOMP", schema = Constants.STUDY_SCHEMA)
public class LinkSubjectStudycomp implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long				id;
	private StudyComp			studyComp;
	private Study				study;
	private Person				person;
	private StudyCompStatus	studyComponentStatus;

	// Constructors

	/** default constructor */
	public LinkSubjectStudycomp() {
	}

	/** minimal constructor */
	public LinkSubjectStudycomp(Long id) {
		this.id = id;
	}

	/** full constructor */
	public LinkSubjectStudycomp(Long id, StudyComp studyComp, Study study, Person person, StudyCompStatus studyComponentStatus) {
		this.id = id;
		this.studyComp = studyComp;
		this.study = study;
		this.person = person;
		this.studyComponentStatus = studyComponentStatus;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name = "subject_component_generator", sequenceName = "SUBJECT_COMPONENT_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "subject_component_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
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
	@JoinColumn(name = "PERSON_SUBJECT_ID")
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_COMP_STATUS_ID")
	public StudyCompStatus getStudyComponentStatus() {
		return studyComponentStatus;
	}

	public void setStudyComponentStatus(StudyCompStatus studyComponentStatus) {
		this.studyComponentStatus = studyComponentStatus;
	}

}
