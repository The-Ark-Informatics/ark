package au.org.theark.dao;

import au.org.theark.core.exception.ArkSystemException;

public interface ContainerDAO {
	
	public void getStudy(String name) throws ArkSystemException;

}
