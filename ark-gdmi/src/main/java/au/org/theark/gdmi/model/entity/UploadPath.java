package au.org.theark.gdmi.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * UploadPath entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "UPLOAD_PATH", schema = "GDMI")
public class UploadPath implements java.io.Serializable {

	// Fields

	private long id;
	private long studyId;
	private String localPath;
	private String userId;
	private String insertTime;
	private String updateUser;
	private String updateTime;

	// Constructors

	/** default constructor */
	public UploadPath() {
	}

	/** minimal constructor */
	public UploadPath(long id, String userId, String insertTime) {
		this.id = id;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	/** full constructor */
	public UploadPath(long id, long studyId, String localPath, String userId,
			String insertTime, String updateUser, String updateTime) {
		this.id = id;
		this.studyId = studyId;
		this.localPath = localPath;
		this.userId = userId;
		this.insertTime = insertTime;
		this.updateUser = updateUser;
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

	@Column(name = "STUDY_ID", precision = 22, scale = 0)
	public long getStudyId() {
		return this.studyId;
	}

	public void setStudyId(long studyId) {
		this.studyId = studyId;
	}

	@Column(name = "LOCAL_PATH", length = 1000)
	public String getLocalPath() {
		return this.localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
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

}