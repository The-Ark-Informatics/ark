package au.org.theark.core.model.study.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import au.org.theark.core.Constants;

/**
 * Relationship entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RELATIONSHIP", schema = Constants.STUDY_SCHEMA, uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
public class Relationship implements java.io.Serializable {

	// Fields

	private Long id;
	private String name;
	private String description;
	private Set<LinkSubjectContact> linkSubjectContacts = new HashSet<LinkSubjectContact>(
			0);

	// Constructors

	/** default constructor */
	public Relationship() {
	}

	/** minimal constructor */
	public Relationship(Long id) {
		this.id = id;
	}

	/** full constructor */
	public Relationship(Long id, String name, String description,
			Set<LinkSubjectContact> linkSubjectContacts) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.linkSubjectContacts = linkSubjectContacts;
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

	@Column(name = "NAME", unique = true, length = 20)
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "relationship")
	public Set<LinkSubjectContact> getLinkSubjectContacts() {
		return this.linkSubjectContacts;
	}

	public void setLinkSubjectContacts(
			Set<LinkSubjectContact> linkSubjectContacts) {
		this.linkSubjectContacts = linkSubjectContacts;
	}

}