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
package au.org.theark.core.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.StatusNotAvailableException;
import au.org.theark.core.model.geno.entity.LinkSubjectStudyPipeline;
import au.org.theark.core.model.config.entity.UserConfig;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkModuleRole;
import au.org.theark.core.model.study.entity.ArkPermission;
import au.org.theark.core.model.study.entity.ArkRole;
import au.org.theark.core.model.study.entity.ArkRolePolicyTemplate;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.ArkUserRole;
import au.org.theark.core.model.study.entity.LinkStudyArkModule;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.vo.ArkModuleVO;
import au.org.theark.core.vo.ArkUserVO;

/**
 * @author nivedan
 * 
 * @param <T>
 */
@Repository("arkAuthorisationDao")
public class ArkAuthorisationDao<T> extends HibernateSessionDao implements IArkAuthorisation {

	static final Logger	log	= LoggerFactory.getLogger(ArkAuthorisationDao.class);

	/**
	 * Looks up a ArkUser based on a String that represents the user name in LDAP. If the user name provided is null or is invalid/does not exist in
	 * the database, the method will return NULL for a ArkUser instance.
	 * 
	 * @param ldapUserName
	 * @return ArkUser
	 */
	public ArkUser getArkUser(String ldapUserName) throws EntityNotFoundException {
		StatelessSession session = getStatelessSession();
		Criteria criteria = session.createCriteria(ArkUser.class);
		criteria.add(Restrictions.eq("ldapUserName", ldapUserName));
		ArkUser arkUser = (ArkUser) criteria.uniqueResult();
		// Close the session
		session.close();
		if (arkUser != null) {
			return arkUser;
		}
		else {
			throw new EntityNotFoundException("The given Ldap User does not exist in the database system");
		}
	}

	/**
	 * Overloaded method to lookup an ArkUser by username and study
	 * 
	 * @param ldapUserName
	 * @param study
	 * @return
	 * @throws EntityNotFoundException
	 */
	public ArkUser getArkUser(String ldapUserName, Study study) throws EntityNotFoundException {
		StatelessSession session = getStatelessSession();
		Criteria criteria = session.createCriteria(ArkUser.class);
		criteria.add(Restrictions.eq("ldapUserName", ldapUserName));
		criteria.add(Restrictions.eq("study", study));
		ArkUser arkUser = (ArkUser) criteria.uniqueResult();
		// Close the session
		session.close();
		if (arkUser != null) {
			return arkUser;
		}
		else {
			throw new EntityNotFoundException("The given Ldap User does not exist in the database system");
		}
	}

	/**
	 * Returns a list of ArkRole objects from the backend. This does not use the Stateless session. Can be used by front-end client's.
	 * 
	 * @return List<ArkRole>
	 */
	@SuppressWarnings("unchecked")
	public List<ArkRole> getArkRoles() {

		Criteria criteria = getSession().createCriteria(ArkRole.class);
		List<ArkRole> arkRoleList = criteria.list();
		return arkRoleList;
	}

	/**
	 * <p>
	 * Given a String Role name like Super Administrator or Administrator the method will return the actual instance of ArkRole object.
	 * </p>
	 * 
	 * @param roleName
	 * @return ArkRole
	 */
	public ArkRole getArkRoleByName(String roleName) {
		Criteria criteria = getSession().createCriteria(ArkRole.class);
		criteria.add(Restrictions.eq("name", roleName));
		ArkRole arkRole = (ArkRole) criteria.uniqueResult();
		return arkRole;
	}

	/**
	 * <p>
	 * The method takes in a LdapUserName and calls the isUserAdminHelper to do the grunt of the work. The query is again Ark_User_Role
	 * table/ArkUserRole entity. The getArkRoleByName is invoked to get a object that represents Administrator. The method uses this to then check if
	 * the given user is/has a role of the type in ArkUserRole table/instance. If yes a boolean true is returned and otherwise false is returned.
	 * </p>
	 * <br>
	 * 
	 * @return boolean
	 * @throws EntityNotFoundException
	 */
	public boolean isAdministator(String ldapUserName) throws EntityNotFoundException {

		return isUserAdminHelper(ldapUserName, RoleConstants.ARK_ROLE_ADMINISTATOR);

	}

	public boolean isUserAdminHelper(String ldapUserName, String roleName) throws EntityNotFoundException {
		boolean isAdminType = false;
		StatelessSession session = getStatelessSession();
		// Check or get user ark_user object based on ldapUserName
		ArkUser arkUser = getArkUser(ldapUserName);
		Criteria criteria = session.createCriteria(ArkUserRole.class);
		ArkRole arkRole = getArkRoleByName(roleName);
		criteria.add(Restrictions.eq("arkRole", arkRole));
		criteria.add(Restrictions.eq("arkUser", arkUser));
		criteria.setMaxResults(1);
		ArkUserRole arkUserRole = (ArkUserRole) criteria.uniqueResult();
		if (arkUserRole != null) {
			isAdminType = true;
		}
		session.close();
		return isAdminType;
	}

	@SuppressWarnings("unchecked")
	public List<ArkUserRole> getArkSuperAdministratorList() throws EntityNotFoundException {
		Criteria criteria = getSession().createCriteria(ArkUserRole.class);
		ArkRole arkRole = getArkRoleByName(RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR);
		criteria.add(Restrictions.eq("arkRole", arkRole));
		return criteria.list();
	}

	private boolean isUserAdminHelper(String ldapUserName, String roleName, ArkFunction arkFunction, ArkModule arkModule) throws EntityNotFoundException {
		boolean isAdminType = false;
		StatelessSession session = getStatelessSession();
		// Check or get user ark_user object based on ldapUserName
		ArkUser arkUser = getArkUser(ldapUserName);
		Criteria criteria = session.createCriteria(ArkUserRole.class);
		ArkRole arkRole = getArkRoleByName(roleName);
		criteria.add(Restrictions.eq("arkRole", arkRole));
		criteria.add(Restrictions.eq("arkUser", arkUser));
		criteria.add(Restrictions.eq("arkModule", arkModule));

		criteria.setMaxResults(1);
		ArkUserRole arkUserRole = (ArkUserRole) criteria.uniqueResult();
		if (arkUserRole != null) {
			isAdminType = true;
		}
		session.close();
		return isAdminType;
	}

