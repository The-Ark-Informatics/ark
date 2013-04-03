package au.org.theark.core.vo;

import java.util.HashMap;

public class DataExtractionVO {
	private HashMap<String, ExtractionVO> demographicData = new HashMap<String, ExtractionVO>();
	private HashMap<String, ExtractionVO> biocollectionData = new HashMap<String, ExtractionVO>();
	private HashMap<String, ExtractionVO> biospecimenData = new HashMap<String, ExtractionVO>();
//	private List<BioCollection> biocollections = new ArrayList<BioCollection>();
//	private List<Biospecimen> biospecimens = new ArrayList<Biospecimen>();
	//maybe it needs something more like above
																//but if we just order/group by subject uid its should be fine

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

	public HashMap<String, ExtractionVO> getBiocollectionData() {
		return biocollectionData;
	}

	public void setBiocollectionData(HashMap<String, ExtractionVO> biocollectionData) {
		this.biocollectionData = biocollectionData;
	}

	public HashMap<String, ExtractionVO> getBiospecimenData() {
		return biospecimenData;
	}

	public void setBiospecimenData(HashMap<String, ExtractionVO> biospecimenData) {
		this.biospecimenData = biospecimenData;
	}

	/* I guess the key to each of these is infact a biospecimen uid and not a subject uid, and subjectUID is just a key value pair?  i am open to suggestions */
	private HashMap<String, ExtractionVO> biospecimenCustomData = new HashMap<String, ExtractionVO>();

	public HashMap<String, ExtractionVO> getBiospecimenCustomData() {
		return biospecimenCustomData;
	}

	public void setBiospecimenCustomData(HashMap<String, ExtractionVO> biospecimenCustomData) {
		this.biospecimenCustomData = biospecimenCustomData;
	}

	/* I guess the key to each of these is infact a biocollection uid and not a subject uid, and subjectUID is just a key value pair?  i am open to suggestions */
	private HashMap<String, ExtractionVO> biocollectionCustomData = new HashMap<String, ExtractionVO>();

	public HashMap<String, ExtractionVO> getBiocollectionCustomData() {
		return biocollectionCustomData;
	}

	public void setBiocollectionCustomData(HashMap<String, ExtractionVO> biocollectionCustomData) {
		this.biocollectionCustomData = biocollectionCustomData;
	}
	
	
	/* I guess the key to each of these is infact a pheno id and not a subject uid, and subjectUID is just a key value pair?  i am open to suggestions */
	private HashMap<String, ExtractionVO> phenoCustomData = new HashMap<String, ExtractionVO>();

	public HashMap<String, ExtractionVO> getPhenoCustomData() {
		return phenoCustomData;
	}

	public void setPhenoCustomData(HashMap<String, ExtractionVO> phenoCustomData) {
		this.phenoCustomData = phenoCustomData;
	}

	
	
}
