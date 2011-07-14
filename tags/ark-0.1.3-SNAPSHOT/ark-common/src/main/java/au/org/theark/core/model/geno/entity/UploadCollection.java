package au.org.theark.core.model.geno.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import au.org.theark.core.model.Constants;

/**
 * UploadCollection entity. @author MyEclipse Persistence Tools
 */
@Entity(name="au.org.theark.geno.model.entity.UploadCollection")
@Table(name = "UPLOAD_COLLECTION", schema = Constants.GENO_TABLE_SCHEMA)
public class UploadCollection implements java.io.Serializable {

	// Fields

	private Long id;
	private GenoCollection collection;
	private Upload upload;
	private String userId;
	private Date insertTime;
	private String updateUserId;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public UploadCollection() {
	}

	/** minimal constructor */
	public UploadCollection(Long id, GenoCollection collection, Upload upload) {
		this.id = id;
		this.collection = collection;
		this.upload = upload;
	}

	/** full constructor */
	public UploadCollection(Long id, GenoCollection collection, Upload upload,
			String userId, Date insertTime, String updateUserId,
			Date updateTime) {
		this.id = id;
		this.collection = collection;
		this.upload = upload;
		this.userId = userId;
		this.insertTime = insertTime;
		this.updateUserId = updateUserId;
		this.updateTime = updateTime;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name="Upload_Collection_PK_Seq",sequenceName=Constants.UPLOAD_COLLECTION_PK_SEQ)
	@GeneratedValue(strategy=GenerationType.AUTO,generator="Upload_Collection_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COLLECTION_ID", nullable = false)
	public GenoCollection getCollection() {
		return this.collection;
	}

	public void setCollection(GenoCollection collection) {
		this.collection = collection;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UPLOAD_ID", nullable = false)
	public Upload getUpload() {
		return this.upload;
	}

	public void setUpload(Upload upload) {
		this.upload = upload;
	}

	@Column(name = "USER_ID")
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

	@Column(name = "UPDATE_USER_ID")
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

}