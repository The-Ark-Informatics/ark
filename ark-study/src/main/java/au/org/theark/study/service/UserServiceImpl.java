package au.org.theark.study.service;

import java.util.List;

import javax.naming.InvalidNameException;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.PersonNotFoundException;
import au.org.theark.core.exception.UnAuthorizedOperation;
import au.org.theark.core.exception.UserNameExistsException;
import au.org.theark.core.vo.EtaUserVO;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.study.model.dao.ILdapUserDao;
import au.org.theark.study.model.dao.IUserDao;
import au.org.theark.study.model.entity.EtaUser;
import au.org.theark.study.model.entity.Person;


@Transactional
@Service("userService")
public class UserServiceImpl implements IUserService {

	final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
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
		EtaUserVO userVO= iLdapUserDao.getUser(userName);
		user = new EtaUser();
		user.setUserName(userVO.getUserName());
		user.setUserPassword(userVO.getPassword());
		return user;
	}

	public EtaUserVO getUserRole(String username) throws ArkSystemException{
		EtaUserVO userVO = null;
		userVO = iLdapUserDao.getUserRole(username);
		return userVO;
	}
	
	public void getUserRole(EtaUserVO etaUserVO, String moduleName) throws ArkSystemException{
		iLdapUserDao.getUserRole(etaUserVO, moduleName);
	}
	
	public void getUserRole(EtaUserVO etaUserVO, List<ModuleVO> listOfAllModules) throws ArkSystemException{
		iLdapUserDao.getUserRole(etaUserVO,listOfAllModules);
	}
	
	public List<ModuleVO> getUserRoles(EtaUserVO etaUserVO, String studyName) throws ArkSystemException{
		return iLdapUserDao.getUserRoles(etaUserVO, studyName);
	}

	public EtaUser getCurrentUser() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This method returns the user from a user table.This is not the same as LDAP
	 * @param id
	 * @return
	 */
	public EtaUser getUser(Long id) {
		return userDAO.getUser(id);
	}

	public void createLdapUser(EtaUserVO userVO) throws  InvalidNameException,UserNameExistsException,Exception {
		log.info("UserServiceImpl.createLdapUser() ");
		iLdapUserDao.create(userVO);		
	}

	public List<ModuleVO> getModules(boolean isForDisplay) throws ArkSystemException {
		
		return iLdapUserDao.getModules(isForDisplay);
	}

	public List<String> getModuleRoles(String moduleId) throws ArkSystemException {
		return iLdapUserDao.getModuleRoles(moduleId);
	}
	
	public List<EtaUserVO> searchUser(EtaUserVO user) throws  ArkSystemException{
		return iLdapUserDao.searchUser(user);
	}
	
	public void updateLdapUser(EtaUserVO userVO) throws ArkSystemException{
		iLdapUserDao.update(userVO);
	}
	
	public List<Person> searchPerson(Person person) throws PersonNotFoundException{
		return userDAO.searchPerson(person);
	}
	
	public void deleteLdapUser(EtaUserVO etaUserVO) throws UnAuthorizedOperation, ArkSystemException{
		 iLdapUserDao.delete(etaUserVO);
	}

}
