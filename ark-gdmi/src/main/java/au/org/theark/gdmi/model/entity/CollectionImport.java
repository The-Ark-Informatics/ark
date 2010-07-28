package au.org.theark.gdmi.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * CollectionImport entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "COLLECTION_IMPORT", schema = "GDMI")
public class CollectionImport implements java.io.Serializable {

	// Fields

	private long id;
	private ImportType importType;
	private DelimiterType delimiterType;
	private MarkerGroup markerGroup;
	private Collection collection;
	private String startTime;
	private String finishTime;
	private String userId;
	private String insertTime;
	private String updateUserId;
	private String updateTime;

	// Constructors

	/** default constructor */
	public CollectionImport() {
	}

	/** minimal constructor */
	public CollectionImport(long id, ImportType importType,
			DelimiterType delimiterType, MarkerGroup markerGroup,
			Collection collection, String startTime, String userId,
			String insertTime) {
		this.id = id;
		this.importType = importType;
		this.delimiterType = delimiterType;
		this.markerGroup = markerGroup;
		this.collection = collection;
		this.startTime = startTime;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	/** full constructor */
	public CollectionImport(long id, ImportType importType,
			DelimiterType delimiterType, MarkerGroup markerGroup,
			Collection collection, String startTime, String finishTime,
			String userId, String insertTime, String updateUserId,
			String updateTime) {
		this.id = id;
		this.importType = importType;
		this.delimiterType = delimiterType;
		this.markerGroup = markerGroup;
		this.collection = collection;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.userId = userId;
		this.insertTime = insertTime;
		this.updateUserId = updateUserId;
		this.updateTime = updateTime;
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
	@JoinColumn(name = "IMPORT_TYPE_ID", nullable = false)
	public ImportType getImportType() {
		return this.importType;
	}

	public void setImportType(ImportType importType) {
		this.importType = importType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DELIMITER_TYPE_ID", nullable = false)
	public DelimiterType getDelimiterType() {
		return this.delimiterType;
	}

	public void setDelimiterType(DelimiterType delimiterType) {
		this.delimiterType = delimiterType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MARKER_GROUP_ID", nullable = false)
	public MarkerGroup getMarkerGroup() {
		return this.markerGroup;
	}

	public void setMarkerGroup(MarkerGroup markerGroup) {
		this.markerGroup = markerGroup;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COLLECTION_ID", nullable = false)
	public Collection getCollection() {
		return this.collection;
	}

	public void setCollection(Collection collection) {
		this.collection = collection;
	}

	@Column(name = "START_TIME", nullable = false)
	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Column(name = "FINISH_TIME")
	public String getFinishTime() {
		return this.finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
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

}