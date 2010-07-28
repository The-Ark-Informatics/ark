package au.org.theark.gdmi.model.entity;

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
 * NewLogPer entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "NEW_LOG_PER", schema = "GDMI")
public class NewLogPer implements java.io.Serializable {

	// Fields

	private long id;
	private String meaning;
	private Set<SystemOption> systemOptions = new HashSet<SystemOption>(0);

	// Constructors

	/** default constructor */
	public NewLogPer() {
	}

	/** minimal constructor */
	public NewLogPer(long id) {
		this.id = id;
	}

	/** full constructor */
	public NewLogPer(long id, String meaning, Set<SystemOption> systemOptions) {
		this.id = id;
		this.meaning = meaning;
		this.systemOptions = systemOptions;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "MEANING", length = 100)
	public String getMeaning() {
		return this.meaning;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "newLogPer")
	public Set<SystemOption> getSystemOptions() {
		return this.systemOptions;
	}

	public void setSystemOptions(Set<SystemOption> systemOptions) {
		this.systemOptions = systemOptions;
	}

}