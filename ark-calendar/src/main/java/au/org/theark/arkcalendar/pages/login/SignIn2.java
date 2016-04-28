package au.org.theark.arkcalendar.pages.login;

import org.apache.wicket.authroles.authentication.panel.SignInPanel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import au.org.theark.arkcalendar.TemplatePage;

public class SignIn2 extends TemplatePage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Construct
	 */
	public SignIn2()
	{
		this(null);
	}

	/**
	 * Constructor
	 * 
	 * @param parameters
	 *            The page parameters
	 */
	public SignIn2(final PageParameters parameters)
	{
		// Take our standard Logon Panel from the auth-role module and add it to the Page. That is
		// all what is necessary.
		add(new SignInPanel("signInPanel", false));
	}

}
