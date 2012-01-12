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

import java.sql.Blob;
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
import javax.persistence.UniqueConstraint;

import au.org.theark.core.Constants;

/**
 * Study entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "STUDY", schema = Constants.STUDY_SCHEMA, uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
public class Study implements java.io.Serializable {

	// Fields
	private Long								id;
	private StudyStatus						studyStatus;
	private String								name;
	private String								description;
	private Date								dateOfApplication;
	private Long								estimatedYearOfCompletion;
	private String								chiefInvestigator;
	private String								coInvestigator;

	private String								contactPerson;
	private String								contactPersonPhone;
	private String								ldapGroupName;
	private Boolean							autoConsent;
	private String								subStudyBiospecimenPrefix;
	private Blob								studyLogoBlob;
	private String								filename;

	// SubjectUID autogeneration parameters
	private Boolean							autoGenerateSubjectUid;
	private Long								subjectUidStart;
	private String								subjectUidPrefix;
	private SubjectUidToken					subjectUidToken;
	private SubjectUidPadChar				subjectUidPadChar;

	private Set<LinkStudySubstudy>		linkStudySubstudiesForid		= new HashSet<LinkStudySubstudy>(0);
	private Set<LinkStudyStudysite>		linkStudyStudysites				= new HashSet<LinkStudyStudysite>(0);
	private Set<StudyComp>					studyComps							= new HashSet<StudyComp>(0);
	private Set<LinkSubjectStudycomp>	linkSubjectStudycomps			= new HashSet<LinkSubjectStudycomp>(0);
	private Set<LinkSubjectStudy>			linkSubjectStudies				= new HashSet<LinkSubjectStudy>(0);
	private Set<LinkSubjectContact>		linkSubjectContacts				= new HashSet<LinkSubjectContact>(0);
	private Set<LinkStudyStudycomp>		linkStudyStudycomps				= new HashSet<LinkStudyStudycomp>(0);
	private Set<LinkStudySubstudy>		linkStudySubstudiesForSubid	= new HashSet<LinkStudySubstudy>(0);

	// Constructors

	/** default constructor */
	public Study() {
	}

	/** minimal constructor */
	public Study(Long id) {
		this.id = id;
	}

	/** full constructor */
	public Study(Long id, StudyStatus studyStatus, String name, String description, Date dateOfApplication, Long estimatedYearOfCompletion, String chiefInvestigator, String coInvestigator,
			Boolean autoGenerateSubjectUid, Long subjectUIdStart, String subjectIdPrefix, String contactPerson, String contactPersonPhone, String ldapGroupName, Boolean autoConsent,
			String subStudyBiospecimenPrefix, String filename, SubjectUidToken subjectIdToken, SubjectUidPadChar subjectUIdPadChar, Set<LinkStudySubstudy> linkStudySubstudiesForid,
			Set<LinkStudyStudysite> linkStudyStudysites, Set<StudyComp> studyComps, Set<LinkSubjectStudycomp> linkSubjectStudycomps,
			Set<LinkSubjectStudy> linkSubjectStudies, Set<LinkSubjectContact> linkSubjectContacts, Set<LinkStudyStudycomp> linkStudyStudycomps, Set<LinkStudySubstudy> linkStudySubstudiesForSubid) {
		this.id = id;
		this.studyStatus = studyStatus;
		this.name = name;
		this.description = description;
		this.dateOfApplication = dateOfApplication;
		this.estimatedYearOfCompletion = estimatedYearOfCompletion;
		this.chiefInvestigator = chiefInvestigator;
		this.coInvestigator = coInvestigator;
		this.autoGenerateSubjectUid = autoGenerateSubjectUid;
		this.subjectUidStart = subjectUIdStart;
		this.subjectUidPrefix = subjectIdPrefix;
		this.contactPerson = contactPerson;
		this.contactPersonPhone = contactPersonPhone;
		this.ldapGroupName = ldapGroupName;
		this.autoConsent = autoConsent;
		this.subStudyBiospecimenPrefix = subStudyBiospecimenPrefix;
		this.subjectUidPrefix = subjectIdPrefix;
		this.subjectUidToken = subjectIdToken;
		this.subjectUidPadChar = subjectUIdPadChar;
		this.linkStudySubstudiesForid = linkStudySubstudiesForid;
		this.linkStudyStudysites = linkStudyStudysites;
		this.studyComps = studyComps;
		this.linkSubjectStudycomps = linkSubjectStudycomps;
		this.linkSubjectStudies = linkSubjectStudies;
		this.linkSubjectContacts = linkSubjectContacts;
		this.linkStudyStudycomps = linkStudyStudycomps;
		this.linkStudySubstudiesForSubid = linkStudySubstudiesForSubid;
	}

	// Property accessors

	@Id
	@SequenceGenerator(name = "study_generator", sequenceName = "STUDY_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "study_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_STATUS_ID")
	public StudyStatus getStudyStatus() {
		return this.studyStatus;
	}

	public void setStudyStatus(StudyStatus studyStatus) {
		this.studyStatus = studyStatus;
	}

	@Column(name = "NAME", unique = true, length = 150)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION", length = 255)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_OF_APPLICATION", length = 7)
	public Date getDateOfApplication() {
		return this.dateOfApplication;
	}

	public void setDateOfApplication(Date dateOfApplication) {
		this.dateOfApplication = dateOfApplication;
	}

	@Column(name = "ESTIMATED_YEAR_OF_COMPLETION", precision = 22, scale = 0)
	public Long getEstimatedYearOfCompletion() {
		return this.estimatedYearOfCompletion;
	}

	public void setEstimatedYearOfCompletion(Long estimatedYearOfCompletion) {
		this.estimatedYearOfCompletion = estimatedYearOfCompletion;
	}

	@Column(name = "CHIEF_INVESTIGATOR", length = 50)
	public String getChiefInvestigator() {
		return this.chiefInvestigator;
	}

	public void setChiefInvestigator(String chiefInvestigator) {
		this.chiefInvestigator = chiefInvestigator;
	}

	@Column(name = "CO_INVESTIGATOR", length = 50)
	public String getCoInvestigator() {
		return this.coInvestigator;
	}

	public void setCoInvestigator(String coInvestigator) {
		this.coInvestigator = coInvestigator;
	}

	@Column(name = "SUBJECTUID_START", precision = 22, scale = 0)
	public Long getSubjectUidStart() {
		return subjectUidStart;
	}

	public void setSubjectUidStart(Long subjectUIdStart) {
		this.subjectUidStart = subjectUIdStart;
	}

	@Column(name = "SUBJECTUID_PREFIX", length = 3)
	public String getSubjectUidPrefix() {
		return this.subjectUidPrefix;
	}

	public void setSubjectUidPrefix(String subjectIdPrefix) {
		this.subjectUidPrefix = subjectIdPrefix;
	}

	@Column(name = "CONTACT_PERSON", length = 50)
	public String getContactPerson() {
		return this.contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	@Column(name = "CONTACT_PERSON_PHONE", length = 20)
	public String getContactPersonPhone() {
		return this.contactPersonPhone;
	}

	public void setContactPersonPhone(String contactPersonPhone) {
		this.contactPersonPhone = contactPersonPhone;
	}

	@Column(name = "LDAP_GROUP_NAME", length = 100)
	public String getLdapGroupName() {
		return this.ldapGroupName;
	}

	public void setLdapGroupName(String ldapGroupName) {
		this.ldapGroupName = ldapGroupName;
	}

	@Column(name = "AUTO_CONSENT", precision = 1, scale = 0)
	public Boolean getAutoConsent() {
		return this.autoConsent;
	}

	public void setAutoConsent(Boolean autoConsent) {
		this.autoConsent = autoConsent;
	}

	@Column(name = "AUTO_GENERATE_SUBJECTUID", precision = 1, scale = 0)
	public Boolean getAutoGenerateSubjectUid() {
		return autoGenerateSubjectUid;
	}

	public void setAutoGenerateSubjectUid(Boolean autoGenerateSubjectUid) {
		this.autoGenerateSubjectUid = autoGenerateSubjectUid;
	}

	@Column(name = "SUB_STUDY_BIOSPECIMEN_PREFIX", length = 20)
	public String getSubStudyBiospecimenPrefix() {
		return this.subStudyBiospecimenPrefix;
	}

	public void setStudyLogoBlob(Blob studyLogoBlob) {
		this.studyLogoBlob = studyLogoBlob;
	}

	@Column(name = "STUDY_LOGO")
	public Blob getStudyLogoBlob() {
		return studyLogoBlob;
	}

	public void setSubStudyBiospecimenPrefix(String subStudyBiospecimenPrefix) {
		this.subStudyBiospecimenPrefix = subStudyBiospecimenPrefix;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "subStudy")
	public Set<LinkStudySubstudy> getLinkStudySubstudiesForid() {
		return this.linkStudySubstudiesForid;
	}

	public void setLinkStudySubstudiesForid(Set<LinkStudySubstudy> linkStudySubstudiesForid) {
		this.linkStudySubstudiesForid = linkStudySubstudiesForid;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "study")
	public Set<LinkStudyStudysite> getLinkStudyStudysites() {
		return this.linkStudyStudysites;
	}

	public void setLinkStudyStudysites(Set<LinkStudyStudysite> linkStudyStudysites) {
		this.linkStudyStudysites = linkStudyStudysites;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "study")
	public Set<StudyComp> getStudyComps() {
		return this.studyComps;
	}

	public void setStudyComps(Set<StudyComp> studyComps) {
		this.studyComps = studyComps;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "study")
	public Set<LinkSubjectStudycomp> getLinkSubjectStudycomps() {
		return this.linkSubjectStudycomps;
	}

	public void setLinkSubjectStudycomps(Set<LinkSubjectStudycomp> linkSubjectStudycomps) {
		this.linkSubjectStudycomps = linkSubjectStudycomps;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "study")
	public Set<LinkSubjectStudy> getLinkSubjectStudies() {
		return this.linkSubjectStudies;
	}

	public void setLinkSubjectStudies(Set<LinkSubjectStudy> linkSubjectStudies) {
		this.linkSubjectStudies = linkSubjectStudies;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "study")
	public Set<LinkSubjectContact> getLinkSubjectContacts() {
		return this.linkSubjectContacts;
	}

	public void setLinkSubjectContacts(Set<LinkSubjectContact> linkSubjectContacts) {
		this.linkSubjectContacts = linkSubjectContacts;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "study")
	public Set<LinkStudyStudycomp> getLinkStudyStudycomps() {
		return this.linkStudyStudycomps;
	}

	public void setLinkStudyStudycomps(Set<LinkStudyStudycomp> linkStudyStudycomps) {
		this.linkStudyStudycomps = linkStudyStudycomps;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mainStudy")
	public Set<LinkStudySubstudy> getLinkStudySubstudiesForSubid() {
		return this.linkStudySubstudiesForSubid;
	}

	public void setLinkStudySubstudiesForSubid(Set<LinkStudySubstudy> linkStudySubstudiesForSubid) {
		this.linkStudySubstudiesForSubid = linkStudySubstudiesForSubid;
	}

	/**
	 * @param filename
	 *           the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return the filename
	 */
	@Column(name = "FILENAME")
	public String getFilename() {
		return filename;
	}

	/**
	 * @param subjectIdToken
	 *           the subjectIdToken to set
	 */
	public void setSubjectUidToken(SubjectUidToken subjectIdToken) {
		this.subjectUidToken = subjectIdToken;
	}

	/**
	 * @return the subjectIdToken
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBJECTUID_TOKEN_ID")
	public SubjectUidToken getSubjectUidToken() {
		return subjectUidToken;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBJECTUID_PADCHAR_ID")
	public SubjectUidPadChar getSubjectUidPadChar() {
		return this.subjectUidPadChar;
	}

	public void setSubjectUidPadChar(SubjectUidPadChar subjectUidPadChar) {
		this.subjectUidPadChar = subjectUidPadChar;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((filename == null) ? 0 : filename.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Study other = (Study) obj;
		if (filename == null) {
			if (other.filename != null)
				return false;
		} else if (!filename.equals(other.filename))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
