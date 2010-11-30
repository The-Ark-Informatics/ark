package au.org.theark.core.web.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 * <p>
 * Abstract class for the Container panels that contains Search,SearchResult and Detail panels.
 * Defines the WebMarkupContainers and initialises them.It also defines the Model CompoundPropertyModel
 * and provides methods that the sub-classes must implement such as initialiseSearchResults,initialiseDetailPanel
 * and initialiseSearchPanel()
 * </p>
 * @author nivedann
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractContainerPanel<T> extends AbstractCRUDPanel {

	protected IModel<Object> iModel;
	protected CompoundPropertyModel<T> cpModel;
	/**
	 * @param id
	 */
	public AbstractContainerPanel(String id) {

		super(id);
		initialiseMarkupContainers();
	}

	
	protected WebMarkupContainer initialiseFeedBackPanel(){
		/* Feedback Panel */
		feedBackPanel= new FeedbackPanel("feedbackMessage");
		feedBackPanel.setOutputMarkupId(true);
		return feedBackPanel;
	}
	
	
	protected abstract WebMarkupContainer initialiseSearchResults();
	
	protected abstract WebMarkupContainer initialiseDetailPanel();
	
	protected abstract WebMarkupContainer initialiseSearchPanel();
	
	
}
