package au.org.theark.core.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;

import au.org.theark.core.Constants;

/**
 * Global common class that determines permissions od particular action
 * @author cellis
 *
 */
public class ArkSecurity
{
	protected static SecurityManager securityManager;
	protected static Subject currentUser;
	
	/**
	 * Determines whether a particular actionis permitted by the user in context (eg Save, Edit, Delete)
	 * @param actionType
	 * @return true if action is permitted
	 */
	public static boolean isActionPermitted(String actionType)
	{
		boolean actionPermitted = false;
		
		securityManager = ThreadContext.getSecurityManager();
		currentUser = SecurityUtils.getSubject();

		if (actionType.equalsIgnoreCase(Constants.SAVE))
		{
			actionPermitted = hasSavePermission();
		}
		else if (actionType.equalsIgnoreCase(Constants.EDIT))
		{
			actionPermitted = hasEditPermission();
		}
		else if (actionType.equalsIgnoreCase(Constants.DELETE))
		{
			actionPermitted = hasDeletePermission();	
		}
		return actionPermitted;
	}
	
	/**
	 * Determines if current user has Save permissions
	 * @return true if CREATE or UPDATE permission allowed
	 */
	public static boolean hasSavePermission()
	{
		boolean flag = false;
		// Save allowed if user has CREATE or UPDATE permissions
		if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.CREATE) || 
				securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.UPDATE))
		{
			flag = true;
		}
		else
		{
			flag = false;
		}
		return flag;
	}
	
	/**
	 * Determines if current user has Edit permissions
	 * @return true if UPDATE permission allowed
	 */
	public static boolean hasEditPermission()
	{
		boolean flag = false;
	
		if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.UPDATE))
		{
			flag = true;
		}
		else
		{
			flag = false;
		}
		
		return flag;
	}
	
	/**
	 * Determines if current user has Delete permissions
	 * @return true if DELETE permission allowed
	 */
	public static boolean hasDeletePermission()
	{
		boolean flag = false;
		
		if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.DELETE))
		{
			flag = true;
		}
		else
		{
			flag = false;
		}
	
		return flag;
	}	
}