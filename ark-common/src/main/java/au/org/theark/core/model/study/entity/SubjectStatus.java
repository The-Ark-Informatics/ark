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

/**
 * SubjectStatus entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SUBJECT_STATUS", schema = "ETA", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
public class SubjectStatus implements java.io.Serializable {

	// Fields

	private Long subjectStatusKey;
	private String name;
	private String description;
	private Set<LinkSubjectStudy> linkSubjectStudies = new HashSet<LinkSubjectStudy>(
			0);

	// Constructors

	/** default constructor */
	public SubjectStatus() {
	}

	/** minimal constructor */
	public SubjectStatus(Long subjectStatusKey) {
		this.subjectStatusKey = subjectStatusKey;
	}

	/** full constructor */
	public SubjectStatus(Long subjectStatusKey, String name,
			String description, Set<LinkSubjectStudy> linkSubjectStudies) {
		this.subjectStatusKey = subjectStatusKey;
		this.name = name;
		this.description = description;
		this.linkSubjectStudies = linkSubjectStudies;
	}

	// Property accessors
	@Id
	@Column(name = "SUBJECT_STATUS_KEY", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getSubjectStatusKey() {
		return this.subjectStatusKey;
	}

	public void setSubjectStatusKey(Long subjectStatusKey) {
		this.subjectStatusKey = subjectStatusKey;
	}

	@Column(name = "NAME", unique = true, length = 50)
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "subjectStatus")
	public Set<LinkSubjectStudy> getLinkSubjectStudies() {
		return this.linkSubjectStudies;
	}

	public void setLinkSubjectStudies(Set<LinkSubjectStudy> linkSubjectStudies) {
		this.linkSubjectStudies = linkSubjectStudies;
	}

}