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
package au.org.theark.admin.model.dao;

import java.util.Collection;
import java.util.List;

import au.org.theark.admin.model.vo.ArkRoleModuleFunctionVO;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkFunctionType;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkModuleFunction;
import au.org.theark.core.model.study.entity.ArkModuleRole;
import au.org.theark.core.model.study.entity.ArkPermission;
import au.org.theark.core.model.study.entity.ArkRole;
import au.org.theark.core.model.study.entity.ArkRolePolicyTemplate;

public interface IAdminDao {
	/**
	 * Create a new arkRolePolicyTemplate
	 * 
	 * @param arkRolePolicyTemplate
	 */
	public void createArkRolePolicyTemplate(ArkRolePolicyTemplate arkRolePolicyTemplate);

	/**
	 * Update an arkRolePolicyTemplate
	 * 
	 * @param arkRolePolicyTemplate
	 */
	public void updateArkRolePolicyTemplate(ArkRolePolicyTemplate arkRolePolicyTemplate);

	/**
	 * Delete an arkRolePolicyTemplate entity
	 * 
	 * @param arkRolePolicyTemplate
	 */
	public void deleteArkRolePolicyTemplate(ArkRolePolicyTemplate arkRolePolicyTemplate);

	/**
	 * Create or update an arkRolePolicyTemplate entity
	 * 
	 * @param arkRolePolicyTemplate
	 */
	public void createOrUpdateArkRolePolicyTemplate(ArkRolePolicyTemplate arkRolePolicyTemplate);

	public List<ArkRole> getArkRoleList();

	public List<ArkModule> getArkModuleList();

	public List<ArkFunction> getArkFunctionList();

	public ArkRolePolicyTemplate getArkRolePolicyTemplate(Long id);

	public List<ArkRolePolicyTemplate> getArkRolePolicyTemplateList();

	public ArkPermission getArkPermissionByName(String name);

	public ArkFunction getArkFunction(Long id);

	public ArkModule getArkModule(Long id);

	public void createOrUpdateArkFunction(ArkFunction arkFunction);

	public void createOrUpdateArkModule(ArkModule arkModule);

	public void deleteArkFunction(ArkFunction arkFunction);

	public void deleteArkModule(ArkModule arkModule);

	public List<ArkFunctionType> getArkFunctionTypeList();

	public List<ArkFunction> searchArkFunction(ArkFunction arkFunction);

	public List<ArkModule> searchArkModule(ArkModule arkModule);

	public long getArkModuleCount(ArkModule arkModuleCriteria);

	public List<ArkModule> searchPageableArkModules(ArkModule arkModuleCriteria, int first, int count);

	public long getArkFunctionCount(ArkFunction arkFunctionCriteria);

	public List<ArkFunction> searchPageableArkFunctions(ArkFunction arkFunctionCriteria, int first, int count);

	public long getArkRoleModuleFunctionVOCount(ArkRoleModuleFunctionVO arkRoleModuleFunctionVoCriteria);

	public List<ArkRoleModuleFunctionVO> searchPageableArkRoleModuleFunctionVO(ArkRoleModuleFunctionVO arkRoleModuleFunctionVo, int first, int count);
	
	public ArkRole getArkRoleByName(String name);

	public List<ArkRoleModuleFunctionVO> getArkRoleModuleFunctionVoList(ArkRole arkRole);

	public List<ArkRolePolicyTemplate> getArkRolePolicyTemplateList(ArkRolePolicyTemplate arkRolePolicyTemplate);

	public List<ArkModule> getArkModuleList(ArkRole arkRole);

	public long getArkModuleFunctionCount(ArkModuleFunction arkModuleFunctionCriteria);
	
	public List<ArkModuleFunction> searchPageableArkModuleFunctions(ArkModuleFunction arkModuleFunctionCriteria, int first, int count);

	public ArkModuleFunction getArkModuleFunction(Long id);

	public List<ArkFunction> getArkFunctionListByArkModule(ArkModule arkModule);
	
	public void createOrUpdateArkModuleFunction(ArkModule arkModule, Collection<ArkFunction> selectedArkFunctions);
	
	public ArkRole getArkRole(Long id);
	
	public long getArkRoleCount(ArkRole arkRoleCriteria);

	public List<ArkRole> searchPageableArkRoles(ArkRole arkRoleCriteria, int first, int count);
	
	public void createOrUpdateArkRole(ArkRole arkRole);
	
	public ArkModuleRole getArkModuleRole(Long id);

	public List<ArkRole> getArkRoleListByArkModule(ArkModule arkModule);
	
	public long getArkModuleRoleCount(ArkModuleRole arkModuleRoleCriteria);
	
	public List<ArkModuleRole> searchPageableArkModuleRoles(ArkModuleRole arkModulRoleCriteria, int first, int count);

	public void createArkModuleRole(ArkModule arkModule, Collection<ArkRole> selectedArkRoles);
	
	public void updateArkModuleRole(ArkModule arkModule, Collection<ArkRole> selectedArkRoles);

	public List<ArkModule> getArkModuleListByArkRole(ArkRole arkRole);
}
