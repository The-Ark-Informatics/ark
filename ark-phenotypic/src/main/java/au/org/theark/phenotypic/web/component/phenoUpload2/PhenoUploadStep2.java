package au.org.theark.phenotypic.web.component.phenoUpload2;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.TextArea;

import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.phenotypic.web.component.phenoUpload2.form.ContainerForm;

/**
 * The first step of this wizard.
 */
public class PhenoUploadStep2 extends AbstractWizardStepPanel
{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6923277221441497110L;
	private ContainerForm						containerForm;
	private java.util.Collection<String>	validationMessages;
	private TextArea<String> validationTextArea;
	
	
	public PhenoUploadStep2(String id) {
		super(id);
		initialiseDetailForm();
	}
	
	private void initialiseDetailForm() 
	{
		validationTextArea = new TextArea<String>("validationMessages");
		add(validationTextArea);
		
		//this.containerForm = containerForm;
		//this.validationMessages = containerForm.getModelObject().getValidationMessages();
	}

	/**
	 * Construct.
	 
	public PhenoUploadStep2(ContainerForm containerForm, java.util.Collection<String>	validationMessages)
	{
		super("Step 2/5: File Validation", "The file has been validated. If there are no errors, click Next to continue.");

		
	}
	*/
	
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

	@Override
	public void handleWizardState(AbstractWizardForm<?> form,
			AjaxRequestTarget target) {
		// TODO Auto-generated method stub
		
	}
}
