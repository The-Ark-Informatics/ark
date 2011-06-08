package au.org.theark.core.model.geno.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;

/**
 * SubjectMarkerMetaData entity. @author MyEclipse Persistence Tools
 */
@Entity(name="au.org.theark.geno.model.entity.SubjectMarkerMetaData")
@Table(name = "SUBJECT_MARKER_META_DATA", schema = Constants.GENO_TABLE_SCHEMA)
public class SubjectMarkerMetaData implements java.io.Serializable {

	// Fields

	private Long id;
	private MetaData metaData;
	private Marker marker;
	private LinkSubjectStudy subject;
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
			LinkSubjectStudy subject, String userId, String insertTime) {
		this.id = id;
		this.metaData = metaData;
		this.marker = marker;
		this.subject = subject;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	/** full constructor */
	public SubjectMarkerMetaData(Long id, MetaData metaData, Marker marker,
			LinkSubjectStudy subject, String userId, String insertTime,
			String updateUserId, String updateTime) {
		this.id = id;
		this.metaData = metaData;
		this.marker = marker;
		this.subject = subject;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBJECT_ID", nullable = false)
	public LinkSubjectStudy getSubject() {
		return this.subject;
	}

	public void setSubject(LinkSubjectStudy subject) {
		this.subject = subject;
	}

	@Column(name = "USER_ID", nullable = false)
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

	@Column(name = "UPDATE_USER_ID")
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