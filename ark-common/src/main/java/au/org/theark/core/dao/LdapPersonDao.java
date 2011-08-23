/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.core.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Repository;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.util.UIHelper;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.core.vo.RoleVO;

/**
 * The Data Access Object Implementation that does the grunt of the work to pull data from LDAP The implementation must be auto-wired for spring to
 * detect it. We use the @Repository annotation here. It must only be defined in the implementation class. This is a service specifically for Shiro
 * Security based Authentication and Authorization.
 * 
 * @author nivedann
 * 
 */
@Repository("ldapPersonDao")
public class LdapPersonDao implements ILdapPersonDao {
	static Logger			log	= LoggerFactory.getLogger(LdapPersonDao.class);
	private LdapTemplate	ldapTemplate;
	private String			baseModuleDn;
	private String			baseGroupDn;
	private String			basePeopleDn;
	/**
	 * A property that reflects the value of the LDAP paths defined in application context
	 */
	private String			baseDC;

	public String getBaseModuleDn() {
		return baseModuleDn;
	}

	public void setBaseModuleDn(String baseModuleDn) {
		this.baseModuleDn = baseModuleDn;
	}

	public String getBaseGroupDn() {
		return baseGroupDn;
	}

	public void setBaseGroupDn(String baseGroupDn) {
		this.baseGroupDn = baseGroupDn;
	}

	public String getBasePeopleDn() {
		return basePeopleDn;
	}

	public void setBasePeopleDn(String basePeopleDn) {
		this.basePeopleDn = basePeopleDn;
	}

	public String getBaseDC() {
		return baseDC;
	}

	public void setBaseDC(String baseDC) {
		this.baseDC = baseDC;
	}

