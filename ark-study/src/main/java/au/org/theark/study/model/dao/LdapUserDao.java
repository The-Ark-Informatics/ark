package au.org.theark.study.model.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.util.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.stereotype.Repository;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.UnAuthorizedOperation;
import au.org.theark.core.exception.UserNameExistsException;
import au.org.theark.core.security.RoleConstants;
import au.org.theark.core.util.UIHelper;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.core.vo.RoleVO;
import au.org.theark.core.vo.SiteVO;
import au.org.theark.core.vo.StudyVO;
import au.org.theark.study.service.Constants;


@Repository("ldapUserDao")
public class LdapUserDao implements ILdapUserDao{
	
	static Logger log = LoggerFactory.getLogger(LdapUserDao.class);
	//All these need to be auto injected by spring, this bean itself is a injected into the service
	private LdapTemplate ldapTemplate;
	
	private String baseModuleDn;
	private String baseGroupDn;
	private String basePeopleDn;
	private String baseSiteDn;
	

	/**
	 * A property that reflects the value of the LDAP paths defined in
	 * application context
	 */
	private String baseDC;
	
	public String getBaseSiteDn() {
		return baseSiteDn;
	}

	public void setBaseSiteDn(String baseSiteDn) {
		this.baseSiteDn = baseSiteDn;
	}
	public String getBaseModuleDn() {
		return baseModuleDn;
	}

	public void setBaseModuleDn(String baseModuleDn) {
		this.baseModuleDn = baseModuleDn;
	}

	public String getBaseGroupDn() {
		return baseGroupDn;
	}

	public void setBaseGroupDn(String baseGroupDn) {
		this.baseGroupDn = baseGroupDn;
	}

	public String getBasePeopleDn() {
		return basePeopleDn;
	}

	public void setBasePeopleDn(String basePeopleDn) {
		this.basePeopleDn = basePeopleDn;
	}
	

	public String getBaseDC() {
		return baseDC;
	}
	
	public void setBaseDC(String baseDC) {
		this.baseDC = baseDC;
	}

