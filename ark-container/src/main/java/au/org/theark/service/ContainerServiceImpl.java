package au.org.theark.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.dao.ContainerDAO;


@Service("containerService")
public class ContainerServiceImpl implements ContainerService{

	protected ContainerDAO containerDao;
	
	@Autowired
	public void setContainerDao(ContainerDAO containerDao) {
		this.containerDao = containerDao;
	}


	public void getStudy(String name) throws ArkSystemException {
		// TODO Auto-generated method stub
		
	}



}
