package au.org.theark.gdmi.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ExtractPath entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EXTRACT_PATH", schema = "GDMI")
public class ExtractPath implements java.io.Serializable {

	// Fields

	private long id;
	private long studyId;
	private String localPath;
	private String userId;
	private String insertTime;
	private String updateUserId;
	private String updateTime;

	// Constructors

	/** default constructor */
	public ExtractPath() {
	}

	/** minimal constructor */
	public ExtractPath(long id, String userId, String insertTime) {
		this.id = id;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	/** full constructor */
	public ExtractPath(long id, long studyId, String localPath, String userId,
			String insertTime, String updateUserId, String updateTime) {
		this.id = id;
		this.studyId = studyId;
		this.localPath = localPath;
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