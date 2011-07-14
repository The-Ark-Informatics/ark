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
 * ImportType entity. @author MyEclipse Persistence Tools
 */
@Entity(name="au.org.theark.geno.model.entity.ImportType")
@Table(name = "IMPORT_TYPE", schema = Constants.GENO_TABLE_SCHEMA)
public class ImportType implements java.io.Serializable {

	// Fields

	private Long id;
	private String name;
	private Set<CollectionImport> collectionImports = new HashSet<CollectionImport>(
			0);

	// Constructors

	/** default constructor */
	public ImportType() {
	}

	/** minimal constructor */
	public ImportType(Long id) {
		this.id = id;
	}

	/** full constructor */
	public ImportType(Long id, String name,
			Set<CollectionImport> collectionImports) {
		this.id = id;
		this.name = name;
		this.collectionImports = collectionImports;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "importType")
	public Set<CollectionImport> getCollectionImports() {
		return this.collectionImports;
	}

	public void setCollectionImports(Set<CollectionImport> collectionImports) {
		this.collectionImports = collectionImports;
	}

}