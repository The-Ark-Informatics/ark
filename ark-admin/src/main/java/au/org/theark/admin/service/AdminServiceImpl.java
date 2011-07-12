package au.org.theark.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.admin.model.dao.IAdminDao;
import au.org.theark.admin.model.vo.AdminVO;
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
public class AdminServiceImpl<T> implements IAdminService<T>
{	
	private IAdminDao				adminDao;

	public IAdminDao getAdminDao()
	{
		return adminDao;
	}
	
	@Autowired
	public void setAdminDao(IAdminDao adminDao)
	{
		this.adminDao = adminDao;
	}

	public void createArkRolePolicyTemplate(AdminVO adminVo)
	{
		adminDao.createArkRolePolicyTemplate(adminVo.getArkRolePolicyTemplate());
	}

	public void updateArkRolePolicyTemplate(AdminVO adminVo)
	{
		adminDao.updateArkRolePolicyTemplate(adminVo.getArkRolePolicyTemplate());	
	}
	
	public void deleteArkRolePolicyTemplate(AdminVO adminVo)
	{
		adminDao.deleteArkRolePolicyTemplate(adminVo.getArkRolePolicyTemplate());
	}

	public void createOrUpdateArkRolePolicyTemplate(AdminVO adminVo)
	{
		adminDao.createOrUpdateArkRolePolicyTemplate(adminVo.getArkRolePolicyTemplate());
	}

	public List<ArkRolePolicyTemplate> getGroupedArkRolePolicyTemplates()
	{
		return adminDao.getGroupedArkRolePolicyTemplates();
	}
}
