package au.org.spark.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SparkWebController {

	@RequestMapping("/welcome")
	public ModelAndView helloWorld() {
	 
	String message = "<br><div align='center'>"
	+ "<h3>********** To create a leading open source knowledge discovery platform that leverages massively parallel computational power for heterogeneous genomic data sets **********<br><br>";
	return new ModelAndView("welcome", "message", message);
	}	
	
}
