package au.org.theark.core.model.study.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import au.org.theark.core.Constants;

/**
 * AddressType entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ADDRESS_TYPE", schema = Constants.STUDY_SCHEMA)
public class AddressType implements java.io.Serializable {

	// Fields

	private Long id;
	private String name;
	private String description;

	// Constructors

	/** default constructor */
	public AddressType() {
	}

	/** minimal constructor */
	public AddressType(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AddressType(Long id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
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

	@Column(name = "NAME", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}