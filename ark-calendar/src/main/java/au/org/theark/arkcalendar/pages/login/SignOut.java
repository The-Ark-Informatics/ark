package au.org.theark.arkcalendar.pages.login;

import au.org.theark.arkcalendar.TemplatePage;

public class SignOut extends TemplatePage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param parameters
	 *            Page parameters (ignored since this is the home page)
	 */
	public SignOut()
	{
		getSession().invalidate();
	}
}