	/**
	 * <p>
	 * The method takes in a LdapUserName and calls the isUserAdminHelper whihc queries the Database using a Stateless session. The query is again
	 * Ark_User_Role table/ArkUserRole entity. The getArkRoleByName is invoked to get a object that represents Administrator. The method uses this to
	 * then check if the given user is/has a role of the type in ArkUserRole table/instance. If yes a boolean true is returned and otherwise false is
	 * returned.
	 * </p>
	 * <br>
	 * 
	 * @return boolean
	 * @see au.org.theark.core.dao.IStudyDao#isAdministator(java.lang.String)
	 */
	public boolean isSuperAdministrator(String ldapUserName) throws EntityNotFoundException {
		return isUserAdminHelper(ldapUserName, RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR);
	}

	public boolean isSuperAdministator(String ldapUserName, ArkFunction arkFunction, ArkModule arkModule) throws EntityNotFoundException {
		return isUserAdminHelper(ldapUserName, RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR, arkFunction, arkModule);
	}

	/**
	 * Use this method when we want to load the Collection of Administrator roles as a Collectio<String>. The method looks up Ark Super Administrator
	 * and Administrator roles given a LdapUserName. It populates it into a Collection<String> that represent a unique set of administration roles for
	 * this user. It does not take into account the Module or Study. This is usually when the user has logged in first and we want to know if the user
	 * has a role of type Administator so he can have access to Create function.
	 * 
	 * @param ldapUserName
	 * @return Collection<String>
	 * @throws EntityNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public Collection<String> getUserAdminRoles(String ldapUserName) throws EntityNotFoundException {

		ArkUser arkUser = getArkUser(ldapUserName);

		StatelessSession session = getStatelessSession();
		Criteria criteria = session.createCriteria(ArkUserRole.class);// getSession().createCriteria(ArkUserRole.class);
		ArkRole arkRoleSuperAdmin = getArkRoleByName(RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR);
		ArkRole arkRoleAdmin = getArkRoleByName(RoleConstants.ARK_ROLE_ADMINISTATOR);
		criteria.add(Restrictions.or(Restrictions.eq("arkRole", arkRoleSuperAdmin), Restrictions.eq("arkRole", arkRoleAdmin)));
		criteria.add(Restrictions.eq("arkUser", arkUser));
		List<ArkUserRole> arkUserRoleList = (List<ArkUserRole>) criteria.list();
		Set<String> roles = new HashSet<String>(0);
		for (ArkUserRole arkUserRole : arkUserRoleList) {
			String roleName = arkUserRole.getArkRole().getName();
			roles.add(roleName);
		}

		Collection<String> userRoles = new ArrayList<String>();
		for (String roleName : roles) {
			userRoles.add(roleName);
		}
		session.close();
		return userRoles;
	}

	@SuppressWarnings("unchecked")
	public List<ArkUserRole> getArkUserAdminRoles(String ldapUserName) throws EntityNotFoundException {
		ArkUser arkUser = getArkUser(ldapUserName);
		StatelessSession session = getStatelessSession();
		Criteria criteria = session.createCriteria(ArkUserRole.class);// getSession().createCriteria(ArkUserRole.class);
		ArkRole arkRoleStudyAdmin = getArkRoleByName(RoleConstants.ARK_ROLE_STUDY_ADMINISTATOR);
		criteria.add(Restrictions.eq("arkRole", arkRoleStudyAdmin));
		criteria.add(Restrictions.eq("arkUser", arkUser));
		List<ArkUserRole> arkUserRoleList = (List<ArkUserRole>) criteria.list();
		session.close();
		return arkUserRoleList;
	}

	public String getUserRoleForStudy(String ldapUserName, Study study) throws EntityNotFoundException {
		String roleName = "";
		ArkUser arkUser = getArkUser(ldapUserName);
		StatelessSession session = getStatelessSession();

		Criteria criteria = session.createCriteria(ArkUserRole.class);// getSession().createCriteria(ArkUserRole.class);
		criteria.createAlias("arkUser", "auserObject");
		criteria.add(Restrictions.eq("arkUser", arkUser));
		criteria.add(Restrictions.eq("auserObject.study", study));
		criteria.setMaxResults(1);
		ArkUserRole arkUserRole = (ArkUserRole) criteria.uniqueResult();
		if (arkUserRole != null) {
			roleName = arkUserRole.getArkRole().getName();
		}
		session.close();
		return roleName;
	}

	/**
	 * Retrieve a Logged in user's role by providing the Ldap User Name, Usecase id, module id & or study id. We need the Ldap User Name & ArkUseCase
	 * Id as a mandatory one.
	 * 
	 * @throws EntityNotFoundException
	 */

	@SuppressWarnings("unchecked")
	public String getUserRole(String ldapUserName, ArkFunction arkFunction, ArkModule arkModule, Study study) throws EntityNotFoundException {
		String roleName = "";

		ArkUser arkUser = getArkUser(ldapUserName);
		Criteria criteria = getSession().createCriteria(ArkUserRole.class);
		criteria.createAlias("arkUser", "auserObject");

		criteria.add(Restrictions.eq("arkUser", arkUser));
		// Even if there is a study in session the criteria must be applied only if the logged in user has a study registered for him. Ie if he is not a
		// Super Admin
		if (!isSuperAdministrator(ldapUserName) && study != null) {

			criteria.add(Restrictions.eq("study", study));
			if (arkModule != null) {
				criteria.add(Restrictions.eq("arkModule", arkModule));
			}
			// criteria.setMaxResults(1);
			List<ArkUserRole> list = (List<ArkUserRole>) criteria.list();
			if (list.size() > 0) {
				ArkUserRole arkUserRole = (ArkUserRole) criteria.list().get(0);
				// ArkUserRole arkUserRole = (ArkUserRole)criteria.list().get(0);
				if (arkUserRole != null) {
					roleName = arkUserRole.getArkRole().getName();
				}
			}

		}
		else {
			if (arkModule != null) {
				criteria.add(Restrictions.eq("arkModule", arkModule));
			}

			criteria.setMaxResults(1);
			ArkUserRole arkUserRole = (ArkUserRole) criteria.uniqueResult();
			if (arkUserRole != null) {
				roleName = arkUserRole.getArkRole().getName();
			}
		}

		return roleName;
	}

	public ArkFunction getArkFunctionByName(String functionName) {
		Criteria criteria = getSession().createCriteria(ArkFunction.class);
		criteria.add(Restrictions.eq("name", functionName));
		criteria.setMaxResults(1);
		ArkFunction arkFunction = (ArkFunction) criteria.uniqueResult();
		return arkFunction;
	}

