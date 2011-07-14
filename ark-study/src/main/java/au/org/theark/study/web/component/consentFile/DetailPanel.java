package au.org.theark.study.web.component.consentFile;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.study.web.component.consentFile.form.ContainerForm;
import au.org.theark.study.web.component.consentFile.form.DetailForm;

@SuppressWarnings("serial")
public class DetailPanel extends Panel
{
	private DetailForm detailForm;
	private FeedbackPanel feedBackPanel;
	private WebMarkupContainer searchResultPanelContainer;
	private WebMarkupContainer detailPanelContainer;
	private WebMarkupContainer detailPanelFormContainer; 
	private WebMarkupContainer searchPanelContainer;
	private WebMarkupContainer viewButtonContainer;
	private WebMarkupContainer editButtonContainer;
	private ContainerForm containerForm;
	
	public DetailPanel(String id,
			FeedbackPanel feedBackPanel,
			WebMarkupContainer searchResultPanelContainer,
			WebMarkupContainer detailPanelContainer,
			WebMarkupContainer detailPanelFormContainer, 
			WebMarkupContainer searchPanelContainer,
			WebMarkupContainer viewButtonContainer,
			WebMarkupContainer editButtonContainer,
			ContainerForm containerForm)
	{
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.searchResultPanelContainer = searchResultPanelContainer;
		this.detailPanelContainer = detailPanelContainer;
		this.detailPanelFormContainer = detailPanelFormContainer;
		this.searchPanelContainer = searchPanelContainer;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.containerForm = containerForm;
		
		}

	public void initialisePanel()
	{
		detailForm = new DetailForm("detailForm", 
				feedBackPanel, 
				searchResultPanelContainer, 
				detailPanelContainer, 
				detailPanelFormContainer, 
				searchPanelContainer, 
				viewButtonContainer, 
				editButtonContainer, 
				containerForm);
		
		detailForm.initialiseDetailForm();
		add(detailForm);
	}

	public DetailForm getDetailForm()
	{
		return detailForm;
	}

	public void setDetailForm(DetailForm detailsForm)
	{
		this.detailForm = detailsForm;
	}
}