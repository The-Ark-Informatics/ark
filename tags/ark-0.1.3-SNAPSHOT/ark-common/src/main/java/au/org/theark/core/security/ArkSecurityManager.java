package au.org.theark.core.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;

/**
 * A wrapper for Shiro Security Manager
 * @author nivedann
 *
 */
public class ArkSecurityManager {
	
	private static ArkSecurityManager arkManager;
	
	public static  ArkSecurityManager getInstance(){
		
		if(arkManager == null){
			arkManager = new ArkSecurityManager();
		}
		return arkManager;
	}
	
	private  SecurityManager getShiroSecurityManager(){
		return  ThreadContext.getSecurityManager();
		
	}
	
	public  boolean subjectHasRole(String roleName){
		
		Subject currentUser = SecurityUtils.getSubject();
		return getShiroSecurityManager().hasRole(currentUser.getPrincipals(), roleName);
	}

}
