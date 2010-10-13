package au.org.theark.study.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Phone entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PHONE", schema = "ETA")
public class Phone implements java.io.Serializable {

	// Fields

	private Long phoneKey;
	private PhoneType phoneType;
	private Person person;
	private Long phoneNumber;
	private Long areaCode;

	// Constructors

	/** default constructor */
	public Phone() {
	}

	/** minimal constructor */
	public Phone(Long phoneKey) {
		this.phoneKey = phoneKey;
	}

	/** full constructor */
	public Phone(Long phoneKey, PhoneType phoneType, Person person,
			Long phoneNumber,Long areaCode) {
		this.phoneKey = phoneKey;
		this.phoneType = phoneType;
		this.person = person;
		this.phoneNumber = phoneNumber;
		this.areaCode = areaCode;
	}

	// Property accessors
	@Id
	@Column(name = "PHONE_KEY", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getPhoneKey() {
		return this.phoneKey;
	}

	public void setPhoneKey(Long phoneKey) {
		this.phoneKey = phoneKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PHONE_TYPE_KEY")
	public PhoneType getPhoneType() {
		return this.phoneType;
	}

	public void setPhoneType(PhoneType phoneType) {
		this.phoneType = phoneType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_KEY")
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column(name = "PHONE_NUMBER", precision = 22, scale = 0)
	public Long getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@Column(name = "AREA_CODE", precision = 22, scale = 0)
	public Long getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(Long areaCode) {
		this.areaCode = areaCode;
	}

}