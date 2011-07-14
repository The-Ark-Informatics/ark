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
 * LinkSubjectContact entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "LINK_SUBJECT_CONTACT", schema = Constants.STUDY_SCHEMA)
public class LinkSubjectContact implements java.io.Serializable {

	// Fields

	private Long id;
	private Study study;
	private Relationship relationship;
	private Person personBySubjectId;
	private Person personByContactId;

	// Constructors

	/** default constructor */
	public LinkSubjectContact() {
	}

	/** minimal constructor */
	public LinkSubjectContact(Long id) {
		this.id = id;
	}

	/** full constructor */
	public LinkSubjectContact(Long id, Study study,
			Relationship relationship, Person personBySubjectId,
			Person personByContactId) {
		this.id = id;
		this.study = study;
		this.relationship = relationship;
		this.personBySubjectId = personBySubjectId;
		this.personByContactId = personByContactId;
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
	@JoinColumn(name = "RELATIONSHIP_ID")
	public Relationship getRelationship() {
		return this.relationship;
	}

	public void setRelationship(Relationship relationship) {
		this.relationship = relationship;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_SUBJECT_ID")
	public Person getpersonBySubjectId() {
		return this.personBySubjectId;
	}

	public void setPersonBySubjectId(Person personBySubjectId) {
		this.personBySubjectId = personBySubjectId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_CONTACT_ID")
	public Person getPersonByContactId() {
		return this.personByContactId;
	}

	public void setPersonByContactId(Person personByContactId) {
		this.personByContactId = personByContactId;
	}

}