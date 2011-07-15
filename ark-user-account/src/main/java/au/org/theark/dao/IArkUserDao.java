package au.org.theark.dao;

import javax.naming.InvalidNameException;

import au.org.theark.vo.ArkUserVO;

/**
 * @author nivedann
 * 
 */
public interface IArkUserDao {

	/**
	 * Create Ark User in LDAP
	 * 
	 * @param userName
	 * @param password
	 */
	public void createArkUser(ArkUserVO arkUserVO) throws InvalidNameException;

}
