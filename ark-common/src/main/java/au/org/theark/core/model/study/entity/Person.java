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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import au.org.theark.core.Constants;
import au.org.theark.core.audit.annotations.ArkAuditDisplay;

/**
 * Person entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PERSON", schema = Constants.STUDY_SCHEMA)
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Person implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String firstName;
	private String middleName;
	private String lastName;
	private String preferredName;
	private GenderType genderType;
	private VitalStatus vitalStatus;
	private TitleType titleType;
	private MaritalStatus maritalStatus;
	private Date dateOfBirth;
	private Date dateOfDeath;
	private String causeOfDeath;
	private PersonContactMethod personContactMethod;
	private Date dateLastKnownAlive;
	private String currentOrDeathAge;

	private Set<LinkSubjectStudy> linkSubjectStudies = new HashSet<LinkSubjectStudy>(0);
	private Set<Address> addresses = new HashSet<Address>(0);
	private Set<Phone> phones = new HashSet<Phone>(0);
	private Set<EmailAccount> emailAccounts = new HashSet<EmailAccount>(0);
	private Set<LinkSubjectStudycomp> linkSubjectStudycomps = new HashSet<LinkSubjectStudycomp>(0);
	private Set<LinkSubjectContact> linkSubjectContactsForContactKey = new HashSet<LinkSubjectContact>(0);
	private Set<LinkSubjectContact> linkSubjectContactsForSubjectKey = new HashSet<LinkSubjectContact>(0);
	private Set<PersonLastnameHistory> personLastnameHistory = new HashSet<PersonLastnameHistory>(0);
	private List<OtherID> otherIDs = new ArrayList<OtherID>(0);

	public Person() {
	}

	public Person(Long id) {
		this.id = id;
	}

	@Id
	@SequenceGenerator(name = "person_generator", sequenceName = "PERSON_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "person_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	@ArkAuditDisplay
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "FIRST_NAME", length = 50)
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "MIDDLE_NAME", length = 50)
	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	@Column(name = "LAST_NAME", length = 50)
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "PREFERRED_NAME", length = 150)
	public String getPreferredName() {
		return this.preferredName;
	}

	public void setPreferredName(String preferredName) {
		this.preferredName = preferredName;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GENDER_TYPE_ID")
	public GenderType getGenderType() {
		return this.genderType;
	}

	public void setGenderType(GenderType genderType) {
		this.genderType = genderType;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_OF_BIRTH", length = 10)
	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VITAL_STATUS_ID")
	public VitalStatus getVitalStatus() {
		return this.vitalStatus;
	}

	public void setVitalStatus(VitalStatus vitalStatus) {
		this.vitalStatus = vitalStatus;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TITLE_TYPE_ID")
	public TitleType getTitleType() {
		return this.titleType;
	}

	public void setTitleType(TitleType titleType) {
		this.titleType = titleType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MARITAL_STATUS_ID")
	public MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person")
	public Set<LinkSubjectStudy> getLinkSubjectStudies() {
		return this.linkSubjectStudies;
	}

	public void setLinkSubjectStudies(Set<LinkSubjectStudy> linkSubjectStudies) {
		this.linkSubjectStudies = linkSubjectStudies;
	}

	@NotAudited
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person")
	public Set<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person")
	public Set<Phone> getPhones() {
		return this.phones;
	}

	public void setPhones(Set<Phone> phones) {
		this.phones = phones;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person")
	public Set<EmailAccount> getEmailAccounts() {
		return emailAccounts;
	}

	public void setEmailAccounts(Set<EmailAccount> emailAccounts) {
		this.emailAccounts = emailAccounts;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person")
	public Set<LinkSubjectStudycomp> getLinkSubjectStudycomps() {
		return this.linkSubjectStudycomps;
	}

	public void setLinkSubjectStudycomps(
			Set<LinkSubjectStudycomp> linkSubjectStudycomps) {
		this.linkSubjectStudycomps = linkSubjectStudycomps;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "personByContactId")
	public Set<LinkSubjectContact> getLinkSubjectContactsForContactKey() {
		return this.linkSubjectContactsForContactKey;
	}

	public void setLinkSubjectContactsForContactKey(
			Set<LinkSubjectContact> linkSubjectContactsForContactKey) {
		this.linkSubjectContactsForContactKey = linkSubjectContactsForContactKey;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "personBySubjectId")
	public Set<LinkSubjectContact> getLinkSubjectContactsForSubjectKey() {
		return this.linkSubjectContactsForSubjectKey;
	}

	public void setLinkSubjectContactsForSubjectKey(
			Set<LinkSubjectContact> linkSubjectContactsForSubjectKey) {
		this.linkSubjectContactsForSubjectKey = linkSubjectContactsForSubjectKey;
	}

	/**
	 * @param personContactMethod
	 *            the personContactMethod to set
	 */
	public void setPersonContactMethod(PersonContactMethod personContactMethod) {
		this.personContactMethod = personContactMethod;
	}

	/**
	 * @return the personContactMethod
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_CONTACT_METHOD_ID")
	public PersonContactMethod getPersonContactMethod() {
		return personContactMethod;
	}

	/**
	 * @param personLastnameHistory
	 *            the personLastnameHistory to set
	 */
	public void setPersonLastnameHistory(
			Set<PersonLastnameHistory> personLastnameHistory) {
		this.personLastnameHistory = personLastnameHistory;
	}

	/**
	 * @return the personLastnameHistory
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person")
	public Set<PersonLastnameHistory> getPersonLastnameHistory() {
		return personLastnameHistory;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person", orphanRemoval=true)
	public List<OtherID> getOtherIDs() {
		return otherIDs;
	}
	
	public void setOtherIDs(List<OtherID> otherIDs) {
		this.otherIDs = otherIDs;
	}

	/**
	 * @param causeOfDeath
	 *            the causeOfDeath to set
	 */
	public void setCauseOfDeath(String causeOfDeath) {
		this.causeOfDeath = causeOfDeath;
	}

	/**
	 * @return the causeOfDeath
	 */
	@Column(name = "CAUSE_OF_DEATH", length = 255)
	public String getCauseOfDeath() {
		return causeOfDeath;
	}

	/**
	 * @param dateOfDeath
	 *            the dateOfDeath to set
	 */
	public void setDateOfDeath(Date dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
	}

	/**
	 * @return the dateOfDeath
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_OF_DEATH", length = 10)
	public Date getDateOfDeath() {
		return dateOfDeath;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_LAST_KNOWN_ALIVE", length = 7)
	public Date getDateLastKnownAlive() {
		return dateLastKnownAlive;
	}

	public void setDateLastKnownAlive(Date dateLastKnownAlive) {
		this.dateLastKnownAlive = dateLastKnownAlive;
	}

	@Column(name = "CURRENT_OR_DEATH_AGE", length = 50)
	public String getCurrentOrDeathAge() {
		return currentOrDeathAge;
	}
	
	public void setCurrentOrDeathAge(String currentOrDeathAge) {
		this.currentOrDeathAge = currentOrDeathAge;
	}

	@Transient
	public String getDescriptiveLastNameHistory() {
		return getPersonLastnameHistory().stream().map(name -> name.getLastName()).collect(Collectors.joining(", "));
	}

	@Transient
	public String getDescriptiveOtherIDs() {
		return getOtherIDs().stream().map(otherID -> otherID.getOtherID_Source() + ": " + otherID.getOtherID()).collect(Collectors.joining("\n"));
	}

	@Transient
	public String getFullName() {
		StringBuilder fullNameBuilder = new StringBuilder();
		if (getFirstName() != null) {
			fullNameBuilder.append(getFirstName());
			fullNameBuilder.append(" ");
		}
		if (getMiddleName() != null) {
			fullNameBuilder.append(getMiddleName());
			fullNameBuilder.append(" ");
		}
		if (getLastName() != null) {
			fullNameBuilder.append(getLastName());
		}

		return fullNameBuilder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((addresses == null) ? 0 : addresses.hashCode());
		result = prime * result
				+ ((causeOfDeath == null) ? 0 : causeOfDeath.hashCode());
		result = prime
				* result
				+ ((dateLastKnownAlive == null) ? 0 : dateLastKnownAlive
						.hashCode());
		result = prime * result
				+ ((dateOfBirth == null) ? 0 : dateOfBirth.hashCode());
		result = prime * result
				+ ((dateOfDeath == null) ? 0 : dateOfDeath.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((genderType == null) ? 0 : genderType.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result
				+ ((maritalStatus == null) ? 0 : maritalStatus.hashCode());
		result = prime * result
				+ ((middleName == null) ? 0 : middleName.hashCode());
		result = prime
				* result
				+ ((personContactMethod == null) ? 0 : personContactMethod
						.hashCode());
		result = prime
				* result
				+ ((personLastnameHistory == null) ? 0 : personLastnameHistory
						.hashCode());
		result = prime * result + ((phones == null) ? 0 : phones.hashCode());
		result = prime * result
				+ ((preferredName == null) ? 0 : preferredName.hashCode());
		result = prime * result
				+ ((titleType == null) ? 0 : titleType.hashCode());
		result = prime * result
				+ ((vitalStatus == null) ? 0 : vitalStatus.hashCode());
		result = prime * result 
				+ ((otherIDs == null) ? 0 : otherIDs.hashCode());
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
		Person other = (Person) obj;
		if (addresses == null) {
			if (other.addresses != null)
				return false;
		} else if (!addresses.equals(other.addresses))
			return false;
		if (causeOfDeath == null) {
			if (other.causeOfDeath != null)
				return false;
		} else if (!causeOfDeath.equals(other.causeOfDeath))
			return false;
		if (dateLastKnownAlive == null) {
			if (other.dateLastKnownAlive != null)
				return false;
		} else if (!dateLastKnownAlive.equals(other.dateLastKnownAlive))
			return false;
		if (dateOfBirth == null) {
			if (other.dateOfBirth != null)
				return false;
		} else if (!dateOfBirth.equals(other.dateOfBirth))
			return false;
		if (dateOfDeath == null) {
			if (other.dateOfDeath != null)
				return false;
		} else if (!dateOfDeath.equals(other.dateOfDeath))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (genderType == null) {
			if (other.genderType != null)
				return false;
		} else if (!genderType.equals(other.genderType))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (maritalStatus == null) {
			if (other.maritalStatus != null)
				return false;
		} else if (!maritalStatus.equals(other.maritalStatus))
			return false;
		if (middleName == null) {
			if (other.middleName != null)
				return false;
		} else if (!middleName.equals(other.middleName))
			return false;
		if (personContactMethod == null) {
			if (other.personContactMethod != null)
				return false;
		} else if (!personContactMethod.equals(other.personContactMethod))
			return false;
		if (personLastnameHistory == null) {
			if (other.personLastnameHistory != null)
				return false;
		} else if (!personLastnameHistory.equals(other.personLastnameHistory))
			return false;
		if (phones == null) {
			if (other.phones != null)
				return false;
		} else if (!phones.equals(other.phones))
			return false;
		if (preferredName == null) {
			if (other.preferredName != null)
				return false;
		} else if (!preferredName.equals(other.preferredName))
			return false;
		if (titleType == null) {
			if (other.titleType != null)
				return false;
		} else if (!titleType.equals(other.titleType))
			return false;
		if (vitalStatus == null) {
			if (other.vitalStatus != null)
				return false;
		} else if (!vitalStatus.equals(other.vitalStatus))
			return false;
		if (otherIDs == null) {
			if (other.otherIDs != null) {
				return false;
			}
		} else if (!otherIDs.equals(other.otherIDs))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", firstName=" + firstName
				+ ", middleName=" + middleName + ", lastName=" + lastName + "]";
	}

}
