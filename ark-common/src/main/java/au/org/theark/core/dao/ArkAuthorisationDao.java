package au.org.theark.core.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.Restrictions;
import org.jfree.util.Log;
import org.springframework.stereotype.Repository;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkPermission;
import au.org.theark.core.model.study.entity.ArkRole;
import au.org.theark.core.model.study.entity.ArkRolePermission;
import au.org.theark.core.model.study.entity.ArkUsecase;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.ArkUserRole;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.RoleConstants;

/**
 * @author nivedann
 *
 */
@Repository("arkAuthorisationDao")
public class ArkAuthorisationDao<T>  extends HibernateSessionDao implements IArkAuthorisation{


	/**
	 * Looks up a ArkUser based on a String that represents the user name in LDAP.
	 * If the user name provided is null or is invalid/does not exist in the database, the method will return NULL for a ArkUser
	 * instance.
	 * @param ldapUserName
	 * @return ArkUser
	 */
	protected ArkUser getArkUser(String ldapUserName) throws EntityNotFoundException{
		StatelessSession session = getStatelessSession();
		Criteria criteria = session.createCriteria(ArkUser.class);
		criteria.add(Restrictions.eq("ldapUserName", ldapUserName));
		ArkUser arkUser  = (ArkUser)criteria.uniqueResult();
		//Close the session
		session.close();
		if(arkUser != null){
			return arkUser;
		}else{
			throw new EntityNotFoundException("The given Ldap User does not exist in the database system");
		}
	}
	
	/**
	 * Returns a list of ArkRole objects from the backend. This does not use the Stateless session.
	 * Can be used by front-end client's.
	 * @return  List<ArkRole>
	 */
	public List<ArkRole> getArkRoles(){
		
		Criteria criteria = getSession().createCriteria(ArkRole.class);
		List<ArkRole> arkRoleList = criteria.list();
		return arkRoleList;
	}
	
	/**<p>
	 * Given a String Role name like Super Administrator or Administrator the method will return the actual instance of
	 * ArkRole object.</p>
	 * @param roleName
	 * @return ArkRole
	 */
	protected ArkRole getArkRoleByName(String roleName){
		StatelessSession session = getStatelessSession();
		Criteria criteria = session.createCriteria(ArkRole.class);
		criteria.add(Restrictions.eq("name", roleName));
		ArkRole arkRole = (ArkRole)criteria.uniqueResult();
		session.close();
		return arkRole;
	}
	
