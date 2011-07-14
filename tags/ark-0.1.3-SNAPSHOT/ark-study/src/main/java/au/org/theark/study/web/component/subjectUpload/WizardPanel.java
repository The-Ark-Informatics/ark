package au.org.theark.study.web.component.subjectUpload;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.web.component.ArkExcelWorkSheetAsGrid;
import au.org.theark.study.web.component.subjectUpload.form.ContainerForm;
import au.org.theark.study.web.component.subjectUpload.form.WizardForm;


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
	private ArkExcelWorkSheetAsGrid arkExcelWorkSheetAsGrid; 

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

	/**
	 * @param arkExcelWorkSheetAsGrid the arkExcelWorkSheetAsGrid to set
	 */
	public void setArkExcelWorkSheetAsGrid(ArkExcelWorkSheetAsGrid arkExcelWorkSheetAsGrid)
	{
		this.arkExcelWorkSheetAsGrid = arkExcelWorkSheetAsGrid;
	}

	/**
	 * @return the arkExcelWorkSheetAsGrid
	 */
	public ArkExcelWorkSheetAsGrid getArkExcelWorkSheetAsGrid()
	{
		return arkExcelWorkSheetAsGrid;
	}
}