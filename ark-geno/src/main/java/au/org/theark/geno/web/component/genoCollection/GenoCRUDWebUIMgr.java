/**
 * 
 */
package au.org.theark.geno.web.component.genoCollection;

import java.util.Collection;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import au.org.theark.geno.model.entity.GenoCollection;
import au.org.theark.geno.web.component.genoCollection.form.ContainerForm;

/**
 * @author elam
 *
 */
public class GenoCRUDWebUIMgr {
	
	/* Panels */
	protected FeedbackPanel feedbackPanel;
	protected DetailPanel detailPanel;
	protected SearchPanel searchComponentPanel;
	protected SearchResultListPanel searchResultPanel;
	
	/*Web Markup Containers */
	protected ContainerForm containerForm;
	protected WebMarkupContainer searchPanelContainer;
	protected WebMarkupContainer searchResultPanelContainer;
	protected WebMarkupContainer detailPanelContainer;
	protected WebMarkupContainer detailPanelFormContainer;
	protected WebMarkupContainer viewButtonContainer;
	protected WebMarkupContainer editButtonContainer;
	
	/* Pageable-List view */
	protected PageableListView<GenoCollection> pageableListView;
	
	
	public void ShowSearchResults(Collection<GenoCollection> genoCollectionCol, AjaxRequestTarget target) {
		containerForm.getModelObject().setGenoCollectionCollection(genoCollectionCol);
		pageableListView.removeAll();
		searchResultPanelContainer.setVisible(true);// Make the WebMarkupContainer that houses the search results visible
		target.addComponent(searchResultPanelContainer);// For ajax this is required so
	}
	
	public void HideSearchResults() {
		
	}

	public void RefreshFeedback(AjaxRequestTarget target) {
		target.addComponent(feedbackPanel);
	}
}
