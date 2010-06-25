package au.org.theark.study.model.entity;

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
 * EmailAccountType entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EMAIL_ACCOUNT_TYPE", schema = "ETA")
public class EmailAccountType implements java.io.Serializable {

	// Fields

	private Long emailAccountTypeKey;
	private String name;
	private String description;
	private Set<EmailAccount> emailAccounts = new HashSet<EmailAccount>(0);

	// Constructors

	/** default constructor */
	public EmailAccountType() {
	}

	/** minimal constructor */
	public EmailAccountType(Long emailAccountTypeKey) {
		this.emailAccountTypeKey = emailAccountTypeKey;
	}

	/** full constructor */
	public EmailAccountType(Long emailAccountTypeKey, String name,
			String description, Set<EmailAccount> emailAccounts) {
		this.emailAccountTypeKey = emailAccountTypeKey;
		this.name = name;
		this.description = description;
		this.emailAccounts = emailAccounts;
	}

	// Property accessors
	@Id
	@Column(name = "EMAIL_ACCOUNT_TYPE_KEY", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getEmailAccountTypeKey() {
		return this.emailAccountTypeKey;
	}

	public void setEmailAccountTypeKey(Long emailAccountTypeKey) {
		this.emailAccountTypeKey = emailAccountTypeKey;
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