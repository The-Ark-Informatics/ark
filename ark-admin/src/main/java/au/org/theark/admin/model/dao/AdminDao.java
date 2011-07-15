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
import au.org.theark.core.model.study.entity.ArkFunctionType;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkPermission;
import au.org.theark.core.model.study.entity.ArkRole;
import au.org.theark.core.model.study.entity.ArkRolePolicyTemplate;

@SuppressWarnings("unchecked")
@Repository("adminDao")
public class AdminDao extends HibernateSessionDao implements IAdminDao {
	static Logger	log	= LoggerFactory.getLogger(AdminDao.class);

	public void createArkRolePolicyTemplate(ArkRolePolicyTemplate arkRolePolicyTemplate) {
		getSession().save(arkRolePolicyTemplate);
	}

	public void updateArkRolePolicyTemplate(ArkRolePolicyTemplate arkRolePolicyTemplate) {
		getSession().update(arkRolePolicyTemplate);
	}

	public void deleteArkRolePolicyTemplate(ArkRolePolicyTemplate arkRolePolicyTemplate) {
		getSession().delete(arkRolePolicyTemplate);
	}

	public void createOrUpdateArkRolePolicyTemplate(ArkRolePolicyTemplate arkRolePolicyTemplate) {
		getSession().saveOrUpdate(arkRolePolicyTemplate);
	}

