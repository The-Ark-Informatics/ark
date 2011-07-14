package au.org.theark.core.model.geno.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;

/**
 * DecodeMask entity. @author MyEclipse Persistence Tools
 */
@Entity(name="au.org.theark.geno.model.entity.DecodeMask")
@Table(name = "DECODE_MASK", schema = Constants.GENO_TABLE_SCHEMA)
public class DecodeMask implements java.io.Serializable {

	// Fields

	private Long id;
	private GenoCollection collection;
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
	public GenoCollection getCollection() {
		return collection;
	}

	public void setCollection(GenoCollection collection) {
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