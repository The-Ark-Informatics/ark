package au.org.theark.admin.model.dao;

import java.util.List;

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
}
