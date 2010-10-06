package au.org.theark.gdmi.model.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
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

	private Long id;
	private Status status;
	private Long studyId;
	private String name;
	private String description;
	private Date startDate;
	private Date expiryDate;
	private String userId;
	private Date insertTime;
	private String updateUserId;
	private Date updateTime;
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
	public Collection(Long id, Status status, Long studyId, String userId,
			Date insertTime) {
		this.id = id;
		this.status = status;
		this.studyId = studyId;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	/** full constructor 
	 * @param decodeMasks */
	public Collection(Long id, Status status, Long studyId, String name,
			String description, Date startDate, Date expiryDate, String userId,
			Date insertTime, String updateUserId, Date updateTime,
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
	@SequenceGenerator(name="Collection_PK_Seq",sequenceName="GDMI.COLLECTION_PK_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO,generator="Collection_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
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
	public Long getStudyId() {
		return this.studyId;
	}

	public void setStudyId(Long studyId) {
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE", length = 7)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSERT_TIME", nullable = false)
	public Date getInsertTime() {
		return this.insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	@Column(name = "UPDATE_USER_ID", length = 50)
	public String getUpdateUserId() {
		return this.updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME")
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
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