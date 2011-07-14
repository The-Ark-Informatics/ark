package au.org.theark.core.web.component.wizard;

import org.apache.wicket.extensions.wizard.IWizard;
import org.apache.wicket.extensions.wizard.IWizardModel;
import org.apache.wicket.extensions.wizard.WizardButton;

public class ArkWizardPreviousButton extends WizardButton
{
	private static final long	serialVersionUID	= 1L;

	public ArkWizardPreviousButton(String id, IWizard wizard)
	{
		super(id, wizard, "org.apache.wicket.extensions.wizard.previous");
	}

	public boolean isEnabled()
	{
		return getWizardModel().isLastAvailable();
	}

	public boolean isVisible()
	{
		return getWizardModel().isLastVisible();
	}

	public void onClick()
	{
		IWizardModel wizardModel = getWizardModel();
		wizardModel.getActiveStep().applyState();
		wizardModel.previous();
	}
}