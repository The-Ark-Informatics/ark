package au.org.theark.core.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap; 
import java.util.List;

public class DataExtractionVO {
	private HashMap<String, ExtractionVO> demographicData = new HashMap<String, ExtractionVO>();
	private List<LinkedExtractionVO> genoData = new ArrayList<LinkedExtractionVO>();//stepping away from previous pattern...seeing what best approach is
	private HashMap<String, ExtractionVO> biocollectionData = new HashMap<String, ExtractionVO>();
	private HashMap<String, ExtractionVO> biospecimenData = new HashMap<String, ExtractionVO>();
	private HashMap<String, ExtractionVO> subjectCustomData = new HashMap<String, ExtractionVO>();
	/*the key to each of these is infact a biospecimen uid and not a subject uid, and subjectUID is just a key value pair?  i am open to suggestions */
	private HashMap<String, ExtractionVO> biospecimenCustomData = new HashMap<String, ExtractionVO>();
	/* the key to each of these is infact a phenoCollection id and not a subject uid, and subjectUID is just a key value pair?  i am open to suggestions */
	//notethat the linked hash map this time.  this might help order by subject then phenocollectionid
	private HashMap<String, ExtractionVO> phenoCustomData = new LinkedHashMap<String, ExtractionVO>();
	/* the key to each of these is infact a biocollection uid and not a subject uid, and subjectUID is just a key value pair?  i am open to suggestions */
	private HashMap<String, ExtractionVO> biocollectionCustomData = new HashMap<String, ExtractionVO>();


	public HashMap<String, ExtractionVO> getDemographicData() {
		return demographicData;
	}

	public void setDemographicData(HashMap<String, ExtractionVO> subjectAndData) {
		this.demographicData = subjectAndData;
	}
	

	public List<LinkedExtractionVO> getGenoData() {
		return genoData;
	}

	public void setGenoData(List<LinkedExtractionVO> subjectAndData) {
		this.genoData = subjectAndData;
	}
	

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


	public HashMap<String, ExtractionVO> getBiospecimenCustomData() {
		return biospecimenCustomData;
	}

	public void setBiospecimenCustomData(HashMap<String, ExtractionVO> biospecimenCustomData) {
		this.biospecimenCustomData = biospecimenCustomData;
	}


	public HashMap<String, ExtractionVO> getBiocollectionCustomData() {
		return biocollectionCustomData;
	}

	public void setBiocollectionCustomData(HashMap<String, ExtractionVO> biocollectionCustomData) {
		this.biocollectionCustomData = biocollectionCustomData;
	}
	
	
	public HashMap<String, ExtractionVO> getPhenoCustomData() {
		return phenoCustomData;
	}

	public void setPhenoCustomData(HashMap<String, ExtractionVO> phenoCustomData) {
		this.phenoCustomData = phenoCustomData;
	}

	
	
}
