package au.org.theark.security;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;

import au.org.theark.web.pages.LoginPage;

public class CustomUnauthorizedStrategy implements IUnauthorizedComponentInstantiationListener {
	
	public void onUnauthorizedInstantiation(Component component) {
		
		System.out.println("Unauthorized Instantiation");
		if(component instanceof Page){
			System.out.println("\nComponent is instance of Page " + component.getClass().getName());
			throw new RestartResponseAtInterceptPageException(LoginPage.class);	
		}else{
			System.out.println("\n SetVisible False for component " + component.getClass().getName());
			component.setVisible(false);
		}
		
	}

}
