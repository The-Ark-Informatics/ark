package au.org.theark.gdmi.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * SystemOption entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYSTEM_OPTION", schema = "GDMI")
public class SystemOption implements java.io.Serializable {

	// Fields

	private long id;
	private NewLogPer newLogPer;
	private long idleTimeout;
	private long refreshInterval;
	private long maxUploadLimit;
	private String smtpHost;
	private long smtpPort;
	private String eventLogPath;
	private String userId;
	private String insertTime;
	private String updateUserId;
	private String updateTime;

	// Constructors

	/** default constructor */
	public SystemOption() {
	}

	/** minimal constructor */
	public SystemOption(long id, NewLogPer newLogPer, String userId,
			String insertTime) {
		this.id = id;
		this.newLogPer = newLogPer;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	/** full constructor */
	public SystemOption(long id, NewLogPer newLogPer, long idleTimeout,
			long refreshInterval, long maxUploadLimit, String smtpHost,
			long smtpPort, String eventLogPath, String userId,
			String insertTime, String updateUserId, String updateTime) {
		this.id = id;
		this.newLogPer = newLogPer;
		this.idleTimeout = idleTimeout;
		this.refreshInterval = refreshInterval;
		this.maxUploadLimit = maxUploadLimit;
		this.smtpHost = smtpHost;
		this.smtpPort = smtpPort;
		this.eventLogPath = eventLogPath;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NEW_LOG_PER_PK", nullable = false)
	public NewLogPer getNewLogPer() {
		return this.newLogPer;
	}

	public void setNewLogPer(NewLogPer newLogPer) {
		this.newLogPer = newLogPer;
	}

	@Column(name = "IDLE_TIMEOUT", precision = 22, scale = 0)
	public long getIdleTimeout() {
		return this.idleTimeout;
	}

	public void setIdleTimeout(long idleTimeout) {
		this.idleTimeout = idleTimeout;
	}

	@Column(name = "REFRESH_INTERVAL", precision = 22, scale = 0)
	public long getRefreshInterval() {
		return this.refreshInterval;
	}

	public void setRefreshInterval(long refreshInterval) {
		this.refreshInterval = refreshInterval;
	}

	@Column(name = "MAX_UPLOAD_LIMIT", precision = 22, scale = 0)
	public long getMaxUploadLimit() {
		return this.maxUploadLimit;
	}

	public void setMaxUploadLimit(long maxUploadLimit) {
		this.maxUploadLimit = maxUploadLimit;
	}

	@Column(name = "SMTP_HOST", length = 1024)
	public String getSmtpHost() {
		return this.smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	@Column(name = "SMTP_PORT", precision = 22, scale = 0)
	public long getSmtpPort() {
		return this.smtpPort;
	}

	public void setSmtpPort(long smtpPort) {
		this.smtpPort = smtpPort;
	}

	@Column(name = "EVENT_LOG_PATH", length = 4000)
	public String getEventLogPath() {
		return this.eventLogPath;
	}

	public void setEventLogPath(String eventLogPath) {
		this.eventLogPath = eventLogPath;
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