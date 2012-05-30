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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import au.org.theark.core.Constants;

/**
 * LinkSubjectStudy entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "LINK_SUBJECT_STUDY", schema = Constants.STUDY_SCHEMA)
public class LinkSubjectStudy implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long							id;
	private Study							study;
	private SubjectStatus				subjectStatus;
	private Person							person;
	private String							subjectUID;
	private ConsentOption				consentToActiveContact;
	private ConsentOption				consentToPassiveDataGathering;
	private ConsentOption				consentToUseData;
	private ConsentStatus				consentStatus;
	private ConsentType					consentType;
	private Date							consentDate;
	private String							heardAboutStudy;
	private String							comment;
	private YesNo							consentDownloaded;

	private Set<Consent>					consents					= new HashSet<Consent>();
	private Set<SubjectCustomFieldData> subjectCustomFieldDataSet = new HashSet<SubjectCustomFieldData>();

	public LinkSubjectStudy() {
		person = new Person();

	}

	public LinkSubjectStudy(Long id) {
		this.id = id;
	}

	public LinkSubjectStudy(Long id, Study study, SubjectStatus subjectStatus, Person person, Set<SubjectCustomFieldData> subjectCustomFieldDataSet) {
		this.id = id;
		this.study = study;
		this.subjectStatus = subjectStatus;
		this.person = person;
		this.subjectCustomFieldDataSet = subjectCustomFieldDataSet;
		
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "linkSubjectStudy")
	public Set<SubjectCustomFieldData> getSubjectCustomFieldDataSet() {
		return subjectCustomFieldDataSet;
	}

	public void setSubjectCustomFieldDataSet(Set<SubjectCustomFieldData> subjectCustomFieldDataSet) {
		this.subjectCustomFieldDataSet = subjectCustomFieldDataSet;
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

	@Id
	@SequenceGenerator(name = "link_subject_study_generator", sequenceName = "LINK_SUBJECT_STUDY_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "link_subject_study_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
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
	@JoinColumn(name = "SUBJECT_STATUS_ID")
	public SubjectStatus getSubjectStatus() {
		return this.subjectStatus;
	}

	public void setSubjectStatus(SubjectStatus subjectStatus) {
		this.subjectStatus = subjectStatus;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_ID")
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column(name = "SUBJECT_UID", length = 50)
	public String getSubjectUID() {
		return subjectUID;
	}

	public void setSubjectUID(String subjectUID) {
		this.subjectUID = subjectUID;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "linkSubjectStudy")
	public Set<Consent> getConsents() {
		return consents;
	}

	public void setConsents(Set<Consent> consents) {
		this.consents = consents;
	}

	@Column(name = "HEARD_ABOUT_STUDY", length = 1000)
	public String getHeardAboutStudy() {
		return heardAboutStudy;
	}

	public void setHeardAboutStudy(String heardAboutStudy) {
		this.heardAboutStudy = heardAboutStudy;
	}

	@Column(name = "COMMENTS", length = 1000)
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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
