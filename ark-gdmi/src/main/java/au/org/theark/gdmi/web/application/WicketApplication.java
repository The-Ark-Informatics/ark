package au.org.theark.gdmi.web.application;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import au.org.theark.gdmi.web.pages.HomePage;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see org.genepi.test.Start#main(String[])
 */
public class WicketApplication extends WebApplication
{    
	
	public void init(){
		
		addComponentInstantiationListener(new SpringComponentInjector(this,context(),true));
	}
    /**
     * Constructor
     */
	public WicketApplication()
	{
	}
	
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	public Class<HomePage> getHomePage()
	{
		return HomePage.class;
	}
	
	public ApplicationContext context(){
		
		return WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
	}

}
