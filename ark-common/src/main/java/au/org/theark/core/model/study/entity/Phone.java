package au.org.theark.core.model.study.entity;

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

import au.org.theark.core.Constants;

/**
 * Phone entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PHONE",  schema = Constants.STUDY_SCHEMA)
public class Phone implements java.io.Serializable {

	// Fields

	private Long id;
	private PhoneType phoneType;
	private Person person;
	private String phoneNumber;
	private String areaCode;

	// Constructors

	/** default constructor */
	public Phone() {
	}

	/** minimal constructor */
	public Phone(Long id) {
		this.id = id;
	}

	/** full constructor */
	public Phone(Long id, PhoneType phoneType, Person person,
			String phoneNumber,String areaCode) {
		this.id = id;
		this.phoneType = phoneType;
		this.person = person;
		this.phoneNumber = phoneNumber;
		this.areaCode = areaCode;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name="phone_generator", sequenceName="PHONE_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "phone_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PHONE_TYPE_ID")
	public PhoneType getPhoneType() {
		return this.phoneType;
	}

	public void setPhoneType(PhoneType phoneType) {
		this.phoneType = phoneType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_ID")
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column(name = "PHONE_NUMBER", length = 50)
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@Column(name = "AREA_CODE", precision = 22, scale = 0)
	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
}