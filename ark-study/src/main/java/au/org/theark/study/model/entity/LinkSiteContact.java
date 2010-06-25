package au.org.theark.study.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * LinkSiteContact entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "LINK_SITE_CONTACT", schema = "ETA")
public class LinkSiteContact implements java.io.Serializable {

	// Fields

	private Long linkSiteContactKey;
	private StudySite studySite;
	private Person person;

	// Constructors

	/** default constructor */
	public LinkSiteContact() {
	}

	/** minimal constructor */
	public LinkSiteContact(Long linkSiteContactKey) {
		this.linkSiteContactKey = linkSiteContactKey;
	}

	/** full constructor */
	public LinkSiteContact(Long linkSiteContactKey, StudySite studySite,
			Person person) {
		this.linkSiteContactKey = linkSiteContactKey;
		this.studySite = studySite;
		this.person = person;
	}

	// Property accessors
	@Id
	@Column(name = "LINK_SITE_CONTACT_KEY", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getLinkSiteContactKey() {
		return this.linkSiteContactKey;
	}

	public void setLinkSiteContactKey(Long linkSiteContactKey) {
		this.linkSiteContactKey = linkSiteContactKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_SITE_KEY")
	public StudySite getStudySite() {
		return this.studySite;
	}

	public void setStudySite(StudySite studySite) {
		this.studySite = studySite;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_KEY")
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

}