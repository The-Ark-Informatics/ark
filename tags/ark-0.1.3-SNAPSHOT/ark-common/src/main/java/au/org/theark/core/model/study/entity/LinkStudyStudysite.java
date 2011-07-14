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
 * LinkStudyStudysite entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "LINK_STUDY_STUDYSITE", schema = Constants.STUDY_SCHEMA)
public class LinkStudyStudysite implements java.io.Serializable {

	// Fields

	private Long id;
	private Study study;
	private StudySite studySite;

	// Constructors

	/** default constructor */
	public LinkStudyStudysite() {
	}

	/** minimal constructor */
	public LinkStudyStudysite(Long id) {
		this.id = id;
	}

	/** full constructor */
	public LinkStudyStudysite(Long id, Study study,
			StudySite studySite) {
		this.id = id;
		this.study = study;
		this.studySite = studySite;
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
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return this.study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_SITE_ID")
	public StudySite getStudySite() {
		return this.studySite;
	}

	public void setStudySite(StudySite studySite) {
		this.studySite = studySite;
	}

}