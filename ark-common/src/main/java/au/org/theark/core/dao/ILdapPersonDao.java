package au.org.theark.core.dao;

import java.util.List;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.vo.ArkUserVO;
/**
 * Public LDAP resource related interfaces that will be shared by other applications.
 * The interfaces help in looking up a user and his roles and groups in the Ark system.
 * @author nivedann
 *
 */
public interface ILdapPersonDao {
	

	public ArkUserVO getUser(String username) throws ArkSystemException;

	public List<String> getUserRole(String userName) throws ArkSystemException;
	

}
