package au.org.theark.study.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * AddressType entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ADDRESS_TYPE", schema = "ETA")
public class AddressType implements java.io.Serializable {

	// Fields

	private Long addressTypeKey;
	private String name;
	private String description;

	// Constructors

	/** default constructor */
	public AddressType() {
	}

	/** minimal constructor */
	public AddressType(Long addressTypeKey) {
		this.addressTypeKey = addressTypeKey;
	}

	/** full constructor */
	public AddressType(Long addressTypeKey, String name, String description) {
		this.addressTypeKey = addressTypeKey;
		this.name = name;
		this.description = description;
	}

	// Property accessors
	@Id
	@Column(name = "ADDRESS_TYPE_KEY", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getAddressTypeKey() {
		return this.addressTypeKey;
	}

	public void setAddressTypeKey(Long addressTypeKey) {
		this.addressTypeKey = addressTypeKey;
	}

	@Column(name = "NAME", length = 50)
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