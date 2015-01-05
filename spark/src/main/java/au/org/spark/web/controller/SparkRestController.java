package au.org.spark.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.MediaType;

import au.org.spark.web.view.JavaBean;

@RestController
public class SparkRestController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public @ResponseBody JavaBean getJSONJavaBean() {
        return new JavaBean();
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
