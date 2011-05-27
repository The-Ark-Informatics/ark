package au.org.theark.report.web.component.viewReport.consentDetails;

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

	public String getConsentStatus() {
		return consentStatus;
	}
	
	public String getSubjectStatus() {
		return subjectStatus;
	}

	public String getTitle() {
		return title;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public String getSuburb() {
		return suburb;
	}

	public String getState() {
		return state;
	}
	
	public String getPostcode() {
		return postcode;
	}

	public String getCountry() {
		return country;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public String getEmail() {
		return email;
	}

	public String getSex() {
		return sex;
	}

	public Date getConsentDate() {
		return consentDate;
	}

}
