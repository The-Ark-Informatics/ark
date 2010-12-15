package au.org.theark.core.model.study.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import au.org.theark.core.Constants;

/**
 * Correspondence entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CORRESPONDENCE", schema = Constants.STUDY_SCHEMA)
public class Correspondence implements java.io.Serializable {

	// Fields

	private Long id;
	private CorrespondenceType correspondenceType;
	private Date dateOfCorrespondence;
	private String summary;
	private Set<Document> documents = new HashSet<Document>(0);

	// Constructors

	/** default constructor */
	public Correspondence() {
	}

	/** minimal constructor */
	public Correspondence(Long id) {
		this.id = id;
	}

	/** full constructor */
	public Correspondence(Long id,
			CorrespondenceType correspondenceType, Date dateOfCorrespondence,
			String summary, Set<Document> documents) {
		this.id = id;
		this.correspondenceType = correspondenceType;
		this.dateOfCorrespondence = dateOfCorrespondence;
		this.summary = summary;
		this.documents = documents;
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
	@JoinColumn(name = "CORRESPONDENCE_TYPE_ID")
	public CorrespondenceType getCorrespondenceType() {
		return this.correspondenceType;
	}

	public void setCorrespondenceType(CorrespondenceType correspondenceType) {
		this.correspondenceType = correspondenceType;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_OF_CORRESPONDENCE", length = 7)
	public Date getDateOfCorrespondence() {
		return this.dateOfCorrespondence;
	}

	public void setDateOfCorrespondence(Date dateOfCorrespondence) {
		this.dateOfCorrespondence = dateOfCorrespondence;
	}

	@Column(name = "SUMMARY")
	public String getSummary() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "correspondence")
	public Set<Document> getDocuments() {
		return this.documents;
	}

	public void setDocuments(Set<Document> documents) {
		this.documents = documents;
	}

}