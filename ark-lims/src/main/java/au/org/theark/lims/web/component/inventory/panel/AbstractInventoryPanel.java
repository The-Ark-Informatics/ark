package au.org.theark.lims.web.component.inventory.panel;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.security.ArkLdapRealm;

/**
 * <p>
 * Abstract class for the Inventory Container panel Defines the WebMarkupContainers and initialises them. It also defines the Model
 * CompoundPropertyModel and provides methods that the sub-classes must implement such as initialiseSearchResults,initialiseDetailPanel and
 * initialiseSearchPanel()
 * </p>
 * 
 * @author cellis
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractInventoryPanel<T> extends Panel {

	@SpringBean(name = "arkLdapRealm")
	private ArkLdapRealm						realm;

	protected FeedbackPanel					feedbackPanel;

	/* Web Markup Containers */
	protected WebMarkupContainer			treeContainer;
	protected WebMarkupContainer			detailContainer;

	protected IModel<Object>				iModel;
	protected CompoundPropertyModel<T>	cpModel;

	/**
	 * @param id
	 */
	public AbstractInventoryPanel(String id) {
		super(id);
		Subject currentUser = SecurityUtils.getSubject();
		realm.clearCachedAuthorizationInfo(currentUser.getPrincipals());
		initialiseMarkupContainers();
	}

	protected WebMarkupContainer initialiseFeedbackPanel() {
		/* Feedback Panel */
		feedbackPanel = new FeedbackPanel("feedbackMessage");
		feedbackPanel.setOutputMarkupId(true);
		return feedbackPanel;
	}

	private void initialiseMarkupContainers() {
		treeContainer = new WebMarkupContainer("treeContainer");
		treeContainer.setOutputMarkupPlaceholderTag(true);
		treeContainer.setVisible(true);

		detailContainer = new WebMarkupContainer("detailContainer");
		detailContainer.setOutputMarkupPlaceholderTag(true);
		detailContainer.setVisible(false);
	}
}
