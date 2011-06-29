package au.org.theark.study.service;

import java.util.List;

import javax.naming.InvalidNameException;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.UserNameExistsException;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.vo.ArkUserVO;

public interface IUserService {
	
	/**
	 * Interface to update a person's details including their password. It does not at present
	 * modify or update the Groups and roles. The modification will only apply to Person attributes.
	 * 
	 * @param ArkUserVO etaUserVO
	 * @throws InvalidNameException
	 */
	 public void updateLdapUser(ArkUserVO etaUserVO) throws ArkSystemException;
	
	/**
	 * User this method to lookup a user in LDAP. The user object acts as a filter condition
	 * that is applied for the search.
	 * @param ArkUserVO
	 * @return List<ArkUserVO> 
	 * @throws InvalidNameException
	 */
	public List<ArkUserVO> searchUser(ArkUserVO user) throws ArkSystemException;

	
	/**
	 * 
	 * @param username
	 * @return
	 * @throws ArkSystemException
	 */
	public ArkUserVO getCurrentUser(String username) throws ArkSystemException;	
	

	/**
	 * 
	 * @param arkUserVO
	 * @throws UserNameExistsException
	 * @throws ArkSystemException
	 */
	public void createArkUser(ArkUserVO arkUserVO) throws UserNameExistsException, ArkSystemException;
	
	/**
	 * 
	 * @param arkUserVO
	 * @throws ArkSystemException
	 * @throws EntityNotFoundException
	 */
	public void updateArkUser(ArkUserVO arkUserVO) throws ArkSystemException, EntityNotFoundException;
	
	/**
	 * 
	 * @param arkLdapUserName
	 * @param study
	 * @return
	 * @throws ArkSystemException
	 */
	public ArkUserVO lookupArkUser(String arkLdapUserName,Study study) throws ArkSystemException;
	
	/**
	 * Remove the user from the Ark Database system.
	 * @param arkUserVO
	 * @throws ArkSystemException
	 * @throws EntityNotFoundException
	 */
	public void deleteArkUser(ArkUserVO arkUserVO) throws ArkSystemException, EntityNotFoundException;
	

}