	/**
	 * <p>
	 * The method takes in a LdapUserName and calls the isUserAdminHelper to do th grunt of the work.
	 * The query is again Ark_User_Role table/ArkUserRole entity. The getArkRoleByName is invoked
	 * to get a object that represents Administrator. The method uses this to then check if the given user
	 * is/has a role of the type in ArkUserRole table/instance. If yes a boolean true is returned and otherwise
	 * false is returned.</p><br>
	 * @return boolean 
	 * @throws EntityNotFoundException 
	 */
	public boolean isAdministator(String ldapUserName) throws EntityNotFoundException {
		
		return isUserAdminHelper(ldapUserName,RoleConstants.ARK_ROLE_ADMINISTATOR);

	}

	
	private boolean  isUserAdminHelper(String ldapUserName, String roleName) throws EntityNotFoundException{
		boolean isAdminType = false;
		StatelessSession session = getStatelessSession();
		//Check or get user ark_user object based on ldapUserName
		ArkUser arkUser  = getArkUser(ldapUserName);
		Criteria criteria = session.createCriteria(ArkUserRole.class);
		ArkRole arkRole  = getArkRoleByName(roleName);
		criteria.add(Restrictions.eq("arkRole", arkRole));
		criteria.add(Restrictions.eq("arkUser", arkUser));
		criteria.setMaxResults(1);
		ArkUserRole arkUserRole = (ArkUserRole) criteria.uniqueResult();
		if(arkUserRole != null){
			isAdminType = true;
		}
		session.close();
		return isAdminType;
		
	}
	/**
	 * <p>
	 * The method takes in a LdapUserName and calls the isUserAdminHelper whihc queries the Database using a Stateless session.
	 * The query is again Ark_User_Role table/ArkUserRole entity. The getArkRoleByName is invoked
	 * to get a object that represents Administrator. The method uses this to then check if the given user
	 * is/has a role of the type in ArkUserRole table/instance. If yes a boolean true is returned and otherwise
	 * false is returned.</p><br>
	 * 
	 * @return boolean 
	 * @see au.org.theark.core.dao.IStudyDao#isAdministator(java.lang.String)
	 */
	public boolean isSuperAdministrator(String ldapUserName) throws EntityNotFoundException{
		return isUserAdminHelper(ldapUserName,RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR);
	}
	
	
	/**
	 * Use this method when we want to load the Collection of Administrator roles as a Collectio<String>.
	 * The method looks up Ark Super Administrator and Administrator roles given a LdapUserName. It populates it into a Collection<String>
	 * that represent a unique set of administration roles for this user. It does not take into account the Module or Study.
	 * This is usually when the user has logged in first and we want to know if the user has a role of type  Administator so he can have access to Create function.
	 * @param ldapUserName
	 * @return Collection<String>
	 * @throws EntityNotFoundException
	 */
	public Collection<String> getUserAdminRoles(String ldapUserName) throws EntityNotFoundException{
		
		ArkUser arkUser  = getArkUser(ldapUserName);
		Criteria criteria = getSession().createCriteria(ArkUserRole.class);
		ArkRole arkRoleSuperAdmin  = getArkRoleByName(RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR);
		ArkRole arkRoleAdmin  = getArkRoleByName(RoleConstants.ARK_ROLE_ADMINISTATOR);
		criteria.add(Restrictions.or(Restrictions.eq("arkRole", arkRoleSuperAdmin),Restrictions.eq("arkRole", arkRoleAdmin)));
		criteria.add(Restrictions.eq("arkUser", arkUser));
		List<ArkUserRole> arkUserRoleList = (List<ArkUserRole>)criteria.list();
		Set<String> roles  = new HashSet<String>(0);
		for (ArkUserRole arkUserRole : arkUserRoleList) {
			String roleName = arkUserRole.getArkRole().getName();
			roles.add(roleName);
		}
	
		Collection<String> userRoles = new ArrayList<String>();
		for (String roleName : roles) {
			userRoles.add(roleName);
		}
		return userRoles;
	}
	
	public String getUserRoleForStudy(String ldapUserName,Study study) throws EntityNotFoundException{
		String roleName ="";
		ArkUser arkUser  = getArkUser(ldapUserName);
		Criteria criteria = getSession().createCriteria(ArkUserRole.class);
		criteria.createAlias("arkUser", "auserObject");
		criteria.add(Restrictions.eq("arkUser", arkUser));
		criteria.add(Restrictions.eq("auserObject.study", study));
		criteria.setMaxResults(1);
		ArkUserRole arkUserRole  = (ArkUserRole)criteria.uniqueResult();
		if(arkUserRole != null){
			roleName = arkUserRole.getArkRole().getName();
		}
		return roleName;
	}
	
	/**
	 * Retrieve a Logged in user's role by providing the Ldap User Name, Usecase id, module id & or study id.
	 * We need the Ldap User Name & ArkUseCase Id as a mandatory one.
	 * @throws EntityNotFoundException 
	 */
	
	public String getUserRole(String ldapUserName,ArkUsecase arkUseCase, ArkModule arkModule,Study study) throws EntityNotFoundException{
		String roleName = "";
		
		ArkUser arkUser  = getArkUser(ldapUserName);
		Criteria criteria = getSession().createCriteria(ArkUserRole.class);

		criteria.createAlias("arkUser", "auserObject");
		criteria.add(Restrictions.eq("arkUser", arkUser));
		
		//Even if there is a study in session the criteria must be applied only if the logged in user has a study registered for him. Ie if he is not a Super Admin
		if(!isSuperAdministrator(ldapUserName)){
			if(study != null){
				criteria.add(Restrictions.eq("auserObject.study", study));	
			}
		}
		
		if(arkUseCase != null){
			criteria.add(Restrictions.eq("arkUsecase", arkUseCase));
		}
		
		if(arkModule != null){
			criteria.add(Restrictions.eq("arkModule", arkModule));
		}
		
		criteria.setMaxResults(1);
		ArkUserRole arkUserRole  = (ArkUserRole)criteria.uniqueResult();
		if(arkUserRole != null){
			roleName = arkUserRole.getArkRole().getName();
		}
		return roleName;
	}
	
	
	
	
	public ArkUsecase getArkUsecaseByName(String usecaseName){
		Criteria criteria = getSession().createCriteria(ArkUsecase.class);
		criteria.add(Restrictions.eq("name", usecaseName));
		criteria.setMaxResults(1);
		ArkUsecase arkUsecase  = (ArkUsecase)criteria.uniqueResult();
		return arkUsecase;
	}
	
