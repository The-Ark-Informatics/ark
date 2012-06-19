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
import javax.persistence.Id;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;

/**
 * 
 * @author cellis
 *
 */
@Entity
@Table(name = "BIOSPECIMENUID_SEQUENCE", schema = Constants.LIMS_TABLE_SCHEMA)
public class BiospecimenUidSequence implements java.io.Serializable {


	private static final long	serialVersionUID	= 1L;


	private String					studyNameId;
	private Integer				uidSequence;
	private Boolean				insertLock;


	public BiospecimenUidSequence() {
	}

	public BiospecimenUidSequence(String studyNameId, Integer uidSequence, Boolean insertLock) {
		this.studyNameId = studyNameId;
		this.uidSequence = uidSequence;
		this.insertLock = insertLock;
	}

	@Id
	@Column(name = "STUDY_NAME_ID", unique = true, nullable = false)
	public String getStudyNameId() {
		return this.studyNameId;
	}

	public void setStudyNameId(String studyNameId) {
		this.studyNameId = studyNameId;
	}

	@Column(name = "UID_SEQUENCE", nullable = false)
	public Integer getUidSequence() {
		return this.uidSequence;
	}

	public void setUidSequence(Integer uidSequence) {
		this.uidSequence = uidSequence;
	}

	@Column(name = "INSERT_LOCK", nullable = false)
	public Boolean getInsertLock() {
		return insertLock;
	}

	public void setInsertLock(Boolean insertLock) {
		this.insertLock = insertLock;
	}
}
