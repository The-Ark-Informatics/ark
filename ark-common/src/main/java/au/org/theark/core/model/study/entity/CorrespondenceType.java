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
 * CorrespondenceType entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CORRESPONDENCE_TYPE", schema = Constants.STUDY_SCHEMA, uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
public class CorrespondenceType implements java.io.Serializable {

	// Fields

	private Long id;
	private String name;
	private String typeDescription;
	private Set<Correspondence> correspondences = new HashSet<Correspondence>(0);

	// Constructors

	/** default constructor */
	public CorrespondenceType() {
	}

	/** minimal constructor */
	public CorrespondenceType(Long id) {
		this.id = id;
	}

	/** full constructor */
	public CorrespondenceType(Long id, String name,
			String typeDescription, Set<Correspondence> correspondences) {
		this.id = id;
		this.name = name;
		this.typeDescription = typeDescription;
		this.correspondences = correspondences;
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

	@Column(name = "NAME", unique = true, nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "TYPE_DESCRIPTION")
	public String getTypeDescription() {
		return this.typeDescription;
	}

	public void setTypeDescription(String typeDescription) {
		this.typeDescription = typeDescription;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "correspondenceType")
	public Set<Correspondence> getCorrespondences() {
		return this.correspondences;
	}

	public void setCorrespondences(Set<Correspondence> correspondences) {
		this.correspondences = correspondences;
	}

}