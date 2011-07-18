package au.org.theark.admin.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.admin.model.vo.AdminVO;
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

		if (arkRolePolicyTemplate.getId() != null) {
			criteria.add(Restrictions.eq("id", arkRolePolicyTemplate.getId()));
		}

		if (arkRolePolicyTemplate.getArkRole() != null) {
			criteria.add(Restrictions.eq("arkRole", arkRolePolicyTemplate.getArkRole()));
		}

		if (arkRolePolicyTemplate.getArkModule() != null) {
			criteria.add(Restrictions.eq("arkModule", arkRolePolicyTemplate.getArkModule()));
		}
		
		if (arkRolePolicyTemplate.getArkFunction() != null) {
			criteria.add(Restrictions.eq("arkFunction", arkRolePolicyTemplate.getArkFunction()));
		}

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
		if (arkFunction.getId() != null) {
			criteria.add(Restrictions.eq("id", arkFunction.getId()));
		}

		if (arkFunction.getName() != null) {
			criteria.add(Restrictions.ilike("name", arkFunction.getName(), MatchMode.ANYWHERE));
		}
		return criteria.list();
	}

	public List<ArkModule> searchArkModule(ArkModule arkModule) {
		Criteria criteria = getSession().createCriteria(ArkFunction.class);
		if (arkModule.getId() != null) {
			criteria.add(Restrictions.eq("id", arkModule.getId()));
		}

		if (arkModule.getName() != null) {
			criteria.add(Restrictions.ilike("name", arkModule.getName(), MatchMode.ANYWHERE));
		}
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
			criteria.add(Restrictions.ilike("name", arkModuleCriteria.getName(), MatchMode.ANYWHERE));

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
			criteria.add(Restrictions.ilike("name", arkFunctionCriteria.getName(), MatchMode.ANYWHERE));

		if (arkFunctionCriteria.getArkFunctionType() != null)
			criteria.add(Restrictions.eq("arkFunctionType", arkFunctionCriteria.getArkFunctionType()));

		return criteria;
	}

	public int getArkRolePolicyTemplateCount(ArkRolePolicyTemplate arkRolePolicyTemplateCriteria) {
		Criteria criteria = buildArkRolePolicyTemplateCriteria(arkRolePolicyTemplateCriteria);
		criteria.setProjection(Projections.rowCount());
		Integer totalCount = (Integer) criteria.uniqueResult();
		return totalCount;
	}

	public List<ArkRolePolicyTemplate> searchPageableArkRolePolicyTemplates(ArkRolePolicyTemplate arkRolePolicyTemplateCriteria, int first, int count) {
		Criteria criteria = buildArkRolePolicyTemplateCriteria(arkRolePolicyTemplateCriteria);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		List<ArkRolePolicyTemplate> list = criteria.list();

		return list;
	}
	
	protected Criteria buildArkRolePolicyTemplateCriteria(ArkRolePolicyTemplate arkRolePolicyTemplateCriteria) {
		Criteria criteria = getSession().createCriteria(ArkRolePolicyTemplate.class, "arpt");

		if (arkRolePolicyTemplateCriteria.getId() != null) {
			criteria.add(Restrictions.eq("arpt.id", arkRolePolicyTemplateCriteria.getId()));
		}

		if (arkRolePolicyTemplateCriteria.getArkRole() != null) {
			criteria.add(Restrictions.eq("arpt.arkRole", arkRolePolicyTemplateCriteria.getArkRole()));
		}

		if (arkRolePolicyTemplateCriteria.getArkModule() != null) {
			criteria.add(Restrictions.eq("arpt.arkModule", arkRolePolicyTemplateCriteria.getArkModule()));
		}
		
		if (arkRolePolicyTemplateCriteria.getArkFunction() != null) {
			criteria.add(Restrictions.eq("arpt.arkFunction", arkRolePolicyTemplateCriteria.getArkFunction()));
		}
		
		//ResultTransformer resultTransformer = Transformers.aliasToBean(ArkRolePolicyTemplate.class);
		//criteria.setResultTransformer(resultTransformer);

		return criteria;
	}
	
	public List<ArkRolePolicyTemplate> getGroupedArkRolePolicyTemplates() {
		Criteria criteria = getSession().createCriteria(ArkRolePolicyTemplate.class);
		criteria.createAlias("arkRole", "role");
		criteria.createAlias("arkModule", "module");
		criteria.createAlias("arkFunction", "function");

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("role.id"));
		projectionList.add(Projections.groupProperty("module.id"));
		projectionList.add(Projections.groupProperty("function.id"));

		criteria.setProjection(projectionList);
		return criteria.list();
	}
	
	public int getArkRolePolicyCount(ArkRolePolicyTemplate arkRolePolicyTemplateCriteria) {
		Criteria criteria = buildArkRolePolicyTemplateCriteria(arkRolePolicyTemplateCriteria);
		criteria.setProjection(Projections.rowCount());
		Integer totalCount = (Integer) criteria.uniqueResult();
		return totalCount;
	}

	public List<AdminVO> searchPageableArkRolePolicies(ArkRolePolicyTemplate arkRolePolicyTemplateCriteria, int first, int count) {
/*		Criteria criteria = getSession().createCriteria(ArkRolePolicy.class, "arpt");

		if (arkRolePolicyTemplateCriteria.getArkRole() != null) {
			criteria.add(Restrictions.eq("arkRole", arkRolePolicyTemplateCriteria.getArkRole()));
		}

		if (arkRolePolicyTemplateCriteria.getArkModule() != null) {
			criteria.add(Restrictions.eq("arkModule", arkRolePolicyTemplateCriteria.getArkModule()));
		}
		
		if (arkRolePolicyTemplateCriteria.getArkFunction() != null) {
			criteria.add(Restrictions.eq("arkFunction", arkRolePolicyTemplateCriteria.getArkFunction()));
		}
*/		
		Criteria criteria = buildArkRolePolicyTemplateCriteria(arkRolePolicyTemplateCriteria);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		
		// Permissions
		DetachedCriteria createPermissionCriteria = DetachedCriteria.forClass(ArkPermission.class, "ap");
		createPermissionCriteria.add(Restrictions.eq("ap.id", new Long(1)));
		DetachedCriteria readPermissionCriteria = DetachedCriteria.forClass(ArkPermission.class, "ap");
		readPermissionCriteria.add(Restrictions.eq("ap.id", new Long(2)));
		DetachedCriteria updatePermissionCriteria = DetachedCriteria.forClass(ArkPermission.class, "ap");
		updatePermissionCriteria.add(Restrictions.eq("ap.id", new Long(3)));
		DetachedCriteria deletePermissionCriteria = DetachedCriteria.forClass(ArkPermission.class, "ap");
		deletePermissionCriteria.add(Restrictions.eq("ap.id", new Long(4)));
		
		createPermissionCriteria.add(Property.forName("arpt.arkPermission").eqProperty("ap"));
		readPermissionCriteria.add(Property.forName("arpt.arkPermission").eqProperty("ap"));
		updatePermissionCriteria.add(Property.forName("arpt.arkPermission").eqProperty("ap"));
		deletePermissionCriteria.add(Property.forName("arpt.arkPermission").eqProperty("ap"));
		
		Projection projection = Projections.max("ap.name");
		createPermissionCriteria.setProjection(projection);
		readPermissionCriteria.setProjection(projection);
		updatePermissionCriteria.setProjection(projection);
		deletePermissionCriteria.setProjection(projection);

		//criteria.add(Property.forName("createPermission").eq(createPermissionCriteria));
		//criteria.add(Property.forName("readPermission").eq(readPermissionCriteria));
		//criteria.add(Property.forName("updatePermission").eq(updatePermissionCriteria));
		//criteria.add(Property.forName("deletePermission").eq(deletePermissionCriteria));
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("arpt.arkRole"));
		projectionList.add(Projections.groupProperty("arpt.arkFunction"));
		//projectionList.add(Projections.sqlProjection("sum(arpt.arkPermission.id)", new String[] {"createPermission"}, new Type[] { Hibernate.LONG } ));
		
		
		projectionList.add(Projections.max("arpt.arkPermission.id"), "createPermission");
		projectionList.add(Projections.max("arpt.arkPermission.id"), "readPermission");
		projectionList.add(Projections.max("arpt.arkPermission.id"), "updatePermission");
		projectionList.add(Projections.max("arpt.arkPermission.id"), "deletePermission");
		
		criteria.setProjection(projectionList);

		List criterialist = criteria.list();
		return criterialist;
	}
	
	protected Criteria buildArkRolePolicyPolicyCriteria(ArkRolePolicyTemplate arkRolePolicyTemplateCriteria) {
		Criteria criteria = getSession().createCriteria(ArkRolePolicyTemplate.class);

		if (arkRolePolicyTemplateCriteria.getId() != null) {
			criteria.add(Restrictions.eq("id", arkRolePolicyTemplateCriteria.getId()));
		}

		if (arkRolePolicyTemplateCriteria.getArkRole() != null) {
			criteria.add(Restrictions.eq("arkRole", arkRolePolicyTemplateCriteria.getArkRole()));
		}

		if (arkRolePolicyTemplateCriteria.getArkModule() != null) {
			criteria.add(Restrictions.eq("arkModule", arkRolePolicyTemplateCriteria.getArkModule()));
		}
		
		if (arkRolePolicyTemplateCriteria.getArkFunction() != null) {
			criteria.add(Restrictions.eq("arkFunction", arkRolePolicyTemplateCriteria.getArkFunction()));
		}

		return criteria;
	}
}