package au.org.theark.study.service;

import java.util.Collection;
import java.util.List;

import javax.naming.InvalidNameException;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.PersonNotFoundException;
import au.org.theark.core.exception.UnAuthorizedOperation;
import au.org.theark.core.exception.UserNameExistsException;
import au.org.theark.core.model.study.entity.EtaUser;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.ModuleVO;

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

	/**
	 * The interface that will return a list of Modules/Applications that the study is linked to. It is useful where
	 * one has to do certain operations/management based on the linked applications such as assigning roles to user.
	 * Where the user is restricted to assigning the roles only to the applications the study is linked to.
	 * @param studyId
	 * @param isForDisplay
	 * @return
	 * @throws ArkSystemException
	 */
	public List<ModuleVO> getModulesAndRolesForStudy(String studyNameCn) throws ArkSystemException;
	
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
	
	/**
	 * Returns a Collection of module names linked to a study. The module name is encapsulated in ModuleVo
	 * object.
	 * @param studyNameCN
	 * @param isForDisplay
	 * @return
	 * @throws ArkSystemException
	 */
	public Collection<ModuleVO> getModulesLinkedToStudy(String studyNameCN, boolean isForDisplay) throws ArkSystemException;
	
	
	/**
	 * If a study is passed, then interface will  look up all ArkUser's linked to a particular Study in Context.
	 * If all users in ArkUser table (backend) are to be pulled then do not provide the Study. This will be a mechanism via the
	 * UI to selectively search for all users or users for the given study.The implementation should pull 
	 * the users from the backend and then get the personal demographic information from LDAP from the people group.
	 * @param arkUserVO
	 * @return
	 * @throws ArkSystemException
	 */
	public List<ArkUserVO> lookupArkUser(ArkUserVO arkUserVO, Study study) throws ArkSystemException;
	
	
	public boolean isArkUserPresent(String userName);
	
	public void createArkUser(ArkUserVO arkUserVO) throws UserNameExistsException, ArkSystemException;
	
	public void updateArkUser(ArkUserVO arkUserVO) throws ArkSystemException, EntityNotFoundException;
	
	public ArkUserVO lookupArkUser(String arkLdapUserName,Study study) throws ArkSystemException;
	
	/**
	 * Remove the user from the Ark Database system.
	 * @param arkUserVO
	 * @throws ArkSystemException
	 * @throws EntityNotFoundException
	 */
	public void deleteArkUser(ArkUserVO arkUserVO) throws ArkSystemException, EntityNotFoundException;
	

}
