package au.org.theark.core.model.worktracking.entity;

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

@Entity
@Table(name = "RESEARCHER", schema = Constants.ADMIN_SCHEMA)
public class Researcher implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long								id;
	private String								firstName;
	private String								lastName;
	private String								institute;
	private String								address;
	private ResearcherRole						researcherRole;
	private ResearcherStatus					researcherStatus;
	private Date								lastActiveDate;
	private String								officePhone;
	private String								mobile;
	private String								email;
	private String								fax;
	private String								comment;
	private BillingType							billingType;
	private Long								acccountNumber;
	private Integer								bsb;
	private String								bank;
	private String								accountName;
	
	public Researcher() {
	}

	public Researcher(Long id) {
		this.id = id;
	}

	public Researcher(Long id, String firstName, String lastName,
			String institute, String address, ResearcherRole researcherRole,
			ResearcherStatus researcherStatus, Date lastActiveDate,
			String officePhone, String mobile, String email, String fax,
			String comment, BillingType billingType, Long acccountNumber,
			Integer bsb, String bank, String accountName) {
		
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.institute = institute;
		this.address = address;
		this.researcherRole = researcherRole;
		this.researcherStatus = researcherStatus;
		this.lastActiveDate = lastActiveDate;
		this.officePhone = officePhone;
		this.mobile = mobile;
		this.email = email;
		this.fax = fax;
		this.comment = comment;
		this.billingType = billingType;
		this.acccountNumber = acccountNumber;
		this.bsb = bsb;
		this.bank = bank;
		this.accountName = accountName;
	}

	@Id
	@SequenceGenerator(name = "reseacher_generator", sequenceName = "RESEARCHER_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "reseacher_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "FIRST_NAME", length = 30)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "LAST_NAME", length = 45)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "INSTITUTE", length = 50)
	public String getInstitute() {
		return institute;
	}

	public void setInstitute(String institute) {
		this.institute = institute;
	}

	@Column(name = "ADDRESS", length = 255)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLE_ID")
	public ResearcherRole getResearcherRole() {
		return researcherRole;
	}

	public void setResearcherRole(ResearcherRole researcherRole) {
		this.researcherRole = researcherRole;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_ID")
	public ResearcherStatus getResearcherStatus() {
		return researcherStatus;
	}

	public void setResearcherStatus(ResearcherStatus researcherStatus) {
		this.researcherStatus = researcherStatus;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_ACTIVE_DATE", length = 7)
	public Date getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(Date lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	@Column(name = "OFFICE_PHONE", length = 12)
	public String getOfficePhone() {
		return officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	@Column(name = "MOBILE", length = 12)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "EMAIL", length = 45)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "FAX", length = 12)
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(name = "COMMENT", length = 255)
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BILLING_TYPE_ID")
	public BillingType getBillingType() {
		return billingType;
	}

	public void setBillingType(BillingType billingType) {
		this.billingType = billingType;
	}

	@Column(name = "ACCOUNT_NUMBER")
	public Long getAcccountNumber() {
		return acccountNumber;
	}
	
	public void setAcccountNumber(Long acccountNumber) {
		this.acccountNumber = acccountNumber;
	}

	@Column(name = "BSB")
	public Integer getBsb() {
		return bsb;
	}

	public void setBsb(Integer bsb) {
		this.bsb = bsb;
	}

	@Column(name = "BANK", length = 50)
	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	@Column(name = "ACCOUNT_NAME", length = 50)
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
}
