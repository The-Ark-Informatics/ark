package au.org.theark.phenotypic.web.component.phenoUpload2;

import org.apache.wicket.extensions.wizard.WizardStep;

import au.org.theark.phenotypic.web.component.phenoUpload2.form.ContainerForm;

/**
 * The first step of this wizard.
 */
public class PhenoUploadStep5 extends WizardStep
{
	private ContainerForm						containerForm;
	private java.util.Collection<String>	validationMessages;
	
	/**
	 * Construct.
	 */
	public PhenoUploadStep5(ContainerForm containerForm, java.util.Collection<String>	validationMessages)
	{
		super("Step 5/5: Upload Finished", "The the data is successfully uploaded, click Finish.");
		this.containerForm = containerForm;
		this.setValidationMessages(validationMessages);
	}

	/**
	 * @param validationMessages the validationMessages to set
	 */
	public void setValidationMessages(java.util.Collection<String> validationMessages)
	{
		this.validationMessages = validationMessages;
	}

	/**
	 * @return the validationMessages
	 */
	public java.util.Collection<String> getValidationMessages()
	{
		return validationMessages;
	}
}
