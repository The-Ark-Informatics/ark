package au.org.theark.core.vo;

import java.io.Serializable;
import java.util.Date;

public class ArkSubjectSessionVO implements Serializable {

	private static final long	serialVersionUID	= 402893651068142890L;
	private String					sessionId;
	private String					userId;
	private String					host;
	private Date					startTimestamp;
	private Date					lastAccessTime;
	private String					action;
	
	public ArkSubjectSessionVO(String sessionId) {
		super();
		this.sessionId = sessionId;
	}
	
	public ArkSubjectSessionVO(String sessionId, String userId, String host, Date startTimestamp, Date lastAccessTime, String action) {
		super();
		this.sessionId = sessionId;
		this.userId = userId;
		this.host = host;
		this.startTimestamp = startTimestamp;
		this.lastAccessTime = lastAccessTime;
		this.action = action;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the startTimestamp
	 */
	public Date getStartTimestamp() {
		return startTimestamp;
	}

	/**
	 * @param startTimestamp the startTimestamp to set
	 */
	public void setStartTimestamp(Date startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	/**
	 * @return the lastAccessTime
	 */
	public Date getLastAccessTime() {
		return lastAccessTime;
	}

	/**
	 * @param lastAccessTime the lastAccessTime to set
	 */
	public void setLastAccessTime(Date lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sessionId == null) ? 0 : sessionId.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArkSubjectSessionVO other = (ArkSubjectSessionVO) obj;
		if (sessionId == null) {
			if (other.sessionId != null)
				return false;
		}
		else if (!sessionId.equals(other.sessionId))
			return false;
		return true;
	}
}
