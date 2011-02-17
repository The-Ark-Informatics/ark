package au.org.theark.phenotypic.web.component.phenoUpload2;

import org.apache.wicket.extensions.wizard.WizardStep;

import au.org.theark.phenotypic.web.component.phenoUpload2.form.ContainerForm;

/**
 * The first step of this wizard.
 */
public class PhenoUploadStep4 extends WizardStep
{
	private ContainerForm						containerForm;
	private java.util.Collection<String>	validationMessages;
	
	/**
	 * Construct.
	 */
	public PhenoUploadStep4(ContainerForm containerForm, java.util.Collection<String>	validationMessages)
	{
		super("Step 4/5: Confirm Upload", "Data will now be written to the database, click Next to continue, otherwise click Cancel.");
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
