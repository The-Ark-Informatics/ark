package au.org.theark.core.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;

/**
 * Global common class that provide helper methods to determine permissions of particular action/module
 * @author cellis
 *
 */
public class ArkPermissionHelper
{
	private transient static Logger log = LoggerFactory.getLogger(ArkPermissionHelper.class);
	
	/**
	 * Determines whether a particular module function is accessible/permitted by the user in context
	 * @param actionType
	 * @return true if user in context has any of the CREATE, UPDATE, or READ permissions
	 */
	public static boolean isModuleFunctionAccessPermitted()
	{
		boolean modulePermitted = true;
		
		SecurityManager securityManager = ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();
		
		boolean hasSearchPermission = hasSearchPermission(securityManager, currentUser); 
		boolean hasSavePermission = hasSavePermission(securityManager, currentUser);
		boolean hasEditPermission = hasEditPermission(securityManager, currentUser);

		boolean hasPermissions = (hasSearchPermission || hasSavePermission || hasEditPermission);
		if (!(hasPermissions))
		{
			modulePermitted = false;
		}
		return modulePermitted;
	}
	
	/**
	 * Determines whether a particular module is accessible by the user, for the study in context
	 * @param arkModuleName
	 * @return true if module set to be accessed/used within the study in context
	 */
	public static boolean isModuleAccessPermitted(String arkModuleName)
	{
		boolean modulePermitted = true;
		
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		
		if(sessionStudyId != null)
		{
			String arkModule = (String) SecurityUtils.getSubject().getSession().getAttribute(arkModuleName);
			if(arkModule != null)
			{
				if(arkModule.equals(arkModuleName))
				{
					modulePermitted = true;
				}
				else
				{
					modulePermitted = false;
				}
			}
		}
		else
		{
			modulePermitted = false;
		}
		return modulePermitted;
	}
	
	/**
	 * Determines whether a particular action is permitted by the user in context (eg Save, Edit, Delete)
	 * @param actionType
	 * @return true if action is permitted
	 */
	public static boolean isActionPermitted(String actionType)
	{
		boolean actionPermitted = false;
		
		SecurityManager securityManager = ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();

		if (actionType.equalsIgnoreCase(Constants.SEARCH))
		{
			actionPermitted = hasSearchPermission(securityManager, currentUser);	
		}
		else if (actionType.equalsIgnoreCase(Constants.SAVE) || actionType.equalsIgnoreCase(Constants.NEW))
		{
			actionPermitted = hasSavePermission(securityManager, currentUser);
		}
		else if (actionType.equalsIgnoreCase(Constants.EDIT))
		{
			actionPermitted = hasEditPermission(securityManager, currentUser);
		}
		else if (actionType.equalsIgnoreCase(Constants.DELETE))
		{
			actionPermitted = hasDeletePermission(securityManager, currentUser);	
		}
		
		return actionPermitted;
	}
	
	/**
	 * Determines if current user has Search permissions
	 * @param securityManager
	 * @param currentUser
	 * @return true if READ permission allowed
	 */
	public static boolean hasSearchPermission(SecurityManager securityManager, Subject currentUser)
	{
		boolean flag = false;
		
		if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.READ))
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
	 * Determines if current user has Save permissions
	 * @param securityManager
	 * @param currentUser
	 * @return true if CREATE or UPDATE permission allowed
	 */
	public static boolean hasSavePermission(SecurityManager securityManager, Subject currentUser)
	{
		boolean flag = false;
		
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
	 * @param securityManager
	 * @param currentUser
	 * @return true if UPDATE permission allowed
	 */
	public static boolean hasEditPermission(SecurityManager securityManager, Subject currentUser)
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
	 * @param securityManager
	 * @param currentUser
	 * @return true if DELETE permission allowed
	 */
	public static boolean hasDeletePermission(SecurityManager securityManager, Subject currentUser)
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

	/**
	 * @param log the log to set
	 */
	public static void setLog(Logger log)
	{
		ArkPermissionHelper.log = log;
	}

	/**
	 * @return the log
	 */
	public static Logger getLog()
	{
		return log;
	}	
}