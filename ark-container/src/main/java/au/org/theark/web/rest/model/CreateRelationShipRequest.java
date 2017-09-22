package au.org.theark.web.rest.model;

public class CreateRelationShipRequest {
	
	private String subjectUID;
	
	private String relativeUID;
	
	private Long studyId;
	
	private String relationType;
	
	private String relationShip;
	
	public String getSubjectUID() {
		return subjectUID;
	}

	public void setSubjectUID(String subjectUID) {
		this.subjectUID = subjectUID;
	}

	public String getRelativeUID() {
		return relativeUID;
	}

	public void setRelativeUID(String relativeUID) {
		this.relativeUID = relativeUID;
	}

	public Long getStudyId() {
		return studyId;
	}

	public void setStudyId(Long studyId) {
		this.studyId = studyId;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public String getRelationShip() {
		return relationShip;
	}

	public void setRelationShip(String relationShip) {
		this.relationShip = relationShip;
	}
	
	
	
	
	

}
