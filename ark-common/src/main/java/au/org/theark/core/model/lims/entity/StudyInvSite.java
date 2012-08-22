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
 * StudyInvSite entity
 */
@Entity
@Table(name = "study_inv_site", schema = Constants.LIMS_TABLE_SCHEMA)
public class StudyInvSite implements java.io.Serializable {

	private static final long	serialVersionUID	= 8076302336716748287L;
	private Long					id;
	private Study					study;
	private InvSite				invSite;

	public StudyInvSite() {
	}

	@Id
	@SequenceGenerator(name = "study_inv_site_generator", sequenceName = "STUDY_INV_SITE_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "study_inv_site_generator")
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the study
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return study;
	}

	/**
	 * @param study the study to set
	 */
	public void setStudy(Study study) {
		this.study = study;
	}

	/**
	 * @return the invSite
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INV_SITE_ID")
	public InvSite getInvSite() {
		return invSite;
	}

	/**
	 * @param invSite the invSite to set
	 */
	public void setInvSite(InvSite invSite) {
		this.invSite = invSite;
	}

	
}
