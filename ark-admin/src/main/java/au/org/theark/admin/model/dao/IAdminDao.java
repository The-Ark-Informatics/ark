package au.org.theark.admin.model.dao;

import java.util.List;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkFunctionType;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkPermission;
import au.org.theark.core.model.study.entity.ArkRole;
import au.org.theark.core.model.study.entity.ArkRolePolicyTemplate;

public interface IAdminDao
{
	/**
	 * Create a new arkRolePolicyTemplate
	 * @param arkRolePolicyTemplate
	 */
	public void createArkRolePolicyTemplate(ArkRolePolicyTemplate arkRolePolicyTemplate);
	
	/**
	 * Update an arkRolePolicyTemplate
	 * @param arkRolePolicyTemplate
	 */
	public void updateArkRolePolicyTemplate(ArkRolePolicyTemplate arkRolePolicyTemplate);
	
	/**
	 * Delete an arkRolePolicyTemplate entity
	 * @param arkRolePolicyTemplate
	 */
	public void deleteArkRolePolicyTemplate(ArkRolePolicyTemplate arkRolePolicyTemplate);
	
	/**
	 * Create or update an arkRolePolicyTemplate entity
	 * @param arkRolePolicyTemplate
	 */
	public void createOrUpdateArkRolePolicyTemplate(ArkRolePolicyTemplate arkRolePolicyTemplate);

	/**
	 * Get a list of ArkRolePolicyTemplate(s), grouped by Role, Module and function
	 * @return
	 */
	public List<ArkRolePolicyTemplate> getGroupedArkRolePolicyTemplates();
	
	public List<ArkRole> getArkRoleList();
	
	public List<ArkModule> getArkModuleList();

	public List<ArkFunction> getArkFunctionList();

	public ArkRolePolicyTemplate getArkRolePolicyTemplate(Long id);

	public List<ArkRolePolicyTemplate> getArkRolePolicyTemplateList();

	public List<ArkRolePolicyTemplate> searchArkRolePolicyTemplate(ArkRolePolicyTemplate arkRolePolicyTemplate);

	public ArkPermission getArkPermissionByName(String name);

	public ArkFunction getArkFunction(Long id);

	public ArkModule getArkModule(Long id);

	public void creatOrUpdateArkFunction(ArkFunction arkFunction);

	public void creatOrUpdateArkModule(ArkModule arkModule);

	public void deleteArkFunction(ArkFunction arkFunction);

	public void deleteArkModule(ArkModule arkModule);

	public List<ArkFunctionType> getArkFunctionTypeList();

	public List<ArkFunction> searchArkFunction(ArkFunction arkFunction);

	public List<ArkModule> searchArkModule(ArkModule arkModule);
}
