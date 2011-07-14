package au.org.theark.core.web.component.wizard;

import org.apache.wicket.extensions.wizard.CancelButton;
import org.apache.wicket.extensions.wizard.FinishButton;
import org.apache.wicket.extensions.wizard.NextButton;
import org.apache.wicket.extensions.wizard.PreviousButton;
import org.apache.wicket.extensions.wizard.Wizard;
import org.apache.wicket.markup.html.panel.Panel;
import au.org.theark.core.web.component.wizard.ArkWizardLastButton;

import au.org.theark.core.Constants;

@SuppressWarnings("serial")
public class ArkWizardButtonBarPanel extends Panel
{
	public ArkWizardButtonBarPanel(String id, Wizard wizard)
	{
		super(id);
		add(new PreviousButton(Constants.PREVIOUS, wizard));
		add(new NextButton(Constants.NEXT, wizard));
		add(new ArkWizardLastButton(Constants.LAST, wizard));
		add(new CancelButton(Constants.CANCEL, wizard));
		add(new FinishButton(Constants.FINISH, wizard));
	}
}