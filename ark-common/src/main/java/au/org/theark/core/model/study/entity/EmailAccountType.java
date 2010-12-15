package au.org.theark.core.model.study.entity;

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

/**
 * EmailAccountType entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EMAIL_ACCOUNT_TYPE", schema = Constants.STUDY_SCHEMA)
public class EmailAccountType implements java.io.Serializable {

	// Fields

	private Long id;
	private String name;
	private String description;
	private Set<EmailAccount> emailAccounts = new HashSet<EmailAccount>(0);

	// Constructors

	/** default constructor */
	public EmailAccountType() {
	}

	/** minimal constructor */
	public EmailAccountType(Long id) {
		this.id = id;
	}

	/** full constructor */
	public EmailAccountType(Long id, String name,
			String description, Set<EmailAccount> emailAccounts) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.emailAccounts = emailAccounts;
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

	@Column(name = "NAME", length = 20)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION", length = 50)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "emailAccountType")
	public Set<EmailAccount> getEmailAccounts() {
		return this.emailAccounts;
	}

	public void setEmailAccounts(Set<EmailAccount> emailAccounts) {
		this.emailAccounts = emailAccounts;
	}

}