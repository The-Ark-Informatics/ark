package au.org.theark.core.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
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
import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.vo.ArkModuleVO;
import au.org.theark.core.vo.ArkUserVO;

/**
 * @author nivedann
 *
 */
/**
 * @author nivedan
 *
 * @param <T>
 */
@Repository("arkAuthorisationDao")
public class ArkAuthorisationDao<T>  extends HibernateSessionDao implements IArkAuthorisation{

	final Logger log = LoggerFactory.getLogger(ArkAuthorisationDao.class);
	/**
	 * Looks up a ArkUser based on a String that represents the user name in LDAP.
	 * If the user name provided is null or is invalid/does not exist in the database, the method will return NULL for a ArkUser
	 * instance.
	 * @param ldapUserName
	 * @return ArkUser
	 */
	public ArkUser getArkUser(String ldapUserName) throws EntityNotFoundException{
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
	 * Overloaded method to lookup an ArkUser by username and study
	 * @param ldapUserName
	 * @param study
	 * @return
	 * @throws EntityNotFoundException
	 */
	public ArkUser getArkUser(String ldapUserName, Study study) throws EntityNotFoundException{
		StatelessSession session = getStatelessSession();
		Criteria criteria = session.createCriteria(ArkUser.class);
		criteria.add(Restrictions.eq("ldapUserName", ldapUserName));
		criteria.add(Restrictions.eq("study", study));
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
		Criteria criteria = getSession().createCriteria(ArkRole.class);
		criteria.add(Restrictions.eq("name", roleName));
		ArkRole arkRole = (ArkRole)criteria.uniqueResult();
		return arkRole;
	}
	
	/**
	 * <p>
	 * The method takes in a LdapUserName and calls the isUserAdminHelper to do the grunt of the work.
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
	
	private boolean isUserAdminHelper(String ldapUserName, String roleName, ArkFunction arkFunction, ArkModule arkModule) throws EntityNotFoundException{
		boolean isAdminType = false;
		StatelessSession session = getStatelessSession();
		//Check or get user ark_user object based on ldapUserName
		ArkUser arkUser  = getArkUser(ldapUserName);
		Criteria criteria = session.createCriteria(ArkUserRole.class);
		ArkRole arkRole  = getArkRoleByName(roleName);
		criteria.add(Restrictions.eq("arkRole", arkRole));
		criteria.add(Restrictions.eq("arkUser", arkUser));
		criteria.add(Restrictions.eq("arkModule",arkModule));
		
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
	
	public boolean isSuperAdministator(String ldapUserName, ArkFunction arkFunction, ArkModule arkModule) throws EntityNotFoundException{
		return isUserAdminHelper(ldapUserName,RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR,arkFunction,arkModule);
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
		
		StatelessSession session = getStatelessSession();
		Criteria criteria =  session.createCriteria(ArkUserRole.class);// getSession().createCriteria(ArkUserRole.class);
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
		session.close();
		return userRoles;
	}
	
	public String getUserRoleForStudy(String ldapUserName,Study study) throws EntityNotFoundException{
		String roleName ="";
		ArkUser arkUser  = getArkUser(ldapUserName);
		StatelessSession session = getStatelessSession();
		
		Criteria criteria = session.createCriteria(ArkUserRole.class);//getSession().createCriteria(ArkUserRole.class);
		criteria.createAlias("arkUser", "auserObject");
		criteria.add(Restrictions.eq("arkUser", arkUser));
		criteria.add(Restrictions.eq("auserObject.study", study));
		criteria.setMaxResults(1);
		ArkUserRole arkUserRole  = (ArkUserRole)criteria.uniqueResult();
		if(arkUserRole != null){
			roleName = arkUserRole.getArkRole().getName();
		}
		session.close();
		return roleName;
	}
	
	/**
	 * Retrieve a Logged in user's role by providing the Ldap User Name, Usecase id, module id & or study id.
	 * We need the Ldap User Name & ArkUseCase Id as a mandatory one.
	 * @throws EntityNotFoundException 
	 */
	
	public String getUserRole(String ldapUserName,ArkFunction arkFunction, ArkModule arkModule,Study study) throws EntityNotFoundException{
		String roleName = "";
		
		ArkUser arkUser  = getArkUser(ldapUserName);
		Criteria criteria = getSession().createCriteria(ArkUserRole.class);
		criteria.createAlias("arkUser", "auserObject");
		
		criteria.add(Restrictions.eq("arkUser", arkUser));		
		//Even if there is a study in session the criteria must be applied only if the logged in user has a study registered for him. Ie if he is not a Super Admin
		if(!isSuperAdministrator(ldapUserName) && study != null){
			
			criteria.add(Restrictions.eq("study", study));
			if(arkModule != null){
				criteria.add(Restrictions.eq("arkModule", arkModule));
			}
			//criteria.setMaxResults(1);
			List<ArkUserRole> list  = (List<ArkUserRole>)criteria.list();
			if(list.size() > 0 ){
				ArkUserRole arkUserRole  = (ArkUserRole)criteria.list().get(0);
				//ArkUserRole arkUserRole  = (ArkUserRole)criteria.list().get(0);
				if(arkUserRole != null){
					roleName = arkUserRole.getArkRole().getName();
				}
			}
		
		}else{
			if(arkModule != null){
				criteria.add(Restrictions.eq("arkModule", arkModule));
			}
			
			criteria.setMaxResults(1);
			ArkUserRole arkUserRole  = (ArkUserRole)criteria.uniqueResult();
			if(arkUserRole != null){
				roleName = arkUserRole.getArkRole().getName();
			}
		}
		
		return roleName;
	}
	
	
	
	
	public ArkFunction getArkFunctionByName(String functionName){
		Criteria criteria = getSession().createCriteria(ArkFunction.class);
		criteria.add(Restrictions.eq("name", functionName));
		criteria.setMaxResults(1);
		ArkFunction arkFunction  = (ArkFunction)criteria.uniqueResult();
		return arkFunction;
	}
	
	public ArkFunction getArkFunctionById(Long functionId){
		Criteria criteria = getSession().createCriteria(ArkFunction.class);
		criteria.add(Restrictions.eq("id", functionId));
		criteria.setMaxResults(1);
		ArkFunction arkFunction  = (ArkFunction)criteria.uniqueResult();
		return arkFunction;
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
	
	public Collection<String> getArkRolePermission(ArkFunction arkFunction,String userRole, ArkModule arkModule) throws EntityNotFoundException{
		
		Collection<String> stringPermissions = new ArrayList<String>();
		ArkRole arkRole  = getArkRoleByName(userRole);
		Criteria criteria = getSession().createCriteria(ArkRolePolicyTemplate.class);
		
		if(arkModule != null){
			criteria.add(Restrictions.eq("arkModule", arkModule));
		}
		
		if(arkFunction != null){
			criteria.add(Restrictions.eq("arkFunction", arkFunction));
		}
		
		if(arkRole != null){
			criteria.add(Restrictions.eq("arkRole", arkRole));
			List<ArkRolePolicyTemplate> templateList =  criteria.list();
			for (ArkRolePolicyTemplate arkRolePolicyTemplate : templateList) {
				stringPermissions.add(arkRolePolicyTemplate.getArkPermission().getName());
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


	public Collection<Class<T>> getEntityList(Class aClass){
		Collection<Class<T>> arkModuleList = new ArrayList<Class<T>>();
		Criteria criteria = getSession().createCriteria(aClass);
		arkModuleList = criteria.list();
		return (Collection<Class<T>>) arkModuleList;
	}
	
	/**
	 * This overloaded interface can be used when we only want all ther Permissions for a Given Role. It is applicable for Super Administator role
	 * where we don't need to specify the ArkModule or ArkRole. If this method is invoked for any other role it will return all the permissions for each role, the permissions
	 * will be duplicated. So avoid invoking this method for Non SuperAdministator type roles.
	 * @param userRole
	 * @return
	 * @throws EntityNotFoundException 
	 */
	public Collection<String> getArkRolePermission(String userRole) throws EntityNotFoundException{
		//Delegate the call to the other getArkRolePermission by passing null for arkFunction and arkModule
		return getArkRolePermission(null,userRole,null);
	}
	
	public Collection<ArkModuleRole> getArkModuleAndLinkedRoles(){
		Collection<ArkModuleRole> arkModuleList = new ArrayList<ArkModuleRole>();
		Criteria criteria = getSession().createCriteria(ArkModuleRole.class);
		criteria.createAlias("arkModule", "moduleName");
		criteria.addOrder(Order.asc("moduleName.name"));
		arkModuleList =  criteria.list();
		Log.info(arkModuleList.size());
		for (Iterator iterator = arkModuleList.iterator(); iterator.hasNext();) {
			ArkModuleRole arkModuleRole = (ArkModuleRole) iterator.next();
			Log.info("\n Module Name " + arkModuleRole.getArkModule().getName());
			Log.info("\n Role Name " + arkModuleRole.getArkRole().getName());
		}
		return arkModuleList;
	}
	
	/**
	 * Create a new Ark User in the system and associates the arkuser with the study and links the user to one or more Modules and Roles that 
	 * was configured for the Study.
	 */
	public void createArkUser(ArkUserVO arkUserVO){
		Session session = getSession();
		session.save(arkUserVO.getArkUserEntity());
		List<ArkUserRole> arkUserRoleList = arkUserVO.getArkUserRoleList();
		for (ArkUserRole arkUserRole : arkUserRoleList) {
			if(arkUserRole.getArkRole() != null){
				arkUserRole.setArkUser(arkUserVO.getArkUserEntity());
				session.save(arkUserRole);
			}
		}
	}
	
	/**
	 * Get a list of Modules that are linked to the study and then get the roles linked to each module
	 * A VO List of ArkModuleVO will contain the ArkModule and a list of ArkRoles.
	 * @param study
	 * @return Collection<ArkModuleVO>
	 */
	public Collection<ArkModuleVO> getArkModulesLinkedToStudy(Study study){

		Collection<LinkStudyArkModule> arkStudyLinkedModuleList = new ArrayList<LinkStudyArkModule>();
		
		Collection<ArkModuleVO> arkModuleVOList = new ArrayList<ArkModuleVO>();
		
		Criteria criteria = getSession().createCriteria(LinkStudyArkModule.class);
		criteria.add(Restrictions.eq("study", study));
		arkStudyLinkedModuleList  = criteria.list();
		
		//For each one in the List get the associated Roles i.e for each module get the Roles
		for (LinkStudyArkModule linkStudyArkModule : arkStudyLinkedModuleList) {
			//Here is a Module linked to a study get the Roles linked to this module
			ArkModuleVO arkModuleVO = new ArkModuleVO();
			arkModuleVO.setArkModule(linkStudyArkModule.getArkModule());
			arkModuleVO.setArkModuleRoles(getArkRoleLinkedToModule(linkStudyArkModule.getArkModule()));
			arkModuleVOList.add(arkModuleVO);
		}
		
		//getArkUserRoleList(study,null);
		return arkModuleVOList;
	}
	
	public Collection<ArkModule> getArkModulesLinkedWithStudy(Study study){
		Criteria criteria = getSession().createCriteria(LinkStudyArkModule.class);
		criteria.add(Restrictions.eq("study", study));
		Collection<LinkStudyArkModule> arkStudyLinkedModuleList = new ArrayList<LinkStudyArkModule>();
		arkStudyLinkedModuleList  = criteria.list();
		Collection<ArkModule> arkModuleList = new ArrayList<ArkModule>();
		for (LinkStudyArkModule linkStudyArkModule : arkStudyLinkedModuleList) {
			arkModuleList.add(linkStudyArkModule.getArkModule());
		}
		return arkModuleList;
	}
	
	public ArrayList<ArkRole> getArkRoleLinkedToModule(ArkModule arkModule){
		
		Collection<ArkModuleRole> arkModuleList = new ArrayList<ArkModuleRole>();
		Criteria criteria = getSession().createCriteria(ArkModuleRole.class);
		criteria.add(Restrictions.eq("arkModule", arkModule));
		arkModuleList =  criteria.list();
		ArrayList<ArkRole> moduleArkRolesList = new ArrayList<ArkRole>();
		for (ArkModuleRole arkModuleRole : arkModuleList) {
			ArkRole arkRole = arkModuleRole.getArkRole();
			moduleArkRolesList.add(arkRole);
		}
		return moduleArkRolesList;
	}
	
	public List<ArkUserRole> getArkUserRoleList(Study study,ArkUser arkUser){
		
		Criteria criteria = getSession().createCriteria(LinkStudyArkModule.class,"linkStudyArkModule");
		criteria.add(Restrictions.eq("study", study));
		criteria.createAlias("arkUserRoleList", "userRole",Criteria.LEFT_JOIN);
		criteria.add(Restrictions.or(Restrictions.eq("userRole.arkUser", arkUser), Restrictions.isNull("userRole.arkUser")));
		criteria.add(Restrictions.eq("userRole.study", study));
		
		ProjectionList projection =Projections.projectionList();
		projection.add(Projections.property("userRole.id"),"id");
		projection.add(Projections.property("userRole.arkUser"),"arkUser");
		projection.add(Projections.property("userRole.arkRole"),"arkRole");
		projection.add(Projections.property("linkStudyArkModule.arkModule"),"arkModule");
		projection.add(Projections.property("linkStudyArkModule.study"),"study");
		
		
		criteria.setProjection(projection);

		criteria.setResultTransformer(Transformers.aliasToBean(ArkUserRole.class));
		List<ArkUserRole>  listOfResults = criteria.list();
		
		return listOfResults;
			
	}
	

	/**
	 * Will update the ArkUser or adds the user into ArkUser table. It determines the List of ArkUserRoles for
	 * insertion and removal and processes these ArkUserRole lists.
	 * @param arkUserVO
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public void updateArkUser(ArkUserVO arkUserVO) throws EntityNotFoundException,ArkSystemException{
		
		try{
			
			
			if(arkUserVO.getArkUserEntity() != null && arkUserVO.getArkUserEntity().getId() != null){
				//User is present in the ArkUserTable can go for update of the entity and related objects (ArkUserRoles)
				Session session = getSession();
				session.update(arkUserVO.getArkUserEntity());
				
				//Insert new ArkUserRole
				for (ArkUserRole arkUserRoleToAdd : getArkUserRolesToAdd(arkUserVO)) {
					if(arkUserRoleToAdd.getArkRole() != null){
						arkUserRoleToAdd.setArkUser(arkUserVO.getArkUserEntity());
						session.save(arkUserRoleToAdd);
					}
				}

				for (ArkUserRole arkUserRoleToRemove : getArkUserRolesToRemove(arkUserVO)) {
					session.delete(arkUserRoleToRemove);
				}
			}else{
				createArkUser(arkUserVO);
				
			}
			
		}catch(Exception exception){
			StringBuffer sb = new StringBuffer();
			sb.append("There was an exception while updating the ArkUser in the backend.");
			sb.append(exception.getMessage());
			log.error(sb.toString() );
			throw new ArkSystemException();
		}
		
	}
	
	/**
	 * Determines a List of ArkUserRoles that are not present in the existing(backend) ArkUserRole
	 * mapping table for the given ArkUser.
	 * @param arkUserVO
	 * @return List<ArkUserRole> 
	 * @throws EntityNotFoundException
	 */
	private List<ArkUserRole>  getArkUserRolesToAdd(ArkUserVO arkUserVO) throws EntityNotFoundException{
		//Get a List of existing ArkUserRole from backend and determine items to add/remove
		List<ArkUserRole> arkUserRolesToAdd = new ArrayList<ArkUserRole>();
		
		List<ArkUserRole> existingArkUserRoleList = getArkUserLinkedModuleAndRoles(arkUserVO);
		
		for (ArkUserRole arkUserRole : arkUserVO.getArkUserRoleList()) {
			if(!existingArkUserRoleList.contains(arkUserRole)){
				arkUserRolesToAdd.add(arkUserRole);
			}else{
				
			}
		}
		return arkUserRolesToAdd;
	}
	
	/**
	 * Determines a List of ArkUserRoles that need to be removed from the backend as part of the update.
	 * mapping table for the given ArkUser.
	 * @param arkUserVO
	 * @return List<ArkUserRole> 
	 * @throws EntityNotFoundException
	 */
	private List<ArkUserRole>  getArkUserRolesToRemove(ArkUserVO arkUserVO) throws EntityNotFoundException{
		//Get a List of existing ArkUserRole from backend and determine items to add/remove
		List<ArkUserRole> arkUserRolesToRemove = new ArrayList<ArkUserRole>();
		
		List<ArkUserRole> existingArkUserRoleList = getArkUserLinkedModuleAndRoles(arkUserVO);
		for (ArkUserRole arkUserRole : existingArkUserRoleList) {
			//If the selected items from the View/Model does not contain an existing ArkUserRole from backend remove it
			if(!arkUserVO.getArkUserRoleList().contains(arkUserRole)){
				arkUserRolesToRemove.add(arkUserRole);
			}
		}
		return arkUserRolesToRemove;
	}
	
	
	/**
	 * Get the ArkUserRole details for the given user for a specific study
	 * @throws EntityNotFoundException 
	 */
	public List<ArkUserRole> getArkUserLinkedModuleAndRoles(ArkUserVO arkUserVO) throws EntityNotFoundException {
		
		ArkUser arkUser;
		List<ArkUserRole> arkUserRoleList = new ArrayList<ArkUserRole>();

		arkUser = getArkUser(arkUserVO.getUserName());
		arkUserVO.setArkUserPresentInDatabase(true);
		Criteria criteria = getSession().createCriteria(ArkUserRole.class);
		criteria.add(Restrictions.eq("arkUser", arkUser));
		criteria.add(Restrictions.eq("study", arkUserVO.getStudy()));
		arkUserRoleList = criteria.list();
		
		return arkUserRoleList;
		
	}
	
	/**
	 * Get a List of ArkUserRole objects for a given study and Module. This method can be used to determine
	 * a list of Ark Modules arkUsers are linked to for a given study.
	 * @param study
	 * @param arkModule
	 * @return
	 */
	public List<ArkUserRole> getArkUserLinkedModule(Study study, ArkModule arkModule){
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
	 * @return  List<LinkStudyArkModule>
	 */
	public List<LinkStudyArkModule> getLinkStudyArkModulesList(Study study){

		Collection<LinkStudyArkModule> arkStudyLinkedModuleList = new ArrayList<LinkStudyArkModule>();
		
		Criteria criteria = getSession().createCriteria(LinkStudyArkModule.class);
		criteria.add(Restrictions.eq("study", study));
		return criteria.list();
	}
	
	public void deleteArkUser(ArkUserVO arkUserVO) throws ArkSystemException, EntityNotFoundException{
		
		Session session = getSession();
		//Remove all roles linked to this ark user
		for (ArkUserRole arkUserRole : arkUserVO.getArkUserRoleList()) {
			session.delete(arkUserRole);
		}
		
		session.delete(arkUserVO.getArkUserEntity());
	}

}
