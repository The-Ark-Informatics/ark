package au.org.theark.core.model.study.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * LinkStudyStudycomp entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "LINK_STUDY_STUDYCOMP", schema = "ETA")
public class LinkStudyStudycomp implements java.io.Serializable {

	// Fields

	private Long linkStudyStudycompKey;
	private StudyComp studyComp;
	private Study study;
	private StudyCompStatus studyCompStatus;

	// Constructors

	/** default constructor */
	public LinkStudyStudycomp() {
	}

	/** minimal constructor */
	public LinkStudyStudycomp(Long linkStudyStudycompKey) {
		this.linkStudyStudycompKey = linkStudyStudycompKey;
	}

	/** full constructor */
	public LinkStudyStudycomp(Long linkStudyStudycompKey, StudyComp studyComp,
			Study study, StudyCompStatus studyCompStatus) {
		this.linkStudyStudycompKey = linkStudyStudycompKey;
		this.studyComp = studyComp;
		this.study = study;
		this.studyCompStatus = studyCompStatus;
	}

	// Property accessors
	@Id
	@Column(name = "LINK_STUDY_STUDYCOMP_KEY", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getLinkStudyStudycompKey() {
		return this.linkStudyStudycompKey;
	}

	public void setLinkStudyStudycompKey(Long linkStudyStudycompKey) {
		this.linkStudyStudycompKey = linkStudyStudycompKey;
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
	@JoinColumn(name = "STUDY_COMP_STATUS_KEY")
	public StudyCompStatus getStudyCompStatus() {
		return this.studyCompStatus;
	}

	public void setStudyCompStatus(StudyCompStatus studyCompStatus) {
		this.studyCompStatus = studyCompStatus;
	}

}