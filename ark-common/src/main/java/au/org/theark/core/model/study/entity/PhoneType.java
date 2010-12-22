package au.org.theark.core.model.study.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import au.org.theark.core.Constants;

/**
 * PhoneType entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PHONE_TYPE", schema = Constants.STUDY_SCHEMA, uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
public class PhoneType implements java.io.Serializable {

	// Fields

	private Long id;
	private String name;
	private String description;
	private Set<Phone> phones = new HashSet<Phone>(0);

	// Constructors

	/** default constructor */
	public PhoneType() {
	}

	/** minimal constructor */
	public PhoneType(Long id) {
		this.id = id;
	}

	/** full constructor */
	public PhoneType(Long id, String name, String description,
			Set<Phone> phones) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.phones = phones;
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

	@Column(name = "NAME", unique = true, nullable = false, length = 20)
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "phoneType")
	public Set<Phone> getPhones() {
		return this.phones;
	}

	public void setPhones(Set<Phone> phones) {
		this.phones = phones;
	}

}