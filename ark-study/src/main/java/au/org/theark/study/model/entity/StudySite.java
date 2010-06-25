package au.org.theark.study.model.entity;

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
import javax.persistence.UniqueConstraint;

/**
 * StudySite entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "STUDY_SITE", schema = "ETA", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
public class StudySite implements java.io.Serializable {

	// Fields

	private Long studySiteKey;
	private Address address;
	private String name;
	private String description;
	private Long domainTypeKey;
	private Set<LinkSiteContact> linkSiteContacts = new HashSet<LinkSiteContact>(
			0);
	private Set<LinkStudyStudysite> linkStudyStudysites = new HashSet<LinkStudyStudysite>(
			0);

	// Constructors

	/** default constructor */
	public StudySite() {
	}

	/** minimal constructor */
	public StudySite(Long studySiteKey) {
		this.studySiteKey = studySiteKey;
	}

	/** full constructor */
	public StudySite(Long studySiteKey, Address address, String name,
			String description, Long domainTypeKey,
			Set<LinkSiteContact> linkSiteContacts,
			Set<LinkStudyStudysite> linkStudyStudysites) {
		this.studySiteKey = studySiteKey;
		this.address = address;
		this.name = name;
		this.description = description;
		this.domainTypeKey = domainTypeKey;
		this.linkSiteContacts = linkSiteContacts;
		this.linkStudyStudysites = linkStudyStudysites;
	}

	// Property accessors
	@Id
	@Column(name = "STUDY_SITE_KEY", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getStudySiteKey() {
		return this.studySiteKey;
	}

	public void setStudySiteKey(Long studySiteKey) {
		this.studySiteKey = studySiteKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ADDRESS_KEY")
	public Address getAddress() {
		return this.address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Column(name = "NAME", unique = true)
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

	@Column(name = "DOMAIN_TYPE_KEY", precision = 22, scale = 0)
	public Long getDomainTypeKey() {
		return this.domainTypeKey;
	}

	public void setDomainTypeKey(Long domainTypeKey) {
		this.domainTypeKey = domainTypeKey;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "studySite")
	public Set<LinkSiteContact> getLinkSiteContacts() {
		return this.linkSiteContacts;
	}

	public void setLinkSiteContacts(Set<LinkSiteContact> linkSiteContacts) {
		this.linkSiteContacts = linkSiteContacts;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "studySite")
	public Set<LinkStudyStudysite> getLinkStudyStudysites() {
		return this.linkStudyStudysites;
	}

	public void setLinkStudyStudysites(
			Set<LinkStudyStudysite> linkStudyStudysites) {
		this.linkStudyStudysites = linkStudyStudysites;
	}

}