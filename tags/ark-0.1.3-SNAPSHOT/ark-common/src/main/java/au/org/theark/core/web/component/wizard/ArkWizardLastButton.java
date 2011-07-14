package au.org.theark.core.web.component.wizard;

import org.apache.wicket.extensions.wizard.IWizard;
import org.apache.wicket.extensions.wizard.IWizardModel;
import org.apache.wicket.extensions.wizard.WizardButton;

public class ArkWizardLastButton extends WizardButton
{
	private static final long	serialVersionUID	= 1L;

	public ArkWizardLastButton(String id, IWizard wizard)
	{
		super(id, wizard, "org.apache.wicket.extensions.wizard.last");
	}

	public final boolean isEnabled()
	{
		return getWizardModel().isLastAvailable();
	}

	public final boolean isVisible()
	{
		return getWizardModel().isLastVisible();
	}

	public final void onClick()
	{
		IWizardModel wizardModel = getWizardModel();
		wizardModel.getActiveStep().applyState();
		wizardModel.last();
	}
}