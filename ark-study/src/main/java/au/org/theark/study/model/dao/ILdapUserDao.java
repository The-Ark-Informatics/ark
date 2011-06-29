package au.org.theark.study.model.dao;

import java.util.List;

import javax.naming.InvalidNameException;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.UserNameExistsException;
import au.org.theark.core.vo.ArkUserVO;


/**
 * An interface that communicates with LDAP resource.
 * @author nivedann
 *
 */
public interface ILdapUserDao {
	
	
	/**
	 * Interface to update a person's details including their password. IT does not at present
	 * modify or update the Groups and roles. The modification will only apply to Person attributes.
	 * bind the user to a specific group in ldap.
	 * @param ArkUserVO userVO
	 * @throws InvalidNameException
	 */
	 public void update(ArkUserVO userVO) throws ArkSystemException;
	
	/**
	 * Fetches the current user's details from the LDAP.
	 * @param username
	 * @return ETAUserVO
	 * @throws InvalidNameException
	 */
	public ArkUserVO getUser(String username) throws ArkSystemException;

	/**
	 * Interface to return a zero or more users as a List, that match the search criteria.
	 * @param userVO
	 * @return
	 */
	public List<ArkUserVO> searchUser(ArkUserVO userVO) throws  ArkSystemException;
	
	/**
	 * This is a new interface that persists the user into ArkUsers group in LDAP
	 * @param arkUserVO
	 * @throws UserNameExistsException
	 * @throws ArkSystemException
	 */
	public void createArkUser(ArkUserVO arkUserVO) throws UserNameExistsException, ArkSystemException;	
	
	/**
	 * 
	 * @param userVO
	 * @throws ArkSystemException
	 */
	public void updateArkUser(ArkUserVO userVO) throws ArkSystemException;
	
	/**
	 * 
	 * @param arkUserName
	 * @return
	 * @throws ArkSystemException
	 */
	public ArkUserVO lookupArkUser(String arkUserName) throws ArkSystemException;

}
