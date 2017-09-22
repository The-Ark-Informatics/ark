package au.org.theark.web.rest.service;

import au.org.theark.core.vo.ArkUserVO;

public interface ILoginWebServiceRest {
	
	public boolean authenticate(ArkUserVO user);
	
	public boolean hasRightSimilarToSubjectAdministrator();

}
