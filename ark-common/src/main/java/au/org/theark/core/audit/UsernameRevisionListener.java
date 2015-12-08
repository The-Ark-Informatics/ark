package au.org.theark.core.audit;

import org.apache.shiro.SecurityUtils;
import org.hibernate.envers.RevisionListener;

public class UsernameRevisionListener implements RevisionListener {

	@Override
	public void newRevision(Object revisionEntity) {
		UsernameRevisionEntity ure = (UsernameRevisionEntity) revisionEntity;
		
		try {
			ure.setUsername(SecurityUtils.getSubject().getPrincipal().toString());
		} catch (Exception e) {
			
			e.printStackTrace();
			
			if(Thread.currentThread().getName().contains("Quartz")) { //If this is being called from a background thread
				System.out.println(AuditThreadLocalHelper.USERNAME.get());
				ure.setUsername(AuditThreadLocalHelper.USERNAME.get());
			}	
		}
			
	}

}
