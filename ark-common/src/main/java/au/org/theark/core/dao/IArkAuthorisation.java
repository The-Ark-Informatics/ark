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
import java.util.List;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkModuleRole;
import au.org.theark.core.model.study.entity.ArkRole;
import au.org.theark.core.model.study.entity.ArkRolePolicyTemplate;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.ArkUserRole;
import au.org.theark.core.model.study.entity.LinkStudyArkModule;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.vo.ArkModuleVO;
import au.org.theark.core.vo.ArkUserVO;

/**
 * @author nivedann
 * @param <T>
 * 
 */
public interface IArkAuthorisation<T> {

	/**
	 * This interface checks if the user is a Super Administrator
	 */
	public boolean isSuperAdministrator(String userName) throws EntityNotFoundException;

	public boolean isSuperAdministator(String ldapUserName, ArkFunction arkFunction, ArkModule arkModule) throws EntityNotFoundException;

	/**
	 * This interface checks if the user is a Administrator in the Ark System.
	 * 
	 * @param userName
	 * @return
	 */
	public boolean isAdministator(String userName) throws EntityNotFoundException;

	/**
	 * Returns a Collection of Roles as String for the given Ldap User Name.
	 * 
	 * @param ldapUserName
	 * @return
	 * @throws EntityNotFoundException
	 */
	public Collection<String> getUserAdminRoles(String ldapUserName) throws EntityNotFoundException;

	public String getUserRoleForStudy(String ldapUserName, Study study) throws EntityNotFoundException;

	public ArkFunction getArkFunctionByName(String functionName);

	public ArkFunction getArkFunctionById(Long functionId);

	public ArkModule getArkModuleByName(String moduleName);

	public ArkModule getArkModuleById(Long moduleId);

	/**
	 * 
	 * @param ldapUserName
	 * @param arkUseCase
	 * @param arkModule
	 * @param study
	 * @return
	 * @throws EntityNotFoundException
	 */
	public String getUserRole(String ldapUserName, ArkFunction arkFunction, ArkModule arkModule, Study study) throws EntityNotFoundException;

	/**
	 * For a given ArkRole,ArkFunction and ArkModule return a list of Permissions. This interface does not require the user(ArkUser) information.The
	 * ArkRole for the current user should be pre-determined before invoking this method.To get the user's role call getUserRole and then call this
	 * method. This method will use ArkRolePolicyTemplate table to return the list of Permissions for the given parameters.
	 * 
	 * @param ldapUserName
	 * @param arkUseCase
	 * @param userRole
	 * @param arkModule
	 * @param study
	 * @throws EntityNotFoundException
	 */
	public Collection<String> getArkRolePermission(ArkFunction arkFunction, String userRole, ArkModule arkModule) throws EntityNotFoundException;

	/**
	 * This overloaded interface can be used when we only want all their Permissions for a Given Role. It is applicable for Super Administator role
	 * where we don't need to specify the ArkModule or ArkRole. If this method is invoked for any other role it will return all the permissions for
	 * each role, the permissions will be duplicated. So avoid invoking this method for Non SuperAdministator type roles.
	 * 
	 * @param userRole
	 * @return
	 */
	public Collection<String> getArkRolePermission(String userRole) throws EntityNotFoundException;

	/**
	 * Returns All Permissions as collection of Strings
	 * 
	 * @return Collection<String> that represents ArkPermission
	 */
	public Collection<String> getArkPermission();

	public Collection<Class<T>> getEntityList(Class<T> aClass);

	public ArkUser getArkUser(String ldapUserName) throws EntityNotFoundException;

	public Collection<ArkModuleRole> getArkModuleAndLinkedRoles();

	/**
	 * Persist the Ark User name that was stored in LDAP and the related Study and Roles information for this user.
	 * 
	 * @param arkUserVO
	 */
	public void createArkUser(ArkUserVO arkUserVO);

	public Collection<ArkModuleVO> getArkModulesAndRolesLinkedToStudy(Study study);

	public ArrayList<ArkRole> getArkRoleLinkedToModule(ArkModule arkModule);

	public void updateArkUser(ArkUserVO arkUserVO) throws EntityNotFoundException, ArkSystemException;

	/**
	 * Returns a List of ArkUserRole objects if the specified ArkUser was present in the database. In the event that the Arkuser was not located in the
	 * database, an empty ArkUserRole list will be returned.
	 * 
	 * @param arkUserVO
	 * @return List<ArkUserRole>
	 * @throws
	 */
	public List<ArkUserRole> getArkUserLinkedModuleAndRoles(ArkUserVO arkUserVO) throws EntityNotFoundException;

	public Collection<ArkModule> getArkModulesLinkedWithStudy(Study study);

	/**
	 * 
	 * @param study
	 * @param arkModule
	 * @return
	 */
	public List<ArkUserRole> getArkUserLinkedModule(Study study, ArkModule arkModule);

	public List<LinkStudyArkModule> getLinkStudyArkModulesList(Study study);

	public void deleteArkUser(ArkUserVO arkUserVO) throws ArkSystemException, EntityNotFoundException;

	public List<ArkUserRole> getArkRoleListByUser(ArkUserVO arkUserVo);

	public List<ArkRolePolicyTemplate> getArkRolePolicyTemplate(ArkRole arkRole, ArkModule arkModule);
	
	public List<ArkUserRole> getArkRoleListByUserAndStudy(ArkUserVO arkUserVo, Study study);
	
	public List<Study> getStudyListForUser(ArkUserVO arkUserVo);
	
	public List<Study> getStudyListForUserAndModule(ArkUserVO arkUserVo, ArkModule arkModule);

	public boolean arkUserHasModuleAccess(ArkUser arkUser, ArkModule arkModule);
	
	public List<ArkModule> getArkModuleListByArkUser(ArkUser arkUser);
	
	public Boolean isArkUserLinkedToStudies(ArkUser arkUser);
	
	public List<ArkUserRole> getArkSuperAdministratorList() throws EntityNotFoundException;
}
