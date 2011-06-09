package au.org.theark.report.model.vo.report;

import java.io.Serializable;
import java.util.Date;

public class ConsentDetailsDataRow implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected String subjectUID;
	protected String consentStatus;
	protected String subjectStatus;
	protected String title;
	protected String firstName;
	protected String lastName;
	protected String streetAddress;
	protected String suburb;
	protected String state;
	protected String postcode;
	protected String country;
	protected String workPhone;
	protected String homePhone;
	protected String email;
	protected String sex;
	protected Date consentDate;

	public ConsentDetailsDataRow() {
		
	}
	
	public ConsentDetailsDataRow(String subjectUID, String consentStatus, String subjectStatus, 
			String title, String firstName, String lastName,
			String streetAddress, String suburb, String state, String postcode, String country, 
			String workPhone, String homePhone, String email, 
			String sex, Date consentDate) {
		super();
		this.subjectUID = subjectUID;
		this.consentStatus = consentStatus;
		this.subjectStatus = subjectStatus;
		this.title = title;
		this.firstName = firstName;
		this.lastName = lastName;
		this.streetAddress = streetAddress;
		this.suburb = suburb;
		this.state = state;
		this.postcode = postcode;
		this.country = country;
		this.workPhone = workPhone;
		this.homePhone = homePhone;
		this.email = email;
		this.sex = sex;
		this.consentDate = consentDate;
	}

	public String getSubjectUID() {
		return subjectUID;
	}

	public void setSubjectUID(String subjectUID) {
		this.subjectUID = subjectUID;
	}

	public String getConsentStatus() {
		return consentStatus;
	}

	public void setConsentStatus(String consentStatus) {
		this.consentStatus = consentStatus;
	}

	public String getSubjectStatus() {
		return subjectStatus;
	}

	public void setSubjectStatus(String subjectStatus) {
		this.subjectStatus = subjectStatus;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getSuburb() {
		return suburb;
	}

	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getConsentDate() {
		return consentDate;
	}

	public void setConsentDate(Date consentDate) {
		this.consentDate = consentDate;
	}

}
