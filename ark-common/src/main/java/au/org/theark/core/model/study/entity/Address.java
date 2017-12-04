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

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import au.org.theark.core.Constants;

@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Table(name = "ADDRESS", schema = Constants.STUDY_SCHEMA)
public class Address implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Person person;
	private String addressLineOne;
	private String streetAddress;
	private String postCode;
	private String city;
	private Country country;
	private State state;
	private String otherState;
	private AddressStatus addressStatus;
	private AddressType addressType;
	private Date dateReceived;
	private String comments;
	private Boolean preferredMailingAddress;
	private String source;
	private Date validFrom;
	private Date validTo;

	private Set<StudySite> studySites = new HashSet<StudySite>(0);

	public Address() {
	}

	public Address(Long id) {
		this.id = id;
	}

	@Id
	@SequenceGenerator(name = "address_generator", sequenceName = "ADDRESS_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "address_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	// TODO Lets keep naming consistant
	//@Audited
	@Column(name = "STREET_ADDRESS")
	public String getStreetAddress() {
		return this.streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	//@Audited
	@Column(name = "POST_CODE", length = 10)
	public String getPostCode() {
		return this.postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	
	//@Audited
	@Column(name = "CITY", length = 30)
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	//@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ADDRESS_STATUS_ID")
	public AddressStatus getAddressStatus() {
		return this.addressStatus;
	}

	public void setAddressStatus(AddressStatus addressStatus) {
		this.addressStatus = addressStatus;
	}

	@NotAudited
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "address")
	public Set<StudySite> getStudySites() {
		return this.studySites;
	}

	public void setStudySites(Set<StudySite> studySites) {
		this.studySites = studySites;
	}

	//@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ADDRESS_TYPE_ID")
	public AddressType getAddressType() {
		return addressType;
	}

	public void setAddressType(AddressType addressType) {
		this.addressType = addressType;
	}

	//@Audited
	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_RECEIVED", length = 10)
	public Date getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(Date dateReceived) {
		this.dateReceived = dateReceived;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	//@Audited
	@Column(name = "COMMENTS", length = 255)
	public String getComments() {
		return comments;
	}

	//@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COUNTRY_ID")
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	//@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATE_ID")
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	//@Audited
	@Column(name = "OTHER_STATE", length = 45)
	public String getOtherState() {
		return otherState;
	}

	public void setOtherState(String otherState) {
		this.otherState = otherState;
	}
	
	//@Audited
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PERSON_ID")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	//@Audited
	@Column(name = "PREFERRED_MAILING_ADDRESS", nullable = false)
	public Boolean getPreferredMailingAddress() {
		return preferredMailingAddress;
	}

	public void setPreferredMailingAddress(Boolean preferredMailingAddress) {
		this.preferredMailingAddress = preferredMailingAddress;
	}

	//@Audited
	@Column(name = "ADDRESS_LINE_1")
	public String getAddressLineOne() {
		return addressLineOne;
	}

	public void setAddressLineOne(String addressLineOne) {
		this.addressLineOne = addressLineOne;
	}

	//@Audited
	@Column(name = "SOURCE")
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	//@Audited
	@Temporal(TemporalType.DATE)
	@Column(name = "VALID_FROM", length = 7)
	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}
	//@Audited
	@Temporal(TemporalType.DATE)
	@Column(name = "VALID_TO", length = 7)
	public Date getValidTo() {
		return validTo;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}
	
}
