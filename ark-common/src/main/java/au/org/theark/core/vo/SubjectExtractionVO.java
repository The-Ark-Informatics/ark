package au.org.theark.core.vo;

import java.util.HashMap;

public class SubjectExtractionVO {
//	private String subjectId;//keeping it a bit stupid and ignorant of entities for rapid moves to other formats
	//private String studyId;
	private HashMap<String, String> keyValues = new HashMap<String, String>();
	
	/*public String getSubjectId() {
		return subjectId;
	}
	
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getStudyId() {
		return studyId;
	}
	
	public void setStudyId(String studyId) {
		this.studyId = studyId;
	}
	*/
	public java.util.HashMap<String, String> getKeyValues() {
		return keyValues;
	}
	public void setKeyValues(java.util.HashMap<String, String> keyValues) {
		this.keyValues = keyValues;
	}
	
	
}
