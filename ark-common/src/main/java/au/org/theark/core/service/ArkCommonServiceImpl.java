package au.org.theark.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.Constants;
import au.org.theark.core.dao.ILdapPersonDao;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.vo.ArkUserVO;
/**
 * The implementation of IArkCommonService. We want to auto-wire and hence use the @Service annotation.
 * 
 * @author nivedann
 *
 */

@Transactional
@Service(Constants.ARK_COMMON_SERVICE)
public class ArkCommonServiceImpl implements IArkCommonService{

	private ILdapPersonDao ldapInterface;
	
	@Autowired
	public void setLdapInterface(ILdapPersonDao ldapInterface) {
		this.ldapInterface = ldapInterface;
	}


	public ArkUserVO getUser(String name) throws ArkSystemException {
		return ldapInterface.getUser(name);
	}
	
	public List<String> getUserRole(String userName) throws ArkSystemException{
		return ldapInterface.getUserRole(userName);
	}

}
