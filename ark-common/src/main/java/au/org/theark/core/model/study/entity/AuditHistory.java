package au.org.theark.core.model.study.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import au.org.theark.core.Constants;

/**
 * AuditHistory entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AUDIT_HISTORY", schema =  Constants.STUDY_SCHEMA)
public class AuditHistory implements java.io.Serializable {

	// Fields

	private Long id;
	private StudyStatus studyStatus;
	private Date dateTime;
	private ActionType actionType;
	private String arkUserId;
	private String comment;
	private Long entityId;
	private EntityType entityType;

	// Constructors

	/** default constructor */
	public AuditHistory() {
	}

	/** minimal constructor */
	public AuditHistory(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AuditHistory(Long id, StudyStatus studyStatus,
			Date dateTime, ActionType actionType, String etaUserId,
			String comment, Long entityKey, EntityType entityType) {
		this.id = id;
		this.studyStatus = studyStatus;
		this.dateTime = dateTime;
		this.actionType = actionType;
		this.arkUserId = etaUserId;
		this.comment = comment;
		this.entityId = entityKey;
		this.entityType = entityType;
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


	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_TIME", length = 7)
	public Date getDateTime() {
		return this.dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	@Column(name = "COMMENT")
	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Column(name = "ENTITY_ID", precision = 22, scale = 0)
	public Long getEntityId() {
		return this.entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public StudyStatus getStudyStatus() {
		return studyStatus;
	}

	public void setStudyStatus(StudyStatus studyStatus) {
		this.studyStatus = studyStatus;
	}

	@Column(name = "ACTION_TYPE_ID", precision = 22, scale = 0)
	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	
	@Column(name = "ARK_USER_ID", length = 50)
	public String getArkUserId() {
		return arkUserId;
	}

	public void setArkUserId(String arkUserId) {
		this.arkUserId = arkUserId;
	}

	@Column(name = "ENTITY_TYPE_ID", precision = 22, scale = 0)
	public EntityType getEntityType() {
		return entityType;
	}

	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}

}