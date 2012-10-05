package au.org.theark.lims.web.component.inventory.panel;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.session.ArkSession;
import au.org.theark.core.web.component.button.ArkBusyAjaxButton;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.inventory.form.ContainerForm;
import au.org.theark.lims.web.component.inventory.panel.box.BoxDetailPanel;
import au.org.theark.lims.web.component.inventory.panel.freezer.FreezerDetailPanel;
import au.org.theark.lims.web.component.inventory.panel.rack.RackDetailPanel;
import au.org.theark.lims.web.component.inventory.panel.site.SiteDetailPanel;
import au.org.theark.lims.web.component.inventory.tree.InventoryLinkTree;

public class InventoryTreePanel extends Panel {


	private static final long		serialVersionUID	= -6281929692337423853L;
	private static final Logger	log					= LoggerFactory.getLogger(InventoryTreePanel.class);
	private Form<Void>				treeForm				= new Form<Void>("treeForm");
	private FeedbackPanel			feedbackPanel;
	private WebMarkupContainer		detailContainer;
	private ContainerForm			containerForm;
	
	protected ArkBusyAjaxButton	addSite;
	protected ArkBusyAjaxButton	addFreezer;
	protected ArkBusyAjaxButton	addRack;
	protected ArkBusyAjaxButton	addBox;
	
	protected InventoryLinkTree   tree;

	public InventoryTreePanel(String id, FeedbackPanel feedbackPanel, WebMarkupContainer detailContainer, ContainerForm containerForm) {
		super(id);
		this.feedbackPanel = feedbackPanel;
		this.detailContainer = detailContainer;
		this.containerForm = containerForm;

		tree = new InventoryLinkTree("tree", feedbackPanel, detailContainer, containerForm);
		tree.setRootLess(true);
		
		setOutputMarkupPlaceholderTag(true);
		initialiseButtons();
		addComponents();
		
		//Check seassion has contained a node and highlight the node.
		Object prevSelectedNode=ArkSession.get().getNodeObject();
		if(prevSelectedNode!=null){
			this.tree.getTreeState().selectNode(prevSelectedNode, true);
			this.tree.updateTree();
		}
	}

	private void initialiseButtons() {
		addSite = new ArkBusyAjaxButton("addSite") {


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

	private void addComponents() {
		treeForm.addOrReplace(addSite);
		treeForm.addOrReplace(addFreezer);
		treeForm.addOrReplace(addRack);
		treeForm.addOrReplace(addBox);

		addOrReplace(treeForm);
		addOrReplace(tree);
	}

	public void onAddSiteSubmit(AjaxRequestTarget target) {
		resetModel();

		SiteDetailPanel detailPanel = new SiteDetailPanel("detailPanel", feedbackPanel, detailContainer, containerForm, tree, (DefaultMutableTreeNode) null);
		detailPanel.initialisePanel();

		refreshDetailPanel(target, detailPanel);
	}

	public void onAddFreezerSubmit(AjaxRequestTarget target) {
		resetModel();

		FreezerDetailPanel detailPanel = new FreezerDetailPanel("detailPanel", feedbackPanel, detailContainer, containerForm, tree, null);
		detailPanel.initialisePanel();

		refreshDetailPanel(target, detailPanel);
	}

	public void onAddRackSubmit(AjaxRequestTarget target) {
		resetModel();

		RackDetailPanel detailPanel = new RackDetailPanel("detailPanel", feedbackPanel, detailContainer, containerForm, tree, null);
		detailPanel.initialisePanel();

		refreshDetailPanel(target, detailPanel);
	}

	public void onAddBoxSubmit(AjaxRequestTarget target) {
		resetModel();

		BoxDetailPanel detailPanel = new BoxDetailPanel("detailPanel", feedbackPanel, detailContainer, containerForm, tree, null);
		detailPanel.initialisePanel();

		refreshDetailPanel(target, detailPanel);
	}

	/**
	 * Reset the model object to a new LimsVO
	 */
	public void resetModel() {
		LimsVO limsVo = new LimsVO();
		containerForm.setModelObject(limsVo);
	}

	/**
	 * Refresh/repaint the detailPanel
	 * 
	 * @param target
	 * @param detailPanel
	 */
	public void refreshDetailPanel(AjaxRequestTarget target, Panel detailPanel) {
		detailContainer.addOrReplace(detailPanel);
		detailContainer.setVisible(true);

		target.add(feedbackPanel);
		target.add(detailContainer);
		target.add(detailPanel);
	}

	public static Logger getLog() {
		return log;
	}
}