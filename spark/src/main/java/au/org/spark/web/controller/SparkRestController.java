package au.org.spark.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import au.org.spark.service.OpenStackService;
import au.org.spark.service.SshService;
import au.org.spark.web.view.JavaBean;


@RestController
public class SparkRestController {
	
	@Autowired
	OpenStackService openStackService;
	
	@Autowired
	SshService sshService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public @ResponseBody JavaBean getJSONJavaBean() {
    	openStackService.listContainers();
        return new JavaBean();
    }
    
    @RequestMapping(value = "/listContainers", method = RequestMethod.GET)
    public @ResponseBody List<String> listContainers() {
    	return openStackService.listContainers();
    }
    
    @RequestMapping(value = "/listFiles", method = RequestMethod.GET)
    public @ResponseBody List<String> listFiles(@RequestParam("dirName") String dirName) {
    	List<String> list=null;
    	try{
    		list = sshService.listFiles(dirName);
    	}catch(Exception e){
    		list = new ArrayList<String>();
    		list.add("No dir found");
    	}
    	return list;
    }
    
    @RequestMapping(value="/mapping/producesjson", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody JavaBean byProducesJson() {
		return new JavaBean();
	}
    
    @RequestMapping(value="/mapping/producesxml", method=RequestMethod.GET, produces=MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody JavaBean byProducesXml() {
		return new JavaBean();
	}   

}
