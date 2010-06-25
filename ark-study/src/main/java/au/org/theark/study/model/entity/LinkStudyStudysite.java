package au.org.theark.study.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * LinkStudyStudysite entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "LINK_STUDY_STUDYSITE", schema = "ETA")
public class LinkStudyStudysite implements java.io.Serializable {

	// Fields

	private Long linkStudyStudysiteKey;
	private Study study;
	private StudySite studySite;

	// Constructors

	/** default constructor */
	public LinkStudyStudysite() {
	}

	/** minimal constructor */
	public LinkStudyStudysite(Long linkStudyStudysiteKey) {
		this.linkStudyStudysiteKey = linkStudyStudysiteKey;
	}

	/** full constructor */
	public LinkStudyStudysite(Long linkStudyStudysiteKey, Study study,
			StudySite studySite) {
		this.linkStudyStudysiteKey = linkStudyStudysiteKey;
		this.study = study;
		this.studySite = studySite;
	}

	// Property accessors
	@Id
	@Column(name = "LINK_STUDY_STUDYSITE_KEY", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getLinkStudyStudysiteKey() {
		return this.linkStudyStudysiteKey;
	}

	public void setLinkStudyStudysiteKey(Long linkStudyStudysiteKey) {
		this.linkStudyStudysiteKey = linkStudyStudysiteKey;
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
	@JoinColumn(name = "STUDY_SITE_KEY")
	public StudySite getStudySite() {
		return this.studySite;
	}

	public void setStudySite(StudySite studySite) {
		this.studySite = studySite;
	}

}