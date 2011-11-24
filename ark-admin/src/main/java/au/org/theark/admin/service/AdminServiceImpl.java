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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.admin.model.dao.IAdminDao;
import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.admin.model.vo.ArkRoleModuleFunctionVO;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkFunctionType;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkModuleFunction;
import au.org.theark.core.model.study.entity.ArkModuleRole;
import au.org.theark.core.model.study.entity.ArkPermission;
import au.org.theark.core.model.study.entity.ArkRole;
import au.org.theark.core.model.study.entity.ArkRolePolicyTemplate;

/**
 * The implementation of IAdminService. We want to auto-wire and hence use the @Service annotation.
 * 
 * @author cellis
 * @param <T>
 * 
 */
@Transactional
@Service(au.org.theark.admin.service.Constants.ARK_ADMIN_SERVICE)
public class AdminServiceImpl<T> implements IAdminService<T> {
	private IAdminDao	iAdminDao;

	public IAdminDao getAdminDao() {
		return iAdminDao;
	}

	@Autowired
	public void setAdminDao(IAdminDao adminDao) {
		this.iAdminDao = adminDao;
	}

	public void createArkRolePolicyTemplate(AdminVO adminVo) {
		iAdminDao.createArkRolePolicyTemplate(adminVo.getArkRolePolicyTemplate());
	}

	public void updateArkRolePolicyTemplate(AdminVO adminVo) {
		iAdminDao.updateArkRolePolicyTemplate(adminVo.getArkRolePolicyTemplate());
	}

	public void deleteArkRolePolicyTemplate(AdminVO adminVo) {
		iAdminDao.deleteArkRolePolicyTemplate(adminVo.getArkRolePolicyTemplate());
	}

	public void createOrUpdateArkRolePolicyTemplate(AdminVO adminVo) {
		iAdminDao.createOrUpdateArkRolePolicyTemplate(adminVo.getArkRolePolicyTemplate());
	}

	public List<ArkFunction> getArkFunctionList() {
		return iAdminDao.getArkFunctionList();
	}

	public List<ArkModule> getArkModuleList() {
		return iAdminDao.getArkModuleList();
	}

	public ArkPermission getArkPermissionByName(String name) {
		return iAdminDao.getArkPermissionByName(name);
	}

	public List<ArkRole> getArkRoleList() {
		return iAdminDao.getArkRoleList();
	}

	public ArkRolePolicyTemplate getArkRolePolicyTemplate(Long id) {
		return iAdminDao.getArkRolePolicyTemplate(id);
	}

	public List<ArkRolePolicyTemplate> getArkRolePolicyTemplateList() {
		return iAdminDao.getArkRolePolicyTemplateList();
	}

	public ArkFunction getArkFunction(Long id) {
		return iAdminDao.getArkFunction(id);
	}

	public ArkModule getArkModule(Long id) {
		return iAdminDao.getArkModule(id);
	}

	public void createOrUpdateArkFunction(AdminVO adminVo) {
		iAdminDao.createOrUpdateArkFunction(adminVo.getArkFunction());
	}

	public void createOrUpdateArkModule(AdminVO adminVo) {
		iAdminDao.createOrUpdateArkModule(adminVo.getArkModule());
	}

	public void deleteArkFunction(AdminVO adminVo) {
		iAdminDao.deleteArkFunction(adminVo.getArkFunction());
	}

	public void deleteArkModule(AdminVO adminVo) {
		iAdminDao.deleteArkModule(adminVo.getArkModule());
	}

	public List<ArkFunctionType> getArkFunctionTypeList() {
		return iAdminDao.getArkFunctionTypeList();
	}

	public List<ArkFunction> searchArkFunction(ArkFunction arkFunction) {
		return iAdminDao.searchArkFunction(arkFunction);
	}

	public List<ArkModule> searchArkModule(ArkModule arkModule) {
		return iAdminDao.searchArkModule(arkModule);
	}

	public int getArkFunctionCount(ArkFunction arkFunctionCriteria) {
		return iAdminDao.getArkFunctionCount(arkFunctionCriteria);
	}

	public int getArkModuleCount(ArkModule arkModuleCriteria) {
		return iAdminDao.getArkModuleCount(arkModuleCriteria);
	}

	public List<ArkFunction> searchPageableArkFunctions(ArkFunction arkFunctionCriteria, int first, int count) {
		return iAdminDao.searchPageableArkFunctions(arkFunctionCriteria, first, count);
	}

