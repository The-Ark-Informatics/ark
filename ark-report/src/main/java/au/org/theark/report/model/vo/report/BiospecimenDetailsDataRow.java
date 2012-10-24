package au.org.theark.report.model.vo.report;

import java.io.Serializable;

public class BiospecimenDetailsDataRow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String studyName;
	private String subjectUId;
	private Long 	biospecimenId;
	private String	biospecimenUid;
	private String parentId;
	private String sampleType;
	private Double quantity;
	private String initialStatus;
	private String site;
	private String freezer;
	private String rack;
	private String box;
	
	public BiospecimenDetailsDataRow() {
	}

	public BiospecimenDetailsDataRow(String studyName, String subjectUId,
			Long biospecimenId, String parentId, String sampleType,
			Double quantity, String initialStatus, String site, String freezer,
			String rack, String box, String biospecimenUid) {
		this.studyName = studyName;
		this.subjectUId = subjectUId;
		this.biospecimenId = biospecimenId;
		this.parentId = parentId;
		this.sampleType = sampleType;
		this.quantity = quantity;
		this.initialStatus = initialStatus;
		this.site = site;
		this.freezer = freezer;
		this.rack = rack;
		this.box = box;
		this.biospecimenUid=biospecimenUid;
	}

	public String getStudyName() {
		return studyName;
	}

	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}

	public String getSubjectUId() {
		return subjectUId;
	}

	public void setSubjectUId(String subjectUId) {
		this.subjectUId = subjectUId;
	}

	public Long getBiospecimenId() {
		return biospecimenId;
	}

	public void setBiospecimenId(Long biospecimenId) {
		this.biospecimenId = biospecimenId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getSampleType() {
		return sampleType;
	}

	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public String getInitialStatus() {
		return initialStatus;
	}

	public void setInitialStatus(String initialStatus) {
		this.initialStatus = initialStatus;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getFreezer() {
		return freezer;
	}

	public void setFreezer(String freezer) {
		this.freezer = freezer;
	}

	public String getRack() {
		return rack;
	}

	public void setRack(String rack) {
		this.rack = rack;
	}

	public String getBox() {
		return box;
	}

	public void setBox(String box) {
		this.box = box;
	}

	public String getBiospecimenUid() {
		return biospecimenUid;
	}

	public void setBiospecimenUid(String biospecimenUid) {
		this.biospecimenUid = biospecimenUid;
	}
}
