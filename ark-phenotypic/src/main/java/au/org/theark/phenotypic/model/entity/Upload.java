package au.org.theark.phenotypic.model.entity;

import java.sql.Blob;
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

/**
 * Upload entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity(name="au.org.theark.phenotypic.model.entity.Upload")
@Table(name = "UPLOAD", schema = "PHENOTYPIC")
public class Upload implements java.io.Serializable {

	// Fields

	private Long id;
	private FileFormat fileFormat;
	private String filename;
	private Set<UploadCollection> uploadCollections = new HashSet<UploadCollection>(
			0);
	private Blob payload;
	private String userId;
	private Date insertTime;
	private String updateUserId;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public Upload() {
	}

	/** minimal constructor */
	public Upload(Long id, FileFormat fileFormat, String userId, Date insertTime) {
		this.id = id;
		this.fileFormat = fileFormat;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	/** full constructor */
	public Upload(Long id, FileFormat fileFormat, String filename,	Set<UploadCollection> uploadCollections) {
		this.id = id;
		this.fileFormat = fileFormat;
		this.filename = filename;
		this.uploadCollections = uploadCollections;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name="Upload_PK_Seq",sequenceName="PHENOTYPIC.UPLOAD_PK_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO,generator="Upload_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FILE_FORMAT_ID", nullable = false)
	public FileFormat getFileFormat() {
		return this.fileFormat;
	}

	public void setFileFormat(FileFormat fileFormat) {
		this.fileFormat = fileFormat;
	}

	@Column(name = "FILENAME", length = 260)
	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	@Column(name = "PAYLOAD")
	public Blob getPayload() {
		return this.payload;
	}

	public void setPayload(Blob payload) {
		this.payload = payload;
	}
	
	@Column(name = "USER_ID", nullable = false, length = 50)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

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

	@Column(name = "UPDATE_TIME")
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "upload")
	public Set<UploadCollection> getUploadCollections() {
		return this.uploadCollections;
	}

	public void setUploadCollections(Set<UploadCollection> uploadCollections) {
		this.uploadCollections = uploadCollections;
	}

}
