package au.org.theark.geno.model.entity;

import java.sql.Clob;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import au.org.theark.geno.service.Constants;

/**
 * SnpFilter entity. @author MyEclipse Persistence Tools
 */
@Entity(name="au.org.theark.geno.model.entity.SnpFilter")
@Table(name = "SNP_FILTER", schema = Constants.GENO_TABLE_SCHEMA)
public class SnpFilter implements java.io.Serializable {

	// Fields

	private Long id;
	private Long collectionId;
	private String name;
	private String description;
	private Clob rulesXml;
	private String userId;
	private String insertTime;
	private String updateUser;
	private String updateTime;
	private Set<DataSet> dataSets = new HashSet<DataSet>(0);

	// Constructors

	/** default constructor */
	public SnpFilter() {
	}

	/** minimal constructor */
	public SnpFilter(Long id, Long collectionId, String userId,
			String insertTime) {
		this.id = id;
		this.collectionId = collectionId;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	/** full constructor */
	public SnpFilter(Long id, Long collectionId, String name,
			String description, Clob rulesXml, String userId,
			String insertTime, String updateUser, String updateTime,
			Set<DataSet> dataSets) {
		this.id = id;
		this.collectionId = collectionId;
		this.name = name;
		this.description = description;
		this.rulesXml = rulesXml;
		this.userId = userId;
		this.insertTime = insertTime;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.dataSets = dataSets;
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

	@Column(name = "COLLECTION_ID", nullable = false, precision = 22, scale = 0)
	public Long getCollectionId() {
		return this.collectionId;
	}

	public void setCollectionId(Long collectionId) {
		this.collectionId = collectionId;
	}

	@Column(name = "NAME", length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION", length = 1024)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "RULES_XML")
	public Clob getRulesXml() {
		return this.rulesXml;
	}

	public void setRulesXml(Clob rulesXml) {
		this.rulesXml = rulesXml;
	}

	@Column(name = "USER_ID", nullable = false, length = 50)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "INSERT_TIME", nullable = false)
	public String getInsertTime() {
		return this.insertTime;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}

	@Column(name = "UPDATE_USER", length = 50)
	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Column(name = "UPDATE_TIME")
	public String getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "snpFilter")
	public Set<DataSet> getDataSets() {
		return this.dataSets;
	}

	public void setDataSets(Set<DataSet> dataSets) {
		this.dataSets = dataSets;
	}

}