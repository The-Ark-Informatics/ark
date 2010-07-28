package au.org.theark.gdmi.model.entity;

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Collection entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "COLLECTION", schema = "GDMI")
public class Collection implements java.io.Serializable {

	// Fields

	private long id;
	private Status status;
	private long studyId;
	private String name;
	private String description;
	private Date startDate;
	private Date expiryDate;
	private String userId;
	private String insertTime;
	private String updateUserId;
	private String updateTime;
	private Set<MetaData> metaDatas = new HashSet<MetaData>(0);
	private Set<CollectionImport> collectionImports = new HashSet<CollectionImport>(
			0);
	private Set<EncodedData> encodedDatas = new HashSet<EncodedData>(0);
	private Set<DecodeMask> decodeMasks = new HashSet<DecodeMask>(0);
	private Set<DataSet> dataSets = new HashSet<DataSet>(0);
	private Set<SubjectGroup> subjectGroups = new HashSet<SubjectGroup>(0);
	private Set<UploadCollection> uploadCollections = new HashSet<UploadCollection>(
			0);

	// Constructors

	/** default constructor */
	public Collection() {
	}

	/** minimal constructor */
	public Collection(long id, Status status, long studyId, String userId,
			String insertTime) {
		this.id = id;
		this.status = status;
		this.studyId = studyId;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	/** full constructor 
	 * @param decodeMasks */
	public Collection(long id, Status status, long studyId, String name,
			String description, Date startDate, Date expiryDate, String userId,
			String insertTime, String updateUserId, String updateTime,
			Set<MetaData> metaDatas, Set<CollectionImport> collectionImports,
			Set<EncodedData> encodedDatas, Set<DecodeMask> decodeMasks, 
			Set<DataSet> dataSets, Set<SubjectGroup> subjectGroups,
			Set<UploadCollection> uploadCollections) {
		this.id = id;
		this.status = status;
		this.studyId = studyId;
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.expiryDate = expiryDate;
		this.userId = userId;
		this.insertTime = insertTime;
		this.updateUserId = updateUserId;
		this.updateTime = updateTime;
		this.metaDatas = metaDatas;
		this.collectionImports = collectionImports;
		this.encodedDatas = encodedDatas;
		this.decodeMasks = decodeMasks;
		this.dataSets = dataSets;
		this.subjectGroups = subjectGroups;
		this.uploadCollections = uploadCollections;
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
	@JoinColumn(name = "STATUS_ID", nullable = false)
	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Column(name = "STUDY_ID", nullable = false, precision = 22, scale = 0)
	public long getStudyId() {
		return this.studyId;
	}

	public void setStudyId(long studyId) {
		this.studyId = studyId;
	}

	@Column(name = "NAME", length = 50)
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

	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE", length = 7)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "EXPIRY_DATE", length = 7)
	public Date getExpiryDate() {
		return this.expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "collection")
	public Set<MetaData> getMetaDatas() {
		return this.metaDatas;
	}

	public void setMetaDatas(Set<MetaData> metaDatas) {
		this.metaDatas = metaDatas;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "collection")
	public Set<CollectionImport> getCollectionImports() {
		return this.collectionImports;
	}

	public void setCollectionImports(Set<CollectionImport> collectionImports) {
		this.collectionImports = collectionImports;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "collection")
	public Set<EncodedData> getEncodedDatas() {
		return this.encodedDatas;
	}

	public void setEncodedDatas(Set<EncodedData> encodedDatas) {
		this.encodedDatas = encodedDatas;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "collection")
	public Set<DecodeMask> getDecodeMasks() {
		return this.decodeMasks;
	}

	public void setDecodeMasks(Set<DecodeMask> decodeMasks) {
		this.decodeMasks = decodeMasks;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "collection")
	public Set<DataSet> getDataSets() {
		return this.dataSets;
	}

	public void setDataSets(Set<DataSet> dataSets) {
		this.dataSets = dataSets;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "collection")
	public Set<SubjectGroup> getSubjectGroups() {
		return this.subjectGroups;
	}

	public void setSubjectGroups(Set<SubjectGroup> subjectGroups) {
		this.subjectGroups = subjectGroups;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "collection")
	public Set<UploadCollection> getUploadCollections() {
		return this.uploadCollections;
	}

	public void setUploadCollections(Set<UploadCollection> uploadCollections) {
		this.uploadCollections = uploadCollections;
	}

}