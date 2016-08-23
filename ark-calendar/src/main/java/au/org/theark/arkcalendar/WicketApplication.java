package au.org.theark.arkcalendar;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.component.IRequestableComponent;
import org.apache.wicket.util.time.Duration;

import au.org.theark.arkcalendar.pages.dashboard.DashBoardPage;
import au.org.theark.arkcalendar.pages.login.SignIn2;
import au.org.theark.arkcalendar.util.SignIn2Session;

/**
 * Application object for your web application.
 * If you want to run this application without deploying, run the Start class.
 * 
 * @see au.org.theark.arkcalendar.Start#main(String[])
 */
public class WicketApplication extends WebApplication 
{
	
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage()
	{
//		return HomePage.class;
//		return ExtendedCalendarPage.class;
		return DashBoardPage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init()
	{
		super.init();
		
		getApplicationSettings().setPageExpiredErrorPage(SignIn2.class);
		
		getMarkupSettings().setStripWicketTags(true);
		
		getResourceSettings().setDefaultCacheDuration(Duration.NONE);

		// add your configuration here
		// Register the authorization strategy
		getSecuritySettings().setAuthorizationStrategy(new IAuthorizationStrategy.AllowAllAuthorizationStrategy()
		{
			public <T extends IRequestableComponent> boolean isInstantiationAuthorized(
				Class<T> componentClass)
			{
				
			
				
				// Check if the new Page requires authentication (implements the marker interface)
				
				try{
				if (DashBoardPage.class.isAssignableFrom(componentClass))
				{
					// Is user signed in?
					if (Session.exists() &&((SignIn2Session)Session.get()).isSignedIn())
					{
						return true;
					}

					// Intercept the request, but remember the target for later.
					// Invoke Component.continueToOriginalDestination() after successful logon to
					// continue with the target remembered.
					throw new RestartResponseAtInterceptPageException(SignIn2.class);
				}
				}catch(Exception e){
					e.printStackTrace();
					throw new RestartResponseAtInterceptPageException(SignIn2.class);
				}
				// okay to proceed
				return true;
			}
		});
		
		
	}
	
	
	/**
	 * @see org.apache.wicket.protocol.http.WebApplication#newSession(org.apache.wicket.request.Request,
	 *      Response)
	 */
	@Override
	public Session newSession(Request request, Response response)
	{
		return new SignIn2Session(request);
	}
	
	

//	@Override
//	protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
//		// TODO Auto-generated method stub
//		return SignIn2Session.class;
//	}
//
//	@Override
//	protected Class<? extends WebPage> getSignInPageClass() {
//		// TODO Auto-generated method stub
//		return SignIn2.class;
//	}
	
	
}
