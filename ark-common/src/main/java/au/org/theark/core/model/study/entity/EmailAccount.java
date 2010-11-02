package au.org.theark.core.model.study.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * EmailAccount entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EMAIL_ACCOUNT", schema = "ETA", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
public class EmailAccount implements java.io.Serializable {

	// Fields

	private Long emailAccountKey;
	private EmailAccountType emailAccountType;
	private String name;
	private boolean primaryAccount;
	private Long personKey;

	// Constructors

	/** default constructor */
	public EmailAccount() {
	}

	/** minimal constructor */
	public EmailAccount(Long emailAccountKey) {
		this.emailAccountKey = emailAccountKey;
	}

	/** full constructor */
	public EmailAccount(Long emailAccountKey,
			EmailAccountType emailAccountType, String name,
			boolean primaryAccount, Long personKey) {
		this.emailAccountKey = emailAccountKey;
		this.emailAccountType = emailAccountType;
		this.name = name;
		this.primaryAccount = primaryAccount;
		this.personKey = personKey;
	}

	// Property accessors
	@Id
	@Column(name = "EMAIL_ACCOUNT_KEY", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getEmailAccountKey() {
		return this.emailAccountKey;
	}

	public void setEmailAccountKey(Long emailAccountKey) {
		this.emailAccountKey = emailAccountKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMAIL_ACCOUNT_TYPE_KEY")
	public EmailAccountType getEmailAccountType() {
		return this.emailAccountType;
	}

	public void setEmailAccountType(EmailAccountType emailAccountType) {
		this.emailAccountType = emailAccountType;
	}

	@Column(name = "NAME", unique = true)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "PRIMARY_ACCOUNT", precision = 1, scale = 0)
	public boolean getPrimaryAccount() {
		return this.primaryAccount;
	}

	public void setPrimaryAccount(boolean primaryAccount) {
		this.primaryAccount = primaryAccount;
	}

	@Column(name = "PERSON_KEY", precision = 22, scale = 0)
	public Long getPersonKey() {
		return this.personKey;
	}

	public void setPersonKey(Long personKey) {
		this.personKey = personKey;
	}

}