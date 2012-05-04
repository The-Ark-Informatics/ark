package au.org.theark.core.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import au.org.theark.core.vo.ArkSubjectVO;

/**
 * A very simple implementation of a SessionAttribute listner, that maintains a synchronised List of current users logged into the ARK 
 * @author cellis
 *
 */
public class SessionAttributeListener implements HttpSessionAttributeListener, Serializable {

	private static final long	serialVersionUID	= 9115645497579631900L;
	//TODO: Further implement to maintain detailed session information 
	private static List<ArkSubjectVO>	activeUsers	= Collections.synchronizedList(new ArrayList<ArkSubjectVO>()); // listener is being called from multiple
																																		// threads and ArrayList is unsynchronized

	public static List<ArkSubjectVO> getActiveUsers() {
		return activeUsers;
	}

	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		if (event.getName().equals(au.org.theark.core.Constants.ARK_USERID))
		{
			Subject subject = SecurityUtils.getSubject();
			String sessionId = subject.getSession().getId().toString();
			String userId = subject.getPrincipal().toString();
			String host = subject.getSession().getHost();
			ArkSubjectVO arkSubjectVo = new ArkSubjectVO(sessionId, userId, host);
			activeUsers.add(arkSubjectVo);
		}
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		String currentUser = SecurityUtils.getSubject().getPrincipal().toString();
		if (event.getName().equals("org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY") && event.getValue().toString().equalsIgnoreCase(currentUser))
		{
			// actual user logging off
			activeUsers.remove(SecurityUtils.getSubject());
		}
			
		if(event.getName().equals(au.org.theark.core.Constants.ARK_LOGOFF_SESSION_ID)) {
			// Forced session kill by admin
			for (Iterator<ArkSubjectVO> iterator = activeUsers.iterator(); iterator.hasNext();) {
				ArkSubjectVO arkSubjectVo = (ArkSubjectVO) iterator.next();
				if(arkSubjectVo.getSessionId().equalsIgnoreCase(event.getValue().toString())){
					activeUsers.remove(arkSubjectVo);
				}
			}
		}
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
	}
}
