package au.org.theark.core.web.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

public class AbstractCRUDPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -906844577221105889L;
	
	protected static FeedbackPanel feedBackPanel;
	
	/*Web Markup Containers */
	protected static WebMarkupContainer searchPanelContainer;
	protected static WebMarkupContainer searchResultPanelContainer;
	protected static WebMarkupContainer detailPanelContainer;
	protected static WebMarkupContainer detailPanelFormContainer;
	protected static WebMarkupContainer viewButtonContainer;
	protected static WebMarkupContainer editButtonContainer;

	public AbstractCRUDPanel(String id) {
		super(id);
	}
	
	public void initialiseMarkupContainers() {
		
		searchPanelContainer = new WebMarkupContainer("searchContainer");
		searchPanelContainer.setOutputMarkupPlaceholderTag(true);
		
		detailPanelContainer = new WebMarkupContainer("detailContainer");
		detailPanelContainer.setOutputMarkupPlaceholderTag(true);
		detailPanelContainer.setVisible(false);

		//Contains the controls of the details
		detailPanelFormContainer = new WebMarkupContainer("detailFormContainer");
		detailPanelFormContainer.setOutputMarkupPlaceholderTag(true);
		detailPanelFormContainer.setEnabled(false);
		
		//The wrapper for ResultsList panel that will contain a ListView
		searchResultPanelContainer = new WebMarkupContainer("resultListContainer");
		searchResultPanelContainer.setOutputMarkupPlaceholderTag(true);
		searchResultPanelContainer.setVisible(true);
		
		/* Defines a Read-Only Mode */
		viewButtonContainer = new WebMarkupContainer("viewButtonContainer");
		viewButtonContainer.setOutputMarkupPlaceholderTag(true);
		viewButtonContainer.setVisible(false);
		
		/* Defines a edit mode */
		editButtonContainer = new WebMarkupContainer("editButtonContainer");
		editButtonContainer.setOutputMarkupPlaceholderTag(true);
		editButtonContainer.setVisible(false);
		
	}
	
	protected void preProcessDetailPanel(AjaxRequestTarget target) {
		
		searchPanelContainer.setVisible(false);
		searchResultPanelContainer.setVisible(false);
		editButtonContainer.setVisible(true);
		viewButtonContainer.setVisible(false);
		detailPanelContainer.setVisible(true);
		detailPanelFormContainer.setEnabled(true);

		target.addComponent(searchPanelContainer);
		target.addComponent(searchResultPanelContainer);
		target.addComponent(viewButtonContainer);
		target.addComponent(editButtonContainer);
		target.addComponent(detailPanelContainer);
		target.addComponent(detailPanelFormContainer);
		
	}

}
