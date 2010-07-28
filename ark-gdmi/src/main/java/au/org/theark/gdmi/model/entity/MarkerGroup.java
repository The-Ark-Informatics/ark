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
 * MarkerGroup entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "MARKER_GROUP", schema = "GDMI")
public class MarkerGroup implements java.io.Serializable {

	// Fields

	private long id;
	private MarkerType markerType;
	private long studyId;
	private long uploadId;
	private String name;
	private String description;
	private long visible;
	private String userId;
	private String insertTime;
	private String updateUserId;
	private String updateTime;
	private Set<CollectionImport> collectionImports = new HashSet<CollectionImport>(
			0);
	private Set<UploadMarkerGroup> uploadMarkerGroups = new HashSet<UploadMarkerGroup>(
			0);
	private Set<Marker> markers = new HashSet<Marker>(0);

	// Constructors

	/** default constructor */
	public MarkerGroup() {
	}

	/** minimal constructor */
	public MarkerGroup(long id, MarkerType markerType, long studyId,
			long uploadId, String userId, String insertTime) {
		this.id = id;
		this.markerType = markerType;
		this.studyId = studyId;
		this.uploadId = uploadId;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	/** full constructor */
	public MarkerGroup(long id, MarkerType markerType, long studyId,
			long uploadId, String name, String description, long visible,
			String userId, String insertTime, String updateUserId,
			String updateTime, Set<CollectionImport> collectionImports,
			Set<UploadMarkerGroup> uploadMarkerGroups, Set<Marker> markers) {
		this.id = id;
		this.markerType = markerType;
		this.studyId = studyId;
		this.uploadId = uploadId;
		this.name = name;
		this.description = description;
		this.visible = visible;
		this.userId = userId;
		this.insertTime = insertTime;
		this.updateUserId = updateUserId;
		this.updateTime = updateTime;
		this.collectionImports = collectionImports;
		this.uploadMarkerGroups = uploadMarkerGroups;
		this.markers = markers;
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
	@JoinColumn(name = "MARKER_TYPE_ID", nullable = false)
	public MarkerType getMarkerType() {
		return this.markerType;
	}

	public void setMarkerType(MarkerType markerType) {
		this.markerType = markerType;
	}

	@Column(name = "STUDY_ID", nullable = false, precision = 22, scale = 0)
	public long getStudyId() {
		return this.studyId;
	}

	public void setStudyId(long studyId) {
		this.studyId = studyId;
	}

	@Column(name = "UPLOAD_ID", nullable = false, precision = 22, scale = 0)
	public long getUploadId() {
		return this.uploadId;
	}

	public void setUploadId(long uploadId) {
		this.uploadId = uploadId;
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

	@Column(name = "VISIBLE", precision = 1, scale = 0)
	public long getVisible() {
		return this.visible;
	}

	public void setVisible(long visible) {
		this.visible = visible;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "markerGroup")
	public Set<CollectionImport> getCollectionImports() {
		return this.collectionImports;
	}

	public void setCollectionImports(Set<CollectionImport> collectionImports) {
		this.collectionImports = collectionImports;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "markerGroup")
	public Set<UploadMarkerGroup> getUploadMarkerGroups() {
		return this.uploadMarkerGroups;
	}

	public void setUploadMarkerGroups(Set<UploadMarkerGroup> uploadMarkerGroups) {
		this.uploadMarkerGroups = uploadMarkerGroups;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "markerGroup")
	public Set<Marker> getMarkers() {
		return this.markers;
	}

	public void setMarkers(Set<Marker> markers) {
		this.markers = markers;
	}

}