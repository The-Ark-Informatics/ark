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
 * LinkSiteContact entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "LINK_SITE_CONTACT", schema = Constants.STUDY_SCHEMA)
public class LinkSiteContact implements java.io.Serializable {

	// Fields

	private Long id;
	private StudySite studySite;
	private Person person;

	// Constructors

	/** default constructor */
	public LinkSiteContact() {
	}

	/** minimal constructor */
	public LinkSiteContact(Long id) {
		this.id = id;
	}

	/** full constructor */
	public LinkSiteContact(Long id, StudySite studySite,
			Person person) {
		this.id = id;
		this.studySite = studySite;
		this.person = person;
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
	@JoinColumn(name = "STUDY_SITE_ID")
	public StudySite getStudySite() {
		return this.studySite;
	}

	public void setStudySite(StudySite studySite) {
		this.studySite = studySite;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_ID")
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

}