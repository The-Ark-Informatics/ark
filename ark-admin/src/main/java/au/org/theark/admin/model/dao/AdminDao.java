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
import au.org.theark.core.model.study.entity.ArkRolePolicyTemplate;

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

	@SuppressWarnings("unchecked")
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
}
