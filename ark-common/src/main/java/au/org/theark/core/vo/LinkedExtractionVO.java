package au.org.theark.core.vo;

import java.util.Date;
import java.util.LinkedHashMap;

public class LinkedExtractionVO {
	/*
	 * This is only used in pheno now.  Maybe subclass/interface this later instead
	 */
	private String subjectUid;//keeping it a bit stupid and ignorant of entities for rapid moves to other formats
	// Pheno related field, for date of particular record
	private Date recordDate;
	//private String studyId;
	private LinkedHashMap<String, String> keyValues = new LinkedHashMap<String, String>();
	
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

	public java.util.LinkedHashMap<String, String> getKeyValues() {
		return keyValues;
	}
	public void setKeyValues(java.util.LinkedHashMap<String, String> keyValues) {
		this.keyValues = keyValues;
	}
	
}
