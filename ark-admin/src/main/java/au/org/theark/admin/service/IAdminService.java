package au.org.theark.admin.service;

import java.util.List;

import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.core.model.study.entity.ArkRolePolicyTemplate;

public interface IAdminService<T>
{	
	/**
	 * Create a new arkRolePolicyTemplate, via the reference AdminVO object
	 * @param adminVo
	 */
	public void createArkRolePolicyTemplate(AdminVO adminVo);
	
	/**
	 * Update an arkRolePolicyTemplate, via the reference AdminVO object
	 * @param adminVo
	 */
	public void updateArkRolePolicyTemplate(AdminVO adminVo);
	
	/**
	 * Delete an arkRolePolicyTemplate entity, via the reference AdminVO object
	 * @param adminVo
	 */
	public void deleteArkRolePolicyTemplate(AdminVO adminVo);
	
	/**
	 * Create or update an arkRolePolicyTemplate entity
	 * @param arkRolePolicyTemplate
	 */
	public void createOrUpdateArkRolePolicyTemplate(AdminVO adminVo);
	
	/**
	 * Get a list of ArkRolePolicyTemplate(s), grouped by Role, Module and function
	 * @return
	 */
	public List<ArkRolePolicyTemplate> getGroupedArkRolePolicyTemplates();
}
