package au.org.theark.core.model.study.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AuditHistory entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AUDIT_HISTORY", schema = "ETA")
public class AuditHistory implements java.io.Serializable {

	// Fields

	private Long auditHistoryKey;
	private String studyStatus;
	private Date dateTime;
	private Long actionTypeKey;
	private String etaUserId;
	private String comment;
	private Long entityKey;

	// Constructors

	/** default constructor */
	public AuditHistory() {
	}

	/** minimal constructor */
	public AuditHistory(Long auditHistoryKey) {
		this.auditHistoryKey = auditHistoryKey;
	}

	/** full constructor */
	public AuditHistory(Long auditHistoryKey, String studyStatus,
			Date dateTime, Long actionTypeKey, String etaUserId,
			String comment, Long entityKey) {
		this.auditHistoryKey = auditHistoryKey;
		this.studyStatus = studyStatus;
		this.dateTime = dateTime;
		this.actionTypeKey = actionTypeKey;
		this.etaUserId = etaUserId;
		this.comment = comment;
		this.entityKey = entityKey;
	}

	// Property accessors
	@Id
	@Column(name = "AUDIT_HISTORY_KEY", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getAuditHistoryKey() {
		return this.auditHistoryKey;
	}

	public void setAuditHistoryKey(Long auditHistoryKey) {
		this.auditHistoryKey = auditHistoryKey;
	}

	@Column(name = "STUDY_STATUS", length = 20)
	public String getStudyStatus() {
		return this.studyStatus;
	}

	public void setStudyStatus(String studyStatus) {
		this.studyStatus = studyStatus;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_TIME", length = 7)
	public Date getDateTime() {
		return this.dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	@Column(name = "ACTION_TYPE_KEY", precision = 22, scale = 0)
	public Long getActionTypeKey() {
		return this.actionTypeKey;
	}

	public void setActionTypeKey(Long actionTypeKey) {
		this.actionTypeKey = actionTypeKey;
	}

	@Column(name = "ETA_USER_ID", length = 50)
	public String getEtaUserId() {
		return this.etaUserId;
	}

	public void setEtaUserId(String etaUserId) {
		this.etaUserId = etaUserId;
	}

	@Column(name = "COMMENT")
	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Column(name = "ENTITY_KEY", precision = 22, scale = 0)
	public Long getEntityKey() {
		return this.entityKey;
	}

	public void setEntityKey(Long entityKey) {
		this.entityKey = entityKey;
	}

}