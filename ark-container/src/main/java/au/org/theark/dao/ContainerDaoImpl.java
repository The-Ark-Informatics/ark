package au.org.theark.dao;

import org.springframework.stereotype.Repository;

import au.org.theark.core.exception.ArkSystemException;
@Repository("containerDao")
public class ContainerDaoImpl implements ContainerDAO{

	public void getStudy(String name) throws ArkSystemException{
	   
		System.out.println("getStudy invoked");
	}
}
