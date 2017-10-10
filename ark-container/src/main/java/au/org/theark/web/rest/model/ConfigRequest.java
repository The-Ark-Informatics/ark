package au.org.theark.web.rest.model;

public class ConfigRequest {
	
	private Long id;
	
	private Long studyId;
	private Boolean statusAllowed;
	private String customFieldName;
	private Boolean dobAllowed;
	private Boolean ageAllowed;
	private Boolean inbreedAllowed;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getStudyId() {
		return studyId;
	}
	public void setStudyId(Long studyId) {
		this.studyId = studyId;
	}
	public Boolean getStatusAllowed() {
		return statusAllowed;
	}
	public void setStatusAllowed(Boolean statusAllowed) {
		this.statusAllowed = statusAllowed;
	}
	public String getCustomFieldName() {
		return customFieldName;
	}
	public void setCustomFieldName(String customFieldName) {
		this.customFieldName = customFieldName;
	}
	public Boolean getDobAllowed() {
		return dobAllowed;
	}
	public void setDobAllowed(Boolean dobAllowed) {
		this.dobAllowed = dobAllowed;
	}
	public Boolean getAgeAllowed() {
		return ageAllowed;
	}
	public void setAgeAllowed(Boolean ageAllowed) {
		this.ageAllowed = ageAllowed;
	}
	public Boolean getInbreedAllowed() {
		return inbreedAllowed;
	}
	public void setInbreedAllowed(Boolean inbreedAllowed) {
		this.inbreedAllowed = inbreedAllowed;
	}
	

}