	public LdapTemplate getLdapTemplate() {
		return ldapTemplate;
	}

	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}

	public ArkUserVO getUser(String username) throws ArkSystemException {
		ArkUserVO userVO = new ArkUserVO();
		try {

			LdapName ldapName = new LdapName(basePeopleDn);
			ldapName.add(new Rdn("cn", username));
			Name nameObj = (Name) ldapName;
			userVO = (ArkUserVO) ldapTemplate.lookup(nameObj, new PersonContextMapper());

		}
		catch (InvalidNameException ne) {

			throw new ArkSystemException("A System error has occured");

		}

		return userVO;
	}

	private static class PersonContextMapper implements ContextMapper {

		public Object mapFromContext(Object ctx) {

			DirContextAdapter context = (DirContextAdapter) ctx;

			ArkUserVO etaUserVO = new ArkUserVO();
			etaUserVO.setUserName(context.getStringAttribute("cn"));
			etaUserVO.setFirstName(context.getStringAttribute("givenName"));
			etaUserVO.setLastName(context.getStringAttribute("sn"));
			etaUserVO.setEmail(context.getStringAttribute("mail"));
			String ldapPassword = new String((byte[]) context.getObjectAttribute("userPassword"));
			etaUserVO.setPassword(ldapPassword);
			return etaUserVO;
		}
	}

	/**
	 * Fetches a user from LDAP based on username Invokes a method isMemberof to populate a list of modules/groups and roles.
	 */
	public List<String> getUserRole(String username) throws ArkSystemException {

		ArkUserVO userVO = getUser(username);

		List<ModuleVO> moduleList = new ArrayList<ModuleVO>();
		userVO.setModules(moduleList);
		List<String> roles = new ArrayList<String>();
		userVO.setUserRoleList(roles);
		// Provide the Module Name from a list of modules available.

		// get LDAP Module Names
		List<ModuleVO> listOfAllModules = getModules(false);

		isMemberof(listOfAllModules, userVO);

		return userVO.getUserRoleList();
	}

	public List<ModuleVO> getModules(boolean isDisplay) {
		log.debug("getModules()");
		AndFilter moduleFilter = new AndFilter();
		moduleFilter.and(new EqualsFilter("objectClass", "organizationalUnit"));

		List<?> modules = ldapTemplate.search(getBaseModuleDn(), moduleFilter.encode(), SearchControls.ONELEVEL_SCOPE, new AttributesMapper() {

			public Object mapFromAttributes(Attributes attrs) {
				try {
					Object returnObject = attrs.get("ou").get();
					return returnObject;
				}
				catch (javax.naming.NamingException e) {
					//logger.error("$AttributesMapper.mapFromAttributes(Attributes) - NamingException", e); //$NON-NLS-1$
					// e.printStackTrace();
				}
				return attrs;
			}
		});

		return buildModuleNames(modules, isDisplay);

	}

	public void isMemberof(List<ModuleVO> moduleVOlist, ArkUserVO userVO) throws ArkSystemException {

		for (ModuleVO moduleVO : moduleVOlist) {
			isMemberof(moduleVO.getModule(), userVO);
		}
	}

	/**
	 * The method will search for a user in the group and returns a list of objects or sub-groups where a match is found. For a given moduleName, it
	 * will return all sub-groups i.e super_administration group and all the study(groups) where the user is a member of.
	 * 
	 * @param moduleName
	 * @param username
	 * @return
	 */
	public void isMemberof(String moduleName, ArkUserVO etaUserVO) throws ArkSystemException {
		/* Given Module Id and Role Name we can determine 1. If the user is a member of the module 2. If he has a particular role */
		log.debug("\n --- isMemberof = " + moduleName);

		boolean isStudyAvailable = false;
		String groupBase = "ou=groups";
		AndFilter moduleFilter = new AndFilter();
		moduleFilter.and(new EqualsFilter("objectClass", "groupOfNames"));
		String personDn = "";
		try {
			LdapName ldapName = new LdapName(baseDC);
			ldapName.add(new Rdn("ou", basePeopleDn));
			ldapName.add(new Rdn("cn", etaUserVO.getUserName()));
			personDn = ldapName.toString();

			moduleFilter.and(new EqualsFilter("member", personDn));

			// Fetches a list of roles that have been assigned to the user indicating that the user is a member of the GROUP/Application and the various
			// roles.
			LdapName moduleDn = new LdapName(groupBase);
			moduleDn.add(new Rdn("cn", moduleName.trim()));

			if (etaUserVO.getStudyVO() != null && etaUserVO.getStudyVO().getStudyName() != null) {
				moduleDn.add(new Rdn("cn", etaUserVO.getStudyVO().getStudyName()));// Add the study as a filter too
				isStudyAvailable = true;
			}
			// For the given module, see if the user is member for any sub-groups. That is he can be a member of any study etc
			List<?> userGroups = ldapTemplate.search(moduleDn, moduleFilter.encode(), SearchControls.ONELEVEL_SCOPE, new AttributesMapper() {

				public Object mapFromAttributes(Attributes attrs) {
					try {
						Object returnObject = attrs.get("cn").get();
						return returnObject;
					}
					catch (NamingException e) {

						e.printStackTrace();
					}
					return attrs;
				}
			});

			if (userGroups != null && userGroups.size() > 0) {

				// Add the module/groups the user is a member of into a Modulelist in userVO
				ModuleVO moduleVO = new ModuleVO();
				moduleVO.setModule(moduleName);
				etaUserVO.getModules().add(moduleVO);
				List<RoleVO> roleList = new ArrayList<RoleVO>();
				moduleVO.setRole(roleList);// Initialise the role arraylist
				// Populate the Module.Roles collection. This structure of classifying roles against module allows us to maintain roles
				// without any conflict(roles are in the module namespace)
				// Look up sub-groups for each userGroup returned to get to the roles if any
				for (Object groupName : userGroups) {
					LdapName subModuleDn = new LdapName(groupBase);
					subModuleDn.add(new Rdn("cn", moduleName.trim()));
					subModuleDn.add(new Rdn("cn", groupName.toString()));

					List<?> subGroupRoles = ldapTemplate.search(subModuleDn, moduleFilter.encode(), SearchControls.ONELEVEL_SCOPE, new AttributesMapper() {

						public Object mapFromAttributes(Attributes attrs) {
							try {
								Object returnObject = attrs.get("cn").get();
								return returnObject;
							}
							catch (NamingException e) {

								e.printStackTrace();
							}
							return attrs;
						}
					});
					// If the subGroup size had more than
					if (subGroupRoles.size() > 0) {
						for (Object roleName : subGroupRoles) {
							etaUserVO.getUserRoleList().add(roleName.toString());
							// etaUserVO.getUserRoleList().add(moduleName + "_" + groupName.toString() + "_" + roleName.toString());
						}
					}
					else {
						// Is a role itself and has not sub-groups.e.g. like Super_Administrator
						etaUserVO.getUserRoleList().add(groupName.toString());
						// etaUserVO.getUserRoleList().add(moduleName + "_" + groupName.toString());
					}
				}
			}
		}
		catch (InvalidNameException e) {
			e.printStackTrace();
			log.error("A system error has occured" + e.getStackTrace());
			throw new ArkSystemException("A system error has occured");
		}
		catch (Exception ee) {
			StringBuffer sb = new StringBuffer();
			sb.append("The study");
			sb.append(etaUserVO.getStudyVO().getStudyName());
			sb.append(" in module :");
			sb.append(moduleName);
			sb.append(" stack trace ");
			sb.append(ee.getStackTrace());
			log.debug(sb.toString());
		}
	}

	/**
	 * A Helper method to build the list with either a display items or the actual data from back end.
	 * 
	 * @param moduleList
	 * @param isDisplay
	 */
	private List<ModuleVO> buildModuleNames(List<?> modules, boolean isForDisplay) {
		List<ModuleVO> moduleList = new ArrayList<ModuleVO>();
		for (Iterator<?> iterator = modules.iterator(); iterator.hasNext();) {
			String moduleName = (String) iterator.next();

			ModuleVO moduleVO = new ModuleVO();

			if (isForDisplay) {
				moduleVO.setModule(UIHelper.getDisplayModuleName(moduleName));
			}
			else {
				moduleVO.setModule(moduleName);
			}
			moduleList.add(moduleVO);

			// get the roles for the module
			List<String> roles = getModuleRoles(moduleName);
			for (String roleName : roles) {
				RoleVO roleVO = new RoleVO();
				roleVO.setRole(roleName);
				moduleVO.getRole().add(roleVO);
			}

		}
		return moduleList;
	}

	/**
	 * Gets a list of roles for a given moduleId/application name
	 */
	public List<String> getModuleRoles(String moduleId) {

		log.debug("\n ------ In getModuleRoles() " + moduleId);
		if (moduleId == null || moduleId.trim().length() == 0) {
			return null;// Throw an application exception here
		}

		AndFilter andFilter = new AndFilter();
		andFilter.and(new EqualsFilter("objectClass", "organizationalRole"));

		List<String> roleVoList = new ArrayList<String>();
		LdapName dn;
		try {
			dn = new LdapName(getBaseModuleDn());
			dn.add(new Rdn("ou", moduleId.trim()));// get the module name from the parameter
			dn.add(new Rdn("ou", "roles"));
			log.debug("Filter to apply = " + andFilter.encode() + " DN =" + dn.toString());

			List<?> roles = ldapTemplate.search(dn, andFilter.encode(), SearchControls.SUBTREE_SCOPE, new AttributesMapper() {

				public Object mapFromAttributes(Attributes attrs) {
					try {
						Object returnObject = attrs.get("cn").get();
						return returnObject;
					}
					catch (javax.naming.NamingException e) {
						log.debug("Exception occured in getModuleRoles " + e.getMessage());
						e.printStackTrace();
						//logger.error("$AttributesMapper.mapFromAttributes(Attributes) - NamingException", e); //$NON-NLS-1$
						// e.printStackTrace();
					}
					return attrs;
				}
			});

			roleVoList = buildRoleNames(roles, true);

		}
		catch (InvalidNameException ine) {
			log.error("An Exception occured in getModuleRoles()" + ine.getMessage());
			return null;// need to notify admin
		}
		return roleVoList;
	}

	private List<String> buildRoleNames(List<?> roles, boolean isForDisplay) {

		List<String> displayRoleList = new ArrayList<String>();

		for (Object role : roles) {
			String roleName = (String) role;
			if (isForDisplay) {
				roleName = UIHelper.getDisplayRoleName(roleName);
			}
			displayRoleList.add(roleName);
		}
		return displayRoleList;
	}

	// private String buildUserRoles(List<?> groupList, ModuleVO moduleVO, boolean isStudy){
	// String roleName ="ordinary_user";
	// for (Object group : groupList) {
	// //Check if user is part of the super_administrator group
	// if(group.toString().equalsIgnoreCase("super_administrator")){
	// roleName = moduleVO.getModule() + "_" + group.toString();
	// }
	// else if(!isStudy){
	// roleName = moduleVO.getModule() + "_" + roleName;
	// //This will be a sub-group under the application/module. Get a list of sub-groups which should be the actual grouping of roles.
	// }else{
	// roleName = group.toString();
	// }
	// RoleVO roleVO = new RoleVO();
	// roleVO.setRole(roleName);
	// moduleVO.getRole().add(roleVO );
	// break;
	// }
	// return roleName;
	// }

	private Collection<String> getRolesForGroup(List<?> groupList) {

		for (Object subGroup : groupList) {

		}
		Collection<String> roleCollection = new ArrayList<String>();

		return roleCollection;
	}

}
