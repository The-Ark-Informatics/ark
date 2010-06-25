package au.org.theark.study.model.entity;

import java.sql.Blob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Document entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "DOCUMENT", schema = "ETA")
public class Document implements java.io.Serializable {

	// Fields

	private Long documentKey;
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
	public Document(Long documentKey) {
		this.documentKey = documentKey;
	}

	/** full constructor */
	public Document(Long documentKey, Correspondence correspondence,
			StudyComp studyComp, String name, String description,
			Blob documentContent) {
		this.documentKey = documentKey;
		this.correspondence = correspondence;
		this.studyComp = studyComp;
		this.name = name;
		this.description = description;
		this.documentContent = documentContent;
	}

	// Property accessors
	@Id
	@Column(name = "DOCUMENT_KEY", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getDocumentKey() {
		return this.documentKey;
	}

	public void setDocumentKey(Long documentKey) {
		this.documentKey = documentKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CORRESPONDENCE_KEY")
	public Correspondence getCorrespondence() {
		return this.correspondence;
	}

	public void setCorrespondence(Correspondence correspondence) {
		this.correspondence = correspondence;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_COMP_KEY")
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