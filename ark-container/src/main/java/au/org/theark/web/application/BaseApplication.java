package au.org.theark.web.application;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.settings.IApplicationSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.security.CustomAuthorizationStrategy;
import au.org.theark.security.CustomUnauthorizedStrategy;
import au.org.theark.web.pages.LoginPage;



public abstract class BaseApplication extends WebApplication{
	static final Logger log = LoggerFactory.getLogger(BaseApplication.class);
	@Override
	protected void init(){
		log.info("In constructor of BaseApplication");
		super.init();
		//CustomAuthorizationStrategy cas = new CustomAuthorizationStrategy();
		//getSecuritySettings().setAuthorizationStrategy(cas);
		//getSecuritySettings().setUnauthorizedComponentInstantiationListener(new CustomUnauthorizedStrategy());//TODO:NN Modify the constructor
		//mountBookmarkablePage("/login",LoginPage.class);
		//Strip out the wicket tags 
		//Set up error message stuff
		getMarkupSettings().setStripWicketTags(true);
		IApplicationSettings settings = getApplicationSettings();
		settings.setPageExpiredErrorPage(LoginPage.class);
	}
	
	public Class<? extends Page> getHomePage() {
		return LoginPage.class;//Index page with login control
	}


}
