package au.org.theark.core.vo;

public class SubjectExtractionVO {
//	private String subjectId;//keeping it a bit stupid and ignorant of entities for rapid moves to other formats
	private String studyId;
	private java.util.HashMap<String, String> keyValue;
	
	/*public String getSubjectId() {
		return subjectId;
	}
	
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}*/
	
	public String getStudyId() {
		return studyId;
	}
	
	public void setStudyId(String studyId) {
		this.studyId = studyId;
	}
	
	public java.util.HashMap<String, String> getKeyValue() {
		return keyValue;
	}
	public void setKeyValue(java.util.HashMap<String, String> keyValue) {
		this.keyValue = keyValue;
	}
	
	
}
