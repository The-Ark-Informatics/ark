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
 * SubjectGroup entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SUBJECT_GROUP", schema = "GDMI")
public class SubjectGroup implements java.io.Serializable {

	// Fields

	private Long id;
	private Collection collection;
	private String name;
	private String description;
	private String userId;
	private String insertTime;
	private String updateUser;
	private String updateTime;
	private Set<SubjectSubsetGroup> subjectSubsetGroups = new HashSet<SubjectSubsetGroup>(
			0);
	private Set<DataSet> dataSets = new HashSet<DataSet>(0);

	// Constructors

	/** default constructor */
	public SubjectGroup() {
	}

	/** minimal constructor */
	public SubjectGroup(Long id, Collection collection, String userId,
			String insertTime) {
		this.id = id;
		this.collection = collection;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	/** full constructor */
	public SubjectGroup(Long id, Collection collection, String name,
			String description, String userId, String insertTime,
			String updateUser, String updateTime,
			Set<SubjectSubsetGroup> subjectSubsetGroups, Set<DataSet> dataSets) {
		this.id = id;
		this.collection = collection;
		this.name = name;
		this.description = description;
		this.userId = userId;
		this.insertTime = insertTime;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.subjectSubsetGroups = subjectSubsetGroups;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COLLECTION_ID", nullable = false)
	public Collection getCollection() {
		return this.collection;
	}

	public void setCollection(Collection collection) {
		this.collection = collection;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "subjectGroup")
	public Set<SubjectSubsetGroup> getSubjectSubsetGroups() {
		return this.subjectSubsetGroups;
	}

	public void setSubjectSubsetGroups(
			Set<SubjectSubsetGroup> subjectSubsetGroups) {
		this.subjectSubsetGroups = subjectSubsetGroups;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "subjectGroup")
	public Set<DataSet> getDataSets() {
		return this.dataSets;
	}

	public void setDataSets(Set<DataSet> dataSets) {
		this.dataSets = dataSets;
	}

}