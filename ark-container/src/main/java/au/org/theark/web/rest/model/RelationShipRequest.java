package au.org.theark.web.rest.model;

public class RelationShipRequest {
	
	private Long id;
	
	private String subjectUID;
	
	private String relativeUID;
	
	private Long studyId;
	
	private String parentType;
	
	
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

	public String getParentType() {
		return parentType;
	}

	public void setParentType(String parentType) {
		this.parentType = parentType;
	}
	

}
