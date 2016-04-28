package au.org.spark.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class OpenStackServiceImpl implements OpenStackService {
	
//	@Autowired
	private Account account;
	
	public List<String> listContainers(){
		
		List<String> list= new ArrayList<String>();
		Collection<Container> containers = account.list();
	    for (Container currentContainer : containers) {
//	        System.out.println(currentContainer.getName());
	    	list.add(currentContainer.getName());
	    }
	    return list;
	}
}
