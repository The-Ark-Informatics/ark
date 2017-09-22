package au.org.theark.web.rest.model;

public class GetSubjectRequest {
	
	private String subjectUID;
	
	private Long studyId;

	public String getSubjectUID() {
		return subjectUID;
	}

	public void setSubjectUID(String subjectUID) {
		this.subjectUID = subjectUID;
	}

	public Long getStudyId() {
		return studyId;
	}

	public void setStudyId(Long studyId) {
		this.studyId = studyId;
	}
	

}
