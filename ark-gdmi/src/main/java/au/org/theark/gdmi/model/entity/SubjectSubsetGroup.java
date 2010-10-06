package au.org.theark.gdmi.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * SubjectSubsetGroup entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SUBJECT_SUBSET_GROUP", schema = "GDMI")
public class SubjectSubsetGroup implements java.io.Serializable {

	// Fields

	private Long id;
	private SubjectGroup subjectGroup;
	private Long subjectId;
	private String userId;
	private String insertTime;
	private String updateUserId;
	private String updateTime;

	// Constructors

	/** default constructor */
	public SubjectSubsetGroup() {
	}

	/** minimal constructor */
	public SubjectSubsetGroup(Long id, SubjectGroup subjectGroup,
			Long subjectId, String userId, String insertTime) {
		this.id = id;
		this.subjectGroup = subjectGroup;
		this.subjectId = subjectId;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	/** full constructor */
	public SubjectSubsetGroup(Long id, SubjectGroup subjectGroup,
			Long subjectId, String userId, String insertTime,
			String updateUserId, String updateTime) {
		this.id = id;
		this.subjectGroup = subjectGroup;
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
	@JoinColumn(name = "SUBJECT_GROUP_ID", nullable = false)
	public SubjectGroup getSubjectGroup() {
		return this.subjectGroup;
	}

	public void setSubjectGroup(SubjectGroup subjectGroup) {
		this.subjectGroup = subjectGroup;
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