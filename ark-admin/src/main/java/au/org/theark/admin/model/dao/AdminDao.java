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
package au.org.theark.admin.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.admin.model.vo.ArkRoleModuleFunctionVO;
import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkFunctionType;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkModuleRole;
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

	public int getArkRoleModuleFunctionVOCount(ArkRoleModuleFunctionVO arkRoleModuleFunctionVoCriteria) {
		Criteria criteria = buildarkRoleModuleFunctionVoCriteria(arkRoleModuleFunctionVoCriteria);
		criteria.setProjection(Projections.rowCount());
		Integer totalCount = (Integer) criteria.uniqueResult();
		return totalCount;
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
		return criteria;
	}
	
	protected Criteria buildarkRoleModuleFunctionVoCriteria(ArkRoleModuleFunctionVO arkRoleModuleFunctionVoCriteria) {
		Criteria criteria = getSession().createCriteria(ArkRolePolicyTemplate.class, "arpt");

		if (arkRoleModuleFunctionVoCriteria.getArkRole() != null) {
			criteria.add(Restrictions.eq("arpt.arkRole", arkRoleModuleFunctionVoCriteria.getArkRole()));
		}

		if (arkRoleModuleFunctionVoCriteria.getArkModule() != null) {
			criteria.add(Restrictions.eq("arpt.arkModule", arkRoleModuleFunctionVoCriteria.getArkModule()));
		}
		
		if (arkRoleModuleFunctionVoCriteria.getArkFunction() != null) {
			criteria.add(Restrictions.eq("arpt.arkFunction", arkRoleModuleFunctionVoCriteria.getArkFunction()));
		}

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("arpt.arkRole"), "arkRole");
		projectionList.add(Projections.groupProperty("arpt.arkModule"), "arkModule");
		projectionList.add(Projections.groupProperty("arpt.arkFunction"), "arkFunction");

		criteria.setProjection(projectionList);
		
		ResultTransformer resultTransformer = Transformers.aliasToBean(ArkRoleModuleFunctionVO.class);
		criteria.setResultTransformer(resultTransformer);

		return criteria;
	}

	public List<ArkRoleModuleFunctionVO> searchPageableArkRoleModuleFunctionVO(ArkRoleModuleFunctionVO arkRoleModuleFunctionVo, int first, int count) {
		Criteria criteria = buildarkRoleModuleFunctionVoCriteria(arkRoleModuleFunctionVo);
		criteria.setFirstResult(first);
		criteria.setMaxResults(count);
		
		// Restrict to NOT show Super Administrator (safety)
		// NOTE arpt alias set in buildarkRoleModuleFunctionVoCriteria 
		criteria.add((Restrictions.ne("arpt.arkRole", getArkRoleByName(au.org.theark.core.security.RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR))));
		
		List<ArkRoleModuleFunctionVO> list = criteria.list();
		return list;
	}

	public ArkRole getArkRoleByName(String name) {
		Criteria criteria = getSession().createCriteria(ArkRole.class);
		criteria.add(Restrictions.eq("name", name));
		return (ArkRole) criteria.list().get(0);
	}

	public List<ArkRoleModuleFunctionVO> getArkRoleModuleFunctionVoList(ArkRole arkRole) {
		Criteria criteria = getSession().createCriteria(ArkRolePolicyTemplate.class, "arpt");
		criteria.add(Restrictions.eq("arpt.arkRole", arkRole));

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("arpt.arkRole"), "arkRole");
		projectionList.add(Projections.groupProperty("arpt.arkModule"), "arkModule");
		projectionList.add(Projections.groupProperty("arpt.arkFunction"), "arkFunction");

		criteria.setProjection(projectionList);
		
		ResultTransformer resultTransformer = Transformers.aliasToBean(ArkRoleModuleFunctionVO.class);
		criteria.setResultTransformer(resultTransformer);

		return criteria.list();
	}

	public List<ArkRolePolicyTemplate> getArkRolePolicyTemplateList(ArkRolePolicyTemplate arkRolePolicyTemplateCriteria) {
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
		return criteria.list();
	}

	public List<ArkModule> getArkModuleList(ArkRole arkRole) {
		Criteria criteria = getSession().createCriteria(ArkModuleRole.class);
		
		if(arkRole != null) {
			criteria.add(Restrictions.eq("arkRole", arkRole));
		}
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("arkModule"), "arkModule");
		criteria.setProjection(projectionList);
		
		return criteria.list();
	}
}
