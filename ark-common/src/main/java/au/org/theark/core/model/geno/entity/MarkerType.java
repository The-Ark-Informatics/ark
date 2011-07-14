package au.org.theark.core.model.geno.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;

/**
 * MarkerType entity. @author MyEclipse Persistence Tools
 */
@Entity(name="au.org.theark.geno.model.entity.MarkerType")
@Table(name = "MARKER_TYPE", schema = Constants.GENO_TABLE_SCHEMA)
public class MarkerType implements java.io.Serializable {

	// Fields

	private Long id;
	private String name;
	private Set<MarkerGroup> markerGroups = new HashSet<MarkerGroup>(0);

	// Constructors

	/** default constructor */
	public MarkerType() {
	}

	/** minimal constructor */
	public MarkerType(Long id) {
		this.id = id;
	}

	/** full constructor */
	public MarkerType(Long id, String name, Set<MarkerGroup> markerGroups) {
		this.id = id;
		this.name = name;
		this.markerGroups = markerGroups;
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

	@Column(name = "NAME", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "markerType")
	public Set<MarkerGroup> getMarkerGroups() {
		return this.markerGroups;
	}

	public void setMarkerGroups(Set<MarkerGroup> markerGroups) {
		this.markerGroups = markerGroups;
	}

}