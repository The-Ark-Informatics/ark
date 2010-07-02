package au.org.theark.core.service;

import java.util.List;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.vo.ArkUserVO;

public interface IArkCommonService {
	
	//Place here any common services that must be visible to sub-applications
	//Get reference data etc.get study maybe required but sub-applications access a study via ETA Study module
	public ArkUserVO getUser(String name) throws ArkSystemException ;
	
	public List<String> getUserRole(String userName) throws ArkSystemException;
	
}
