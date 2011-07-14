package au.org.theark.web.application;

import org.apache.wicket.Page;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import au.org.theark.web.pages.LoginPage;

public class ArkWebApplication extends BaseApplication{
	
	
	public void init(){
		log.info("In Constructor of ArkApplication");
		super.init();
		addComponentInstantiationListener(new SpringComponentInjector(this,context(),true));
	}
	
	@Override
	public Class<? extends Page> getHomePage() {
		
		return LoginPage.class;
	}
	
	public ApplicationContext context(){
		
		return WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
	}

}
