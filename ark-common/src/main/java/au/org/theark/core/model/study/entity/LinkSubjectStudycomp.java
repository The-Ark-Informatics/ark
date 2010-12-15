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
 * LinkSubjectStudycomp entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "LINK_SUBJECT_STUDYCOMP", schema = Constants.STUDY_SCHEMA)
public class LinkSubjectStudycomp implements java.io.Serializable {

	// Fields

	private Long id;
	private StudyComp studyComp;
	private Study study;
	private Person person;
	private Long studyCompStatusId;

	// Constructors

	/** default constructor */
	public LinkSubjectStudycomp() {
	}

	/** minimal constructor */
	public LinkSubjectStudycomp(Long id) {
		this.id = id;
	}

	/** full constructor */
	public LinkSubjectStudycomp(Long id,
			StudyComp studyComp, Study study, Person person, Long studyCompStatusId) {
		this.id = id;
		this.studyComp = studyComp;
		this.study = study;
		this.person = person;
		this.studyCompStatusId = studyCompStatusId;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_COMP_ID")
	public StudyComp getStudyComp() {
		return this.studyComp;
	}

	public void setStudyComp(StudyComp studyComp) {
		this.studyComp = studyComp;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return this.study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_SUBJECT_ID")
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column(name = "STUDY_COMP_STATUS_ID", precision = 22, scale = 0)
	public Long getStudyCompStatusId() {
		return this.studyCompStatusId;
	}

	public void setStudyCompStatusId(Long studyCompStatusId) {
		this.studyCompStatusId = studyCompStatusId;
	}

}