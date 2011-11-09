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

import java.io.Serializable;
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

@Entity
@Table(name = "correspondence_audit", schema = Constants.STUDY_SCHEMA)
public class CorrespondenceAudit implements Serializable {

	private Long								id;
	private Correspondences					correspondence;
	private Study								study;
	private Date								auditDate;
	private String								auditTime;
	private CorrespondenceStatusType		correspondenceStatusType;
	private String								studyManager;
	private Date								correspondenceDate;
	private String								correspondenceTime;
	private String								reason;
	private CorrespondenceModeType		correspondenceModeType;
	private CorrespondenceDirectionType	correspondenceDirectionType;
	private CorrespondenceOutcomeType	correspondenceOutcomeType;
	private String								details;
	private String								comments;

	@Id
	@SequenceGenerator(name = "correspondence_audit_generator", sequenceName = "CORRESPONDENCE_AUDIT_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "correspondence_audit_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CORRESPONDENCE_ID")
	public Correspondences getCorrespondence() {
		return correspondence;
	}

	public void setCorrespondence(Correspondences correspondence) {
		this.correspondence = correspondence;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "AUDIT_DATE", length = 7)
	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	@Column(name = "AUDIT_TIME", length = 255)
	public String getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_TYPE_ID")
	public CorrespondenceStatusType getCorrespondenceStatusType() {
		return correspondenceStatusType;
	}

	public void setCorrespondenceStatusType(CorrespondenceStatusType correspondenceStatusType) {
		this.correspondenceStatusType = correspondenceStatusType;
	}

	@Column(name = "STUDY_MANAGER", length = 255)
	public String getStudyManager() {
		return studyManager;
	}

	public void setStudyManager(String studyManager) {
		this.studyManager = studyManager;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CORRESPONDENCE_DATE", length = 7)
	public Date getCorrespondenceDate() {
		return correspondenceDate;
	}

	public void setCorrespondenceDate(Date correspondenceDate) {
		this.correspondenceDate = correspondenceDate;
	}

	@Column(name = "CORRESPONDENCE_TIME", length = 255)
	public String getCorrespondenceTime() {
		return correspondenceTime;
	}

	public void setCorrespondenceTime(String correspondenceTime) {
		this.correspondenceTime = correspondenceTime;
	}

	@Column(name = "REASON", length = 4096)
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODE_TYPE_ID")
	public CorrespondenceModeType getCorrespondenceModeType() {
		return correspondenceModeType;
	}

	public void setCorrespondenceModeType(CorrespondenceModeType correspondenceModeType) {
		this.correspondenceModeType = correspondenceModeType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DIRECTION_TYPE_ID")
	public CorrespondenceDirectionType getCorrespondenceDirectionType() {
		return correspondenceDirectionType;
	}

	public void setCorrespondenceDirectionType(CorrespondenceDirectionType correspondenceDirectionType) {
		this.correspondenceDirectionType = correspondenceDirectionType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OUTCOME_TYPE_ID")
	public CorrespondenceOutcomeType getCorrespondenceOutcomeType() {
		return correspondenceOutcomeType;
	}

	public void setCorrespondenceOutcomeType(CorrespondenceOutcomeType correspondenceOutcomeType) {
		this.correspondenceOutcomeType = correspondenceOutcomeType;
	}

	@Column(name = "DETAILS", length = 4096)
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	@Column(name = "COMMENTS", length = 4096)
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
