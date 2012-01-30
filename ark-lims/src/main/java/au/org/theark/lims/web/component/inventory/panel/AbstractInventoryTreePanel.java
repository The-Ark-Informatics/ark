package au.org.theark.lims.web.component.inventory.panel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.tree.AbstractTree;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.lims.entity.InvSite;
import au.org.theark.core.model.lims.entity.InvTreeNode;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.web.component.button.ArkBusyAjaxButton;
import au.org.theark.lims.model.InventoryModel;
import au.org.theark.lims.model.TreeNodeModel;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.web.Constants;

/**
 * Abstract Inventory Panel that all sub-classes extend (contains Add buttons for inventory)
 * 
 * @author cellis
 * 
 */
public abstract class AbstractInventoryTreePanel extends Panel {
	/**
	 * 
	 */
	private static final long		serialVersionUID	= -2299283711321904639L;
	private static final Logger	log					= LoggerFactory.getLogger(AbstractInventoryTreePanel.class);

	@SpringBean(name = Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService		iInventoryService;

	private List<InvSite>			invSites				= new ArrayList<InvSite>(0);

	protected ArkBusyAjaxButton	addSite;
	protected ArkBusyAjaxButton	addFreezer;
	protected ArkBusyAjaxButton	addRack;
	protected ArkBusyAjaxButton	addBox;

	public AbstractInventoryTreePanel(String id) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
		initialiseButtons();
	}

	/**
	 * Returns the tree on this pages. This is used to collapse, expand the tree and to switch the rootless mode.
	 * 
	 * @return Tree instance on this page
	 */
	protected abstract AbstractTree getTree();

	/**
	 * Creates the model that feeds the tree.
	 * 
	 * @return New instance of tree model.
	 */
	protected TreeModel createTreeModel() {
		InvSite invSite = new InvSite();

		try {
			invSites = iInventoryService.searchInvSite(invSite);
		}
		catch (ArkSystemException e) {
			log.error(e.getMessage());
		}
		return convertToTreeModel();
	}

	private TreeModel convertToTreeModel() {
		TreeModel model = null;
		// Default root node (set to not show)
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new TreeNodeModel("ROOT"));
		add(rootNode, invSites);
		model = new DefaultTreeModel(rootNode);
		return model;
	}

	@SuppressWarnings("unchecked")
	private void add(DefaultMutableTreeNode parentNode, List<? extends InvTreeNode> childNodes) {
		for (InvTreeNode node : childNodes) {
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new InventoryModel(node, node.getNodeType()));
			parentNode.add(childNode);

			// If no children, don't bother
			if (node.getChildren() != null && !node.getChildren().isEmpty()) {
				add(childNode, node.getChildren());
			}
		}
	}

	private void initialiseButtons() {
		addSite = new ArkBusyAjaxButton("addSite") {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public boolean isVisible() {
				return ArkPermissionHelper.isActionPermitted(Constants.SAVE);
			}

			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onAddSiteSubmit(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				this.error("Unexpected error: Unable to process Add Site button");
			}

		};

		addFreezer = new ArkBusyAjaxButton("addFreezer") {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public boolean isVisible() {
				return ArkPermissionHelper.isActionPermitted(Constants.SAVE);
			}

			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onAddFreezerSubmit(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				this.error("Unexpected error: Unable to process Add Freezer button");
			}

		};

		addRack = new ArkBusyAjaxButton("addRack") {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public boolean isVisible() {
				return ArkPermissionHelper.isActionPermitted(Constants.SAVE);
			}

			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onAddRackSubmit(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				this.error("Unexpected error: Unable to process Add Rack button");
			}

		};

		addBox = new ArkBusyAjaxButton("addBox") {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public boolean isVisible() {
				return ArkPermissionHelper.isActionPermitted(Constants.SAVE);
			}

			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onAddBoxSubmit(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				this.error("Unexpected error: Unable to process Add Box button");
			}

		};
	}

	/**
	 * Method implemented by sub-classes to perform action on button press
	 * 
	 * @param target
	 */
	public abstract void onAddSiteSubmit(AjaxRequestTarget target);

	/**
	 * Method implemented by sub-classes to perform action on button press
	 * 
	 * @param target
	 */
	public abstract void onAddFreezerSubmit(AjaxRequestTarget target);

	/**
	 * Method implemented by sub-classes to perform action on button press
	 * 
	 * @param target
	 */
	public abstract void onAddRackSubmit(AjaxRequestTarget target);

	/**
	 * Method implemented by sub-classes to perform action on button press
	 * 
	 * @param target
	 */
	public abstract void onAddBoxSubmit(AjaxRequestTarget target);

}
