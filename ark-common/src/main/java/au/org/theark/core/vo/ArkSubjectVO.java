package au.org.theark.core.vo;

import java.io.Serializable;

public class ArkSubjectVO implements Serializable {

	private static final long	serialVersionUID	= 402893651068142890L;
	private String					sessionId;
	private String					userId;
	private String					host;

	public ArkSubjectVO(String sessionId, String userId, String host) {
		super();
		this.sessionId = sessionId;
		this.userId = userId;
		this.host = host;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
}
