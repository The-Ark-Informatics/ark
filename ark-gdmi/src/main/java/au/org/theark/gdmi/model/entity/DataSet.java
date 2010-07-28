package au.org.theark.gdmi.model.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * DataSet entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "DATA_SET", schema = "GDMI")
public class DataSet implements java.io.Serializable {

	// Fields

	private long id;
	private SnpFilter snpFilter;
	private Collection collection;
	private SubjectGroup subjectGroup;
	private String name;
	private String description;
	private String userId;
	private String insertTime;
	private String updateUserId;
	private String updateTime;
	private Set<DataExtract> dataExtracts = new HashSet<DataExtract>(0);

	// Constructors

	/** default constructor */
	public DataSet() {
	}

	/** minimal constructor */
	public DataSet(long id, SnpFilter snpFilter, Collection collection,
			SubjectGroup subjectGroup, String userId, String insertTime) {
		this.id = id;
		this.snpFilter = snpFilter;
		this.collection = collection;
		this.subjectGroup = subjectGroup;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	/** full constructor */
	public DataSet(long id, SnpFilter snpFilter, Collection collection,
			SubjectGroup subjectGroup, String name, String description,
			String userId, String insertTime, String updateUserId,
			String updateTime, Set<DataExtract> dataExtracts) {
		this.id = id;
		this.snpFilter = snpFilter;
		this.collection = collection;
		this.subjectGroup = subjectGroup;
		this.name = name;
		this.description = description;
		this.userId = userId;
		this.insertTime = insertTime;
		this.updateUserId = updateUserId;
		this.updateTime = updateTime;
		this.dataExtracts = dataExtracts;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SNP_FILTER_ID", nullable = false)
	public SnpFilter getSnpFilter() {
		return this.snpFilter;
	}

	public void setSnpFilter(SnpFilter snpFilter) {
		this.snpFilter = snpFilter;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COLLECTION_ID", nullable = false)
	public Collection getCollection() {
		return this.collection;
	}

	public void setCollection(Collection collection) {
		this.collection = collection;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBJECT_SUBSET_ID", nullable = false)
	public SubjectGroup getSubjectGroup() {
		return this.subjectGroup;
	}

	public void setSubjectGroup(SubjectGroup subjectGroup) {
		this.subjectGroup = subjectGroup;
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

	@Column(name = "UPDATE_USER_ID", length = 50)
	public String getUpdateUserId() {
		return this.updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	@Column(name = "UPDATE_TIME")
	public String getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "dataSet")
	public Set<DataExtract> getDataExtracts() {
		return this.dataExtracts;
	}

	public void setDataExtracts(Set<DataExtract> dataExtracts) {
		this.dataExtracts = dataExtracts;
	}

}