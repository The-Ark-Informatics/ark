package au.org.theark.study.model.dao;

import java.util.List;
import java.util.Set;

import javax.naming.InvalidNameException;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.UnAuthorizedOperation;
import au.org.theark.core.exception.UserNameExistsException;
import au.org.theark.core.vo.ArkUserVO;
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
	 * @param ArkUserVO userVO
	 * @throws InvalidNameException
	 */
	public void create(ArkUserVO userVO) throws  UserNameExistsException,ArkSystemException;
	
	/**
	 * Interface to update a person's details including their password. IT does not at present
	 * modify or update the Groups and roles. The modification will only apply to Person attributes.
	 * bind the user to a specific group in ldap.
	 * @param ArkUserVO userVO
	 * @throws InvalidNameException
	 */
	public void update(ArkUserVO userVO) throws ArkSystemException;
	
	/**
	 * Interface to remove a user permanently from the system and remove the user
	 * from all studies. The system will restrict the removal if the user is the only administrator of a group.
	 * @param etaUserVO
	 * @throws ArkSystemException
	 */
	public void delete(ArkUserVO etaUserVO) throws UnAuthorizedOperation, ArkSystemException;
	
	/**
	 * Fetches the current user's details from the LDAP.
	 * @param username
	 * @return ETAUserVO
	 * @throws InvalidNameException
	 */
	public ArkUserVO getUser(String username) throws ArkSystemException;
	/**
	 * Interface that gets the list of modules and associated roles for the given username
	 * @param username
	 * @return
	 * @throws InvalidNameException
	 */
	public ArkUserVO getUserRole(String username) throws ArkSystemException;
	
	
	public List<ModuleVO> getUserRoles(ArkUserVO etaUserVO, String studyName) throws ArkSystemException;
	
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
	public List<ArkUserVO> searchUser(ArkUserVO userVO) throws  ArkSystemException;
	/**
	 * Create a new Study (group) for each of the given applications.
	 * The method is responsible to get a list of pre-defined roles for each application and add them to the study group.
	 * The default member of the new study and each role would be a system user.
	 * @param studyName
	 * @param applications
	 * @param userName
	 * @throws ArkSystemException
	 */
	public void createStudy(String studyName, Set<String> applications, String userName) throws ArkSystemException,EntityExistsException;
	/**
	 * An interface that adds the provided user to the member list of the application if he does not exist in it and then
	 * add's the user to the specified study member's list and finally links the user to one or more roles within the specified study.
	 * @param studyVO
	 * @param applicationName
	 * @param etaUserVO
	 * @throws ArkSystemException
	 */
	public void addUserToStudy(StudyVO studyVO, String applicationName, List<RoleVO> roles, ArkUserVO etaUserVO ) throws ArkSystemException;
	
	/**
	 * Given a study name the interface will return a Set of applications its linked to.
	 * @param studyNameCN
	 * @return
	 * @throws ArkSystemException
	 */
	
	public Set<String> getModulesLinkedToStudy(String studyNameCN) throws ArkSystemException;
	
	public Set<String> getModulesLinkedToStudy(String studyNameCN, boolean isForDisplay) throws ArkSystemException;
	
	/**
	 * Add or remove the study from one or more Application/Module
	 * @param studyName
	 * @param selectedApplication
	 * @param userName
	 * @throws ArkSystemException
	 * @throws EntityExistsException
	 */
	public void updateStudyApplication(String studyName,Set<String> selectedApplication, String userName)  throws ArkSystemException, EntityExistsException, EntityCannotBeRemoved;

}
