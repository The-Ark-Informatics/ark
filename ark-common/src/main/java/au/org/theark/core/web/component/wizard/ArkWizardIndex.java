package au.org.theark.core.web.component.wizard;

import org.apache.wicket.extensions.wizard.Wizard;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;

/**
 * Index page for the wizard example.
 * 
 * @author Eelco Hillenius
 */
public class ArkWizardIndex extends WebPage
{
	/**
	 * Link to the wizard. It's an internal link instead of a bookmarkable page to help us with backbutton surpression. Wizards by default do not
	 * partipcate in versioning, which has the effect that whenever a button is clicked in the wizard, it will never result in a change of the
	 * redirection url. However, though that'll work just fine when you are already in the wizard, there is still the first access to the wizard. But
	 * if you link to the page that renders it using and internal link, you'll circumvent that.
	 */
	@SuppressWarnings( { "unused", "serial", "unchecked" })
	private static final class WizardLink extends Link
	{

		private final Class<? extends Wizard>	wizardClass;

		/**
		 * Construct.
		 * 
		 * @param <C>
		 * 
		 * @param id
		 *           Component id
		 * @param wizardClass
		 *           Class of the wizard to instantiate
		 */
		public <C extends Wizard> WizardLink(String id, Class<C> wizardClass)
		{
			super(id);
			this.wizardClass = wizardClass;
		}

		/**
		 * @see org.apache.wicket.markup.html.link.Link#onClick()
		 */
		@Override
		public void onClick()
		{
			// setResponsePage(new WebPage());
		}
	}

	/**
	 * Construct.
	 */
	public ArkWizardIndex()
	{
		// add(new WizardLink("staticWizardLink", StaticWizard.class));
		// add(new WizardLink("staticWizardWithPanelsLink", StaticWizardWithPanels.class));
		// add(new WizardLink("newUserWizardLink", NewUserWizard.class));
	}
}
