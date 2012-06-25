package au.org.theark.core.model.worktracking.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import au.org.theark.core.Constants;

@Entity
@Table(name = "BILLING_TYPE", schema = Constants.ADMIN_SCHEMA)
public class BillingType implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String description;
	private Set<Researcher> researchers = new HashSet<Researcher>(0);

	public BillingType() {
	}

	public BillingType(Long id) {
		this.id = id;
	}

	public BillingType(Long id, String name, String description,
			Set<Researcher> researchers) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.researchers = researchers;
	}

	public BillingType(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 25)
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "billingType")
	public Set<Researcher> getResearchers() {
		return researchers;
	}

	public void setResearchers(Set<Researcher> researchers) {
		this.researchers = researchers;
	}
}
