package au.org.theark.study.web.component.address;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.study.web.component.address.form.ContainerForm;
import au.org.theark.study.web.component.address.form.DetailForm;

/**
 * @author nivedann
 *
 */
public class DetailPanel extends Panel{

	private FeedbackPanel feedBackPanel;
	private WebMarkupContainer searchResultPanelContainer;
	private WebMarkupContainer detailPanelContainer;
	private WebMarkupContainer detailPanelFormContainer; 
	private WebMarkupContainer searchPanelContainer;
	private WebMarkupContainer viewButtonContainer;
	private WebMarkupContainer editButtonContainer;
	private ContainerForm containerForm;
	private DetailForm detailForm;
	
	/**
	 * @param id
	 */
	public DetailPanel(String id,
						FeedbackPanel feedBackPanel,
						WebMarkupContainer searchResultPanelContainer,
						WebMarkupContainer detailPanelContainer,
						WebMarkupContainer detailPanelFormContainer, 
						WebMarkupContainer searchPanelContainer,
						WebMarkupContainer viewButtonContainer,
						WebMarkupContainer editButtonContainer,
						ContainerForm containerForm	) {
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
	
	public void initialisePanel(){
		
		detailForm = new DetailForm("detailsForm",
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

	public DetailForm getDetailForm() {
		return detailForm;
	}

	public void setDetailForm(DetailForm detailForm) {
		this.detailForm = detailForm;
	}

}
