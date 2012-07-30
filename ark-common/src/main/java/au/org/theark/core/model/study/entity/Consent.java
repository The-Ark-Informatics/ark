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

/**
 * @author nivedann
 * 
 */
@Entity
@Table(name = "CONSENT", schema = Constants.STUDY_SCHEMA)
public class Consent implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Study study;
	private LinkSubjectStudy linkSubjectStudy;
	private StudyComp studyComp;
	private StudyCompStatus studyComponentStatus;
	private ConsentStatus consentStatus;
	private ConsentType consentType;
	private Date consentDate;
	private String consentedBy;
	private String comments;
	private Date requestedDate;
	private Date receivedDate;
	private Date completedDate;
	private YesNo consentDownloaded;

	public Consent() {

	}

	public Consent(Long id) {
		this.id = id;
	}

	@Id
	@SequenceGenerator(name = "consent_generator", sequenceName = "CONSENT_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "consent_generator")
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
	@JoinColumn(name = "STUDY_COMP_ID")
	public StudyComp getStudyComp() {
		return studyComp;
	}

	public void setStudyComp(StudyComp studyComp) {
		this.studyComp = studyComp;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_COMP_STATUS_ID")
	public StudyCompStatus getStudyComponentStatus() {
		return studyComponentStatus;
	}

	public void setStudyComponentStatus(StudyCompStatus studyComponentStatus) {
		this.studyComponentStatus = studyComponentStatus;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONSENT_STATUS_ID")
	public ConsentStatus getConsentStatus() {
		return consentStatus;
	}

	public void setConsentStatus(ConsentStatus consentStatus) {
		this.consentStatus = consentStatus;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CONSENT_DATE", length = 7)
	public Date getConsentDate() {
		return consentDate;
	}

	public void setConsentDate(Date consentDate) {
		this.consentDate = consentDate;
	}

	@Column(name = "CONSENTED_BY")
	public String getConsentedBy() {
		return consentedBy;
	}

	public void setConsentedBy(String consentedBy) {
		this.consentedBy = consentedBy;
	}

	@Column(name = "COMMENTS")
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONSENT_TYPE_ID")
	public ConsentType getConsentType() {
		return consentType;
	}

	public void setConsentType(ConsentType consentType) {
		this.consentType = consentType;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "REQUESTED_DATE", length = 7)
	public Date getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "RECEIVED_DATE", length = 7)
	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "COMPLETED_DATE", length = 7)
	public Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONSENT_DOWNLOADED_ID")
	public YesNo getConsentDownloaded() {
		return consentDownloaded;
	}

	public void setConsentDownloaded(YesNo consentDownloaded) {
		this.consentDownloaded = consentDownloaded;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LINK_SUBJECT_STUDY_ID")
	public LinkSubjectStudy getLinkSubjectStudy() {
		return linkSubjectStudy;
	}

	public void setLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy) {
		this.linkSubjectStudy = linkSubjectStudy;
	}
}
