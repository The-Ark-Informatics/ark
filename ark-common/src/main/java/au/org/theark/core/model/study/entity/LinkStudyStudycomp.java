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
 * LinkStudyStudycomp entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "LINK_STUDY_STUDYCOMP", schema = Constants.STUDY_SCHEMA)
public class LinkStudyStudycomp implements java.io.Serializable {

	// Fields

	private Long id;
	private StudyComp studyComp;
	private Study study;
	private StudyCompStatus studyCompStatus;

	// Constructors

	/** default constructor */
	public LinkStudyStudycomp() {
	}

	/** minimal constructor */
	public LinkStudyStudycomp(Long id) {
		this.id = id;
	}

	/** full constructor */
	public LinkStudyStudycomp(Long id, StudyComp studyComp,
			Study study, StudyCompStatus studyCompStatus) {
		this.id = id;
		this.studyComp = studyComp;
		this.study = study;
		this.studyCompStatus = studyCompStatus;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getid() {
		return this.id;
	}

	public void setid(Long id) {
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
	@JoinColumn(name = "STUDY_COMP_STATUS_ID")
	public StudyCompStatus getStudyCompStatus() {
		return this.studyCompStatus;
	}

	public void setStudyCompStatus(StudyCompStatus studyCompStatus) {
		this.studyCompStatus = studyCompStatus;
	}

}