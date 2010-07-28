package au.org.theark.gdmi.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * SubjectMarkerMetaData entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SUBJECT_MARKER_META_DATA", schema = "GDMI")
public class SubjectMarkerMetaData implements java.io.Serializable {

	// Fields

	private long id;
	private MetaData metaData;
	private Marker marker;
	private long subjectId;
	private String userId;
	private String insertTime;
	private String updateUserId;
	private String updateTime;

	// Constructors

	/** default constructor */
	public SubjectMarkerMetaData() {
	}

	/** minimal constructor */
	public SubjectMarkerMetaData(long id, MetaData metaData, Marker marker,
			long subjectId, String userId, String insertTime) {
		this.id = id;
		this.metaData = metaData;
		this.marker = marker;
		this.subjectId = subjectId;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	/** full constructor */
	public SubjectMarkerMetaData(long id, MetaData metaData, Marker marker,
			long subjectId, String userId, String insertTime,
			String updateUserId, String updateTime) {
		this.id = id;
		this.metaData = metaData;
		this.marker = marker;
		this.subjectId = subjectId;
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
	@JoinColumn(name = "META_DATA_ID", nullable = false)
	public MetaData getMetaData() {
		return this.metaData;
	}

	public void setMetaData(MetaData metaData) {
		this.metaData = metaData;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MARKER_ID", nullable = false)
	public Marker getMarker() {
		return this.marker;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	@Column(name = "SUBJECT_ID", nullable = false, precision = 22, scale = 0)
	public long getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
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