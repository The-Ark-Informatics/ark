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
package au.org.theark.admin.service;

import java.util.Collection;
import java.util.List;

import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.admin.model.vo.ArkRoleModuleFunctionVO;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkFunctionType;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkModuleFunction;
import au.org.theark.core.model.study.entity.ArkPermission;
import au.org.theark.core.model.study.entity.ArkRole;
import au.org.theark.core.model.study.entity.ArkRolePolicyTemplate;

public interface IAdminService<T> {
	/**
	 * Create a new arkRolePolicyTemplate, via the reference AdminVO object
	 * 
	 * @param adminVo
	 */
	public void createArkRolePolicyTemplate(AdminVO adminVo);

	/**
	 * Update an arkRolePolicyTemplate, via the reference AdminVO object
	 * 
	 * @param adminVo
	 */
	public void updateArkRolePolicyTemplate(AdminVO adminVo);

	/**
	 * Delete an arkRolePolicyTemplate entity, via the reference AdminVO object
	 * 
	 * @param adminVo
	 */
	public void deleteArkRolePolicyTemplate(AdminVO adminVo);

	/**
	 * Create or update an arkRolePolicyTemplate entity
	 * 
	 * @param arkRolePolicyTemplate
	 */
	public void createOrUpdateArkRolePolicyTemplate(AdminVO adminVo);

	public List<ArkRole> getArkRoleList();

	public List<ArkModule> getArkModuleList();

	public List<ArkFunction> getArkFunctionList();

	public ArkRolePolicyTemplate getArkRolePolicyTemplate(Long id);
	
	public List<ArkRolePolicyTemplate> getArkRolePolicyTemplateList(ArkRolePolicyTemplate arkRolePolicyTemplate);

	public ArkPermission getArkPermissionByName(String name);

	public ArkModule getArkModule(Long id);

	public ArkFunction getArkFunction(Long id);

	public List<ArkFunction> searchArkFunction(ArkFunction arkFunction);

	public List<ArkFunctionType> getArkFunctionTypeList();

	public void creatOrUpdateArkFunction(AdminVO adminVo);

	public void deleteArkFunction(AdminVO adminVo);

	public void creatOrUpdateArkModule(AdminVO adminVo);

	public void deleteArkModule(AdminVO adminVo);

	public List<ArkModule> searchArkModule(ArkModule arkModule);

	public int getArkModuleCount(ArkModule arkModuleCriteria);

	public List<ArkModule> searchPageableArkModules(ArkModule arkModuleCriteria, int first, int count);

	public int getArkFunctionCount(ArkFunction arkFunctionCriteria);

	public List<ArkFunction> searchPageableArkFunctions(ArkFunction arkFunctionCriteria, int first, int count);

	public int getArkRoleModuleFunctionVOCount(ArkRoleModuleFunctionVO arkRoleModuleFunctionVO);
	
	public List<ArkRoleModuleFunctionVO> searchPageableArkRoleModuleFunctionVO(ArkRoleModuleFunctionVO arkRoleModuleFunctionVo, int first, int count);
	
	public ArkRole getArkRoleByName(String name);

	public List<ArkRoleModuleFunctionVO> getArkRoleModuleFunctionVoList(ArkRole arkRole);

	public List<ArkModule> getArkModuleList(ArkRole arkRole);

	public int getArkModuleFunctionCount(ArkModuleFunction arkModuleFunction);
	
	public List<ArkModuleFunction> searchPageableArkModuleFunctions(ArkModuleFunction arkModuleFunctionCriteria, int first, int count);

	public ArkModuleFunction getArkModuleFunction(Long id);

	public Collection<ArkFunction> getFunctionListByModule(ArkModule arkModule);

	public void creatOrUpdateArkModuleFunction(AdminVO modelObject);

	public ArkRole getArkRole(Long id);
	
	public int getArkRoleCount(ArkRole arkRoleCriteria);

	public List<ArkRole> searchPageableArkRoles(ArkRole arkRoleCriteria, int first, int count);
	
	public void createOrUpdateArkRole(AdminVO modelObject);
}
