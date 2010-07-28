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
 * DataType entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "META_DATA_TYPE", schema = "GDMI")
public class MetaDataType implements java.io.Serializable {

	// Fields

	private long id;
	private String name;
	private Set<MetaDataField> metaDataFields = new HashSet<MetaDataField>(0);

	// Constructors

	/** default constructor */
	public MetaDataType() {
	}

	/** minimal constructor */
	public MetaDataType(long id) {
		this.id = id;
	}

	/** full constructor */
	public MetaDataType(long id, String name, Set<MetaDataField> metaDataFields) {
		this.id = id;
		this.name = name;
		this.metaDataFields = metaDataFields;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "metaDataType")
	public Set<MetaDataField> getMetaDataFields() {
		return this.metaDataFields;
	}

	public void setMetaDataFields(Set<MetaDataField> metaDataFields) {
		this.metaDataFields = metaDataFields;
	}

}