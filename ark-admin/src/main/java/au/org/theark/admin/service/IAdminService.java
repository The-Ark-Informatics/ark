package au.org.theark.admin.service;

import java.util.List;

import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.admin.model.vo.ArkRoleModuleFunctionVO;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkFunctionType;
import au.org.theark.core.model.study.entity.ArkModule;
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
}