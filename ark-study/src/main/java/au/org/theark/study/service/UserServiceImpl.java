package au.org.theark.study.service;

/**
 * Add Copyright header
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.naming.InvalidNameException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.dao.IArkAuthorisation;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.PersonNotFoundException;
import au.org.theark.core.exception.UnAuthorizedOperation;
import au.org.theark.core.exception.UserNameExistsException;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.ArkUserRole;
import au.org.theark.core.model.study.entity.AuditHistory;
import au.org.theark.core.model.study.entity.EtaUser;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.study.model.dao.ILdapUserDao;
import au.org.theark.study.model.dao.IUserDao;


@Transactional
@Service("userService")
public class UserServiceImpl implements IUserService {

	

	final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	
	private IStudyService studyService;//To gain access to Study Schema
	private IArkCommonService arkCommonService;
	
	private IArkAuthorisation arkAuthorisationService;
	
	/* DAO to access database */
	private IUserDao userDAO;
	
	/* A DAO to access the LDAP */
	private ILdapUserDao iLdapUserDao;
	
	public ILdapUserDao getiLdapPersonDao() {
		return iLdapUserDao;
	}
	
	@Autowired
	public void setiLdapPersonDao(ILdapUserDao iLdapPersonDao) {
		this.iLdapUserDao = iLdapPersonDao;
	}

	@Autowired
	public void setUserDAO(IUserDao userDAO) {
		this.userDAO = userDAO;
	}
	
	public IUserDao getUserDAO() {
		return userDAO;
	}
	
	public IArkCommonService getArkCommonService() {
		return arkCommonService;
	}

	@Autowired
	public void setArkCommonService(IArkCommonService arkCommonService) {
		this.arkCommonService = arkCommonService;
	}
	
	@Autowired
	public void setStudyService(IStudyService studyService) {
		this.studyService = studyService;
	}
	
	
	public IArkAuthorisation getArkAuthorisationService() {
		return arkAuthorisationService;
	}

	@Autowired
	public void setArkAuthorisationService(IArkAuthorisation arkAuthorisationService) {
		this.arkAuthorisationService = arkAuthorisationService;
	}

	
	public Person createPerson(Person personEntity){
		
		return userDAO.createPerson(personEntity);
	}
	/**
	 * Inserts a user into the database.
	 */
	public void createUser(String userName, String password) {
		EtaUser user = new EtaUser();
		user.setId(new Long("101"));
		user.setUserName(userName);
		user.setUserPassword(new Sha256Hash(password).toHex());
		userDAO.createUser(user);
	}

	/**
	 * Use Ldap to get the user
	 */
	public EtaUser getUser(String userName) throws ArkSystemException{
		
		log.info("\n -- getUser() invoked");
		EtaUser user = null;
		ArkUserVO userVO= iLdapUserDao.getUser(userName);
		user = new EtaUser();
		user.setUserName(userVO.getUserName());
		user.setUserPassword(userVO.getPassword());
		return user;
	}
	
	public List<ModuleVO> getUserRoles(ArkUserVO etaUserVO, String studyName) throws ArkSystemException{
		return iLdapUserDao.getUserRoles(etaUserVO, studyName);
	}

	/**
	 * This method returns the user from a user table.This is not the same as LDAP
	 * @param id
	 * @return
	 */
	public EtaUser getUser(Long id) {
		return userDAO.getUser(id);
	}

	public void createLdapUser(ArkUserVO userVO) throws  InvalidNameException,UserNameExistsException,Exception {
		log.info("UserServiceImpl.createLdapUser() ");
		iLdapUserDao.create(userVO);		
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created User (in LDAP) " + userVO.getUserName());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_USER);
		arkCommonService.createAuditHistory(ah);
	}

	
	public List<ModuleVO> getModules(String studyName) throws ArkSystemException{
		return iLdapUserDao.getModulesAndRolesForStudy(studyName);
	}	
	
	public List<ModuleVO> getModules(boolean isForDisplay) throws ArkSystemException {
		
		return iLdapUserDao.getModules(isForDisplay);
	}

	public List<String> getModuleRoles(String moduleId) throws ArkSystemException {
		return iLdapUserDao.getModuleRoles(moduleId);
	}
	
	public List<ArkUserVO> searchUser(ArkUserVO user) throws  ArkSystemException{
		return iLdapUserDao.searchUser(user);
	}
	
	public void updateLdapUser(ArkUserVO userVO) throws ArkSystemException{
		iLdapUserDao.update(userVO);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated User (in LDAP) " + userVO.getUserName());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_USER);
		arkCommonService.createAuditHistory(ah);
	}
	
	public List<Person> searchPerson(Person person) throws PersonNotFoundException{
		return userDAO.searchPerson(person);
	}
	
	public void deleteLdapUser(ArkUserVO userVO) throws UnAuthorizedOperation, ArkSystemException{
		 iLdapUserDao.delete(userVO);
		 
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted User (in LDAP) " + userVO.getUserName());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_USER);
		arkCommonService.createAuditHistory(ah);
	}

	public ArkUserVO getCurrentUser(String username) throws ArkSystemException{
		return iLdapUserDao.getUser(username);
	}
	
	public Collection<ModuleVO> getModulesLinkedToStudy(String studyNameCN, boolean isForDisplay) throws ArkSystemException{
		
		Set<String> modules = iLdapUserDao.getModulesLinkedToStudy(studyNameCN, isForDisplay);
		Collection<ModuleVO> modulesLinkedToStudy = new ArrayList<ModuleVO>();
		for (String moduleName : modules) {
			ModuleVO moduleVo = new ModuleVO();
			moduleVo.setModule(moduleName);
			modulesLinkedToStudy.add(moduleVo);
		}
		return modulesLinkedToStudy;
	}

	public List<ModuleVO> getModulesAndRolesForStudy(String studyNameCn)
			throws ArkSystemException {
		return iLdapUserDao.getModulesAndRolesForStudy(studyNameCn);
	}
	
	/**
	 * The interface to look up all ArkUser's linked to a particular Study in Context.The implementation should pull 
	 * the users from the backend and then get the personal demographic information from LDAP from the people group.
	 * @param arkUserVO
	 * @return
	 * @throws ArkSystemException
	 */
	public List<ArkUserVO> lookupArkUser(ArkUserVO arkUserVO, Study study) throws ArkSystemException{
		//Call the Hibernate service/dao to get the list of users linked to the study
		//If we want all users in ArkUser Table then the person must be a super admin which will get all the users from ArkUser Table across studies
		Collection<ArkUser> usersLinkedToStudy = studyService.lookupArkUser(study);
		List<ArkUserVO> listOfArkUserVO = new ArrayList<ArkUserVO>();
		//For each user in ArkUser Table fetch the details from LDAP
		for (ArkUser arkUser : usersLinkedToStudy) {
			ArkUserVO userVOFromLdap = iLdapUserDao.getUser(arkUser.getLdapUserName()); 
			listOfArkUserVO.add(userVOFromLdap);
		}
		return listOfArkUserVO;
	}
	
	public boolean isArkUserPresent(String userName){
		return userDAO.isArkUserPresent(userName);
	}
	
	/**
	 * This is a new interface that persists the user into ArkUsers group in LDAP
	 * @param arkUserVO
	 * @throws UserNameExistsException
	 * @throws ArkSystemException
	 */
	public void createArkUser(ArkUserVO arkUserVO) throws UserNameExistsException, ArkSystemException{
		
		//Create user in LDAP
		try{
			iLdapUserDao.createArkUser(arkUserVO);
			//Setting the Username and Study on the ArkUser Hibernate entity from the Value Object that would reflect what it is on LDAP
			
			Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			arkUserVO.getArkUserEntity().setStudy(arkCommonService.getStudy(sessionStudyId));
			arkUserVO.getArkUserEntity().setLdapUserName(arkUserVO.getUserName());
			//Create the user in Ark Database as well and persist and update and roles user was assigned
			arkAuthorisationService.createArkUser(arkUserVO);

			AuditHistory ah = new AuditHistory();
			ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
			ah.setComment("Created User (in LDAP) " + arkUserVO.getUserName());
			ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_USER);
			arkCommonService.createAuditHistory(ah);
		}catch(UserNameExistsException personExistsException){
			throw personExistsException;
		}
		
	}
	
	public void updateArkUser(ArkUserVO arkUserVO) throws ArkSystemException, EntityNotFoundException{
		
		iLdapUserDao.updateArkUser(arkUserVO);//Update the LDAP entry
		arkAuthorisationService.updateArkUser(arkUserVO);
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated Ark User (in LDAP) " + arkUserVO.getUserName());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_USER);
		arkCommonService.createAuditHistory(ah);
	}
	
	/**
	 * 
	 * @param arkUserVO
	 * @throws ArkSystemException
	 * @throws EntityNotFoundException 
	 */
	public ArkUserVO lookupArkUser(String arkLdapUserName,Study study) throws ArkSystemException{
		//Fetch the Ark User details from LDAP
		ArkUserVO arkUserVO = iLdapUserDao.lookupArkUser(arkLdapUserName);
		arkUserVO.setStudy(study);
		
		//Fetch ArkUserRole and ArkUser Details from Backend using the arkLdapUserName
		List<ArkUserRole> arkUserRoleList = new ArrayList<ArkUserRole>();
		try {
			ArkUser arkUserEntity = arkAuthorisationService.getArkUser(arkLdapUserName);
			arkUserVO.setArkUserEntity(arkUserEntity);
			arkUserRoleList = arkAuthorisationService.getArkUserLinkedModuleAndRoles(arkUserVO);
			arkUserVO.setArkUserPresentInDatabase(true);
			arkUserVO.setArkUserRoleList(arkUserRoleList);
		} catch (EntityNotFoundException e) {
			log.debug("The specified User is not in the Ark Database");
		}
		
		return arkUserVO;
	}

	public void deleteArkUser(ArkUserVO arkUserVO) throws ArkSystemException, EntityNotFoundException{
		//Note: Only Remove the Ark User from database not in LDAP
		arkAuthorisationService.deleteArkUser(arkUserVO);
		
	}	
	
}
