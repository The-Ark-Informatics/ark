package au.org.theark.gdmi.model.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * MarkerType entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "MARKER_TYPE", schema = "GDMI")
public class MarkerType implements java.io.Serializable {

	// Fields

	private long id;
	private String name;
	private Set<MarkerGroup> markerGroups = new HashSet<MarkerGroup>(0);

	// Constructors

	/** default constructor */
	public MarkerType() {
	}

	/** minimal constructor */
	public MarkerType(long id) {
		this.id = id;
	}

	/** full constructor */
	public MarkerType(long id, String name, Set<MarkerGroup> markerGroups) {
		this.id = id;
		this.name = name;
		this.markerGroups = markerGroups;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
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