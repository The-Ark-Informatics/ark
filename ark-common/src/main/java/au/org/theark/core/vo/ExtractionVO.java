package au.org.theark.core.vo;

import java.util.Date;
import java.util.HashMap;

import au.org.theark.core.model.study.entity.CustomFieldGroup;

public class ExtractionVO {
	/*
	 * This is only used in pheno now.  Maybe subclass/interface this later instead
	 */
	private String subjectUid;//keeping it a bit stupid and ignorant of entities for rapid moves to other formats
	// Pheno related field, for date of particular record
	private Date recordDate;
	private String collectionName;
	//private String studyId;
	private HashMap<String, String> keyValues = new HashMap<String, String>();
	
	public String getSubjectUid() {
		return subjectUid;
	}
	
	public void setSubjectUid(String subjectUid) {
		this.subjectUid = subjectUid;
	}
	
	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public java.util.HashMap<String, String> getKeyValues() {
		return keyValues;
	}
	public void setKeyValues(java.util.HashMap<String, String> keyValues) {
		this.keyValues = keyValues;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
}
