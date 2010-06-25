package au.org.theark.service;

import au.org.theark.core.exception.ArkSystemException;

public interface ContainerService {
	
	public void getStudy(String name) throws ArkSystemException;
	
}
