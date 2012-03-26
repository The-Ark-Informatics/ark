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
package au.org.theark.core.model.audit.entity;

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
import au.org.theark.core.model.study.entity.ConsentOption;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.ConsentType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.YesNo;

/**
 * LinkSubjectStudyHistory entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "LSS_CONSENT_HISTORY", schema = Constants.AUDIT_SCHEMA)
public class LssConsentHistory implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	// Fields
	private Long							id;
	private Date							timestamp;
	private LinkSubjectStudy			linkSubjectStudy;
	private ConsentOption				consentToActiveContact;
	private ConsentOption				consentToPassiveDataGathering;
	private ConsentOption				consentToUseData;
	private ConsentStatus				consentStatus;
	private ConsentType					consentType;
	private Date							consentDate;
	private YesNo							consentDownloaded;

	// Property accessors
	@Id
	@SequenceGenerator(name = "LSS_CONSENT_HISTORY_SEQ", sequenceName = "LSS_CONSENT_HISTORY_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "LSS_CONSENT_HISTORY_SEQ")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the timestamp
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMESTAMP")
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @param linkSubjectStudy the linkSubjectStudy to set
	 */
	public void setLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy) {
		this.linkSubjectStudy = linkSubjectStudy;
	}

	/**
	 * @return the linkSubjectStudy
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LINK_SUBJECT_STUDY_ID")
	public LinkSubjectStudy getLinkSubjectStudy() {
		return linkSubjectStudy;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONSENT_TO_PASSIVE_DATA_GATHERING_ID")
	public ConsentOption	getConsentToPassiveDataGathering() {
		return consentToPassiveDataGathering;
	}

	public void setConsentToPassiveDataGathering(ConsentOption consentToPassiveDataGathering) {
		this.consentToPassiveDataGathering = consentToPassiveDataGathering;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONSENT_TO_USE_DATA_ID")
	public ConsentOption getConsentToUseData() {
		return consentToUseData;
	}

	public void setConsentToUseData(ConsentOption consentToUseData) {
		this.consentToUseData = consentToUseData;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONSENT_TO_ACTIVE_CONTACT_ID")
	public ConsentOption getConsentToActiveContact() {
		return consentToActiveContact;
	}

	public void setConsentToActiveContact(ConsentOption consentToActiveContact) {
		this.consentToActiveContact = consentToActiveContact;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONSENT_STATUS_ID")
	public ConsentStatus getConsentStatus() {
		return consentStatus;
	}

	public void setConsentStatus(ConsentStatus consentStatus) {
		this.consentStatus = consentStatus;
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
	@Column(name = "CONSENT_DATE", length = 7)
	public Date getConsentDate() {
		return consentDate;
	}

	public void setConsentDate(Date consentDate) {
		this.consentDate = consentDate;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONSENT_DOWNLOADED")
	public YesNo getConsentDownloaded() {
		return consentDownloaded;
	}

	public void setConsentDownloaded(YesNo consentDownloaded) {
		this.consentDownloaded = consentDownloaded;
	}
}
