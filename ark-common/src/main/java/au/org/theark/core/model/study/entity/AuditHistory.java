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

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import au.org.theark.core.Constants;

/**
 * AuditHistory entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AUDIT_HISTORY", schema = Constants.STUDY_SCHEMA)
public class AuditHistory implements java.io.Serializable {




	private static final long serialVersionUID = 1L;
	private Long			id;
	private StudyStatus	studyStatus;
	private Date			dateTime;
	private String			actionType;
	private String			arkUserId;
	private String			comment;
	private Long			entityId;
	private String			entityType;


	public AuditHistory() {
	}

	public AuditHistory(Long id) {
		this.id = id;
	}

	public AuditHistory(Long id, StudyStatus studyStatus, Date dateTime, String actionType, String etaUserId, String comment, Long entityKey, String entityType) {
		this.id = id;
		this.studyStatus = studyStatus;
		this.dateTime = dateTime;
		this.actionType = actionType;
		this.arkUserId = etaUserId;
		this.comment = comment;
		this.entityId = entityKey;
		this.entityType = entityType;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name = "ah_generator", sequenceName = "AUDIT_HISTORY_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "ah_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_TIME")
	public Date getDateTime() {
		return this.dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	@Column(name = "COMMENT")
	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Column(name = "ENTITY_ID", precision = 22, scale = 0)
	public Long getEntityId() {
		return this.entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_STATUS_ID")
	public StudyStatus getStudyStatus() {
		return studyStatus;
	}

	public void setStudyStatus(StudyStatus studyStatus) {
		this.studyStatus = studyStatus;
	}

	@Column(name = "ARK_USER_ID", length = 255)
	public String getArkUserId() {
		return arkUserId;
	}

	public void setArkUserId(String arkUserId) {
		this.arkUserId = arkUserId;
	}

	@Column(name = "ACTION_TYPE")
	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	@Column(name = "ENTITY_TYPE")
	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

}
