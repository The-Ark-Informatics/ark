package au.org.theark.core.audit;

import org.apache.shiro.SecurityUtils;
import org.hibernate.envers.RevisionListener;

public class UsernameRevisionListener implements RevisionListener {

	@Override
	public void newRevision(Object revisionEntity) {
		UsernameRevisionEntity ure = (UsernameRevisionEntity) revisionEntity;
		ure.setUsername(SecurityUtils.getSubject().getPrincipal().toString());
	}

}
