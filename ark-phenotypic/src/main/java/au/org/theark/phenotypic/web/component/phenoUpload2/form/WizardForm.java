package au.org.theark.phenotypic.web.component.phenoUpload2.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.wizard.AjaxWizardButtonBar;
import au.org.theark.core.web.form.AbstractWizardForm;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.field.form.ContainerForm;
import au.org.theark.phenotypic.web.component.phenoUpload2.PhenoUploadStep1;
import au.org.theark.phenotypic.web.component.phenoUpload2.PhenoUploadStep2;
import au.org.theark.phenotypic.web.component.phenoUpload2.WizardPanel;

/**
 * @author cellis
 * 
 */
@SuppressWarnings( { "serial", "unused" })
public class WizardForm extends AbstractWizardForm<UploadVO>
{
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService					phenotypicService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService					iArkCommonService;

	private ContainerForm						fieldContainerForm;

	private AjaxWizardButtonBar				buttonBar;
	
	private static final String	HEXES	= "0123456789ABCDEF";
	

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param wizardStep
	 * @param listContainer
	 * @param wizardContainer
	 * @param containerForm
	 * @param viewButtonContainer
	 * @param editButtonContainer
	 * @
	 * param wizardFormContainer
	 * @param searchPanelContainer
	 */
	public WizardForm(String id, FeedbackPanel feedBackPanel, WizardPanel wizardPanel, WebMarkupContainer listContainer, WebMarkupContainer wizardContainer, Form<UploadVO> containerForm,
			WebMarkupContainer viewButtonContainer, WebMarkupContainer editButtonContainer, WebMarkupContainer wizardFormContainer, WebMarkupContainer searchPanelContainer)
	{

		super(id, feedBackPanel, listContainer, wizardContainer, wizardFormContainer, searchPanelContainer, viewButtonContainer, editButtonContainer, containerForm);
	}


	public void initialiseDetailForm()
	{
		initialiseStep1();
		attachValidators();
		addComponents();
	}
	
	public void initialiseStep1()
	{
		PhenoUploadStep1 step1 = new PhenoUploadStep1("step");
		PhenoUploadStep2 step2 = new PhenoUploadStep2("step");
		step1.setNextStep(step2);
		step2.setPreviousStep(step1);
		//PhenoUploadStep3 step3 = new PhenoUploadStep3("step");
		//PhenoUploadStep4 step4 = new PhenoUploadStep4("step");
		//PhenoUploadStep5 step5 = new PhenoUploadStep5("step");
		wizardPanelFormContainer.add(step1);
	}

	private void addComponents()
	{
		add(wizardPanelFormContainer);
	}
	
	@Override
	public void onFinish(AjaxRequestTarget target, Form form)
	{

	}

	private void onSavePostProcess(AjaxRequestTarget target)
	{
		// TODO Auto-generated method stub		
	}

	@Override
	protected void onCancel(AjaxRequestTarget target)
	{
		// Implement Cancel
		UploadVO uploadVO = new UploadVO();
		containerForm.setModelObject(uploadVO);
		onCancelPostProcess(target);
	}

	@Override
	protected void processErrors(AjaxRequestTarget target)
	{
		target.addComponent(feedBackPanel);
	}

	@Override
	public void onError(AjaxRequestTarget target, Form form)
	{
		processErrors(target);	
	}


	@Override
	protected void attachValidators()
	{
		// TODO Auto-generated method stub
		
	}	
}