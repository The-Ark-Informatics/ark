package au.org.theark.core.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.vo.ArkSubjectSessionVO;

/**
 * A very simple implementation of a SessionAttribute listener, that maintains a synchronised List of current users logged into the ARK
 * 
 * @author cellis
 * 
 */
public class SessionAttributeListener implements HttpSessionAttributeListener, Serializable {
	private static final long						serialVersionUID	= 9115645497579631900L;
	protected transient Logger						log					= LoggerFactory.getLogger(SessionAttributeListener.class);
	// NOTE: listener is being called from multiple threads and ArrayList is unsynchronized
	private static List<ArkSubjectSessionVO>	activeUsers			= Collections.synchronizedList(new ArrayList<ArkSubjectSessionVO>());

	public static List<ArkSubjectSessionVO> getActiveUsers() {
		return activeUsers;
	}

	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		if (event.getName().equals(au.org.theark.core.Constants.ARK_USERID)) {
			Subject subject = SecurityUtils.getSubject();
			String sessionId = subject.getSession().getId().toString();
			String userId = subject.getPrincipal().toString();
			String host = subject.getSession().getHost();
			Date startTimestamp = subject.getSession().getStartTimestamp();
			Date lastAccessTime = subject.getSession().getLastAccessTime();
			String action = "Logged in";
			ArkSubjectSessionVO arkSubjectVo = new ArkSubjectSessionVO(sessionId, userId, host, startTimestamp, lastAccessTime, action);
			activeUsers.add(arkSubjectVo);
		}
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		Subject subject = SecurityUtils.getSubject();
		String currentUser = subject.getPrincipal().toString();
		if (event.getName().equals("org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY") && event.getValue().toString().equalsIgnoreCase(currentUser)) {
			// actual user logging off
			String sessionId = subject.getSession().getId().toString();
			ArkSubjectSessionVO arkSubjectVo = new ArkSubjectSessionVO(sessionId);
			activeUsers.remove(arkSubjectVo);
		}

		if (event.getName().equals(au.org.theark.core.Constants.ARK_LOGOFF_SESSION_ID)) {
			// Forced session kill by admin
			for (Iterator<ArkSubjectSessionVO> iterator = activeUsers.iterator(); iterator.hasNext();) {
				ArkSubjectSessionVO arkSubjectVo = (ArkSubjectSessionVO) iterator.next();
				if (arkSubjectVo.getSessionId().equalsIgnoreCase(event.getValue().toString())) {
					activeUsers.remove(arkSubjectVo);
				}
			}
		}
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		String sessionId = SecurityUtils.getSubject().getSession().getId().toString();
		ArkSubjectSessionVO arkSubjectVo = new ArkSubjectSessionVO(sessionId);
		activeUsers.get(activeUsers.indexOf(arkSubjectVo)).setLastAccessTime(new Date());
	}
}