package au.org.theark.core.model.lims.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;

/**
 * @author nivedann
 *
 */
@Entity
@Table(name = "BIOCOLLECTIONUID_SEQUENCE", schema = Constants.LIMS_TABLE_SCHEMA)
public class BioCollectionUidSequence implements Serializable{
	
	

	private static final long serialVersionUID = 1L;

	private String				studyNameId;
	private Integer				uidSequence;
	private Boolean				insertLock;
	
	public BioCollectionUidSequence(){
		
	}
	
	
	@Id
	@Column(name = "STUDY_NAME_ID", unique = true, nullable = false)
	public String getStudyNameId() {
		return this.studyNameId;
	}

	public void setStudyNameId(String studyNameId) {
		this.studyNameId = studyNameId;
	}

	@Column(name = "UID_SEQUENCE", nullable = false)
	public Integer getUidSequence() {
		return this.uidSequence;
	}

	public void setUidSequence(Integer uidSequence) {
		this.uidSequence = uidSequence;
	}

	@Column(name = "INSERT_LOCK", nullable = false)
	public Boolean getInsertLock() {
		return insertLock;
	}

	public void setInsertLock(Boolean insertLock) {
		this.insertLock = insertLock;
	}

}
