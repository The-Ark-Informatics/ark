package au.org.theark.study.model.dao;

import java.util.List;

import javax.naming.InvalidNameException;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.UnAuthorizedOperation;
import au.org.theark.core.exception.UserNameExistsException;
import au.org.theark.core.vo.EtaUserVO;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.core.vo.RoleVO;
import au.org.theark.core.vo.StudyVO;

/**
 * An interface that communicates with LDAP resource.
 * @author nivedann
 *
 */
public interface ILdapUserDao {
	/**
	 * Interface to create/bind a new person into LDAP. It also will allow you to
	 * bind the user to a specific group in ldap.
	 * @param EtaUserVO userVO
	 * @throws InvalidNameException
	 */
	public void create(EtaUserVO userVO) throws  UserNameExistsException,ArkSystemException;
	
	/**
	 * Interface to update a person's details including their password. IT does not at present
	 * modify or update the Groups and roles. The modification will only apply to Person attributes.
	 * bind the user to a specific group in ldap.
	 * @param EtaUserVO userVO
	 * @throws InvalidNameException
	 */
	public void update(EtaUserVO userVO) throws ArkSystemException;
	
	/**
	 * Interface to remove a user permanently from the system and remove the user
	 * from all studies. The system will restrict the removal if the user is the only administrator of a group.
	 * @param etaUserVO
	 * @throws ArkSystemException
	 */
	public void delete(EtaUserVO etaUserVO) throws UnAuthorizedOperation, ArkSystemException;
	
	/**
	 * Fetches the current user's details from the LDAP.
	 * @param username
	 * @return ETAUserVO
	 * @throws InvalidNameException
	 */
	public EtaUserVO getUser(String username) throws ArkSystemException;
	/**
	 * Interface that gets the list of modules and associated roles for the given username
	 * @param username
	 * @return
	 * @throws InvalidNameException
	 */
	public EtaUserVO getUserRole(String username) throws ArkSystemException;
	
	
	public List<ModuleVO> getUserRoles(EtaUserVO etaUserVO, String studyName) throws ArkSystemException;
	/**
	 * Interface that sets the list of roles in the EtaUserVO object for a given module
	 * @param etaUserVO
	 * @param moduleName
	 * @throws InvalidNameException
	 */
	public void getUserRole(EtaUserVO etaUserVO, String moduleName) throws ArkSystemException;
	
	
	
	public void getUserRole(EtaUserVO etaUserVO, List<ModuleVO> listOfAllModules) throws ArkSystemException;
	/**
	 * Interface that retrieves all the modules that integrate with ETA
	 */
	public List<ModuleVO> getModules(boolean isForDisplay);
	/**
	 * Interface that retrieves all the roles for a module
	 */
	public List<String> getModuleRoles(String moduleId);
	/**
	 * Interface to return a zero or more users as a List, that match the search criteria.
	 * @param userVO
	 * @return
	 */
	public List<EtaUserVO> searchUser(EtaUserVO userVO) throws  ArkSystemException;
	
	/**
	 * Interface that creates an LDAP entry for a Study.
	 * @param studyVO
	 * @throws InvalidNameException
	 */
	public void createStudy(StudyVO studyVO, String applicationName, EtaUserVO etaUserVO) throws ArkSystemException;
	
	/**
	 * An interface that adds the provided user to the member list of the application if he does not exist in it and then
	 * add's the user to the specified study member's list and finally links the user to one or more roles within the specified study.
	 * @param studyVO
	 * @param applicationName
	 * @param etaUserVO
	 * @throws ArkSystemException
	 */
	public void addUserToStudy(StudyVO studyVO, String applicationName, List<RoleVO> roles, EtaUserVO etaUserVO ) throws ArkSystemException;
	

}
