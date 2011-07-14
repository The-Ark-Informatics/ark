package au.org.theark.phenotypic.web.component.fieldUpload;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.phenotypic.web.component.fieldUpload.form.ContainerForm;
import au.org.theark.phenotypic.web.component.fieldUpload.form.WizardForm;


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
	private WebMarkupContainer		wizardButtonContainer;
	private ContainerForm			containerForm;

	public WizardPanel(String id, 
							final WebMarkupContainer listContainer, 
							FeedbackPanel feedBackPanel, 
							WebMarkupContainer wizardPanelContainer, 
							WebMarkupContainer searchPanelContainer,
							WebMarkupContainer wizardPanelFormContainer,
							ContainerForm containerForm)
	{
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.listContainer = listContainer;
		this.wizardPanelContainer = wizardPanelContainer;
		this.containerForm = containerForm;
		this.searchPanelContainer = searchPanelContainer;
		this.wizardPanelFormContainer = wizardPanelFormContainer;
		this.containerForm = containerForm;
	}

	public void initialisePanel()
	{
		wizardForm = new WizardForm("wizardForm", 
											feedBackPanel, 
											this, 
											listContainer, 
											wizardPanelContainer, 
											containerForm, 
											wizardButtonContainer, 
											wizardPanelFormContainer,
											searchPanelContainer);
		wizardForm.initialiseDetailForm();
		add(wizardForm);

		setOutputMarkupPlaceholderTag(true);
	}

	public WizardForm getWizardForm()
	{
		return wizardForm;
	}

	public void setWizardForm(WizardForm wizardForm)
	{
		this.wizardForm = wizardForm;
	}
}