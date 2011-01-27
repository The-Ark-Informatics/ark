package au.org.theark.core.web.component.wizard;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.IWizardModel;
import org.apache.wicket.extensions.wizard.IWizardStep;
import org.apache.wicket.extensions.wizard.Wizard;
import org.apache.wicket.extensions.wizard.WizardButtonBar;
import org.apache.wicket.markup.html.form.Form;

@SuppressWarnings( { "unchecked", "serial" })
public class AjaxWizardButtonBar extends WizardButtonBar
{

	private static final long	serialVersionUID	= 1L;

	public AjaxWizardButtonBar(String id, final Wizard wizard)
	{
		super(id, wizard);

		addOrReplace(new AjaxWizardButton("next", wizard, "next")
		{
			@Override
			protected void onClick(AjaxRequestTarget target, Form form)
			{
				IWizardModel wizardModel = getWizardModel();
				IWizardStep step = wizardModel.getActiveStep();

				// let the step apply any state
				step.applyState();

				// if the step completed after applying the state, move the
				// model onward
				if (step.isComplete())
				{
					wizardModel.next();
				}
				else
				{
					error(getLocalizer().getString("org.apache.wicket.extensions.wizard.NextButton.step.did.not.complete", this));
				}

				target.addComponent(wizard);
			}

			public final boolean isEnabled()
			{
				return getWizardModel().isNextAvailable();
			}
		});

		addOrReplace(new AjaxWizardButton("previous", wizard, "prev")
		{

			protected void onClick(AjaxRequestTarget target, Form form)
			{
				getWizardModel().previous();
				target.addComponent(wizard);
			}

			public final boolean isEnabled()
			{
				return getWizardModel().isPreviousAvailable();
			}
		});
	}

}