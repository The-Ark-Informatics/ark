package au.org.theark.phenotypic.web.component.phenoUpload;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.web.component.wizard.ArkCommonWizard;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.web.component.phenoUpload.form.ContainerForm;
import au.org.theark.phenotypic.web.component.phenoUpload.form.WizardForm;

@SuppressWarnings("serial")
public class WizardPanel extends Panel
{
	private WizardForm				wizardForm;
	private FeedbackPanel			feedBackPanel;
	private WebMarkupContainer		listContainer;
	private WebMarkupContainer		searchPanelContainer;
	protected WebMarkupContainer	resultListContainer;
	protected WebMarkupContainer	wizardPanelContainer;
	protected WebMarkupContainer	wizardPanelFormContainer;
	private WebMarkupContainer		viewButtonContainer;
	private WebMarkupContainer		editButtonContainer;
	private ContainerForm			containerForm;

	private WebMarkupContainer		phenoUploadWizardContainer;
	private ArkCommonWizard			phenoUploadWizardPanel;
	private AjaxRequestTarget		target;
	private java.util.Collection<String>	validationMessages;

	public WizardPanel(String id, final WebMarkupContainer listContainer, FeedbackPanel feedBackPanel, WebMarkupContainer wizardPanelContainer, WebMarkupContainer searchPanelContainer,
			WebMarkupContainer wizardPanelFormContainer, ContainerForm containerForm)
	{
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.listContainer = listContainer;
		this.wizardPanelContainer = wizardPanelContainer;
		this.containerForm = containerForm;
		this.searchPanelContainer = searchPanelContainer;
		this.wizardPanelFormContainer = wizardPanelFormContainer;
		this.containerForm = containerForm;

		this.phenoUploadWizardContainer = new WebMarkupContainer("phenoUploadWizardContainer");
		this.phenoUploadWizardContainer.setOutputMarkupPlaceholderTag(true);

		// Collection of custom wizard steps
		WizardStep wizardSteps[] = new WizardStep[5];
		wizardSteps[0] = new PhenoUploadStep1(this.containerForm, this.validationMessages);
		wizardSteps[1] = new PhenoUploadStep2(this.containerForm, this.validationMessages);
		wizardSteps[2] = new PhenoUploadStep3(this.containerForm, this.validationMessages);
		wizardSteps[3] = new PhenoUploadStep4(this.containerForm, this.validationMessages);
		wizardSteps[4] = new PhenoUploadStep5(this.containerForm, this.validationMessages);

		this.phenoUploadWizardPanel = new ArkCommonWizard("phenoUploadWizardPanel", wizardSteps, this.listContainer, this.wizardPanelContainer, this.wizardPanelFormContainer, this.searchPanelContainer,
				target);
		// this.phenoUploadWizardPanel = new ArkCommonWizard("phenoUploadWizardPanel", wizardSteps);
		// this.phenoUploadWizardPanel = new ArkWizardWithPanels("phenoUploadWizardPanel");
		phenoUploadWizardPanel.setVisible(true);

		wizardForm = new WizardForm("wizardForm", feedBackPanel, this, listContainer, wizardPanelContainer, containerForm, viewButtonContainer, editButtonContainer, wizardPanelFormContainer,
				searchPanelContainer, phenoUploadWizardPanel.getButtonBar());
		wizardForm.initialiseDetailForm();
		wizardPanelFormContainer.add(phenoUploadWizardPanel);
		wizardForm.add(wizardPanelFormContainer);
		phenoUploadWizardContainer.add(wizardForm);

		add(phenoUploadWizardContainer);
		setOutputMarkupPlaceholderTag(true);
	}

	public void initialisePanel()
	{
		// TODO: Add initilisation here
	}
}