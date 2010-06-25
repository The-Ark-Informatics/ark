package au.org.theark.study.model.entity;

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

/**
 * Relationship entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RELATIONSHIP", schema = "ETA", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
public class Relationship implements java.io.Serializable {

	// Fields

	private Long relationshipKey;
	private String name;
	private String description;
	private Set<LinkSubjectContact> linkSubjectContacts = new HashSet<LinkSubjectContact>(
			0);

	// Constructors

	/** default constructor */
	public Relationship() {
	}

	/** minimal constructor */
	public Relationship(Long relationshipKey) {
		this.relationshipKey = relationshipKey;
	}

	/** full constructor */
	public Relationship(Long relationshipKey, String name, String description,
			Set<LinkSubjectContact> linkSubjectContacts) {
		this.relationshipKey = relationshipKey;
		this.name = name;
		this.description = description;
		this.linkSubjectContacts = linkSubjectContacts;
	}

	// Property accessors
	@Id
	@Column(name = "RELATIONSHIP_KEY", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getRelationshipKey() {
		return this.relationshipKey;
	}

	public void setRelationshipKey(Long relationshipKey) {
		this.relationshipKey = relationshipKey;
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