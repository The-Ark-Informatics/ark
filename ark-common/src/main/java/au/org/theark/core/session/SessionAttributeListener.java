package au.org.theark.core.session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * A very simple implementation of a SessionAttribute listner, that maintains a synchronised List of current users logged into the ARK 
 * @author cellis
 *
 */
public class SessionAttributeListener implements HttpSessionAttributeListener {
	//TODO: Further implement to maintain detailed session information 
	private static List<Subject>	activeUsers	= Collections.synchronizedList(new ArrayList<Subject>()); // listener is being called from multiple
																																		// threads and ArrayList is unsynchronized

	public static List<Subject> getActiveUsers() {
		return activeUsers;
	}

	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		if (event.getName().equals(au.org.theark.core.Constants.ARK_USERID))
		{
			activeUsers.add(SecurityUtils.getSubject());
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
			for (Iterator<Subject> iterator = activeUsers.iterator(); iterator.hasNext();) {
				Subject subject = (Subject) iterator.next();
				if(subject.getSession().getId().toString().equalsIgnoreCase(event.getValue().toString())){
					activeUsers.remove(subject);
				}
			}
		}
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
	}
}
