package au.org.theark.core.model.geno.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;

/**
 * DataType entity. @author MyEclipse Persistence Tools
 */
@Entity(name="au.org.theark.geno.model.entity.MetaDataType")
@Table(name = "META_DATA_TYPE", schema = Constants.GENO_TABLE_SCHEMA)
public class MetaDataType implements java.io.Serializable {

	// Fields

	private Long id;
	private String name;
	private Set<MetaDataField> metaDataFields = new HashSet<MetaDataField>(0);

	// Constructors

	/** default constructor */
	public MetaDataType() {
	}

	/** minimal constructor */
	public MetaDataType(Long id) {
		this.id = id;
	}

	/** full constructor */
	public MetaDataType(Long id, String name, Set<MetaDataField> metaDataFields) {
		this.id = id;
		this.name = name;
		this.metaDataFields = metaDataFields;
	}

	// Property accessors
	@Id
    @SequenceGenerator(name="MetaDataType_PK_Seq",sequenceName=Constants.META_DATA_TYPE_PK_SEQ)
    @GeneratedValue(strategy=GenerationType.AUTO,generator="MetaDataType_PK_Seq")
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "metaDataType")
	public Set<MetaDataField> getMetaDataFields() {
		return this.metaDataFields;
	}

	public void setMetaDataFields(Set<MetaDataField> metaDataFields) {
		this.metaDataFields = metaDataFields;
	}

}