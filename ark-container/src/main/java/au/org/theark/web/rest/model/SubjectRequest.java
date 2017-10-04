package au.org.theark.web.rest.model;

import java.util.Date;

public class SubjectRequest {
	
	private Long id;
	
	private String subjectUID;
	
	private String firstName;
	
	private String lastName;
	
	private Date dateOfBirth;
	
	private String genderTypeName;
	
	private String vitalStatusName;
	
	private String subjectStatusName;
	
	private Long studyId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubjectUID() {
		return subjectUID;
	}

	public void setSubjectUID(String subjectUID) {
		this.subjectUID = subjectUID;
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

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGenderTypeName() {
		return genderTypeName;
	}

	public void setGenderTypeName(String genderTypeName) {
		this.genderTypeName = genderTypeName;
	}

	public String getVitalStatusName() {
		return vitalStatusName;
	}

	public void setVitalStatusName(String vitalStatusName) {
		this.vitalStatusName = vitalStatusName;
	}

	public String getSubjectStatusName() {
		return subjectStatusName;
	}

	public void setSubjectStatusName(String subjectStatusName) {
		this.subjectStatusName = subjectStatusName;
	}

	public Long getStudyId() {
		return studyId;
	}

	public void setStudyId(Long studyId) {
		this.studyId = studyId;
	}
	

}