	public LdapTemplate getLdapTemplate() {
		return ldapTemplate;
	}
	
	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}
	
	private IStudyDao studyDao;
	
	/*To access Hibernate Study Dao */
	@Autowired
	public void setStudyDao(IStudyDao studyDao) {
		this.studyDao = studyDao;
	}

	public IStudyDao getStudyDao() {
		return studyDao;
	}
	
	/**
	 * A fine-grained method to add a user to a group and to a list of roles.
	 * @param List<ModuleVO> 
	 * @param LdapName that contains the Rdn
	 * @param String contains the DN of the person to be added as a member
	 */
	private void addUserToGroup(List<ModuleVO> moduleList, List<ModuleVO> existingModules, String personPath, int mode) throws InvalidNameException, Exception{
		boolean isPresent = true;
		log.info("addUserToGroup()");
		if(moduleList.size() > 0){
			log.info("User has modules/groups to be bound to. " + moduleList.size());
			//Adds the user to the respective roles within each module
			for (ModuleVO moduleVO : moduleList) {

				if(mode == Constants.ADD && existingModules != null){
					isPresent = existingModules.contains(moduleVO);
				}
				
				log.info("Module Name: " + moduleVO.getModule() + "\n");
				LdapName ldapName = new LdapName(getBaseGroupDn());
				ldapName.add(new Rdn(Constants.CN, moduleVO.getModule().trim()));//Set the CN value, i.e the module name ETA etc cn=ETA
				BasicAttribute ba = new BasicAttribute(Constants.MEMBER, personPath);
				if( ((!isPresent && mode == Constants.ADD) || (existingModules == null))){
					ModificationItem item = new ModificationItem(DirContext.ADD_ATTRIBUTE, ba);
					ldapTemplate.modifyAttributes(ldapName, new ModificationItem[]{item});
				}else if(isPresent && mode == Constants.REMOVE){
					ModificationItem item = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, ba);
					ldapTemplate.modifyAttributes(ldapName, new ModificationItem[]{item});
				}
					log.info("Added user to application members list ");
			}
		}else{
			log.warn("No modules linked to user.Cannot add user to any module or roles.");
		}
	}
	
	/**
	 * Create a user in LDAP People and Groups.
	 * If the user already exists in people wrap the exception and return it
	 * If the user exists under a particular role ignore it and continue with the rest of the roles
	 *
	 */
	public void create(ArkUserVO userVO) throws UserNameExistsException, ArkSystemException{
		
		log.info("\n create(ArkUserVO " + userVO + ")");
		try{
			
			DirContextAdapter dirContextAdapter = new DirContextAdapter();
			//Map the Front end values into LDAP Context
			mapToContext(userVO.getUserName(),userVO.getFirstName(),userVO.getLastName(), userVO.getEmail(),userVO.getPassword(), dirContextAdapter);	
			LdapName ldapName = new LdapName(basePeopleDn);
			ldapName.add(new Rdn(Constants.CN, userVO.getUserName())); 
			Name nameObj = ldapName;
			//Create the person in Person collection in LDAP			
			ldapTemplate.bind(nameObj, dirContextAdapter,null);
			String personPath  = buildPersonDN(userVO.getUserName());
			addUserToGroup(userVO.getModules(), null,personPath,Constants.ADD);//Application
			for(ModuleVO module: userVO.getModules()){
				List<RoleVO> roles = module.getRole();
				addUserToStudy(userVO.getStudyVO(), module.getModule(), roles, userVO);	
			}
		}
		catch(org.springframework.ldap.NameAlreadyBoundException nabe){
		
			log.warn("The given username " + userVO.getUserName()  + "  exists in LDAP. " + nabe.getMessage());
			throw new UserNameExistsException("A user with the username " + userVO.getUserName() + " already exists in our system. Please provide a unique username.");

		}catch(InvalidNameException ine){
		
			log.warn("A System exception occured " + ine.getMessage());
			//TODO Implement a LDAP ContextSourceTransactionManager for client side Transaction management
			//Note LDAP as such does not participate in Txn management.
			throw new ArkSystemException("A System exception occured.");
		
		}catch(Exception exception ){
			//TODO Implement a LDAP ContextSourceTransactionManager for client side Transaction management
			//Note LDAP as such does not participate in Txn management.
			log.error("A system exception has occured when trying to add roles to the groups. Need to rollback the LDAP transaction. Remove the user from LDAP People collection " + exception.getMessage());	
			throw new ArkSystemException("A System exception occured");
		}
	}
	
	public void createSite(SiteVO siteVo) throws EntityExistsException,ArkSystemException{
		log.info("\n create a site in ldap");
		try {
			
			LdapName ldapName = new LdapName(baseSiteDn);
			DirContextAdapter dirContextAdapter = new DirContextAdapter();
			
			List<String> list = new ArrayList<String>();
			
			for (String siteMember : siteVo.getSiteMembers()) {
				list.add(buildPersonDN(siteMember));
			}
			Object[] membersInSite = list.toArray(); 
			dirContextAdapter.setAttributeValues("objectClass", new String[]{"groupOfNames","top"});
			dirContextAdapter.setAttributeValue("cn", siteVo.getSiteName());
			dirContextAdapter.setAttributeValues("member",membersInSite);
			dirContextAdapter.setAttributeValue("description", siteVo.getSiteDescription());
			ldapName.add(new Rdn(Constants.CN,siteVo.getSiteName()));
			Name nameobj = ldapName;
			ldapTemplate.bind(nameobj,dirContextAdapter,null);
		}
		catch(org.springframework.ldap.NameAlreadyBoundException nabe){
			log.error("A with with that name is present in the system.  " + nabe.getMessage());
			StringBuffer error = new StringBuffer();
			error.append( siteVo.getSiteName()  + " already exists in the system.");
			error.append( siteVo.getSiteName());
			throw new EntityExistsException( error.toString());
		}
		catch(InvalidNameException ine){
			log.error("An exception occured while creating a new Site. " + ine.getMessage());
			StringBuffer error = new StringBuffer();
			error.append("A system error occured while creating the Site ");
			error.append( siteVo.getSiteName());
			throw new ArkSystemException( error.toString());
		}
		
	}
	
	protected void mapToContext(String username,String firstName, String lastName, String email,  String password, DirContextOperations dirCtxOperations){
		
		dirCtxOperations.setAttributeValues("objectClass",  new String[]{"inetOrgPerson","organizationalPerson","person"});
		dirCtxOperations.setAttributeValue("cn", username);
		dirCtxOperations.setAttributeValue("sn", lastName);
		dirCtxOperations.setAttributeValue("givenName", firstName);
		dirCtxOperations.setAttributeValue("mail", email);
		dirCtxOperations.setAttributeValue("userPassword", new Sha256Hash(password).toHex());//Service will always hash it.
	}

	/**
	 * 
	 * getUserAccountInfo - userdetails, applications and roles linked to the user
	 * Returns the person details of the current user. 
	 */
	public ArkUserVO getUser(String username) throws ArkSystemException {
		
		ArkUserVO etaUserVO = null;
		log.info("\n getUser ");
		try{
			LdapName ldapName = new LdapName(basePeopleDn);
			ldapName.add(new Rdn("cn",username));
			Name nameObj = (Name)ldapName;
			etaUserVO = (ArkUserVO) ldapTemplate.lookup(nameObj, new PersonContextMapper());	
			log.info("\n etauserVO " + etaUserVO);
			
		}catch(InvalidNameException ne){
			log.info("\nGiven username or user does not exist." + username);
			throw new ArkSystemException("A System error has occured");
			
		}
		return etaUserVO;
	}
	
	/**
	 * Fetches a user from LDAP based on username
	 * Invokes a method isMemberof to populate a list of modules/groups and roles.
	 */
	public ArkUserVO getUserRole(String username) throws ArkSystemException {
		
		ArkUserVO  userVO = getUser(username);

		List<ModuleVO> moduleList = new ArrayList<ModuleVO>();
		userVO.setModules(moduleList);
		List<String> roles = new ArrayList<String>();
		userVO.setUserRoleList(roles);
		//Provide the Module Name from a list of modules available. 
		
		//get LDAP Module Names
		List<ModuleVO> listOfAllModules = getModules(false);
	
		isMemberof(listOfAllModules, userVO);
		
		moduleList = userVO.getModules();
		
		for (ModuleVO moduleVO : moduleList) {
			
			for (RoleVO roleVO : moduleVO.getRole()) {
					log.info("Role :" + roleVO.getRole());
					Pattern pattern = Pattern.compile(moduleVO.getModule() + "_");
					String[] splitContents = pattern.split(roleVO.getRole());
					roleVO.setRole(splitContents[1]);
			}
			
		}
		return userVO;
	}
	
	
	private String buildPersonDN(String userName) throws InvalidNameException {
		LdapName ldapName = new LdapName(baseDC);
		ldapName.add( new Rdn("ou","people"));
		ldapName.add(new Rdn("cn", userName));
		return ldapName.toString();
	}
	
	/**
	 * Method that will return a List roles for the given study for all modules. Given a study name,
	 * the method will look up all the applications that have this study name and if a match is found
	 * will return the list of roles the user is a member of.
	 */
	public List<ModuleVO> getUserRoles(ArkUserVO etaUserVO, String studyName) throws ArkSystemException {
		
		
		List<ModuleVO> userModulesAndRoles = new ArrayList<ModuleVO>(); //The list that will be returned
		List<ModuleVO> listOfAllModules = getModules(false); //Get a List of All System Modules and their available roles.
		String moduleName = "";

		try {
			
			String groupBase = "ou=groups";
			AndFilter moduleFilter = new AndFilter();
			moduleFilter.and(new EqualsFilter("objectClass","groupOfNames"));
			String personDn =  buildPersonDN(etaUserVO.getUserName());
			//Can be refactored to this one below
			//LdapUtility.buildPersonDN(etaUserVO.getUserName(), baseDC);
			
			moduleFilter.and(new EqualsFilter("member",personDn));
			
			for(ModuleVO module: listOfAllModules){
				//Fetches a list of roles that have been assigned to the user indicating that the user is a member of the GROUP/Application and the various roles.
				moduleName = module.getModule();
				LdapName dn;
				dn = new LdapName(groupBase);
				dn.add(new Rdn("cn",moduleName));
				dn.add(new Rdn("cn",studyName));
				
				List<?> userGroups =  ldapTemplate.search(dn,moduleFilter.encode(),SearchControls.ONELEVEL_SCOPE, new AttributesMapper(){
					
					public Object mapFromAttributes(Attributes attrs){
						try {
								Object returnObject = attrs.get("cn").get();
								return returnObject;
						} catch (NamingException e) {
							
								e.printStackTrace();
						}
							return attrs;
					}
				});
				
				if(userGroups != null && userGroups.size() > 0){
					ModuleVO moduleVO = new ModuleVO();
					moduleVO.setModule(module.getModule());
					userModulesAndRoles.add(moduleVO);
					List<RoleVO> roleList = new ArrayList<RoleVO>();
					moduleVO.setRole(roleList);
					for(Object role : userGroups){
						RoleVO roleVO = new RoleVO();
						roleVO.setRole(role.toString());
						roleList.add(roleVO);
					}
				}
			
			}
		}catch(InvalidNameException e){
			e.printStackTrace();
			log.error("A system error has occured" + e.getStackTrace());
			throw new ArkSystemException("A system error has occured");
		}catch(Exception ee){
				StringBuffer sb = new StringBuffer();
				sb.append("The study");
				sb.append(etaUserVO.getStudyVO().getStudyName());
				sb.append(" in module :");
				sb.append(moduleName);
				sb.append(" stack trace ");
				sb.append(ee.getStackTrace());
				log.info(sb.toString());
		}
		return userModulesAndRoles;
	}
	
	
	private static class PersonContextMapper implements ContextMapper {
		
		public Object mapFromContext(Object ctx) {
			log.info("\n PersonContext Mapper...");
			DirContextAdapter context = (DirContextAdapter) ctx;
			
			ArkUserVO etaUserVO = new ArkUserVO();
			etaUserVO.setUserName(context.getStringAttribute("cn"));
			etaUserVO.setFirstName(context.getStringAttribute("givenName"));
			etaUserVO.setLastName(context.getStringAttribute("sn"));
			etaUserVO.setEmail(context.getStringAttribute("mail"));
			String ldapPassword = new String((byte[]) context.getObjectAttribute("userPassword"));
			etaUserVO.setPassword(ldapPassword);
			return etaUserVO;
		}
	}
	
	private static class LiteSiteContextMapper implements ContextMapper {
		
		public Object mapFromContext(Object ctx) {
			log.info("\n LiteSiteContext Mapper...");
			DirContextAdapter context = (DirContextAdapter) ctx;
			SiteVO siteVo = new SiteVO();
			siteVo.setSiteName(context.getStringAttribute("cn"));
			siteVo.setSiteDescription(context.getStringAttribute("description"));
			//If the schema has other attributes then set/add them here
			return siteVo;
		}
	}
	

	/**
	 * Gets a list of roles for a given moduleId/application name
	 */
	public List<String> getModuleRoles(String moduleId) {
		
		log.info("\n ------ In getModuleRoles() " + moduleId);		
		if(moduleId == null || moduleId.trim().length() == 0){
			return null;//Throw an application exception here
		}
		
		AndFilter andFilter = new AndFilter();
		andFilter.and(new EqualsFilter("objectClass","organizationalRole"));
		
		List<String> roleVoList = new ArrayList<String>();
		LdapName dn;
		try {
			dn = new LdapName(getBaseModuleDn());
			dn.add(new Rdn("ou",moduleId.trim()));//get the module name from the parameter
			dn.add(new Rdn("ou","roles"));
			log.info("Filter to apply = " + andFilter.encode() + " DN =" + dn.toString());
			
			List<?> roles = ldapTemplate.search(dn, andFilter.encode(),SearchControls.SUBTREE_SCOPE, new AttributesMapper(){
				
				public Object mapFromAttributes(Attributes attrs){
					try{
						Object returnObject = attrs.get("cn").get();
						return returnObject;
					} catch (javax.naming.NamingException e){
						log.info("Exception occured in getModuleRoles " + e.getMessage());
						e.printStackTrace();
							//logger.error("$AttributesMapper.mapFromAttributes(Attributes) - NamingException", e); //$NON-NLS-1$
							//e.printStackTrace();
					}
					return attrs;
				}
			});  
		
			roleVoList =buildRoleNames(roles, true);

		} catch (InvalidNameException ine) {
			log.error("An Exception occure in getModuleRoles()" + ine.getMessage());
			return null;//need to notify admin
		}
		return roleVoList;
	}

	/**
	 * Returns a list of applications/modules. 
	 */
	public List<ModuleVO> getModules(boolean isDisplay) {
		log.info("getModules()");
		AndFilter moduleFilter = new AndFilter();
		moduleFilter.and(new EqualsFilter("objectClass","organizationalUnit")); 
	
		List<?> modules = ldapTemplate.search(getBaseModuleDn(), moduleFilter.encode(),SearchControls.ONELEVEL_SCOPE, new AttributesMapper(){
			
			public Object mapFromAttributes(Attributes attrs){
				try{
						Object returnObject = attrs.get("ou").get();
						return returnObject;
				} catch (javax.naming.NamingException e){
						//logger.error("$AttributesMapper.mapFromAttributes(Attributes) - NamingException", e); //$NON-NLS-1$
						//e.printStackTrace();
				}
				return attrs;
			}
		}); 
		
		return buildModuleNames(modules, isDisplay );
		
	}
	
	private List<String> buildRoleNames(List<?> roles, boolean isForDisplay){
		
		List<String> displayRoleList = new ArrayList<String>();
		
		for (Object role : roles) {
			String roleName = (String)role;
			if(isForDisplay){
				roleName  = UIHelper.getDisplayRoleName(roleName);
			}
			displayRoleList.add(roleName);
		}
		return displayRoleList;
	}
	
	/**
	 * A Helper method to build the list with either a display items or the actual
	 * data from back end.
	 * @param moduleList
	 * @param isDisplay
	 */
	private List<ModuleVO> buildModuleNames(List<?> modules,  boolean isForDisplay){
		List<ModuleVO> moduleList = new ArrayList<ModuleVO>();
		for (Iterator<?> iterator = modules.iterator(); iterator.hasNext();) {
			String moduleName = (String) iterator.next();
		
			ModuleVO moduleVO =  new ModuleVO();

			if(isForDisplay){
				moduleVO.setModule( UIHelper.getDisplayModuleName(moduleName));
			}else{
				moduleVO.setModule(moduleName);
			}
			moduleList.add(moduleVO);
			
			// get the roles for the module
			List<String> roles = getModuleRoles(moduleName);
			for(String roleName : roles){
					RoleVO roleVO = new RoleVO();
					roleVO.setRole(roleName);
					moduleVO.getRole().add(roleVO);
			}
			
		}
		return moduleList;
	}
	
	
	
	public void isMemberof(List<ModuleVO> moduleVOlist,ArkUserVO userVO) throws ArkSystemException{
		
		for (ModuleVO moduleVO : moduleVOlist) {
			log.info("\n Module Name: "+ moduleVO.getModule());
			isMemberof(moduleVO.getModule(), userVO);
		}
	}
	
	
	/**
	 * The method will search for a user in the group and returns a list of objects or sub-groups where a match is found. 
	 * For a given moduleName, it will return all sub-groups i.e super_administration group and all the  study(groups)
	 * where the user is a member of. 
	 * @param moduleName
	 * @param username
	 * @return
	 */
	public void isMemberof(String moduleName, ArkUserVO etaUserVO) throws ArkSystemException{
		/* Given Module Id and Role Name we can determine 1. If the user is a member of the module 2. If he has a particular role*/
		log.info("\n --- isMemberof = " + moduleName);
				
		boolean isStudyAvailable = false;
		String groupBase = "ou=groups";
		AndFilter moduleFilter = new AndFilter();
		moduleFilter.and(new EqualsFilter("objectClass","groupOfNames"));
		String personDn="";
		try {
			LdapName ldapName = new LdapName(baseDC);
			ldapName.add( new Rdn("ou","people"));
			ldapName.add(new Rdn("cn", etaUserVO.getUserName()));
			personDn = ldapName.toString();
		
			moduleFilter.and(new EqualsFilter("member",personDn));

			//Fetches a list of roles that have been assigned to the user indicating that the user is a member of the GROUP/Application and the various roles.
			LdapName dn;
			dn = new LdapName(groupBase);
			dn.add(new Rdn("cn",moduleName.trim()));
				
			if(etaUserVO.getStudyVO() != null && etaUserVO.getStudyVO().getStudyName() != null){
				dn.add(new Rdn("cn", etaUserVO.getStudyVO().getStudyName()));//Add the study as a filter too
				isStudyAvailable = true;
				log.info("\nStudy is available" + etaUserVO.getStudyVO().getStudyName());
			}
			List<?> userGroups =  ldapTemplate.search(dn,moduleFilter.encode(),SearchControls.ONELEVEL_SCOPE, new AttributesMapper(){
				
				public Object mapFromAttributes(Attributes attrs){
					try {
							Object returnObject = attrs.get("cn").get();
							return returnObject;
					} catch (NamingException e) {
						
							e.printStackTrace();
					}
						return attrs;
				}
			});
			
			if(userGroups != null && userGroups.size() > 0){
				//Add the module to userVO
				ModuleVO moduleVO = new ModuleVO();
				moduleVO.setModule(moduleName);
				etaUserVO.getModules().add(moduleVO);	
				List<RoleVO> roleList = new ArrayList<RoleVO>();
				moduleVO.setRole(roleList);//Initialise the role arraylist
				//Populate the Module.Roles collection. This structure of classifying roles against module allows us to maintain roles 
				//without any conflict(roles are in the module namespace)
				
				String moduleRoleName = buildUserRoles(userGroups,moduleVO, isStudyAvailable);
				//At present, I can see only a list of roles that can be stored into AuthorizationInfo and hence using a List<String>.This list is the one that will be set. 
				//The above Module.Roles collection will still be required for the future or if we can override the Shiro AuthorizationInfo class
				//TODO Re-visit the Shiro requirements and make sure this list is in synch with what is in back-end.
				etaUserVO.getUserRoleList().add(moduleRoleName);
			}
		}catch(InvalidNameException e){
			e.printStackTrace();
			log.error("A system error has occured" + e.getStackTrace());
			throw new ArkSystemException("A system error has occured");
		}catch(Exception ee){
				StringBuffer sb = new StringBuffer();
				sb.append("The study");
				sb.append(etaUserVO.getStudyVO().getStudyName());
				sb.append(" in module :");
				sb.append(moduleName);
				sb.append(" stack trace ");
				sb.append(ee.getStackTrace());
				log.info(sb.toString());
		}
	}
	
	
	private String buildUserRoles(List<?> groupList, ModuleVO moduleVO, boolean isStudy){
		String roleName ="ordinary_user";
		for (Object group : groupList) {
			//Check if user is part of the super_administrator group
			if(group.toString().equalsIgnoreCase("super_administrator")){
				roleName = moduleVO.getModule() + "_" + group.toString();
			}
			else if(!isStudy){
				roleName = moduleVO.getModule() + "_" + roleName;
				//This will be a sub-group under the application/module. Get a list of sub-groups which should be the actual grouping of roles.
			}else{
				roleName = group.toString();
			}
			RoleVO roleVO = new RoleVO();
			roleVO.setRole(roleName);
			moduleVO.getRole().add(roleVO );
			break;
		}
		return roleName;
	}
	
	 
	
	/**
	 * Use when you want to return ALL users from LDAP. Applies for a Super User and Study Admin only.
	 * The criteria is supplied in the userVO
	 * @param userCriteriaVO
	 * @return
	 * @throws InvalidNameException
	 */
	public List<ArkUserVO> searchAllUsers(ArkUserVO userCriteriaVO) throws ArkSystemException{
	
		SecurityManager securityManager =  ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();
		List<ArkUserVO> userList = new ArrayList<ArkUserVO>();
		
		if(securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.ARK_SUPER_ADMIN) || securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.STUDY_ADMIN)){
			
			
			log.info("getBaseDn() " + getBasePeopleDn());//ou=people
			LdapName ldapName;
			try{
				
				AndFilter andFilter = new AndFilter();
				andFilter.and(new EqualsFilter("objectClass","person"));
				
				ldapName = new LdapName(getBasePeopleDn());
				//if userId was specified
				/* User ID */
				if(StringUtils.hasText(userCriteriaVO.getUserName())){
					ldapName.add(new Rdn(Constants.CN,userCriteriaVO.getUserName()));
					andFilter.and(new EqualsFilter(Constants.CN,userCriteriaVO.getUserName()));
				}
				/* Given Name */
				if(StringUtils.hasText(userCriteriaVO.getFirstName())){
					ldapName.add(new Rdn(Constants.GIVEN_NAME,userCriteriaVO.getFirstName()));
					andFilter.and(new EqualsFilter(Constants.GIVEN_NAME,userCriteriaVO.getFirstName()));
				}
				
				/* Surname Name */
				if(StringUtils.hasText(userCriteriaVO.getLastName())){
					ldapName.add(new Rdn(Constants.LAST_NAME,userCriteriaVO.getLastName()));
					andFilter.and(new EqualsFilter(Constants.LAST_NAME,userCriteriaVO.getLastName()));
				}

				/* Email */
				if(StringUtils.hasText(userCriteriaVO.getEmail())){
					ldapName.add(new Rdn(Constants.EMAIL, userCriteriaVO.getEmail()));
					andFilter.and(new EqualsFilter(Constants.EMAIL,userCriteriaVO.getEmail()));
				}
				/* Status is not defined as yet in the schema */
				
				userList  = ldapTemplate.search(getBasePeopleDn(),andFilter.encode(),new PersonContextMapper());
				log.info("Size of list " + userList.size());
			}catch(InvalidNameException ine){
				
				log.error("Exception occured in searchAllUsers " + ine);
				throw new ArkSystemException("A system errror occured");
			}
		}
		
		return userList;
	}
	
	private static class GroupMembersContextMapper implements ContextMapper{
		
		public GroupMembersContextMapper(){
			super();
		}
		
		public Object mapFromContext(Object ctx){
			DirContextAdapter context = (DirContextAdapter) ctx;
			String[] members = context.getStringAttributes("member");
			ArrayList<String> memberList = new ArrayList<String>();
			for (int i = 0; i < members.length; i++) {
				memberList.add(members[i].substring(members[i].indexOf("cn=") + 3, members[i].indexOf(",")));
			}
			return memberList;
		}
	}
	
	private List<ArkUserVO> searchGroupMembers(ArkUserVO userCriteriaVO, String currentUser) throws ArkSystemException{
		log.info("In searchGroupMembers()");
		List<ArkUserVO> userResultsList = new ArrayList<ArkUserVO>();	
		try{
			//The Modules and Roles the current user is linked to as part of the currentUserDetails
			ArkUserVO currentUserDetails = getUserRole(currentUser);
			//Get a List of Modules
			List<ModuleVO> moduleList = currentUserDetails.getModules();
			ArrayList<String> memberList = new ArrayList<String>();
			//Iterate each module
			for (ModuleVO moduleVO : moduleList) {
			
				//User's module
				List<RoleVO> moduleRoleList = moduleVO.getRole();
				log.info("Module Name:" + moduleVO.getModule());
				//Iterate the List of roles for the module in context
				for (RoleVO roleVO : moduleRoleList) {

					log.info("Role Name:" + roleVO.getRole());
					log.info("Base DN: " + getBaseGroupDn());
					//Fetches a list of roles that have been assigned to the user indicating that the user is a member of the GROUP/Application and the various roles.
					LdapName dn;
					dn = new LdapName(getBaseGroupDn());
					dn.add(new Rdn("cn",moduleVO.getModule().trim()));
					dn.add(new Rdn("cn",roleVO.getRole().trim()));
					//1. Lookup all the users in this group and role
					memberList = (ArrayList<String>) ldapTemplate.lookup(dn, new GroupMembersContextMapper());
					
					//2. Get each member from the list above and get their full details.
					userResultsList = getPersonsByCn(memberList, userCriteriaVO);
					//3. Apply the userCriteriaVO on the filter and pull only those users of the group/role who match.
					//4. populate the userResultList and return this list
										
				}
			}
		}catch(InvalidNameException ine){
			log.error("A system error has occured " + ine.getStackTrace());
			throw new ArkSystemException("A system error has occured");
		}
		
		return userResultsList;
	}
	
	/**
	 * Retrieves a sub-set of users from LDAP. The memberCnList List<String> contains 
	 * the list of userNames or CN, and ArkUserVO acts as a criteria that will be applied when
	 * looking up the user. Not all users in the memberCnList will be returned, it also depends
	 * if the criteria matches with the sub-set of users.
	 * @param memberCnList
	 * @param userCriteriaVO
	 * @return
	 */
	public List<ArkUserVO> getPersonsByCn(List<String> memberCnList, ArkUserVO userCriteriaVO) {
		
		if (memberCnList == null || memberCnList.size() < 0) {
			return new ArrayList<ArkUserVO>();
		}

		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "person"));
		
		
		if(StringUtils.hasText(userCriteriaVO.getUserName())){
			filter.and(new EqualsFilter(Constants.CN,userCriteriaVO.getUserName()));
		}
		
		/* Given Name */
		if(StringUtils.hasText(userCriteriaVO.getFirstName())){
			filter.and(new EqualsFilter(Constants.GIVEN_NAME,userCriteriaVO.getFirstName()));
		}
		
		/* Surname Name */
		if(StringUtils.hasText(userCriteriaVO.getLastName())){
			
			filter.and(new EqualsFilter(Constants.LAST_NAME,userCriteriaVO.getLastName()));
		}

		/* Email */
		if(StringUtils.hasText(userCriteriaVO.getEmail())){
			
			filter.and(new EqualsFilter(Constants.EMAIL,userCriteriaVO.getEmail()));
		}

		OrFilter orFilter = new OrFilter();
		filter.and(orFilter);
		//Build the filter that matches the cn's and then apply the criteria
		for (Iterator<String> iterator = memberCnList.iterator(); iterator.hasNext();) {
			String userDN = iterator.next();
			orFilter.or(new EqualsFilter("cn", userDN));

			if(StringUtils.hasText(userCriteriaVO.getUserName())){
				filter.and(new EqualsFilter(Constants.CN,userCriteriaVO.getUserName()));
			}
			/* Given Name */
			if(StringUtils.hasText(userCriteriaVO.getFirstName())){
				filter.and(new EqualsFilter(Constants.GIVEN_NAME,userCriteriaVO.getFirstName()));
			}
			
			/* Surname Name */
			if(StringUtils.hasText(userCriteriaVO.getLastName())){
				
				filter.and(new EqualsFilter(Constants.LAST_NAME,userCriteriaVO.getLastName()));
			}

			/* Email */
			if(StringUtils.hasText(userCriteriaVO.getEmail())){
				
				filter.and(new EqualsFilter(Constants.EMAIL,userCriteriaVO.getEmail()));
			}
		}
		//TODO NN User a light version of PersonContextMapper, dont need to map password details.
		return ldapTemplate.search(getBasePeopleDn(), filter.encode(),new PersonContextMapper());
	}
	
	public List<ArkUserVO> searchUser(ArkUserVO userVO) throws  ArkSystemException{
		
		String userName = null;
		//This is only when you want all the users.
		List<ArkUserVO> userList = new ArrayList<ArkUserVO>();	
		/**
		 * Determine  the role of the user and return results based on role
		 */
		SecurityManager securityManager =  ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();
	
		if(securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.ARK_SUPER_ADMIN) || securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.STUDY_ADMIN)){
			log.info("User can access all users");
			userList = searchAllUsers(userVO);
		}else{
			log.info("User can access only a sub-set of users.");
			//Get logged in user's Groups and roles(and permissions)
			userName = (String) currentUser.getPrincipal();
			//delegate call to another method and get back a list of users into userList
			userList = searchGroupMembers(userVO, userName);
		}
		
		return userList;
	}
	
	public void update(ArkUserVO userVO) throws ArkSystemException {
		log.info("update() invoked: Updating user details in LDAP");
		try{
			//Assuming that all validation is already done update the attributes in LDAP
			LdapName ldapName = new LdapName(getBasePeopleDn());
			ldapName.add(new Rdn("cn", userVO.getUserName()));
			
			BasicAttribute sn = new BasicAttribute("sn", userVO.getLastName());
			BasicAttribute givenName = new BasicAttribute("givenName", userVO.getFirstName());
			BasicAttribute email = new BasicAttribute("mail", userVO.getEmail());

			ModificationItem itemSn = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, sn);
			ModificationItem itemGivenName = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, givenName);
			ModificationItem itemEmail = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, email);

			//Check if password needs to be modified. Make sure client validation is done.
			//TODO Also enforce the validation check here.
			if(userVO.isChangePassword()){
				BasicAttribute userPassword = 	new BasicAttribute("userPassword", new Sha256Hash(userVO.getPassword()).toHex());
				ModificationItem itemPassword = new	ModificationItem (DirContext.REPLACE_ATTRIBUTE,userPassword);
				ldapTemplate.modifyAttributes(ldapName, new ModificationItem[] {itemSn, itemGivenName, itemEmail,itemPassword });
			}else{
				ldapTemplate.modifyAttributes(ldapName, new ModificationItem[] {itemSn, itemGivenName, itemEmail});
			}
			
			updateGroupRoles(userVO);
			
			//Add or remove the users from roles
		
		}catch(InvalidNameException ine){
			throw new ArkSystemException("A System error has occured");
		}
		
	}
	
	/**
	 * Update the modules the user is a member of and its associated roles.
	 * ModuleVO
	 * @param etaUserVO
	 * @param List<ModuleVO> selectedModuleVOList will contain a list of ModuleVO that contains a list of associated roles the
	 *                       user has been assigned from the front end.
	 */
	public void updateGroupRoles(ArkUserVO etaUserVO) throws ArkSystemException{
		
		List<ModuleVO> moduleVOListToAdd = new ArrayList<ModuleVO>();
		List<ModuleVO> moduleVOListToRemove = new ArrayList<ModuleVO>();
		
		List<ModuleVO> existingModuleVOList = getUserRoles(etaUserVO, etaUserVO.getStudyVO().getStudyName());//Get the user's existing membership details Modules and the roles for the given study
		List<ModuleVO> selectedModuleVOList = etaUserVO.getModules();//The list of modules user has selected from the front end
		
		processAddList(selectedModuleVOList, existingModuleVOList, moduleVOListToAdd);
		processRemoveList(selectedModuleVOList, existingModuleVOList,moduleVOListToRemove);//The list of modules and or roles that must be now disassociated from the user.
		
		try {
			/* Add Module and/or Roles*/
			String personPath = buildPersonDN(etaUserVO.getUserName());
			
			if(moduleVOListToAdd != null && moduleVOListToAdd.size() > 0){
				for(ModuleVO moduleToAdd : moduleVOListToAdd)
				{
					getUserStudyRole(etaUserVO,moduleToAdd.getModule(),etaUserVO.getStudyVO());
					if(etaUserVO.getStudyVO().getRoles() != null && etaUserVO.getStudyVO().getRoles().size() > 0){
						//User already is member of the study so we add him to the roles
						addUserToRolesMemberList(etaUserVO.getStudyVO(), moduleToAdd.getModule(), moduleToAdd.getRole(), etaUserVO);
					}else{
						addUserToGroup(moduleVOListToAdd, existingModuleVOList, personPath.toString(),Constants.ADD);
						//User is not part of that study and so we add the user to the study and roles via the one method below.
						addUserToStudy(etaUserVO.getStudyVO(),moduleToAdd.getModule(), moduleToAdd.getRole(), etaUserVO);	
					}
				}
					
			}
			
			/* Process any roles or study the user must be unassociated with.*/
			if(moduleVOListToRemove != null && moduleVOListToRemove.size() > 0){
				
				BasicAttribute ba = new BasicAttribute(Constants.MEMBER, personPath);
				String studyName = etaUserVO.getStudyVO().getStudyName();
				//Iterate the Modules and find out which roles need to be removed
				for(ModuleVO moduleToRemove : moduleVOListToRemove){
					for(RoleVO role: moduleToRemove.getRole()){
						log.info("Role to be removed from: " + role.getRole());
						//Build the ldap here and remove it
						LdapName lname = buildDNForRole(personPath, moduleToRemove.getModule(), studyName, role.getRole(), etaUserVO.getUserName());
						removeMember(lname, ba);
						//Check if there are any other roles the user is part of in this module. If there are no roles the user is linked to in this module
						//Remove the user from the study and group as well.
						
						existingModuleVOList =  getUserRoles(etaUserVO, etaUserVO.getStudyVO().getStudyName());

						if(existingModuleVOList.size() > 0){
							
							boolean isPresent = existingModuleVOList.contains(moduleToRemove);
							if(!isPresent){
								//Remove user from Study Member List
								lname = buildDNForRole(personPath, moduleToRemove.getModule(), studyName, null, etaUserVO.getUserName());
								removeMember(lname, ba);
								//Remove user from the application module
								lname = buildDNForRole(personPath, moduleToRemove.getModule(), null, null, etaUserVO.getUserName());
								removeMember(lname, ba);
							}
						}
					}
				}
			}
		}
		catch(InvalidNameException ine){
			log.error("An exception occured " + ine.getStackTrace());
			throw new ArkSystemException("A System error has occured");
		}
		catch (Exception e) {
			log.error("An Exception occured " + e.getMessage());
			throw new ArkSystemException("A System error has occured" );
			
		}	
	}
	
	/**
	 * <p>
	 * This method is a helper for the Update user details.
	 * It establishes the list of Module and roles that need to be added. 
	 * 1. Iterate through each module that is in the selected list</br>
	 * 2. Check if the selected module exists in the existing list of modules.</br>
	 * 3. If the selected list contains the module, then checks if there is a role that is not currently present and adds it to a list.
	 * 4. If the selected module does NOT exist(inverse of 3), it adds the module and the roles into a list. .</br>
	 * 
	 * </p>
	 * @param selectedModuleVOList
	 * @param existingModuleVOList
	 * @param moduleVOListToAdd
	 */
	private void processAddList(List<ModuleVO> selectedModuleVOList, List<ModuleVO> existingModuleVOList, List<ModuleVO> moduleVOListToAdd){
		
		
		for (ModuleVO selectedModuleVO : selectedModuleVOList) {
			
			List<RoleVO> rolesToAddList = new ArrayList<RoleVO>();
			
			//Check if the selected module is already there in the existing module collection
			if(existingModuleVOList.contains(selectedModuleVO)){
				
				log.info("This module is present");
				//Check if the role is present
				for(ModuleVO existingModuleVO: existingModuleVOList){
					
					/* If the selected module and the current existingModule match then process the roles*/
					if(selectedModuleVO.equals(existingModuleVO)){
					
						//Check if the role exists
						List<RoleVO> existingRoleVOList = existingModuleVO.getRole();
						List<RoleVO> selectedRoleVOList = selectedModuleVO.getRole();
						for(RoleVO selectedRoleVO: selectedRoleVOList){
							
							if(!existingRoleVOList.contains(selectedRoleVO)){
								log.info("This Role " + selectedRoleVO.getRole() + " is not present. Add to list");
								rolesToAddList.add(selectedRoleVO);
							}
						}
						//setModuleListToAdd(rolesToAddList,selectedModuleVO.getModule(),moduleVOListToAdd);
						if(rolesToAddList.size() > 0){
							ModuleVO module = new ModuleVO();
							module.setModule(selectedModuleVO.getModule());
							module.setRole(rolesToAddList);
							moduleVOListToAdd.add(module);	
						}
					}
					rolesToAddList = new ArrayList<RoleVO>();//Re-initialise the list
				}
				
			}else{
				log.info("The module is not present. So add the module and related roles");
				moduleVOListToAdd.add(selectedModuleVO);
				rolesToAddList = new ArrayList<RoleVO>();//Re-initialise the list
			}
		}
	}
	
	/**
	 * <p>
	 * This method is a helper for the Update user details.
	 * It establishes the list of Module and roles that need to be removed. 
	 * 1. Iterate through each module that is currently in the back-end.</br>
	 * 2. Check if the module exists in the currently selected list of modules.</br>
	 * 3. If the selected list does not contain the module then it marks the existing module and all roles linked to it to be removed.</br>
	 * 4. If the existing module does exist(inverse of 3), then checks if the existing roles are listed in the selected module's list of roles.</br>
	 * 5. If it does not find a role that exists then it marks that module and the specific role(s) to be removed.</br>
	 * </p>
	 * @param selectedModuleVOList
	 * @param existingModuleVOList
	 * @param moduleVOListToRemove
	 */
	private void processRemoveList(List<ModuleVO> selectedModuleVOList, List<ModuleVO> existingModuleVOList, List<ModuleVO> moduleVOListToRemove){
		
		for( ModuleVO existingModule : existingModuleVOList){
			
			/*If the existing module was not in the selected list of modules, then mark it for removal*/
			if(!selectedModuleVOList.contains(existingModule)){
			
				moduleVOListToRemove.add(existingModule);
			}
			else{
				//Selected List contains an existing module. Determine the Roles that need to be removed
				for(ModuleVO selectedModule : selectedModuleVOList){
					
					 if(selectedModule.equals(existingModule)){
						 
						 ModuleVO moduleVO = new ModuleVO();
						 moduleVO.setModule(existingModule.getModule());
						 List<RoleVO> roleList = new ArrayList<RoleVO>();
						 moduleVO.setRole(roleList);
						 
						 for(RoleVO existingRole : existingModule.getRole()){

							 if(!selectedModule.getRole().contains(existingRole)){
						
								 roleList.add(existingRole);
							 } 
						 }

						 if(roleList.size() > 0){
							 moduleVOListToRemove.add(moduleVO);
						 }
					 }
				}
			}
		}
		
	}
	
	/**
	 * 
	 */
	public void createStudy(StudyVO studyVO, String applicationName, ArkUserVO etaUserVO) throws ArkSystemException {

		try{

			String personPath = buildPersonDN(etaUserVO.getUserName());
			DirContextAdapter dirContextAdapter = new DirContextAdapter();
			
			String[] members = {personPath};
			mapStudyContext(dirContextAdapter, studyVO.getStudyName(), members);
			
			LdapName ldapName = new LdapName(getBaseGroupDn());
			ldapName.add( new Rdn("cn", applicationName));
			ldapName.add( new Rdn("cn",studyVO.getStudyName()));
			Name nameObj = ldapName;
			ldapTemplate.bind(nameObj,dirContextAdapter, null);
			
		}catch(InvalidNameException ine){
			log.error("An exception occured while creating a new study. " + ine.getMessage());
			StringBuffer error = new StringBuffer();
			error.append("A system error occured while creating the Study ");
			error.append(studyVO.getStudyName());
			throw new ArkSystemException( error.toString());
		}
	}
	
	private void mapStudyContext(DirContextOperations dirContext,String groupOrRoleName,Object[] members){
		dirContext.setAttributeValues("objectClass", new String[]{"groupOfNames","top"});
		dirContext.setAttributeValue("cn", groupOrRoleName);
		dirContext.setAttributeValues("member",members);
	}
	
	/**
	 * A helper to add a user as a MEMBER of a group/study
	 * @param studyVO
	 * @param applicationName
	 * @param roles
	 * @param etaUserVO
	 * @throws ArkSystemException
	 */
	private void addUserToStudyMembersList(StudyVO studyVO, String applicationName,ArkUserVO etaUserVO) throws ArkSystemException{
		try{

			String personPath = buildPersonDN(etaUserVO.getUserName());
			DirContextAdapter dirContextAdapter = new DirContextAdapter();
			String[] members = {personPath};
			mapStudyContext(dirContextAdapter, studyVO.getStudyName(), members);//Adds the user to the groupOfNames for the study
			
			//Add the user as a member of the study
			LdapName ldapName = new LdapName(getBaseGroupDn());
			ldapName.add(new Rdn(Constants.CN,applicationName));
			ldapName.add(new Rdn(Constants.CN, studyVO.getStudyName().trim()));//Set the CN value, i.e the module name ETA etc cn=ETA
			BasicAttribute ba = new BasicAttribute(Constants.MEMBER, personPath);
			ModificationItem item = new ModificationItem(DirContext.ADD_ATTRIBUTE, ba);
			ldapTemplate.modifyAttributes(ldapName, new ModificationItem[]{item});
			
		}catch(InvalidNameException ine){
			log.error("An exception occured while addding user to study. " + ine.getMessage());
			StringBuffer error = new StringBuffer();
			error.append("A system error occured while adding user to the Study ");
			error.append(studyVO.getStudyName());
			throw new ArkSystemException( error.toString());	
		}
		
	}
	
	
	private void addUserToRolesMemberList(StudyVO studyVO, String applicationName,  List<RoleVO> roles, ArkUserVO etaUserVO) throws ArkSystemException {
		
		try{
			
			String personPath = buildPersonDN(etaUserVO.getUserName());
			DirContextAdapter dirContextAdapter = new DirContextAdapter();
			//Associate the user to roles for this study
			BasicAttribute ba = new BasicAttribute(Constants.MEMBER, personPath);
			for(RoleVO role: roles){
				String[] roleMembers = {personPath};
				mapStudyContext(dirContextAdapter,role.getRole(),roleMembers);
				LdapName ldapName = new LdapName(getBaseGroupDn());
				ldapName.add( new Rdn("cn", applicationName));
				ldapName.add( new Rdn("cn",studyVO.getStudyName()));
				ldapName.add( new Rdn("cn", role.getRole()));
				ModificationItem userItem = new ModificationItem(DirContext.ADD_ATTRIBUTE, ba);
				ldapTemplate.modifyAttributes(ldapName, new ModificationItem[]{userItem});
			}
		}catch(InvalidNameException ine){
			log.error("An exception occured while addding user to study. " + ine.getMessage());
			StringBuffer error = new StringBuffer();
			error.append("A system error occured while adding user to the Study ");
			error.append(studyVO.getStudyName());
			throw new ArkSystemException( error.toString());	
		}
	}
	
	/**
	 * The method will add the user as a member of the given Study under the application.
	 * Will associate the user to the various roles assigned for this study.
	 */
	public void addUserToStudy(StudyVO studyVO, String applicationName, List<RoleVO> roles, ArkUserVO etaUserVO) throws ArkSystemException{

			addUserToStudyMembersList(studyVO, applicationName,etaUserVO);
			addUserToRolesMemberList(studyVO, applicationName, roles, etaUserVO);
	}
	
	/**
	 * The method that removes a user from the system. This includes All applications/Modules and its related studies and roles.
	 * @throws UnAuthorizedOperation 
	 * 
	 */
	public void delete(ArkUserVO etaUserVO) throws ArkSystemException, UnAuthorizedOperation{
		
		try{
			/*Logged in user*/
			SecurityManager securityManager =  ThreadContext.getSecurityManager();
			Subject loggedInSubject = SecurityUtils.getSubject();
			boolean isSuperAdmin = securityManager.hasRole(loggedInSubject.getPrincipals(), RoleConstants.ARK_SUPER_ADMIN);
			if(isSuperAdmin){
				etaUserVO = getUser(etaUserVO.getUserName());
				
				LdapName ldapName = new LdapName(baseDC);
				ldapName.add( new Rdn("ou","people"));
				ldapName.add(new Rdn("cn", etaUserVO.getUserName()));
				String personDn = ldapName.toString();
				BasicAttribute ba = new BasicAttribute(Constants.MEMBER, personDn);
				
				List<ModuleVO> allModules = getModules(false);
				//Get a list of groups the user is a member including nested sub groups
				for(ModuleVO module : allModules){
					getUserModuleMembership(etaUserVO, module.getModule());
				}
				
				//List out the Modules, Studies and their roles here
				for(ModuleVO modules: etaUserVO.getModules()){

					for(StudyVO studyVO : modules.getStudies()){
					
						for(RoleVO roleVO:  studyVO.getRoles()){
						
							LdapName lname = buildDNForRole(personDn, modules.getModule(), studyVO.getStudyName(), roleVO.getRole(), etaUserVO.getUserName());
							removeMember(lname, ba);
						
						}
						LdapName lname = buildDNForRole(personDn, modules.getModule(), studyVO.getStudyName(), null, etaUserVO.getUserName());
						removeMember(lname, ba);
					}
					LdapName lname = buildDNForRole(personDn, modules.getModule(), null, null, etaUserVO.getUserName());
					removeMember(lname, ba);
				}
				
				/*Remove the user from People*/
				ldapName = new LdapName(getBasePeopleDn());
				ldapName.add(new Rdn(Constants.CN,etaUserVO.getUserName()));
				ldapTemplate.unbind(ldapName);
				//TODO Add Audit Log for this event
			}else{
				log.warn("The logged in subject does not have the privileges to delete this user");
				//TODO Audit Log
				throw new UnAuthorizedOperation("The logged in subject does not have the privileges to delete this user.");
			}
			
		}catch(InvalidNameException ine){
			log.error("An Exception has occured : " + ine.getStackTrace());
			throw new ArkSystemException("A system exception has occured");
		}catch(Exception ex){
			log.error("An Exception has occured : " + ex.getStackTrace());
			throw new ArkSystemException("A system exception has occured");
		}
		
	}
	
	/**
	 * A helper to remove a user member from a group. A valid LDAP is only required here, the helper will remove it from
	 * a group which can represent a Module/Application in business terms, A study  or a Role.
	 * @param ldapName
	 * @param ba
	 */
	private void removeMember(LdapName ldapName, BasicAttribute ba){
		ModificationItem userItem = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, ba);
		ldapTemplate.modifyAttributes(ldapName, new ModificationItem[]{userItem});
	}
	
	private LdapName buildDNForRole(String personDN, String moduleName, String studyName, String roleName, String userName ) throws InvalidNameException {
		LdapName ldapName = new LdapName(getBaseGroupDn());
		ldapName.add(new Rdn(Constants.CN,moduleName));
		if(studyName != null && studyName.length() > 0){
			ldapName.add(new Rdn(Constants.CN, studyName));	
		}
		if(roleName != null && roleName.length() > 0){
			ldapName.add(new Rdn(Constants.CN, roleName));	
		}
		return ldapName;
	}
	
	/**
	 * The method that will get a list of roles for a given module and study name and set it on the
	 * ArkUserVO instance.
	 * @param etaUserVO
	 * @param moduleName
	 * @param studyVO
	 * @throws ArkSystemException
	 */
	private void getUserStudyRole(ArkUserVO etaUserVO, String moduleName, StudyVO studyVO) throws ArkSystemException {
		
		String groupBase = "ou=groups";
		AndFilter moduleFilter = new AndFilter();
		moduleFilter.and(new EqualsFilter("objectClass","groupOfNames"));
		String personDn="";
		try {
			
			LdapName ldapName = new LdapName(baseDC);
			ldapName.add( new Rdn("ou","people"));
			ldapName.add(new Rdn("cn", etaUserVO.getUserName()));
			personDn = ldapName.toString();
			moduleFilter.and(new EqualsFilter("member",personDn));
			
			LdapName dn;
			dn = new LdapName(groupBase);
			dn.add(new Rdn("cn",moduleName));
			dn.add(new Rdn("cn", studyVO.getStudyName()));	
			List<?> userStudyGroupRoles =  ldapTemplate.search(dn,moduleFilter.encode(),SearchControls.ONELEVEL_SCOPE, new AttributesMapper(){
				
			public Object mapFromAttributes(Attributes attrs){
						try {
							Object returnObject = attrs.get("cn").get();
							return returnObject;
						} catch (NamingException e) {
						
							e.printStackTrace();
						}
						return attrs;
					}
			});
			
			if(userStudyGroupRoles != null && userStudyGroupRoles.size() > 0){
				for(Object object : userStudyGroupRoles){
					String roleName = (String)object.toString();
					RoleVO roleVO= new RoleVO();
					roleVO.setRole(roleName);
					studyVO.getRoles().add(roleVO);
				}
			}
		}catch(InvalidNameException e){
			e.printStackTrace();
			log.error("A system error has occured" + e.getStackTrace());
			throw new ArkSystemException("A system error has occured");
		}catch(Exception ee){
				StringBuffer sb = new StringBuffer();
				sb.append("The study");
				sb.append(etaUserVO.getStudyVO().getStudyName());
				sb.append(" in module :");
				sb.append(moduleName);
				sb.append(" stack trace ");
				sb.append(ee.getStackTrace());
				log.info(sb.toString());
				throw new ArkSystemException("A system exception has occured");
		}		
	}
	
	/**
	 * The method that builds a list of all studies and associated roles for a given application/module name.
	 * The ArkUserVO instance's List<ModuleVO> modules is set with the result.
	 * @param etaUserVO
	 * @param moduleName
	 * @throws ArkSystemException 
	 */
	private void getUserModuleMembership(ArkUserVO etaUserVO, String moduleName) throws ArkSystemException{
		
		String groupBase = "ou=groups";
		AndFilter moduleFilter = new AndFilter();
		moduleFilter.and(new EqualsFilter("objectClass","groupOfNames"));
		String personDn="";
		ModuleVO moduleVO = new ModuleVO();
		try {

			personDn = buildPersonDN(etaUserVO.getUserName());
			moduleFilter.and(new EqualsFilter("member",personDn));
			
			LdapName dn;
			dn = new LdapName(groupBase);
			dn.add(new Rdn("cn",moduleName.trim()));
				
			List<?> userStudyGroups =  ldapTemplate.search(dn,moduleFilter.encode(),SearchControls.ONELEVEL_SCOPE, new AttributesMapper(){
				
			public Object mapFromAttributes(Attributes attrs){
						try {
							Object returnObject = attrs.get("cn").get();
							return returnObject;
						} catch (NamingException e) {
						
							e.printStackTrace();
						}
						return attrs;
					}
			});
						
			if(userStudyGroups != null && userStudyGroups.size() > 0){
				moduleVO.setModule(moduleName);
				etaUserVO.getModules().add(moduleVO);
				
				StudyVO studyVO = new StudyVO();
				moduleVO.getStudies().add(studyVO);
				for(Object object : userStudyGroups){
					String studyName = (String)object.toString();
					studyVO.setStudyName(studyName);
					getUserStudyRole(etaUserVO,moduleName,studyVO);
				}
			}
			
		}catch(InvalidNameException e){
			e.printStackTrace();
			log.error("A system error has occured" + e.getStackTrace());
			throw new ArkSystemException("A system error has occured");
		}catch(Exception ee){
				StringBuffer sb = new StringBuffer();
				sb.append("The study");
				sb.append(etaUserVO.getStudyVO().getStudyName());
				sb.append(" in module :");
				sb.append(moduleName);
				sb.append(" stack trace ");
				sb.append(ee.getStackTrace());
				log.info(sb.toString());
				throw new ArkSystemException("A system exception has occured");
		}		
	}
	
	
	/**
	 * Creates a study in LDAP for the given set of Applications. It also adds the pre-defined roles for each application below the study as a sub-group.
	 * As part of creating the structure it adds a default user to the Study and Roles.
	 * 
	 */
	public void createStudy(String studyName, Set<String> applications,String userName) throws ArkSystemException, EntityExistsException {
		log.debug("Inside createStudy(studyName, applications, userName)");
		try{
			
			String personPath = LdapUtility.buildPersonDN(userName,baseDC);
			
			for (String applicationName : applications) {

				String systemApplicationName = UIHelper.getSystemModuleName(applicationName);
				//Create the study if it does not exist
				DirContextAdapter dirContextAdapter = new DirContextAdapter();
				String[] members = {personPath};
				LdapUtility.mapToGroupContext(dirContextAdapter, studyName, members);
				
				LdapName ldapName = new LdapName(getBaseGroupDn());
				ldapName.add( new Rdn("cn",systemApplicationName));
				ldapName.add( new Rdn("cn",studyName));
				Name nameObj = ldapName;
				ldapTemplate.bind(nameObj,dirContextAdapter, null);
				List<String> applicationRolesList = getModuleRoles(systemApplicationName);
				for (String role : applicationRolesList) {
					
					String systemRoleName = UIHelper.getSystemRoleName(role);
					LdapUtility.mapToGroupContext(dirContextAdapter,systemRoleName , members);
					ldapName = new LdapName(getBaseGroupDn());
					ldapName.add( new Rdn("cn",systemApplicationName));
					ldapName.add( new Rdn("cn",studyName));
					ldapName.add(new Rdn("cn", systemRoleName));
					nameObj=ldapName;
					ldapTemplate.bind(nameObj,dirContextAdapter, null);
				}
			}

		}catch(org.springframework.ldap.NameAlreadyBoundException nabe){
			log.error("A Study with that name is present in the system.  " + nabe.getMessage());
			StringBuffer error = new StringBuffer();
			error.append(studyName  + " already exists in the system.");
			error.append(studyName);
			throw new EntityExistsException( error.toString());
		}
		catch(InvalidNameException ine){
			log.error("An exception occured while creating a new study. " + ine.getMessage());
			StringBuffer error = new StringBuffer();
			error.append("A system error occured while creating the Study ");
			error.append(studyName);
			throw new ArkSystemException( error.toString());
		}catch(Exception ex){
			log.error("An exception occured while creating a new study. " + ex.getMessage());
			StringBuffer error = new StringBuffer();
			error.append("A system error occured while creating the Study ");
			error.append(studyName);
			throw new ArkSystemException( error.toString());
		}
		
	}
	
	/**
	 * The method will return a list of applications/modules in which the study group(studyName)
	 * exists.
	 * @param studyNameCN
	 * @return
	 * @throws InvalidNameException 
	 */
	public Set<String> getModulesLinkedToStudy(String studyNameCN) throws ArkSystemException{
		
		Set<String> linkedApplications = new HashSet<String>();
		try{
			
			/* Get a List of All System Modules and their available roles. */
			List<ModuleVO> listOfAllModules = getModules(false); 
			String moduleName = "";
			String groupBase = "ou=groups";
			AndFilter moduleFilter = new AndFilter();
			moduleFilter.and(new EqualsFilter("objectClass","groupOfNames"));

			for(ModuleVO module: listOfAllModules)
			{
				//Fetches a list of roles that have been assigned to the user indicating that the user is a member of the GROUP/Application and the various roles.
				moduleName = module.getModule();
				LdapName dn = new LdapName(groupBase);
				dn.add(new Rdn("cn",moduleName));
				dn.add(new Rdn("cn",studyNameCN));
				try{
					
					List<?> studyGroup =  ldapTemplate.search(dn,moduleFilter.encode(),SearchControls.OBJECT_SCOPE, new AttributesMapper()
					{
						
						public Object mapFromAttributes(Attributes attrs){
							try {
									Object returnObject = attrs.get("cn").get();
									return returnObject;
							} catch (NamingException e) {
								
									e.printStackTrace();
							}
								return attrs;
						}
					});
					
					if(studyGroup.size() > 0){
						linkedApplications.add(moduleName);
					}
				
				}catch(Exception e){
					log.error(e.getMessage());
				}
					
			}
			
		}catch(InvalidNameException ine){
			log.error(ine.getMessage());
			throw new ArkSystemException("Exception occured while getting modules linked to the study");
		}

		return linkedApplications;
	}
	
	public List<ModuleVO> getModulesAndRolesForStudy(String studyNameCn) throws ArkSystemException{
		
		List<ModuleVO> linkedModules = new ArrayList<ModuleVO>();
		try{
			/* Get a List of All System Modules and their available roles. */
			List<ModuleVO> listOfAllModules = getModules(false); 
			String moduleName = "";
			String groupBase = "ou=groups";
			AndFilter moduleFilter = new AndFilter();
			moduleFilter.and(new EqualsFilter("objectClass","groupOfNames"));

			for(ModuleVO module: listOfAllModules)
			{
				//Fetches a list of roles that have been assigned to the user indicating that the user is a member of the GROUP/Application and the various roles.
				moduleName = module.getModule();
				LdapName dn = new LdapName(groupBase);
				dn.add(new Rdn("cn",moduleName));
				dn.add(new Rdn("cn",studyNameCn));
				try{
					
					List<?> studyGroup =  ldapTemplate.search(dn,moduleFilter.encode(),SearchControls.OBJECT_SCOPE, new AttributesMapper()
					{
						
						public Object mapFromAttributes(Attributes attrs){
							try {
									Object returnObject = attrs.get("cn").get();
									return returnObject;
							} catch (NamingException e) {
								
									e.printStackTrace();
							}
								return attrs;
						}
					});
					
					if(studyGroup.size() > 0){
						linkedModules.add(module);
					}
				
				}catch(Exception e){
					log.error(e.getMessage());
				}
					
			}

		}catch(InvalidNameException ine){
			log.error(ine.getMessage());
			throw new ArkSystemException("Exception occured while getting modules linked to the study");
		}
		
		return linkedModules;
		
	}
	
	public Set<String> getModulesLinkedToStudy(String studyNameCN, boolean isForDisplay) throws ArkSystemException{
		Set<String> linkedApplications  = getModulesLinkedToStudy(studyNameCN);
		Set<String> linkedAppsForDisplay = new HashSet<String>();
		if(isForDisplay){
			for (String moduleName : linkedApplications) {
					moduleName = UIHelper.getDisplayModuleName(moduleName);
					linkedAppsForDisplay.add(moduleName);
			}
		}
		
		return linkedAppsForDisplay;
	}
	
	/**
	 * Note this is a function that disassociates a study from an application. this is called as part of an update to a study definition.
	 * As such the Ark system will not allow a user to Remove a study completely. The study will need to be moved into an archived state.
	 * This function here allows a user to update the study association with applications. Useful if an error occured when associating applications to study.
	 *  
	 */
	private void delinkStudyFromApp(String studyName, Set<String > appsToDelinkFrom ) throws EntityCannotBeRemoved, ArkSystemException{
		
		try{
			for(String appToDelink : appsToDelinkFrom){
				
				String[] membersOfStudy = getStudyMembers(studyName,UIHelper.getSystemModuleName(appToDelink));
				//Check if there are more than 1 member in the study, if not then process it.
				if(membersOfStudy != null && membersOfStudy.length <= 1){
					
					//Get the Roles linked to each application
					List<String> applicationRolesList = getModuleRoles(UIHelper.getSystemModuleName(appToDelink));
					//Iterate each role and unbind it.
					for (String studyRole : applicationRolesList) {
						LdapName ldapName = new LdapName(getBaseGroupDn());
						ldapName.add(new Rdn(Constants.CN, UIHelper.getSystemModuleName(appToDelink)));
						ldapName.add(new Rdn(Constants.CN,studyName));
						ldapName.add(new Rdn(Constants.CN,UIHelper.getSystemRoleName(studyRole)));
						ldapTemplate.unbind(ldapName);
					}
					//TODO: Unbind the Study from Sites if the study was linked to any Site.After the Site use case is completed.
					//Unbind the Study from the Application
					LdapName ldapName = new LdapName(getBaseGroupDn());
					ldapName.add(new Rdn(Constants.CN, UIHelper.getSystemModuleName(appToDelink)));
					ldapName.add(new Rdn(Constants.CN,studyName));
					ldapTemplate.unbind(ldapName);
					
				}else{
					//throw a business exception that the study has currently users linked to it and that it cannot be removed.
					log.warn("\nThe study cannot be removed. There are users linked to the study");
					throw new EntityCannotBeRemoved("The given Study cannot be removed from Application:" + " "
							+ appToDelink + ".There are users/subjects linked to this study.");
				}
				
			}
		}catch(InvalidNameException ine){
			log.error("An exception occured while creating a new study. " + ine.getMessage());
			StringBuffer error = new StringBuffer();
			error.append("A system error occured while creating the Study ");
			error.append(studyName);
			throw new ArkSystemException( error.toString());
		}
	}
	/**
	 * Add's the study to one or more of the applications if the study is not in that list of selected application.
	 * 
	 * @param studyName
	 * @param selectedApplication
	 */
	public void updateStudyApplication(String studyName,Set<String> selectedApplicationList, String userName) throws ArkSystemException, 
																											EntityExistsException,EntityCannotBeRemoved{

		//Get the given study's list of applications it is currently linked to
		Set<String> currentAppsLinked = getModulesLinkedToStudy(studyName, true);
		
		Set<String> newAppsToLink = new HashSet<String>();//Will contain the applications the study must be linked to
		
		//Determine the applications the study should be linked to
		for (String selectedApp : selectedApplicationList) {
			if(!currentAppsLinked.contains(selectedApp)){
				newAppsToLink.add(selectedApp);
			}
		}
		//Determine the applications the study must be removed from
		Set<String> appsToDelink = new HashSet<String>();
		for(String currentApplication : currentAppsLinked){
			if(!selectedApplicationList.contains(currentApplication)){
				appsToDelink.add(currentApplication);
			}
		}
		
		if(appsToDelink != null && appsToDelink.size() > 0){
			delinkStudyFromApp(studyName,appsToDelink);
		}
		//Associate the study with the list of applications that must be linked to
		if(newAppsToLink.size() > 0){
			createStudy(studyName,newAppsToLink,userName);	
		}
	}
	
	//Returns the Member attribute value of a group
	private static class GroupContextMapper implements ContextMapper {
		public Object mapFromContext(Object ctx) {
			DirContextAdapter context = (DirContextAdapter) ctx;
			String[] members = context.getStringAttributes("member");
			return members;
		}
	}
	/**
	 * Returns a list of users who are members of the study group.
	 * Study here represents a group in LDAP
	 * Member is a LDAP member attribute.
	 * @param studyName
	 * @return
	 * @throws InvalidNameException 
	 */
	public String[] getStudyMembers(String studyName, String appName) throws InvalidNameException{
		
		LdapName ldapName = new LdapName(getBaseGroupDn());
		ldapName.add(new Rdn(Constants.CN,appName));
		ldapName.add(new Rdn(Constants.CN, studyName));
		Name nameObj = ldapName;
		String[] listOfMembers = (String[])ldapTemplate.lookup(nameObj, new GroupContextMapper());
		
		return listOfMembers;
	}

	public List<SiteVO> getSite(SiteVO siteVo){
		
		List<SiteVO> siteList = new ArrayList<SiteVO>();
		//Within the Site group ou=site find a site with the given name. This lookup is a very basic one and is not linked to a Study in context.
		
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "groupOfNames"));//We will need a custom objectClass of type Site
		
		
		if(StringUtils.hasText(siteVo.getSiteName())){
			filter.and(new EqualsFilter(Constants.CN,siteVo.getSiteName()));
		}
		
		//We are not looking up based on description so it is excluded
		//If other search attributes like address is needed then either we add these attributes to LDAp or maintain it in database and look up a site from backend and
		//provide the address details.
		
		return ldapTemplate.search(getBaseSiteDn(), filter.encode(),new LiteSiteContextMapper());
		
	}
	
	public void updateSite(SiteVO siteVo)throws ArkSystemException{
		
		try{
			LdapName ldapName = new LdapName(getBaseSiteDn());
			ldapName.add(new Rdn("cn", siteVo.getSiteName()));		
			//There is only one attribute we want to update
			BasicAttribute siteDescription = new BasicAttribute("description", siteVo.getSiteDescription());
			ModificationItem itemDescription = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, siteDescription);
			ldapTemplate.modifyAttributes(ldapName, new ModificationItem[] {itemDescription });
			
		}catch(InvalidNameException ine){
			log.error("Exception occured when updating the Site on LDAP " + ine.getStackTrace());
			throw new ArkSystemException("A System error has occured while trying to update the Site details.");
		}
	}
}
