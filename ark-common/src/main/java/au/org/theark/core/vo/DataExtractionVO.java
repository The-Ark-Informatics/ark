package au.org.theark.core.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import au.org.theark.core.model.lims.entity.Biospecimen;

public class DataExtractionVO {
	private HashMap<String, ExtractionVO> demographicData = new HashMap<String, ExtractionVO>();
	private List<Biospecimen> biospecimens = new ArrayList<Biospecimen>();//maybe it needs something more like above
																//but if we just order/broup by subject uid its should be fine

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

	public void setBiospecimens(List<Biospecimen> biospecimens) {
		this.biospecimens = biospecimens;
	}

	public List<Biospecimen> getBiospecimens() {
		return this.biospecimens;
	}
	
	
}
