package au.org.theark.phenotypic.web.component.phenoUpload2;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.web.component.wizard.ArkCommonWizard;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.web.component.phenoUpload2.form.ContainerForm;
import au.org.theark.phenotypic.web.component.phenoUpload2.form.WizardForm;

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

		wizardForm = new WizardForm("wizardForm", feedBackPanel, this, listContainer, wizardPanelContainer, containerForm, viewButtonContainer, editButtonContainer, wizardPanelFormContainer,
				searchPanelContainer);
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