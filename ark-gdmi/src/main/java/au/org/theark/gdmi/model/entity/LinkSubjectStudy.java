package au.org.theark.gdmi.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * LinkSubjectStudy entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "LINK_SUBJECT_STUDY", schema = "ETA")
public class LinkSubjectStudy implements java.io.Serializable {

	// Fields

	private long linkSubjectStudyKey;
	private long personKey;
	private long studyKey;
	private long subjectStatusKey;

	// Constructors

	/** default constructor */
	public LinkSubjectStudy() {
	}

	/** minimal constructor */
	public LinkSubjectStudy(long linkSubjectStudyKey) {
		this.linkSubjectStudyKey = linkSubjectStudyKey;
	}

	/** full constructor */
	public LinkSubjectStudy(long linkSubjectStudyKey, long personKey,
			long studyKey, long subjectStatusKey) {
		this.linkSubjectStudyKey = linkSubjectStudyKey;
		this.personKey = personKey;
		this.studyKey = studyKey;
		this.subjectStatusKey = subjectStatusKey;
	}

	// Property accessors
	@Id
	@Column(name = "LINK_SUBJECT_STUDY_KEY", unique = true, nullable = false, precision = 22, scale = 0)
	public long getLinkSubjectStudyKey() {
		return this.linkSubjectStudyKey;
	}

	public void setLinkSubjectStudyKey(long linkSubjectStudyKey) {
		this.linkSubjectStudyKey = linkSubjectStudyKey;
	}

	@Column(name = "PERSON_KEY", precision = 22, scale = 0)
	public long getPersonKey() {
		return this.personKey;
	}

	public void setPersonKey(long personKey) {
		this.personKey = personKey;
	}

	@Column(name = "STUDY_KEY", precision = 22, scale = 0)
	public long getStudyKey() {
		return this.studyKey;
	}

	public void setStudyKey(long studyKey) {
		this.studyKey = studyKey;
	}

	@Column(name = "SUBJECT_STATUS_KEY", precision = 22, scale = 0)
	public long getSubjectStatusKey() {
		return this.subjectStatusKey;
	}

	public void setSubjectStatusKey(long subjectStatusKey) {
		this.subjectStatusKey = subjectStatusKey;
	}

}