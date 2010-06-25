package au.org.theark.study.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * LinkSubjectContact entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "LINK_SUBJECT_CONTACT", schema = "ETA")
public class LinkSubjectContact implements java.io.Serializable {

	// Fields

	private Long linkSubjectContactKey;
	private Study study;
	private Relationship relationship;
	private Person personBySubjectKey;
	private Person personByContactKey;

	// Constructors

	/** default constructor */
	public LinkSubjectContact() {
	}

	/** minimal constructor */
	public LinkSubjectContact(Long linkSubjectContactKey) {
		this.linkSubjectContactKey = linkSubjectContactKey;
	}

	/** full constructor */
	public LinkSubjectContact(Long linkSubjectContactKey, Study study,
			Relationship relationship, Person personBySubjectKey,
			Person personByContactKey) {
		this.linkSubjectContactKey = linkSubjectContactKey;
		this.study = study;
		this.relationship = relationship;
		this.personBySubjectKey = personBySubjectKey;
		this.personByContactKey = personByContactKey;
	}

	// Property accessors
	@Id
	@Column(name = "LINK_SUBJECT_CONTACT_KEY", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getLinkSubjectContactKey() {
		return this.linkSubjectContactKey;
	}

	public void setLinkSubjectContactKey(Long linkSubjectContactKey) {
		this.linkSubjectContactKey = linkSubjectContactKey;
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
	@JoinColumn(name = "RELATIONSHIP_KEY")
	public Relationship getRelationship() {
		return this.relationship;
	}

	public void setRelationship(Relationship relationship) {
		this.relationship = relationship;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBJECT_KEY")
	public Person getPersonBySubjectKey() {
		return this.personBySubjectKey;
	}

	public void setPersonBySubjectKey(Person personBySubjectKey) {
		this.personBySubjectKey = personBySubjectKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTACT_KEY")
	public Person getPersonByContactKey() {
		return this.personByContactKey;
	}

	public void setPersonByContactKey(Person personByContactKey) {
		this.personByContactKey = personByContactKey;
	}

}