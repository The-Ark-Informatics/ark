package au.org.theark.core.model.study.entity;

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
import javax.persistence.UniqueConstraint;

import au.org.theark.core.Constants;

/**
 * Phone entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PHONE",  schema = Constants.STUDY_SCHEMA, uniqueConstraints={@UniqueConstraint(columnNames={"AREA_CODE", "PHONE_NUMBER", "PERSON_ID"})})
public class Phone implements java.io.Serializable {

	// Fields

	private Long id;
	private PhoneType phoneType;
	private Person person;
	private String phoneNumber;
	private String areaCode;
	private PhoneStatus phoneStatus;
	private String source;
	private Date dateReceived; 
	private YesNo silentMode;
	private String comment;
	

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

	@Column(name = "PHONE_NUMBER", length = 10)
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	@Column(name = "AREA_CODE", length = 10)
	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PHONE_STATUS_ID")
	public PhoneStatus getPhoneStatus() {
		return phoneStatus;
	}

	public void setPhoneStatus(PhoneStatus phoneStatus) {
		this.phoneStatus = phoneStatus;
	}

	@Column(name = "SOURCE", length = 500)
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_RECEIVED", length = 7)
	public Date getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(Date dateReceived) {
		this.dateReceived = dateReceived;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SILENT")
	public YesNo getSilentMode() {
		return silentMode;
	}

	public void setSilentMode(YesNo silentMode) {
		this.silentMode = silentMode;
	}

	@Column(name = "COMMENT", length = 1000)
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
}