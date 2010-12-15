package au.org.theark.core.model.study.entity;

import java.sql.Blob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import au.org.theark.core.Constants;

/**
 * Document entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "DOCUMENT", schema = Constants.STUDY_SCHEMA)
public class Document implements java.io.Serializable {

	// Fields

	private Long id;
	private Correspondence correspondence;
	private StudyComp studyComp;
	private String name;
	private String description;
	private Blob documentContent;

	// Constructors

	/** default constructor */
	public Document() {
	}

	/** minimal constructor */
	public Document(Long id) {
		this.id = id;
	}

	/** full constructor */
	public Document(Long id, Correspondence correspondence,
			StudyComp studyComp, String name, String description,
			Blob documentContent) {
		this.id = id;
		this.correspondence = correspondence;
		this.studyComp = studyComp;
		this.name = name;
		this.description = description;
		this.documentContent = documentContent;
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
	@JoinColumn(name = "CORRESPONDENCE_ID")
	public Correspondence getCorrespondence() {
		return this.correspondence;
	}

	public void setCorrespondence(Correspondence correspondence) {
		this.correspondence = correspondence;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_COMP_ID")
	public StudyComp getStudyComp() {
		return this.studyComp;
	}

	public void setStudyComp(StudyComp studyComp) {
		this.studyComp = studyComp;
	}

	@Column(name = "NAME", length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "DOCUMENT_CONTENT")
	public Blob getDocumentContent() {
		return this.documentContent;
	}

	public void setDocumentContent(Blob documentContent) {
		this.documentContent = documentContent;
	}

}