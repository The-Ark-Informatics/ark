package au.org.theark.core.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.Biospecimen;

public class DataExtractionVO {
	private HashMap<String, ExtractionVO> demographicData = new HashMap<String, ExtractionVO>();
	private HashMap<String, ExtractionVO> biocollectionData = new HashMap<String, ExtractionVO>();
	private HashMap<String, ExtractionVO> biospecimenData = new HashMap<String, ExtractionVO>();
	private List<BioCollection> biocollections = new ArrayList<BioCollection>();
	private List<Biospecimen> biospecimens = new ArrayList<Biospecimen>();
	//maybe it needs something more like above
																//but if we just order/group by subject uid its should be fine

	public HashMap<String, ExtractionVO> getDemographicData() {
		return biospecimenData;
	}

	public void setDemographicData(HashMap<String, ExtractionVO> subjectAndData) {
		this.biospecimenData = subjectAndData;
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


	public void setBiocollections(List<BioCollection> biocollections) {
		this.biocollections = biocollections;
	}

	public List<BioCollection> getBiocollections() {
		return this.biocollections;
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
