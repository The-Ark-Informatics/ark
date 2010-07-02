package au.org.theark.study.service;

import java.util.List;

import javax.naming.InvalidNameException;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.PersonNotFoundException;
import au.org.theark.core.exception.UnAuthorizedOperation;
import au.org.theark.core.exception.UserNameExistsException;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.study.model.entity.EtaUser;
import au.org.theark.study.model.entity.Person;

public interface IUserService {
	
	public EtaUser getUser(String userName) throws ArkSystemException;
	
	public List<ModuleVO> getUserRoles(ArkUserVO etaUserVO, String studyName) throws ArkSystemException;
	
	public Person createPerson(Person personEntity);
	
	public void createUser(String userName, String password);
	/**
	 * Interface to create/add a User into the LDAP system and also associate the user to
	 * applications and roles.
	 * @param ArkUserVO etaUserVO
	 * @throws InvalidNameException
	 * @throws UserNameExistsException
	 * @throws Exception
	 */
	public void createLdapUser(ArkUserVO etaUserVO) throws ArkSystemException, UserNameExistsException, Exception;
	/**
	 * Interface to update a person's details including their password. It does not at present
	 * modify or update the Groups and roles. The modification will only apply to Person attributes.
	 * 
	 * @param ArkUserVO etaUserVO
	 * @throws InvalidNameException
	 */
	public void updateLdapUser(ArkUserVO etaUserVO) throws ArkSystemException;
	
	public void deleteLdapUser(ArkUserVO etaUserVO) throws UnAuthorizedOperation, ArkSystemException;

	public List<ModuleVO> getModules(boolean isForDisplay) throws ArkSystemException;
	
	public List<String> getModuleRoles(String moduleId) throws ArkSystemException;
	/**
	 * User this method to lookup a user in LDAP. The user object acts as a filter condition
	 * that is applied for the search.
	 * @param ArkUserVO
	 * @return List<ArkUserVO> 
	 * @throws InvalidNameException
	 */
	public List<ArkUserVO> searchUser(ArkUserVO user) throws ArkSystemException;
	/**
	 * Look up persons based on the search criteria and return a list of people who 
	 * match the criteria
	 * If a match was not found throw a business exception to indicate the same.
	 * @param personVO
	 * @return
	 */
	public List<Person> searchPerson(Person personVO) throws PersonNotFoundException;
	
	
	public ArkUserVO getCurrentUser(String username) throws ArkSystemException;	

}
