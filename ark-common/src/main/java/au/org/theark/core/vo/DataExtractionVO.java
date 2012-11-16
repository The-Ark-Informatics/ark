package au.org.theark.core.vo;

import java.util.HashMap;

public class DataExtractionVO {
	private HashMap<String, ExtractionVO> demographicData = new HashMap<String, ExtractionVO>();

	public HashMap<String, ExtractionVO> getDemographicData() {
		return demographicData;
	}

	public void setDemographicData(HashMap<String, ExtractionVO> subjectAndData) {
		this.demographicData = subjectAndData;
	}
	

	private HashMap<String, ExtractionVO> subjectCustomData = new HashMap<String, ExtractionVO>();

	public HashMap<String, ExtractionVO> getSubjectCustomData() {
		return subjectCustomData;
	}

	public void setSubjectCustomData(HashMap<String, ExtractionVO> subjectCustomData) {
		this.subjectCustomData = subjectCustomData;
	}
	
	
	
}