	public ArkUsecase getArkUsecaseById(Long usecaseId){
		Criteria criteria = getSession().createCriteria(ArkUsecase.class);
		criteria.add(Restrictions.eq("id", usecaseId));
		criteria.setMaxResults(1);
		ArkUsecase arkUsecase  = (ArkUsecase)criteria.uniqueResult();
		return arkUsecase;
	}
	
	public ArkModule getArkModuleByName(String moduleName){
		Criteria criteria = getSession().createCriteria(ArkModule.class);
		criteria.add(Restrictions.eq("name", moduleName));
		criteria.setMaxResults(1);
		ArkModule arkModule = (ArkModule)criteria.uniqueResult();
		return arkModule;
	}
	
	public ArkModule getArkModuleById(Long moduleId){
		Criteria criteria = getSession().createCriteria(ArkModule.class);
		criteria.add(Restrictions.eq("id", moduleId));
		criteria.setMaxResults(1);
		ArkModule arkModule = (ArkModule)criteria.uniqueResult();
		return arkModule;
	}
	
	public Collection<String> getArkUserRolePermission(String ldapUserName,ArkUsecase arkUseCase,String userRole, ArkModule arkModule,Study study) throws EntityNotFoundException{
		Collection<String> stringPermissions = new ArrayList<String>();
		ArkUser arkUser  = getArkUser(ldapUserName);
		ArkRole arkRole  = getArkRoleByName(userRole);
		Criteria criteria = getSession().createCriteria(ArkUserRole.class);
		criteria.createAlias("arkUser", "auserObject");
		//criteria.createAlias("arkUserRole","arkUserRoleObject");
		criteria.add(Restrictions.eq("arkUser", arkUser));
		
		//Even if there is a study in session the criteria must be applied only if the logged in user has a study registered for him. Ie if he is not a Super Admin
		if(study != null){
			criteria.add(Restrictions.eq("auserObject.study", study));	
		}
		
		if(arkUseCase != null){
			criteria.add(Restrictions.eq("arkUsecase", arkUseCase));
		}
		
		if(arkModule != null){
			criteria.add(Restrictions.eq("arkModule", arkModule));
		}
		
		if(arkRole != null){
			criteria.add(Restrictions.eq("arkRole", arkRole));
			criteria.setMaxResults(1);
			ArkUserRole arkUserRole  = (ArkUserRole)criteria.uniqueResult();
			Set<ArkRolePermission> rolePermissions = arkUserRole.getArkRole().getArkRolePermission();
			if(rolePermissions.size() > 0){
				for (Iterator<ArkRolePermission> iterator = rolePermissions.iterator(); iterator.hasNext();) {
					ArkRolePermission arkRolePermission =  iterator.next();
					String permission  = arkRolePermission.getArkPermission().getName();
					stringPermissions.add(permission);
				}
				Log.debug("\n  Permissions found");
			}else{
				Log.debug("\n No Permissions found");
			}
		}
		
		return stringPermissions;
	}
	
	/**
	 * Returns a list of All Permissions in Ark System
	 * @return
	 */
	public Collection<String> getArkPermission(){
		
		Collection<String> arkStringPermissions = new ArrayList<String>(); 
		Criteria criteria = getSession().createCriteria(ArkPermission.class);
		List<ArkPermission> arkPermissionList = (List<ArkPermission>)criteria.list();
		for (Iterator <ArkPermission>iterator = arkPermissionList.iterator(); iterator.hasNext();) {
			ArkPermission arkPermission = iterator.next();
			arkStringPermissions.add(arkPermission.getName());
		}
		return arkStringPermissions;
	}

}
