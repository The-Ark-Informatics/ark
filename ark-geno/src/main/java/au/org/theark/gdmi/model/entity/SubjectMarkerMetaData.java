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
@Entity(name="au.org.theark.gdmi.model.entity.SubjectMarkerMetaData")
@Table(name = "SUBJECT_MARKER_META_DATA", schema = "GDMI")
public class SubjectMarkerMetaData implements java.io.Serializable {

	// Fields

	private Long id;
	private MetaData metaData;
	private Marker marker;
	private Long subjectId;
	private String userId;
	private String insertTime;
	private String updateUserId;
	private String updateTime;

	// Constructors

	/** default constructor */
	public SubjectMarkerMetaData() {
	}

	/** minimal constructor */
	public SubjectMarkerMetaData(Long id, MetaData metaData, Marker marker,
			Long subjectId, String userId, String insertTime) {
		this.id = id;
		this.metaData = metaData;
		this.marker = marker;
		this.subjectId = subjectId;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	/** full constructor */
	public SubjectMarkerMetaData(Long id, MetaData metaData, Marker marker,
			Long subjectId, String userId, String insertTime,
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
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
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
	public Long getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(Long subjectId) {
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