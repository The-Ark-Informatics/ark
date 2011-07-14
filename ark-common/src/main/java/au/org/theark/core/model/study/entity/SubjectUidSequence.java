package au.org.theark.core.model.study.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import au.org.theark.core.Constants;

/**
 * StudyStatus entity. 
 * @author elam
 */
@Entity
@Table(name = "SUBJECTUID_SEQUENCE", schema = Constants.STUDY_SCHEMA)
public class SubjectUidSequence implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Fields
	private String studyNameId;
	private Integer uidSequence;
	private Boolean insertLock;

	// Constructors
	/** default constructor */
	public SubjectUidSequence() {
	}

	/** minimal constructor */
	public SubjectUidSequence(String studyNameId, Integer uidSequence, Boolean insertLock){
		this.studyNameId = studyNameId;
		this.uidSequence = uidSequence;
		this.insertLock = insertLock;
	}

	// Property accessors
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