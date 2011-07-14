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
 * FileFormat entity. @author MyEclipse Persistence Tools
 */
@Entity(name="au.org.theark.geno.model.entity.FileFormat")
@Table(name = "FILE_FORMAT", schema = Constants.GENO_TABLE_SCHEMA)
public class FileFormat implements java.io.Serializable {

	// Fields

	private Long id;
	private String name;
	private Set<Upload> uploads = new HashSet<Upload>(0);

	// Constructors

	/** default constructor */
	public FileFormat() {
	}

	/** minimal constructor */
	public FileFormat(Long id) {
		this.id = id;
	}

	/** full constructor */
	public FileFormat(Long id, String name, Set<Upload> uploads) {
		this.id = id;
		this.name = name;
		this.uploads = uploads;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fileFormat")
	public Set<Upload> getUploads() {
		return this.uploads;
	}

	public void setUploads(Set<Upload> uploads) {
		this.uploads = uploads;
	}

}