package au.org.theark.gdmi.model.entity;

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

/**
 * DecodeMask entity. @author MyEclipse Persistence Tools
 */
@Entity(name="au.org.theark.gdmi.model.entity.DecodeMask")
@Table(name = "DECODE_MASK", schema = "GDMI")
public class DecodeMask implements java.io.Serializable {

	// Fields

	private Long id;
	private Collection collection;
	private Marker marker;
	private Long bitPosition;

	// Constructors

	/** default constructor */
	public DecodeMask() {
	}

	/** minimal constructor */
	public DecodeMask(Long id, Marker marker, Long bitPosition) {
		this.id = id;
		this.marker = marker;
		this.bitPosition = bitPosition;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COLLECTION_ID", nullable = false)
	public Collection getCollection() {
		return collection;
	}

	public void setCollection(Collection collection) {
		this.collection = collection;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MARKER_ID", nullable = false)
	public Marker getMarker() {
		return this.marker;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	@Column(name = "BIT_POSITION", nullable = false, precision = 22, scale = 0)
	public long getBitPosition() {
		return this.bitPosition;
	}

	public void setBitPosition(long bitPosition) {
		this.bitPosition = bitPosition;
	}

}