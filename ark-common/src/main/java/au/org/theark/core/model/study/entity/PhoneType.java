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

/**
 * PhoneType entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PHONE_TYPE", schema = "ETA", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
public class PhoneType implements java.io.Serializable {

	// Fields

	private Long phoneTypeKey;
	private String name;
	private String description;
	private Set<Phone> phones = new HashSet<Phone>(0);

	// Constructors

	/** default constructor */
	public PhoneType() {
	}

	/** minimal constructor */
	public PhoneType(Long phoneTypeKey) {
		this.phoneTypeKey = phoneTypeKey;
	}

	/** full constructor */
	public PhoneType(Long phoneTypeKey, String name, String description,
			Set<Phone> phones) {
		this.phoneTypeKey = phoneTypeKey;
		this.name = name;
		this.description = description;
		this.phones = phones;
	}

	// Property accessors
	@Id
	@Column(name = "PHONE_TYPE_KEY", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getPhoneTypeKey() {
		return this.phoneTypeKey;
	}

	public void setPhoneTypeKey(Long phoneTypeKey) {
		this.phoneTypeKey = phoneTypeKey;
	}

	@Column(name = "NAME", unique = true, length = 20)
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