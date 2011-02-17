package au.org.theark.phenotypic.web.component.phenoUpload2;

import org.apache.wicket.extensions.wizard.WizardStep;

import au.org.theark.phenotypic.web.component.phenoUpload2.form.ContainerForm;

/**
 * The first step of this wizard.
 */
public class PhenoUploadStep3 extends WizardStep
{
	private ContainerForm						containerForm;
	private java.util.Collection<String>	validationMessages;
	
	/**
	 * Construct.
	 */
	public PhenoUploadStep3(ContainerForm containerForm, java.util.Collection<String>	validationMessages)
	{
		super("Step 3/5: Data Validation", "The data in the file is now validated, correct any errors and try again, otherwise, click Next to continue.");
		this.containerForm = containerForm;
		this.validationMessages = validationMessages;
	}
}
