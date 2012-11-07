package au.org.theark.core.vo;

import java.util.HashMap;

public class DataExtractionVO {
	private HashMap<String, SubjectExtractionVO> subjectAndData = new HashMap<String, SubjectExtractionVO>();

	public HashMap<String, SubjectExtractionVO> getSubjectAndData() {
		return subjectAndData;
	}

	public void setSubjectAndData(HashMap<String, SubjectExtractionVO> subjectAndData) {
		this.subjectAndData = subjectAndData;
	}
	
}