	public ArkFunction getArkFunctionById(Long functionId) {
		return (ArkFunction)getSession().get(ArkFunction.class, functionId);
		
		/*
		Criteria criteria = getSession().createCriteria(ArkFunction.class);
		criteria.add(Restrictions.eq("id", functionId));
		criteria.setMaxResults(1);
		log.info("functionid=" + functionId);
		log.info("instanceoflong?  " + (functionId instanceof Long));
		log.info("criteria.list().size()=" + criteria.list().size());
		
		ArkFunction arkFunction = (ArkFunction) criteria.uniqueResult();
		return arkFunction;*/
	}

	public ArkModule getArkModuleByName(String moduleName) {
		Criteria criteria = getSession().createCriteria(ArkModule.class);
		criteria.add(Restrictions.eq("name", moduleName));
		criteria.setMaxResults(1);
		ArkModule arkModule = (ArkModule) criteria.uniqueResult();
		return arkModule;
	}

	public ArkModule getArkModuleById(Long moduleId) {
		Criteria criteria = getSession().createCriteria(ArkModule.class);
		criteria.add(Restrictions.eq("id", moduleId));
		criteria.setMaxResults(1);
		ArkModule arkModule = (ArkModule) criteria.uniqueResult();
		return arkModule;
	}

	@SuppressWarnings("unchecked")
	public Collection<String> getArkRolePermission(ArkFunction arkFunction, String userRole, ArkModule arkModule) throws EntityNotFoundException {

		Collection<String> stringPermissions = new ArrayList<String>();
		ArkRole arkRole = getArkRoleByName(userRole);
		Criteria criteria = getSession().createCriteria(ArkRolePolicyTemplate.class);

		if (arkModule != null) {
			criteria.add(Restrictions.eq("arkModule", arkModule));
		}

		if (arkFunction != null) {
			criteria.add(Restrictions.eq("arkFunction", arkFunction));
		}

		if (arkRole != null) {
			criteria.add(Restrictions.eq("arkRole", arkRole));
			
			criteria.createAlias("arkPermission", "permission");
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.groupProperty("permission.name"));
			criteria.setProjection(projectionList);
			
			stringPermissions = criteria.list();
		}
		return stringPermissions;
	}

	/**
	 * Returns a list of All Permissions in Ark System
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<String> getArkPermission() {
		Collection<String> arkStringPermissions = new ArrayList<String>();
		Criteria criteria = getSession().createCriteria(ArkPermission.class);
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("name"));
		criteria.setProjection(projectionList);
		arkStringPermissions = criteria.list();
		return arkStringPermissions;
	}

	@SuppressWarnings("unchecked")
	public Collection<Class<T>> getEntityList(Class aClass) {
		Collection<Class<T>> arkModuleList = new ArrayList<Class<T>>();
		Criteria criteria = getSession().createCriteria(aClass);
		arkModuleList = criteria.list();
		return (Collection<Class<T>>) arkModuleList;
	}

	/**
	 * This overloaded interface can be used when we only want all ther Permissions for a Given Role. It is applicable for Super Administator role
	 * where we don't need to specify the ArkModule or ArkRole. If this method is invoked for any other role it will return all the permissions for
	 * each role, the permissions will be duplicated. So avoid invoking this method for Non SuperAdministator type roles.
	 * 
	 * @param userRole
	 * @return
	 * @throws EntityNotFoundException
	 */
	public Collection<String> getArkRolePermission(String userRole) throws EntityNotFoundException {
		// Delegate the call to the other getArkRolePermission by passing null for arkFunction and arkModule
		return getArkRolePermission(null, userRole, null);
	}

	@SuppressWarnings("unchecked")
	public Collection<ArkModuleRole> getArkModuleAndLinkedRoles() {
		Collection<ArkModuleRole> arkModuleList = new ArrayList<ArkModuleRole>();
		Criteria criteria = getSession().createCriteria(ArkModuleRole.class);
		criteria.createAlias("arkModule", "moduleName");
		criteria.addOrder(Order.asc("moduleName.name"));
		arkModuleList = criteria.list();
	
		//TODO:  What  are we iterating for if we are not doing anything?  delete?
		/*for (Iterator iterator = arkModuleList.iterator(); iterator.hasNext();) {
			ArkModuleRole arkModuleRole = (ArkModuleRole) iterator.next();

		}*/
		return arkModuleList;
	}

	/**
	 * Create a new Ark User in the system and associates the arkuser with the study and links the user to one or more Modules and Roles that was
	 * configured for the Study.
	 */
	public void createArkUser(ArkUserVO arkUserVO) {
		Session session = getSession();
		session.save(arkUserVO.getArkUserEntity());
		List<ArkUserRole> arkUserRoleList = arkUserVO.getArkUserRoleList();
		for (ArkUserRole arkUserRole : arkUserRoleList) {
			if (arkUserRole.getArkRole() != null) {
				arkUserRole.setArkUser(arkUserVO.getArkUserEntity());
				session.save(arkUserRole);
			}
		}
		
		for (UserConfig config : arkUserVO.getArkUserConfigs()) {
			session.saveOrUpdate(config);
		}
	}

	/**
	 * Get a list of Modules that are linked to the study and then get the roles linked to each module A VO List of ArkModuleVO will contain the
	 * ArkModule and a list of ArkRoles. Note:This implementation will exclude Reporting Module from the list specifically.
	 * 
	 * @param study
	 * @return Collection<ArkModuleVO> Minus the Reporting Module
	 */
	@SuppressWarnings("unchecked")
	public Collection<ArkModuleVO> getArkModulesAndRolesLinkedToStudy(Study study) {

		ArkModule arkModuleToExclude = getArkModuleByName(Constants.ARK_MODULE_REPORTING);

		Collection<LinkStudyArkModule> arkStudyLinkedModuleList = new ArrayList<LinkStudyArkModule>();
		Collection<ArkModuleVO> arkModuleVOList = new ArrayList<ArkModuleVO>();

		Criteria criteria = getSession().createCriteria(LinkStudyArkModule.class);

		criteria.add(Restrictions.eq("study", study));
		//criteria.add(Restrictions.ne("arkModule", arkModuleToExclude));
		criteria.createAlias("arkModule", "module");
		criteria.addOrder(Order.asc("module.id"));

		arkStudyLinkedModuleList = criteria.list();

		// For each one in the List get the associated Roles i.e for each module get the Roles
		for (LinkStudyArkModule linkStudyArkModule : arkStudyLinkedModuleList) {
			// Here is a Module linked to a study get the Roles linked to this module
			ArkModuleVO arkModuleVO = new ArkModuleVO();
			arkModuleVO.setArkModule(linkStudyArkModule.getArkModule());
			arkModuleVO.setArkModuleRoles(getArkRoleLinkedToModule(linkStudyArkModule.getArkModule()));
			arkModuleVOList.add(arkModuleVO);
		}

		// getArkUserRoleList(study,null);
		return arkModuleVOList;
	}

	@SuppressWarnings("unchecked")
	public Collection<ArkModule> getArkModulesLinkedWithStudy(Study study) {
		Criteria criteria = getSession().createCriteria(LinkStudyArkModule.class);
		criteria.add(Restrictions.eq("study", study));
		criteria.createAlias("arkModule", "module");
		criteria.addOrder(Order.asc("module.id"));
		Collection<LinkStudyArkModule> arkStudyLinkedModuleList = criteria.list();
		Collection<ArkModule> arkModuleList = new ArrayList<ArkModule>();
		for (LinkStudyArkModule linkStudyArkModule : arkStudyLinkedModuleList) {
			arkModuleList.add(linkStudyArkModule.getArkModule());
		}
		return arkModuleList;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<ArkRole> getArkRoleLinkedToModule(ArkModule arkModule) {
		Collection<ArkModuleRole> arkModuleList = new ArrayList<ArkModuleRole>();
		Criteria criteria = getSession().createCriteria(ArkModuleRole.class);
		criteria.add(Restrictions.eq("arkModule", arkModule));
		criteria.createAlias("arkRole", "role", JoinType.LEFT_OUTER_JOIN);
		criteria.addOrder(Order.asc("role.name"));
		arkModuleList = criteria.list();
		ArrayList<ArkRole> moduleArkRolesList = new ArrayList<ArkRole>();
		for (ArkModuleRole arkModuleRole : arkModuleList) {
			
			ArkRole arkRole = arkModuleRole.getArkRole();
			moduleArkRolesList.add(arkRole);
		}
		return moduleArkRolesList;
	}

	@SuppressWarnings("unchecked")
	public List<ArkUserRole> getArkUserRoleList(Study study, ArkUser arkUser) {

//		Criteria criteria = getSession().createCriteria(LinkStudyArkModule.class, "linkStudyArkModule");
//		criteria.add(Restrictions.eq("study", study));
//		criteria.createAlias("arkUserRoleList", "userRole", Criteria.LEFT_JOIN);
//		criteria.add(Restrictions.or(Restrictions.eq("userRole.arkUser", arkUser), Restrictions.isNull("userRole.arkUser")));
//		criteria.add(Restrictions.eq("userRole.study", study));
//
//		ProjectionList projection = Projections.projectionList();
//		projection.add(Projections.property("userRole.id"), "id");
//		projection.add(Projections.property("userRole.arkUser"), "arkUser");
//		projection.add(Projections.property("userRole.arkRole"), "arkRole");
//		projection.add(Projections.property("linkStudyArkModule.arkModule"), "arkModule");
//		projection.add(Projections.property("linkStudyArkModule.study"), "study");
//
//		criteria.setProjection(projection);
//
//		criteria.setResultTransformer(Transformers.aliasToBean(ArkUserRole.class));
//		List<ArkUserRole> listOfResults = criteria.list();
		
		Criteria criteria = getSession().createCriteria(ArkUserRole.class);
		criteria.add(Restrictions.eq("study", study));
		criteria.add(Restrictions.eq("arkUser", arkUser));
		List<ArkUserRole> listOfResults = criteria.list();
		
		return listOfResults;

	}

	/**
	 * Will update the ArkUser or adds the user into ArkUser table. It determines the List of ArkUserRoles for insertion and removal and processes
	 * these ArkUserRole lists.
	 * 
	 * @param arkUserVO
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public void updateArkUser(ArkUserVO arkUserVO) throws EntityNotFoundException, ArkSystemException {
		try {
			if (arkUserVO.getArkUserEntity() != null && arkUserVO.getArkUserEntity().getId() != null) {	
				// Never update/delete Super User records
				Session session = getSession();
				if(!isUserAdminHelper(arkUserVO.getArkUserEntity().getLdapUserName(), au.org.theark.core.security.RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR)) {
					// User is present in the ArkUserTable can go for update of the entity and related objects (ArkUserRoles)
					session.update(arkUserVO.getArkUserEntity());
	
					// Insert new ArkUserRole
					for (ArkUserRole arkUserRoleToAdd : getArkUserRolesToAdd(arkUserVO)) {
						if (arkUserRoleToAdd.getArkRole() != null) {
							arkUserRoleToAdd.setArkUser(arkUserVO.getArkUserEntity());
							session.save(arkUserRoleToAdd);
						}
					}
	
					for (ArkUserRole arkUserRoleToRemove : getArkUserRolesToRemove(arkUserVO)) {
						session.delete(arkUserRoleToRemove);
					}
				}
				for (UserConfig config : arkUserVO.getArkUserConfigs()) {
					session.update(config);
				}
			}
			else {
				createArkUser(arkUserVO);
			}
		}
		catch (Exception exception) {
			StringBuffer sb = new StringBuffer();
			sb.append("There was an exception while updating the ArkUser in the backend.");
			sb.append(exception.getMessage());
			log.error(sb.toString());
			throw new ArkSystemException();
		}
	}

	/**
	 * Determines a List of ArkUserRoles that are not present in the existing(backend) ArkUserRole mapping table for the given ArkUser.
	 * 
	 * @param arkUserVO
	 * @return List<ArkUserRole>
	 * @throws EntityNotFoundException
	 */
	private List<ArkUserRole> getArkUserRolesToAdd(ArkUserVO arkUserVO) throws EntityNotFoundException {
		// Get a List of existing ArkUserRole from backend and determine items to add/remove
		List<ArkUserRole> arkUserRolesToAdd = new ArrayList<ArkUserRole>();

		List<ArkUserRole> existingArkUserRoleList = getArkUserLinkedModuleAndRoles(arkUserVO);

		for (ArkUserRole arkUserRole : arkUserVO.getArkUserRoleList()) {
			if (!existingArkUserRoleList.contains(arkUserRole)) {
				arkUserRolesToAdd.add(arkUserRole);
			}
			else {

			}
		}
		return arkUserRolesToAdd;
	}

	/**
	 * Determines a List of ArkUserRoles that need to be removed from the backend as part of the update. mapping table for the given ArkUser.
	 * 
	 * @param arkUserVO
	 * @return List<ArkUserRole>
	 * @throws EntityNotFoundException
	 */
	private List<ArkUserRole> getArkUserRolesToRemove(ArkUserVO arkUserVO) throws EntityNotFoundException {
		// Get a List of existing ArkUserRole from backend and determine items to add/remove
		List<ArkUserRole> arkUserRolesToRemove = new ArrayList<ArkUserRole>();

		List<ArkUserRole> existingArkUserRoleList = getArkUserLinkedModuleAndRoles(arkUserVO);
		for (ArkUserRole arkUserRole : existingArkUserRoleList) {
			// If the selected items from the View/Model does not contain an existing ArkUserRole from backend remove it
			if (!arkUserVO.getArkUserRoleList().contains(arkUserRole)) {
				arkUserRolesToRemove.add(arkUserRole);
			}
		}
		return arkUserRolesToRemove;
	}

	/**
	 * Get the ArkUserRole details for the given user for a specific study
	 * 
	 * @throws EntityNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public List<ArkUserRole> getArkUserLinkedModuleAndRoles(ArkUserVO arkUserVO) throws EntityNotFoundException {

		ArkUser arkUser;
		List<ArkUserRole> arkUserRoleList = new ArrayList<ArkUserRole>();

		arkUser = getArkUser(arkUserVO.getUserName());
		arkUserVO.setArkUserPresentInDatabase(true);
		Criteria criteria = getSession().createCriteria(ArkUserRole.class);
		criteria.add(Restrictions.eq("arkUser", arkUser));
		criteria.createAlias("arkModule", "module");
		criteria.addOrder(Order.asc("module.id"));

		// Restrict by Study if NOT Super Administrator
		if (!isUserAdminHelper(arkUser.getLdapUserName(), au.org.theark.core.security.RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR)) {
			criteria.add(Restrictions.eq("study", arkUserVO.getStudy()));
		}

		try {
			arkUserRoleList = criteria.list();
		}
		catch (org.hibernate.TransientObjectException toe) {
			log.error(toe.getMessage(), toe);
		}
		return arkUserRoleList;
	}

	/**
	 * Get a List of ArkUserRole objects for a given study and Module. This method can be used to determine a list of Ark Modules arkUsers are linked
	 * to for a given study.
	 * 
	 * @param study
	 * @param arkModule
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ArkUserRole> getArkUserLinkedModule(Study study, ArkModule arkModule) {
		List<ArkUserRole> arkUserRoleList = new ArrayList<ArkUserRole>();
		Criteria criteria = getSession().createCriteria(ArkUserRole.class);
		criteria.add(Restrictions.eq("arkModule", arkModule));
		criteria.add(Restrictions.eq("study", study));
		arkUserRoleList = criteria.list();
		return arkUserRoleList;
	}

	/**
	 * 
	 * Returns an existing collection of LinkStudyArkModule objects for a given Study
	 * 
	 * @param study
	 * @return List<LinkStudyArkModule>
	 */
	@SuppressWarnings("unchecked")
	public List<LinkStudyArkModule> getLinkStudyArkModulesList(Study study) {
		Criteria criteria = getSession().createCriteria(LinkStudyArkModule.class);
		criteria.add(Restrictions.eq("study", study));
		return criteria.list();
	}

	public void deleteArkUser(ArkUserVO arkUserVO) throws ArkSystemException, EntityNotFoundException {

		Session session = getSession();
		// Remove all roles linked to this ark user
		for (ArkUserRole arkUserRole : arkUserVO.getArkUserRoleList()) {
			session.delete(arkUserRole);
		}

		List<ArkUserRole> listOfRoles = getArkRoleListByUser(arkUserVO);
		if (listOfRoles.size() <= 0) {
			// Remove the ArkUser From the database only
			session.delete(arkUserVO.getArkUserEntity());
		}
	}
	
	public void deleteArkUserRole(ArkUserRole arkUserRole) {
		getSession().delete(arkUserRole);
	}
	
	public void deleteArkUserRolesForStudy(Study study, ArkUser arkUser) {
		for(ArkUserRole arkUserRole : getArkUserRoleList(study, arkUser)){
			getSession().delete(arkUserRole);
		}
	}

	public StudyStatus getStudyStatus(String statusName) throws StatusNotAvailableException {
		StudyStatus studyStatus = new StudyStatus();
		studyStatus.setName("Archive");
		Example studyStatusExample = Example.create(studyStatus);

		Criteria studyStatusCriteria = getSession().createCriteria(StudyStatus.class).add(studyStatusExample);
		if (studyStatusCriteria != null && studyStatusCriteria.list() != null && studyStatusCriteria.list().size() > 0) {
			return (StudyStatus) studyStatusCriteria.list().get(0);
		}
		else {
			log.error("Study Status Table maybe out of synch. Please check if it has an entry for Archive status");
			throw new StatusNotAvailableException();
		}
	}

	@SuppressWarnings("unchecked")
	public List<ArkUserRole> getArkRoleListByUser(ArkUserVO arkUserVo) {
		List<ArkUserRole> arkUserRoleList = new ArrayList<ArkUserRole>(0);
		Criteria criteria = getSession().createCriteria(ArkUserRole.class);
		criteria.add(Restrictions.eq("arkUser", arkUserVo.getArkUserEntity()));
		// Restrict by Study if NOT Super Administrator
		try {
			if (!isUserAdminHelper(arkUserVo.getArkUserEntity().getLdapUserName(), RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR)) {
				Criteria studycriteria = criteria.createCriteria("study");
				studycriteria.addOrder(Order.asc("name"));
			}
		}
		catch (HibernateException e) {
			log.error(e.getMessage(), e);
		}
		catch (EntityNotFoundException e) {
			log.error(e.getMessage(), e);
		}

		criteria.addOrder(Order.asc("arkModule"));
		criteria.addOrder(Order.asc("arkRole"));
		arkUserRoleList = (List<ArkUserRole>) criteria.list();
		return arkUserRoleList;
	}

	@SuppressWarnings("unchecked")
	public List<ArkUserRole> getArkRoleListByUserAndStudy(ArkUserVO arkUserVo, Study study) {
		List<ArkUserRole> arkUserRoleList;
		Criteria criteria = getSession().createCriteria(ArkUserRole.class);
		criteria.add(Restrictions.eq("arkUser", arkUserVo.getArkUserEntity()));
		try {
			if(!isUserAdminHelper(arkUserVo.getArkUserEntity().getLdapUserName(), RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR)
					&& study!=null && study.getId()!=null){
				criteria.add(Restrictions.eq("study", study));	
			}
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		
		//Criteria studycriteria = criteria.createCriteria("study");
		//studycriteria.addOrder(Order.asc("name"));
		//criteria.addOrder(Order.asc("study.name"));
		criteria.addOrder(Order.asc("arkModule"));
		criteria.addOrder(Order.asc("arkRole"));
		
		arkUserRoleList = (List<ArkUserRole>) criteria.list();
		return arkUserRoleList;
	}

	@SuppressWarnings("unchecked")
	public List<ArkRolePolicyTemplate> getArkRolePolicyTemplate(ArkRole arkRole, ArkModule arkModule) {
		List<ArkRolePolicyTemplate> arkRolePolicyTemplateList = new ArrayList<ArkRolePolicyTemplate>(0);
		Criteria criteria = getSession().createCriteria(ArkRolePolicyTemplate.class);
		criteria.add(Restrictions.eq("arkRole", arkRole));
		if (!arkRole.getName().equalsIgnoreCase(au.org.theark.core.security.RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR)) {
			criteria.add(Restrictions.eq("arkModule", arkModule));
		}
		arkRolePolicyTemplateList = (List<ArkRolePolicyTemplate>) criteria.list();
		return arkRolePolicyTemplateList;
	}

	/**
	 * Similar to the method above, this should be called for general queries to return studies because it assumes that the query is being requested by
	 * a non-Super Administrator
	 * 
	 * @param searchStudy
	 * @param studyCriteria
	 */
	private void applyStudySearchCriteria(Study searchStudy, Criteria studyCriteria) {
		applyStudySearchCriteria(searchStudy, studyCriteria, false);
	}

	/**
	 * A common method that can be used to apply a search filter on Study entity via a Criteria that is passed from the caller. The Criteria's entity
	 * can be determined by the caller ( i.e can be Study.class or ArkUserRole.class), this is useful when the ArkUser is a SuperAdministrator when we
	 * want all the studies to be displayed.In such a case the Criteria object must be the Study entity to include all studies.
	 * 
	 * @param searchStudy
	 * @param studyCriteria
	 */
	private void applyStudySearchCriteria(Study searchStudy, Criteria studyCriteria, boolean isSuperUser) {
		if (searchStudy.getId() != null) {
			studyCriteria.add(Restrictions.eq(Constants.STUDY_KEY, searchStudy.getId()));
		}

		if (searchStudy.getName() != null) {
			studyCriteria.add(Restrictions.ilike(Constants.STUDY_NAME, searchStudy.getName(), MatchMode.ANYWHERE));
		}

		if (searchStudy.getDateOfApplication() != null) {
			studyCriteria.add(Restrictions.eq(Constants.DATE_OF_APPLICATION, searchStudy.getDateOfApplication()));
		}

		if (searchStudy.getEstimatedYearOfCompletion() != null) {
			studyCriteria.add(Restrictions.eq(Constants.EST_YEAR_OF_COMPLETION, searchStudy.getEstimatedYearOfCompletion()));
		}

		if (searchStudy.getChiefInvestigator() != null) {
			studyCriteria.add(Restrictions.ilike(Constants.CHIEF_INVESTIGATOR, searchStudy.getChiefInvestigator(), MatchMode.ANYWHERE));
		}

		if (searchStudy.getContactPerson() != null) {
			studyCriteria.add(Restrictions.ilike(Constants.CONTACT_PERSON, searchStudy.getContactPerson(), MatchMode.ANYWHERE));
		}

		if (searchStudy.getStudyStatus() != null) {
			// In future, Super Administrators may be able to search for Archived studies
			studyCriteria.add(Restrictions.eq("studyStatus", searchStudy.getStudyStatus()));
			if (!isSuperUser) {
				// If not a Super Admin always remove Archived studies
				try {
					StudyStatus status = getStudyStatus("Archive");
					studyCriteria.add(Restrictions.ne("studyStatus", status));
				}
				catch (StatusNotAvailableException notAvailable) {
					log.error("Cannot look up and filter on archive status. Reference data could be missing");
				}
			}
		}
		else {
			// If no status is selected, then default to return all except Archived
			try {
				StudyStatus status = getStudyStatus("Archive");
				studyCriteria.add(Restrictions.ne("studyStatus", status));
			}
			catch (StatusNotAvailableException notAvailable) {
				log.error("Cannot look up and filter on archive status. Reference data could be missing");
			}
		}
		studyCriteria.addOrder(Order.asc("parentStudy"));
		studyCriteria.addOrder(Order.asc("id"));
		studyCriteria.addOrder(Order.asc("name"));
	}

	/**
	 * Invoke this mehod when the ArkUser is permitted(a Super Administrator) to view all studies.
	 * 
	 * @param searchStudy
	 * @return List<Study>
	 */
	@SuppressWarnings("unchecked")
	private List<Study> getAllStudiesForSuperAdmin(Study searchStudy) {
		Criteria criteria = getSession().createCriteria(Study.class);
		applyStudySearchCriteria(searchStudy, criteria, true);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Study> getStudyListForUser(ArkUserVO arkUserVo) {
		List<Study> studyList = new ArrayList<Study>(0);
		Study searchStudy = arkUserVo.getStudy();
		try {
			if (isUserAdminHelper(arkUserVo.getArkUserEntity().getLdapUserName(), RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR)) {
				studyList = getAllStudiesForSuperAdmin(arkUserVo.getStudy());// Get all Studies
			}
			else {
				/* Get only the studies the ArkUser is linked to via the ArkUserRole */
				Criteria criteria = getSession().createCriteria(ArkUserRole.class);
				criteria.add(Restrictions.eq("arkUser", arkUserVo.getArkUserEntity()));
				Criteria studyCriteria = criteria.createCriteria("study");
				applyStudySearchCriteria(searchStudy, studyCriteria);
				ProjectionList projectionList = Projections.projectionList();
				projectionList.add(Projections.groupProperty("study"), "study");
				criteria.setProjection(projectionList);
				studyList = criteria.list();

			}
		}
		catch (EntityNotFoundException e1) {
			log.error("The specified Ark User does not exist " + arkUserVo.getArkUserEntity().getLdapUserName());
			e1.printStackTrace();
		}

		return studyList;

	}

	@SuppressWarnings("unchecked")
	public List<Study> getStudyListForUserAndModule(ArkUserVO arkUserVo, ArkModule arkModule) {
		List<Study> studyList = new ArrayList<Study>(0);
		Study searchStudy = arkUserVo.getStudy();
		Criteria criteria = getSession().createCriteria(ArkUserRole.class);

		try {
			// Restrict by user if NOT Super Administrator
			if (isUserAdminHelper(arkUserVo.getArkUserEntity().getLdapUserName(), RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR)) {
				// Fix another bug where the Super Administrator will never be able to INNER JOIN between ArkUserRole and Study on studyId
				// (since a Super Admin should always have null in the arkUserRole's study column)
				studyList = getAllStudiesForSuperAdmin(arkUserVo.getStudy()); // Get all Studies
				return studyList;
			}
			else {
				// Not Super Administrator, so continue with building the query
				criteria.add(Restrictions.eq("arkUser", arkUserVo.getArkUserEntity()));
			}
		}
		catch (EntityNotFoundException e) {
			log.error(e.getMessage(), e);
		}

		if (arkModule != null) {
			criteria.add(Restrictions.eq("arkModule", arkModule));
		}
		else {
			// If no arkModule supplied, return empty list
			log.error("No arkModule supplied, returning empty study list");
			return studyList;
		}

		// Restrict on study criteria (by default, NOT 'Archive' status)
		Criteria studyCriteria = criteria.createCriteria("study");

		if (searchStudy.getId() != null) {
			studyCriteria.add(Restrictions.eq(Constants.STUDY_KEY, searchStudy.getId()));
		}

		if (searchStudy.getName() != null) {
			studyCriteria.add(Restrictions.ilike(Constants.STUDY_NAME, searchStudy.getName(), MatchMode.ANYWHERE));
		}

		if (searchStudy.getDateOfApplication() != null) {
			studyCriteria.add(Restrictions.eq(Constants.DATE_OF_APPLICATION, searchStudy.getDateOfApplication()));
		}

		if (searchStudy.getEstimatedYearOfCompletion() != null) {
			studyCriteria.add(Restrictions.eq(Constants.EST_YEAR_OF_COMPLETION, searchStudy.getEstimatedYearOfCompletion()));
		}

		if (searchStudy.getChiefInvestigator() != null) {
			studyCriteria.add(Restrictions.ilike(Constants.CHIEF_INVESTIGATOR, searchStudy.getChiefInvestigator(), MatchMode.ANYWHERE));
		}

		if (searchStudy.getContactPerson() != null) {
			studyCriteria.add(Restrictions.ilike(Constants.CONTACT_PERSON, searchStudy.getContactPerson(), MatchMode.ANYWHERE));
		}

		if (searchStudy.getStudyStatus() != null) {
			studyCriteria.add(Restrictions.eq("studyStatus", searchStudy.getStudyStatus()));
			try {
				StudyStatus status = getStudyStatus("Archive");
				studyCriteria.add(Restrictions.ne("studyStatus", status));
			}
			catch (StatusNotAvailableException notAvailable) {
				log.error("Cannot look up and filter on archive status. Reference data could be missing");
			}
		}
		else {
			try {
				StudyStatus status = getStudyStatus("Archive");
				studyCriteria.add(Restrictions.ne("studyStatus", status));
			}
			catch (StatusNotAvailableException notAvailable) {
				log.error("Cannot look up and filter on archive status. Reference data could be missing");
			}
		}

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("study"), "study");
		studyCriteria.addOrder(Order.asc("parentStudy"));
		criteria.setProjection(projectionList);

		studyList = criteria.list();
		return studyList;

	}

	public boolean arkUserHasModuleAccess(ArkUser arkUser, ArkModule arkModule) {
		boolean hasModuleAccess = false;
		Criteria criteria = getSession().createCriteria(ArkUserRole.class);

		try {
			// Restrict by user if NOT Super Administrator
			if (!isUserAdminHelper(arkUser.getLdapUserName(), RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR)) {
				criteria.add(Restrictions.eq("arkUser", arkUser));
			}

			// Restrict by arkModule
			criteria.add(Restrictions.eq("arkModule", arkModule));

			List<?> list = criteria.list();
			hasModuleAccess = (list.size() > 0);
		}
		catch (EntityNotFoundException e) {
			log.error(e.getMessage(), e);
		}
		catch (NullPointerException e) {
			log.error(e.getMessage(), e);
		}

		return hasModuleAccess;
	}

	@SuppressWarnings("unchecked")
	public List<ArkModule> getArkModuleListByArkUser(ArkUser arkUser) {
		Criteria criteria = getSession().createCriteria(ArkUserRole.class);
		ArkModule arkModule = null;
		try {
			// Restrict by user if NOT Super Administrator
			if (!isUserAdminHelper(arkUser.getLdapUserName(), RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR)) {
				criteria.add(Restrictions.eq("arkUser", arkUser));
			}
		}
		catch (EntityNotFoundException e) {
			log.error(e.getMessage(), e);
		}
		catch (NullPointerException e) {
			log.error(e.getMessage(), e);
		}
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("arkModule"), "arkModule");

		criteria.setProjection(projectionList);
		criteria.addOrder(Order.asc("arkModule.id"));
		
		criteria.createAlias("arkModule", "am");
		criteria.add(Restrictions.eq("am.enabled", true));
		
		return criteria.list();
	}

	public Boolean isArkUserLinkedToStudies(ArkUser arkUser) {
		Boolean flag = false;

		Criteria criteria = getSession().createCriteria(ArkUserRole.class);
		criteria.add(Restrictions.eq("arkUser", arkUser));
		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) criteria.uniqueResult();
		if (totalCount > 0) {
			flag = true;
		}
		return flag;
	}

	@SuppressWarnings("unchecked")
	public List<ArkUser> getArkUserListByStudy(ArkUser arkUser,Study study) {
			Criteria criteria = getSession().createCriteria(ArkUserRole.class);
			// Restrict by user if NOT Super Administrator
			try {
				if (!isUserAdminHelper(arkUser.getLdapUserName(), RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR)) {
					criteria.add(Restrictions.eq("study", study));
				}
			} catch (EntityNotFoundException e) {
				log.info("User Name doen not exsists");
			}
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.groupProperty("arkUser"), "arkUser");
			criteria.setProjection(projectionList);
			criteria.setResultTransformer(Transformers.aliasToBean(ArkUserRole.class));
			List<ArkUserRole> arkUserRoles=(List<ArkUserRole>)criteria.list();
			List<ArkUser> arkUserLst= new ArrayList<ArkUser>();
			for (ArkUserRole arkRolePol : arkUserRoles) {
				arkUserLst.add(arkRolePol.getArkUser());
			}
			//Added on 2015-12-10 filter study wise users.
			//changed on 2016-04-28.
		return	arkUserLst;
	}

	@SuppressWarnings("unchecked")
	public List<Study> getParentStudyList() {
		List<Study> studyList = new ArrayList<Study>(0);
		
		// Restrict on study criteria (by default, NOT 'Archive' status)
		Criteria studyCriteria = getSession().createCriteria(Study.class);
		StudyStatus status;
		try {
			status = getStudyStatus("Archive");
			studyCriteria.add(Restrictions.ne("studyStatus", status));
		}
		catch (StatusNotAvailableException e) {
			log.error(e.getMessage(), e);
		}
		
		// Can only select studies with null parent, or where the study is a parent
		studyCriteria.add(Restrictions.disjunction().add(Restrictions.isNull("parentStudy")).add(Restrictions.eqProperty("parentStudy.id", "id")));
		studyCriteria.addOrder(Order.asc("name"));
		studyList = studyCriteria.list();
		return studyList;
	}

	@SuppressWarnings("unchecked")
	public List<Study> getAssignedChildStudyListForUser(ArkUserVO arkUserVo) {
		List<Study> studyList = new ArrayList<Study>(0);
		
		try {
			ArkUser arkUser = getArkUser(arkUserVo.getArkUserEntity().getLdapUserName());
			
			/* Get only the studies the ArkUser is linked to via the ArkUserRole */
			Criteria criteria = getSession().createCriteria(ArkUserRole.class);
			criteria.add(Restrictions.eq("arkUser", arkUser));
			// Restrict to child studies (without parent)
			Criteria studyCriteria = criteria.createCriteria("study");
			studyCriteria.add(Restrictions.eq("parentStudy", arkUserVo.getStudy()));
			studyCriteria.add(Restrictions.neProperty("id", "parentStudy.id"));
			studyCriteria.addOrder(Order.asc("name"));
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.groupProperty("study"), "study");
			criteria.setProjection(projectionList);
			
			studyList = criteria.list();
		}
		catch (EntityNotFoundException e) {
			log.error(e.getMessage(), e);
		}
		
		return studyList;
	}
	
	public void createArkUserRole(ArkUserRole arkUserRole) {
		getSession().save(arkUserRole);
	}
	
	/**
	 * List of ArkRolePolicyTemplates for users other than the super Admin.
	 * 
	 * Here I was failed to use the projection list because I need the whole "ArkRolePolicyTemplate" object list
	 * So I used instead of a query for that. 
	 * 
	 * Group by function.
	 */
	@SuppressWarnings("unchecked")
	public List<ArkRolePolicyTemplate> getArkRolePolicytemplateList(ArkUserRole arkUserRole){
		String queryString = "SELECT  arpt FROM ArkRolePolicyTemplate arpt where arpt.arkRole=(:arkRole) and "
							 + "arpt.arkModule=(:arkModule) group by arpt.arkFunction, arpt.id";
		Query query = getSession().createQuery(queryString);
		query.setParameter("arkRole",arkUserRole.getArkRole() );
		query.setParameter("arkModule",arkUserRole.getArkModule() );
		List<ArkRolePolicyTemplate> arkRolePolicyTemplateLst = query.list();
		return arkRolePolicyTemplateLst;
	}
	/**
	 * List of Permission for function. 
	 *
	 * Just I need the list of permission list for a particular function I manage to use the hibernate projectionList property.
	 * Group by Permission.
	 * But still used setResultTransformer to transform the list which include the permission list.
	 * @return
	 */
	public List<ArkPermission> getArkPremissionListForRoleAndModule(ArkRolePolicyTemplate arkRolePolicyTemplate){
		Criteria criteria = getSession().createCriteria(ArkRolePolicyTemplate.class);
		criteria.add(Restrictions.eq("arkRole", arkRolePolicyTemplate.getArkRole()));
		criteria.add(Restrictions.eq("arkModule", arkRolePolicyTemplate.getArkModule()));
		criteria.add(Restrictions.eq("arkFunction", arkRolePolicyTemplate.getArkFunction()));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("arkPermission"), "arkPermission");
		criteria.setProjection(projectionList);
		criteria.setResultTransformer(Transformers.aliasToBean(ArkRolePolicyTemplate.class));
		List<ArkRolePolicyTemplate> arkRolePolicyTemplatesLst=	 (List<ArkRolePolicyTemplate>)criteria.list();
		List<ArkPermission> arkPermissionLst= new ArrayList<ArkPermission>();
		for (ArkRolePolicyTemplate arkRolePol : arkRolePolicyTemplatesLst) {
			arkPermissionLst.add(arkRolePol.getArkPermission());
		}
		return arkPermissionLst;
	}
	
	/**
	 * This method will update the ark user role for a study for a existing user.
	 * @param arkUserVO
	 */
	public void updateArkUserRoleListForExsistingUser(ArkUserVO arkUserVO) {
		Session session = getSession();
		List<ArkUserRole> arkUserRoleList = arkUserVO.getArkUserRoleList();
		for (ArkUserRole arkUserRole : arkUserRoleList) {
			if (arkUserRole.getArkRole() != null) {
				arkUserRole.setArkUser(arkUserVO.getArkUserEntity());
				session.save(arkUserRole);
			}
		}
		for (UserConfig config : arkUserVO.getArkUserConfigs()) {
			session.update(config);
		}
	}

	@Override
	public List<Study> getUserStudyListIncludeChildren(ArkUserVO arkUserVO) {
		Criteria criteria = getSession().createCriteria(ArkUserRole.class);
		criteria.add(Restrictions.eq("arkUser", arkUserVO.getArkUserEntity()));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("study"), "study");
		criteria.setProjection(projectionList);
		criteria.setResultTransformer(Transformers.aliasToBean(ArkUserRole.class));
		List<ArkUserRole> arkUserRoleLst=	 (List<ArkUserRole>)criteria.list();
		List<Study>  studyList=new ArrayList<Study>();
		for (ArkUserRole arkUserRole : arkUserRoleLst) {
			studyList.add(arkUserRole.getStudy());
		}
		return studyList;
	}
	
	public List<Study> getStudiesWithRoleForUser(ArkUserVO arkUserVO, ArkRole arkRole) {
		try {
			String ldapName = arkUserVO.getArkUserEntity().getLdapUserName();
			if(isUserAdminHelper(ldapName, RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR) ||
					isUserAdminHelper(ldapName, RoleConstants.ARK_ROLE_ADMINISTATOR)) {
				return getStudyListForUser(arkUserVO);
			}

		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		Criteria criteria = getSession().createCriteria(ArkUserRole.class);

		criteria.add(Restrictions.eq("arkUser", arkUserVO.getArkUserEntity()));
        criteria.add(Restrictions.eq("arkRole", arkRole));

		criteria.setProjection(Projections.property("study"));

		return criteria.list();
	}
}