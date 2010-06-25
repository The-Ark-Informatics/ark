package au.org.theark.study.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * LinkStudySubstudy entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "LINK_STUDY_SUBSTUDY", schema = "ETA")
public class LinkStudySubstudy implements java.io.Serializable {

	// Fields

	private Long linkStudySubstudyKey;
	private Study studyByStudyKey;
	private Study studyBySubstudyKey;

	// Constructors

	/** default constructor */
	public LinkStudySubstudy() {
	}

	/** minimal constructor */
	public LinkStudySubstudy(Long linkStudySubstudyKey) {
		this.linkStudySubstudyKey = linkStudySubstudyKey;
	}

	/** full constructor */
	public LinkStudySubstudy(Long linkStudySubstudyKey, Study studyByStudyKey,
			Study studyBySubstudyKey) {
		this.linkStudySubstudyKey = linkStudySubstudyKey;
		this.studyByStudyKey = studyByStudyKey;
		this.studyBySubstudyKey = studyBySubstudyKey;
	}

	// Property accessors
	@Id
	@Column(name = "LINK_STUDY_SUBSTUDY_KEY", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getLinkStudySubstudyKey() {
		return this.linkStudySubstudyKey;
	}

	public void setLinkStudySubstudyKey(Long linkStudySubstudyKey) {
		this.linkStudySubstudyKey = linkStudySubstudyKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_KEY")
	public Study getStudyByStudyKey() {
		return this.studyByStudyKey;
	}

	public void setStudyByStudyKey(Study studyByStudyKey) {
		this.studyByStudyKey = studyByStudyKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBSTUDY_KEY")
	public Study getStudyBySubstudyKey() {
		return this.studyBySubstudyKey;
	}

	public void setStudyBySubstudyKey(Study studyBySubstudyKey) {
		this.studyBySubstudyKey = studyBySubstudyKey;
	}

}