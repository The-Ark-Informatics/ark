package au.org.theark.phenotypic.web.component.phenoUpload;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.phenoUpload.form.WizardForm;

/**
 * The first step of this wizard.
 */
public class PhenoUploadStep3 extends AbstractWizardStepPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5099768179441679542L;
	private Form<UploadVO>						containerForm;
	private String	validationMessage;
	public java.util.Collection<String> validationMessages = null;
	private WizardForm wizardForm;
	private CheckBox						overrideDataValidationChkBox;
	
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService phenotypicService;
	
	
	/**
	 * Construct.
	 */
	public PhenoUploadStep3(String id, Form<UploadVO> containerForm, WizardForm wizardForm)
	{
		super(id, "Step 3/5: Data Validation", 
				"The data in the file is now validated, correct any errors and try again, otherwise, click Next to continue.\n" +
				"If data fails validation, but you still wish to import, you may override the validation. This data will be flagged as failed quality control");
		this.containerForm = containerForm;
		this.wizardForm = wizardForm;
		initialiseDetailForm();
	}
	
	@SuppressWarnings("serial")
	private void initialiseDetailForm() 
	{
		setValidationMessage(containerForm.getModelObject().getValidationMessagesAsString());
		addOrReplace(new MultiLineLabel("multiLineLabel", getValidationMessage()));
		
		overrideDataValidationChkBox = new CheckBox("overrideDataValidationChkBox");
		overrideDataValidationChkBox.setVisible(true);

		overrideDataValidationChkBox.add(new AjaxFormComponentUpdatingBehavior("onChange")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				if (containerForm.getModelObject().getOverrideDataValidationChkBox())
				{
					wizardForm.getNextButton().setEnabled(true);
				}
				else
				{
					wizardForm.getNextButton().setEnabled(false);
				}
				target.addComponent(wizardForm.getWizardButtonContainer());
			}
		});
		
		add(overrideDataValidationChkBox);
	}

	/**
	 * @param validationMessages the validationMessages to set
	 */
	public void setValidationMessage(String validationMessage)
	{
		this.validationMessage = validationMessage;
	}

	/**
	 * @return the validationMessages
	 */
	public String getValidationMessage()
	{
		return validationMessage;
	}

	@Override
	public void handleWizardState(AbstractWizardForm<?> form, AjaxRequestTarget target) 
	{
		
	}
	
	@Override
	public void onStepOutNext(AbstractWizardForm<?> form, AjaxRequestTarget target)
	{
		if (containerForm.getModelObject().getOverrideDataValidationChkBox())
			containerForm.getModelObject().setValidationMessages(null);
	}
	
	@Override
	public void onStepInNext(AbstractWizardForm<?> form, AjaxRequestTarget target)
	{
		validationMessage = containerForm.getModelObject().getValidationMessagesAsString();
		addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));
		
		if(validationMessage != null && validationMessage.length() > 0)
		{
			form.getNextButton().setEnabled(false);
			target.addComponent(form.getWizardButtonContainer());
		}
		else
		{
			//validateFileData();
			validationMessages = phenotypicService.validateMatrixPhenoFileData(this.wizardForm.getFile());
			this.containerForm.getModelObject().setValidationMessages(validationMessages);
			validationMessage = containerForm.getModelObject().getValidationMessagesAsString();
			if(validationMessage != null && validationMessage.length() > 0)
			{
				addOrReplace(new MultiLineLabel("multiLineLabel", validationMessage));
				form.getNextButton().setEnabled(false);
				target.addComponent(form.getWizardButtonContainer());
			}
		}
	}
}
