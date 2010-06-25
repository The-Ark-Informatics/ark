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
 * CorrespondenceType entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CORRESPONDENCE_TYPE", schema = "ETA", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
public class CorrespondenceType implements java.io.Serializable {

	// Fields

	private Long correspondenceTypeKey;
	private String name;
	private String typeDescription;
	private Set<Correspondence> correspondences = new HashSet<Correspondence>(0);

	// Constructors

	/** default constructor */
	public CorrespondenceType() {
	}

	/** minimal constructor */
	public CorrespondenceType(Long correspondenceTypeKey) {
		this.correspondenceTypeKey = correspondenceTypeKey;
	}

	/** full constructor */
	public CorrespondenceType(Long correspondenceTypeKey, String name,
			String typeDescription, Set<Correspondence> correspondences) {
		this.correspondenceTypeKey = correspondenceTypeKey;
		this.name = name;
		this.typeDescription = typeDescription;
		this.correspondences = correspondences;
	}

	// Property accessors
	@Id
	@Column(name = "CORRESPONDENCE_TYPE_KEY", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getCorrespondenceTypeKey() {
		return this.correspondenceTypeKey;
	}

	public void setCorrespondenceTypeKey(Long correspondenceTypeKey) {
		this.correspondenceTypeKey = correspondenceTypeKey;
	}

	@Column(name = "NAME", unique = true, length = 50)
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