package au.org.theark.study.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * LinkSubjectStudycomp entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "LINK_SUBJECT_STUDYCOMP", schema = "ETA")
public class LinkSubjectStudycomp implements java.io.Serializable {

	// Fields

	private Long linkSubjectStudycompKey;
	private StudyComp studyComp;
	private Study study;
	private Person person;
	private Long compStatusKey;

	// Constructors

	/** default constructor */
	public LinkSubjectStudycomp() {
	}

	/** minimal constructor */
	public LinkSubjectStudycomp(Long linkSubjectStudycompKey) {
		this.linkSubjectStudycompKey = linkSubjectStudycompKey;
	}

	/** full constructor */
	public LinkSubjectStudycomp(Long linkSubjectStudycompKey,
			StudyComp studyComp, Study study, Person person, Long compStatusKey) {
		this.linkSubjectStudycompKey = linkSubjectStudycompKey;
		this.studyComp = studyComp;
		this.study = study;
		this.person = person;
		this.compStatusKey = compStatusKey;
	}

	// Property accessors
	@Id
	@Column(name = "LINK_SUBJECT_STUDYCOMP_KEY", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getLinkSubjectStudycompKey() {
		return this.linkSubjectStudycompKey;
	}

	public void setLinkSubjectStudycompKey(Long linkSubjectStudycompKey) {
		this.linkSubjectStudycompKey = linkSubjectStudycompKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_COMP_KEY")
	public StudyComp getStudyComp() {
		return this.studyComp;
	}

	public void setStudyComp(StudyComp studyComp) {
		this.studyComp = studyComp;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_KEY")
	public Study getStudy() {
		return this.study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBJECT_KEY")
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column(name = "COMP_STATUS_KEY", precision = 22, scale = 0)
	public Long getCompStatusKey() {
		return this.compStatusKey;
	}

	public void setCompStatusKey(Long compStatusKey) {
		this.compStatusKey = compStatusKey;
	}

}