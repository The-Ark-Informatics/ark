package au.org.theark.core.security;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.ArkModule;

/**
 * Global common class that determines permissions of particular action
 * @author cellis
 *
 */
public class ArkPermissionHelper
{
	private transient static Logger log = LoggerFactory.getLogger(ArkPermissionHelper.class);
	/**
	 * Determines whether a particular module function is accessible/permitted by the user in context
	 * @param actionType
	 * @return true if action is permitted
	 */
	public static boolean isModuleFunctionAccessPermitted()
	{
		boolean modulePermitted = true;
		
		SecurityManager securityManager = ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();

		if (!securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.CREATE) || 
			!securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.UPDATE) ||
			!securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.READ) || 
			!securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.DELETE))
		{
			modulePermitted = false;
		}		
		return modulePermitted;
	}
	
	/**
	 * Determines whether a particular module is accessible by the user, for the study in context
	 * @param study
	 * @param arkModule
	 * @return true if module set to be accessed/used within the specified study adn arkModule
	 */
	@SuppressWarnings("unchecked")
	public static boolean isModuleAccessPermitted(ArkModule arkModule)
	{
		boolean modulePermitted = true;
		
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Collection<ArkModule> arkModulesLinkedToStudy = new ArrayList<ArkModule>(0);  
		arkModulesLinkedToStudy = (Collection<ArkModule>) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SESSION_STUDY_MODULES_KEY);
		
		if(sessionStudyId != null)
		{
			if(arkModulesLinkedToStudy != null)
			{
				if(arkModulesLinkedToStudy.contains(arkModule))
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
	private static boolean hasSearchPermission(SecurityManager securityManager, Subject currentUser)
	{
		boolean flag = false;
		
		// Search allowed if user has READ permission
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
}