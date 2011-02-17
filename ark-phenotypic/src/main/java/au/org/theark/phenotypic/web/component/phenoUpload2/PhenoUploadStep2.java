package au.org.theark.phenotypic.web.component.phenoUpload2;

import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.form.TextArea;

import au.org.theark.phenotypic.web.component.phenoUpload2.form.ContainerForm;

/**
 * The first step of this wizard.
 */
public class PhenoUploadStep2 extends WizardStep
{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6923277221441497110L;
	private ContainerForm						containerForm;
	private java.util.Collection<String>	validationMessages;
	private TextArea<String> validationTextArea;
	
	/**
	 * Construct.
	 */
	public PhenoUploadStep2(ContainerForm containerForm, java.util.Collection<String>	validationMessages)
	{
		super("Step 2/5: File Validation", "The file has been validated. If there are no errors, click Next to continue.");

		validationTextArea = new TextArea<String>("validationMessages");
		add(validationTextArea);
		
		this.containerForm = containerForm;
		this.validationMessages = containerForm.getModelObject().getValidationMessages();
	}
	
	public void replaceValidationMessages()
	{	
		addOrReplace(this.validationTextArea);
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
