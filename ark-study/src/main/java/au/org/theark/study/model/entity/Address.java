package au.org.theark.study.model.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Address entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ADDRESS", schema = "ETA")
public class Address implements java.io.Serializable {

	// Fields

	private Long addressKey;
	private Person person;
	private String streetAddress;
	private String suburb;
	private String postCode;
	private String city;
	private String state;
	private String country;
	private boolean addressStatus;
	private Long addressTypeKey;
	private Set<StudySite> studySites = new HashSet<StudySite>(0);

	// Constructors

	/** default constructor */
	public Address() {
	}

	/** minimal constructor */
	public Address(Long addressKey) {
		this.addressKey = addressKey;
	}

	/** full constructor */
	public Address(Long addressKey, Person person, String streetAddress,
			String suburb, String postCode, String city, String state,
			String country, boolean addressStatus, Long addressTypeKey,
			Set<StudySite> studySites) {
		this.addressKey = addressKey;
		this.person = person;
		this.streetAddress = streetAddress;
		this.suburb = suburb;
		this.postCode = postCode;
		this.city = city;
		this.state = state;
		this.country = country;
		this.addressStatus = addressStatus;
		this.addressTypeKey = addressTypeKey;
		this.studySites = studySites;
	}

	// Property accessors
	@Id
	@Column(name = "ADDRESS_KEY", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getAddressKey() {
		return this.addressKey;
	}

	public void setAddressKey(Long addressKey) {
		this.addressKey = addressKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_KEY")
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column(name = "STREET_ADDRESS")
	public String getStreetAddress() {
		return this.streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	@Column(name = "SUBURB", length = 50)
	public String getSuburb() {
		return this.suburb;
	}

	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}

	@Column(name = "POST_CODE", length = 10)
	public String getPostCode() {
		return this.postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	@Column(name = "CITY", length = 30)
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "STATE", length = 20)
	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "COUNTRY", length = 50)
	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "ADDRESS_STATUS", precision = 1, scale = 0)
	public boolean getAddressStatus() {
		return this.addressStatus;
	}

	public void setAddressStatus(boolean addressStatus) {
		this.addressStatus = addressStatus;
	}

	@Column(name = "ADDRESS_TYPE_KEY", precision = 22, scale = 0)
	public Long getAddressTypeKey() {
		return this.addressTypeKey;
	}

	public void setAddressTypeKey(Long addressTypeKey) {
		this.addressTypeKey = addressTypeKey;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "address")
	public Set<StudySite> getStudySites() {
		return this.studySites;
	}

	public void setStudySites(Set<StudySite> studySites) {
		this.studySites = studySites;
	}

}