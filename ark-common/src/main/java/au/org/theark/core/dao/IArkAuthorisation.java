package au.org.theark.core.dao;

import java.util.Collection;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkUsecase;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;


/**
 * @author nivedann
 * @param <T>
 *
 */
public interface IArkAuthorisation<T> {
	
	/**
	 * This interface checks if the user is a Super Administrator 
	 */
	public boolean isSuperAdministrator(String userName) throws EntityNotFoundException;
	
	public boolean isSuperAdministator(String ldapUserName, ArkUsecase arkUsecase, ArkModule arkModule) throws EntityNotFoundException;
	
	/**
	 * This interface checks if the user is a Administrator in the Ark System.
	 * @param userName
	 * @return
	 */
	public boolean isAdministator(String userName) throws EntityNotFoundException;
	
	/**
	 * Returns a Collection of Roles as String for the given Ldap User Name.
	 * @param ldapUserName
	 * @return
	 * @throws EntityNotFoundException
	 */
	public Collection<String> getUserAdminRoles(String ldapUserName) throws EntityNotFoundException;
	
	
	public String getUserRoleForStudy(String ldapUserName,Study study) throws EntityNotFoundException;
	
	
	public ArkUsecase getArkUsecaseByName(String usecaseName);
	
	public ArkUsecase getArkUsecaseById(Long usecaseId);
	
	public ArkModule getArkModuleByName(String moduleName);
	
	public ArkModule getArkModuleById(Long moduleId);
	/**
	 * 
	 * @param ldapUserName
	 * @param arkUseCase
	 * @param arkModule
	 * @param study
	 * @return
	 * @throws EntityNotFoundException
	 */
	public String getUserRole(String ldapUserName,ArkUsecase arkUseCase, ArkModule arkModule,Study study) throws EntityNotFoundException;
	
	/**
	 * 
	 * @param ldapUserName
	 * @param arkUseCase
	 * @param userRole
	 * @param arkModule
	 * @param study
	 * @throws EntityNotFoundException
	 */
	public Collection<String> getArkUserRolePermission(String ldapUserName,ArkUsecase arkUseCase,String userRole, ArkModule arkModule,Study study) throws EntityNotFoundException;
	
	/**
	 * Returns All Permissions as collection of Strings
	 * @return  Collection<String> that represents ArkPermission
	 */
	public Collection<String> getArkPermission();
	
	public   Collection<Class<T>>  getEntityList(Class<T> aClass);
	
	public ArkUser getArkUser(String ldapUserName) throws EntityNotFoundException;
	

}
