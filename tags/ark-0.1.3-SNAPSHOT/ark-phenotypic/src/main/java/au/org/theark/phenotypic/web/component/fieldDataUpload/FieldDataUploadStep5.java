package au.org.theark.phenotypic.web.component.fieldDataUpload;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;

import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.core.web.form.AbstractWizardStepPanel;
import au.org.theark.phenotypic.model.vo.UploadVO;

/**
 * The final step of this wizard.
 */
public class FieldDataUploadStep5 extends AbstractWizardStepPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6803600838428204753L;
	private Form<UploadVO>						containerForm;
	
	/**
	 * Construct.
	 */
	public FieldDataUploadStep5(String id, Form<UploadVO> containerForm)
	{
		super(id, "Step 5/5: Data Upload Finished", "The data is successfully uploaded, click Finish.");
		this.containerForm = containerForm;
		initialiseDetailForm();
	}
	
	private void initialiseDetailForm() 
	{
	}

	@Override
	public void handleWizardState(AbstractWizardForm<?> form, AjaxRequestTarget target) 
	{
		if(this.containerForm.getModelObject().getValidationMessages() != null)
		{
			form.getNextButton().setEnabled(false);
			target.addComponent(form.getWizardButtonContainer());
		}
	}
}
