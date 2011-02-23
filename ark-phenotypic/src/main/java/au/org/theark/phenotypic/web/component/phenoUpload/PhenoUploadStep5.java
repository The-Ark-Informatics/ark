package au.org.theark.phenotypic.web.component.phenoUpload;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;

import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.phenotypic.model.vo.UploadVO;

/**
 * The final step of this wizard.
 */
public class PhenoUploadStep5 extends AbstractWizardStepPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6803600838428204753L;
	private Form<UploadVO>						containerForm;
	private String	validationMessage;
	
	/**
	 * Construct.
	 */
	public PhenoUploadStep5(String id, Form<UploadVO> containerForm)
	{
		super(id, "Step 5/5: Data Upload Finished", "The data is successfully uploaded, click Finish.");
		this.containerForm = containerForm;
		initialiseDetailForm();
	}
	
	private void initialiseDetailForm() 
	{
		setValidationMessage(containerForm.getModelObject().getValidationMessagesAsString());
		addOrReplace(new MultiLineLabel("multiLineLabel", getValidationMessage()));
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
		addOrReplace(new MultiLineLabel("multiLineLabel", containerForm.getModelObject().getValidationMessagesAsString()));
		
		if(this.containerForm.getModelObject().getValidationMessages() != null)
		{
			form.getNextButton().setEnabled(false);
			target.addComponent(form.getWizardButtonContainer());
		}
	}
}