	public List<ArkModule> searchPageableArkModules(ArkModule arkModuleCriteria, int first, int count) {
		return iAdminDao.searchPageableArkModules(arkModuleCriteria, first, count);
	}

	public int getArkRoleModuleFunctionVOCount(ArkRoleModuleFunctionVO arkRoleModuleFunctionVO) {
		return iAdminDao.getArkRoleModuleFunctionVOCount(arkRoleModuleFunctionVO);
	}

	public List<ArkRoleModuleFunctionVO> searchPageableArkRoleModuleFunctionVO(ArkRoleModuleFunctionVO arkRoleModuleFunctionVo, int first, int count) {
		return iAdminDao.searchPageableArkRoleModuleFunctionVO(arkRoleModuleFunctionVo, first, count);
	}

	public ArkRole getArkRoleByName(String name) {
		return iAdminDao.getArkRoleByName(name);
	}

	public List<ArkRoleModuleFunctionVO> getArkRoleModuleFunctionVoList(ArkRole arkRole) {
		return iAdminDao.getArkRoleModuleFunctionVoList(arkRole);
	}

	public List<ArkRolePolicyTemplate> getArkRolePolicyTemplateList(ArkRolePolicyTemplate arkRolePolicyTemplate) {
		return iAdminDao.getArkRolePolicyTemplateList(arkRolePolicyTemplate);
	}

	public List<ArkModule> getArkModuleList(ArkRole arkRole) {
		return iAdminDao.getArkModuleList(arkRole);
	}

	public int getArkModuleFunctionCount(ArkModuleFunction arkModuleFunctionCriteria) {
		return iAdminDao.getArkModuleFunctionCount(arkModuleFunctionCriteria);
	}

	public List<ArkModuleFunction> searchPageableArkModuleFunctions(ArkModuleFunction arkModuleFunctionCriteria, int first, int count) {
		return iAdminDao.searchPageableArkModuleFunctions(arkModuleFunctionCriteria, first, count);
	}

	public ArkModuleFunction getArkModuleFunction(Long id) {
		return iAdminDao.getArkModuleFunction(id);
	}

	public List<ArkFunction> getArkFunctionListByArkModule(ArkModule arkModule) {
		return iAdminDao.getArkFunctionListByArkModule(arkModule);
	}

	public void createOrUpdateArkModuleFunction(AdminVO modelObject) {
		iAdminDao.createOrUpdateArkModuleFunction(modelObject.getArkModuleFunction().getArkModule(), modelObject.getSelectedArkFunctions());
	}

	public void createOrUpdateArkRole(AdminVO modelObject) {
		iAdminDao.createOrUpdateArkRole(modelObject.getArkRole());
	}

	public ArkRole getArkRole(Long id) {
		return iAdminDao.getArkRole(id);
	}

	public int getArkRoleCount(ArkRole arkRoleCriteria) {
		return iAdminDao.getArkRoleCount(arkRoleCriteria);
	}

	public List<ArkRole> searchPageableArkRoles(ArkRole arkRoleCriteria, int first, int count) {
		return iAdminDao.searchPageableArkRoles(arkRoleCriteria, first, count);
	}

	public List<ArkModule> getArkModuleListByArkRole(ArkRole arkRole) {
		return iAdminDao.getArkModuleListByArkRole(arkRole);
	}

	public void createArkModuleRole(AdminVO modelObject) {
		iAdminDao.createArkModuleRole(modelObject.getArkModuleRole().getArkModule(), modelObject.getSelectedArkRoles());
	}
	
	public void updateArkModuleRole(AdminVO modelObject) {
		iAdminDao.updateArkModuleRole(modelObject.getArkModuleRole().getArkModule(), modelObject.getSelectedArkRoles());
	}

	public ArkModuleRole getArkModuleRole(Long id) {
		return iAdminDao.getArkModuleRole(id);
	}

	public int getArkModuleRoleCount(ArkModuleRole arkModuleRoleCriteria) {
		return iAdminDao.getArkModuleRoleCount(arkModuleRoleCriteria);
	}

	public List<ArkRole> getArkRoleListByArkModule(ArkModule arkModule) {
		return iAdminDao.getArkRoleListByArkModule(arkModule);
	}

	public List<ArkModuleRole> searchPageableArkModuleRoles(ArkModuleRole arkModulRoleCriteria, int first, int count) {
		return iAdminDao.searchPageableArkModuleRoles(arkModulRoleCriteria, first, count);
	}
}