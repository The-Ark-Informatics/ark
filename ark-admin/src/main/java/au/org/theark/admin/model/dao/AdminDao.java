package au.org.theark.admin.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkPermission;
import au.org.theark.core.model.study.entity.ArkRole;
import au.org.theark.core.model.study.entity.ArkRolePolicyTemplate;

@SuppressWarnings("unchecked")
@Repository("adminDao")
public class AdminDao extends HibernateSessionDao implements IAdminDao
{
	static Logger		log	= LoggerFactory.getLogger(AdminDao.class);

	public void createArkRolePolicyTemplate(ArkRolePolicyTemplate arkRolePolicyTemplate)
	{
		getSession().save(arkRolePolicyTemplate);
	}
	
	public void updateArkRolePolicyTemplate(ArkRolePolicyTemplate arkRolePolicyTemplate)
	{
		getSession().update(arkRolePolicyTemplate);
	}

	public void deleteArkRolePolicyTemplate(ArkRolePolicyTemplate arkRolePolicyTemplate)
	{
		getSession().delete(arkRolePolicyTemplate);
	}
	
	public void createOrUpdateArkRolePolicyTemplate(ArkRolePolicyTemplate arkRolePolicyTemplate)
	{
		getSession().saveOrUpdate(arkRolePolicyTemplate);
	}

	public List<ArkRolePolicyTemplate> getGroupedArkRolePolicyTemplates()
	{
		Criteria criteria = getSession().createCriteria(ArkRolePolicyTemplate.class);
		criteria.createAlias("arkRole", "role");
		criteria.createAlias("arkModule", "module");
		criteria.createAlias("arkFunction", "function");
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("role.name"));
		projectionList.add(Projections.groupProperty("module.name"));
		projectionList.add(Projections.groupProperty("function.name"));
		
		criteria.setProjection(projectionList);
		return criteria.list();	
	}

	public List<ArkRole> getArkRoleList()
	{
		Criteria criteria = getSession().createCriteria(ArkRole.class);
		return criteria.list();
	}
	
	public List<ArkModule> getArkModuleList()
	{
		Criteria criteria = getSession().createCriteria(ArkModule.class);
		return criteria.list();
	}
	
	public List<ArkFunction> getArkFunctionList()
	{
		Criteria criteria = getSession().createCriteria(ArkFunction.class);
		return criteria.list();
	}

	public ArkRolePolicyTemplate getArkRolePolicyTemplate(Long id)
	{
		Criteria criteria = getSession().createCriteria(ArkRolePolicyTemplate.class);
		criteria.add(Restrictions.eq("id", id));
		return (ArkRolePolicyTemplate) criteria.list().get(0);
	}

	public List<ArkRolePolicyTemplate> getArkRolePolicyTemplateList()
	{
		Criteria criteria = getSession().createCriteria(ArkRolePolicyTemplate.class);
		return criteria.list();
	}
	
	public List<ArkRolePolicyTemplate> searchArkRolePolicyTemplate(ArkRolePolicyTemplate arkRolePolicyTemplate)
	{
		Criteria criteria = getSession().createCriteria(ArkRolePolicyTemplate.class);
		
		if(arkRolePolicyTemplate.getId() != null)
			criteria.add(Restrictions.eq("id", arkRolePolicyTemplate.getId()));
		
		if(arkRolePolicyTemplate.getArkRole() != null)
			criteria.add(Restrictions.eq("arkRole", arkRolePolicyTemplate.getArkRole()));
		
		if(arkRolePolicyTemplate.getArkModule() != null)
			criteria.add(Restrictions.eq("arkModule", arkRolePolicyTemplate.getArkModule()));
		
		if(arkRolePolicyTemplate.getArkFunction() != null)
			criteria.add(Restrictions.eq("id", arkRolePolicyTemplate.getArkFunction()));
		
		return criteria.list();
	}

	public ArkPermission getArkPermissionByName(String name)
	{
		Criteria criteria = getSession().createCriteria(ArkPermission.class);
		criteria.add(Restrictions.eq("name", name));
		return (ArkPermission) criteria.list().get(0);
	}
}