	public List<ArkRolePolicyTemplate> getGroupedArkRolePolicyTemplates() {
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

	public List<ArkRole> getArkRoleList() {
		Criteria criteria = getSession().createCriteria(ArkRole.class);
		return criteria.list();
	}

	public List<ArkModule> getArkModuleList() {
		Criteria criteria = getSession().createCriteria(ArkModule.class);
		return criteria.list();
	}

	public List<ArkFunction> getArkFunctionList() {
		Criteria criteria = getSession().createCriteria(ArkFunction.class);
		return criteria.list();
	}

	public ArkRolePolicyTemplate getArkRolePolicyTemplate(Long id) {
		Criteria criteria = getSession().createCriteria(ArkRolePolicyTemplate.class);
		criteria.add(Restrictions.eq("id", id));
		return (ArkRolePolicyTemplate) criteria.list().get(0);
	}

	public List<ArkRolePolicyTemplate> getArkRolePolicyTemplateList() {
		Criteria criteria = getSession().createCriteria(ArkRolePolicyTemplate.class);
		return criteria.list();
	}

	public List<ArkRolePolicyTemplate> searchArkRolePolicyTemplate(ArkRolePolicyTemplate arkRolePolicyTemplate) {
		Criteria criteria = getSession().createCriteria(ArkRolePolicyTemplate.class);

		if (arkRolePolicyTemplate.getId() != null)
			criteria.add(Restrictions.eq("id", arkRolePolicyTemplate.getId()));

		if (arkRolePolicyTemplate.getArkRole() != null)
			criteria.add(Restrictions.eq("arkRole", arkRolePolicyTemplate.getArkRole()));

		if (arkRolePolicyTemplate.getArkModule() != null)
			criteria.add(Restrictions.eq("arkModule", arkRolePolicyTemplate.getArkModule()));

		if (arkRolePolicyTemplate.getArkFunction() != null)
			criteria.add(Restrictions.eq("id", arkRolePolicyTemplate.getArkFunction()));

		return criteria.list();
	}

	public ArkPermission getArkPermissionByName(String name) {
		Criteria criteria = getSession().createCriteria(ArkPermission.class);
		criteria.add(Restrictions.eq("name", name));
		return (ArkPermission) criteria.list().get(0);
	}

	public ArkFunction getArkFunction(Long id) {
		Criteria criteria = getSession().createCriteria(ArkFunction.class);
		criteria.add(Restrictions.eq("id", id));
		return (ArkFunction) criteria.list().get(0);
	}

	public ArkModule getArkModule(Long id) {
		Criteria criteria = getSession().createCriteria(ArkModule.class);
		criteria.add(Restrictions.eq("id", id));
		return (ArkModule) criteria.list().get(0);
	}

	public void creatOrUpdateArkFunction(ArkFunction arkFunction) {
		getSession().saveOrUpdate(arkFunction);
	}

	public void creatOrUpdateArkModule(ArkModule arkModule) {
		getSession().saveOrUpdate(arkModule);
	}

	public void deleteArkFunction(ArkFunction arkFunction) {
		getSession().delete(arkFunction);
	}

	public void deleteArkModule(ArkModule arkModule) {
		getSession().delete(arkModule);
	}

	public List<ArkFunctionType> getArkFunctionTypeList() {
		Criteria criteria = getSession().createCriteria(ArkFunctionType.class);
		return criteria.list();
	}

	public List<ArkFunction> searchArkFunction(ArkFunction arkFunction) {
		Criteria criteria = getSession().createCriteria(ArkFunction.class);
		if (arkFunction.getId() != null)
			criteria.add(Restrictions.eq("id", arkFunction.getId()));

		if (arkFunction.getName() != null)
			criteria.add(Restrictions.eq("name", arkFunction.getName()));
		return criteria.list();
	}

	public List<ArkModule> searchArkModule(ArkModule arkModule) {
		Criteria criteria = getSession().createCriteria(ArkFunction.class);
		if (arkModule.getId() != null)
			criteria.add(Restrictions.eq("id", arkModule.getId()));

		if (arkModule.getName() != null)
			criteria.add(Restrictions.eq("name", arkModule.getName()));
		return criteria.list();
	}

	public int getArkModuleCount(ArkModule arkModuleCriteria) {
		Criteria criteria = buildArkModuleCriteria(arkModuleCriteria);
		criteria.setProjection(Projections.rowCount());
		Integer totalCount = (Integer) criteria.uniqueResult();
		return totalCount;
	}

	public List<ArkModule> searchPageableArkModules(ArkModule arkModuleCriteria, int first, int count) {
		Criteria criteria = buildArkModuleCriteria(arkModuleCriteria);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		List<ArkModule> list = criteria.list();

		return list;
	}

	protected Criteria buildArkModuleCriteria(ArkModule arkModuleCriteria) {
		Criteria criteria = getSession().createCriteria(ArkModule.class);

		if (arkModuleCriteria.getId() != null)
			criteria.add(Restrictions.eq("id", arkModuleCriteria.getId()));

		if (arkModuleCriteria.getName() != null)
			criteria.add(Restrictions.eq("name", arkModuleCriteria.getName()));

		return criteria;
	}

	public int getArkFunctionCount(ArkFunction arkFunctionCriteria) {
		Criteria criteria = buildArkFunctionCriteria(arkFunctionCriteria);
		criteria.setProjection(Projections.rowCount());
		Integer totalCount = (Integer) criteria.uniqueResult();
		return totalCount;
	}

	public List<ArkFunction> searchPageableArkFunctions(ArkFunction arkFunctionCriteria, int first, int count) {
		Criteria criteria = buildArkFunctionCriteria(arkFunctionCriteria);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		List<ArkFunction> list = criteria.list();

		return list;
	}

	protected Criteria buildArkFunctionCriteria(ArkFunction arkFunctionCriteria) {
		Criteria criteria = getSession().createCriteria(ArkFunction.class);

		if (arkFunctionCriteria.getId() != null)
			criteria.add(Restrictions.eq("id", arkFunctionCriteria.getId()));

		if (arkFunctionCriteria.getName() != null)
			criteria.add(Restrictions.eq("name", arkFunctionCriteria.getName()));

		if (arkFunctionCriteria.getArkFunctionType() != null)
			criteria.add(Restrictions.eq("arkFunctionType", arkFunctionCriteria.getArkFunctionType()));

		return criteria;
	}
}