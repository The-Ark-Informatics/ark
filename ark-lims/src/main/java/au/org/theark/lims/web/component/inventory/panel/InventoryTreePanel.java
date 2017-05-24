package au.org.theark.lims.web.component.inventory.panel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.lims.entity.InvBox;
import au.org.theark.core.model.lims.entity.InvCell;
import au.org.theark.core.model.lims.entity.InvFreezer;
import au.org.theark.core.model.lims.entity.InvRack;
import au.org.theark.core.model.lims.entity.InvSite;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.session.ArkSession;
import au.org.theark.core.vo.LimsVO;
import au.org.theark.core.web.component.button.ArkBusyAjaxButton;
import au.org.theark.lims.service.IInventoryService;
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
	
	@SpringBean(name = Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService					iInventoryService;
	
	@SuppressWarnings("unused")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>						iArkCommonService;
	
	private Form<Void>				treeForm				= new Form<Void>("treeForm");
	private FeedbackPanel			feedbackPanel;
	private WebMarkupContainer		detailContainer;
	private ContainerForm			containerForm;
	
	protected ArkBusyAjaxButton	addSite;
	protected ArkBusyAjaxButton	addFreezer;
	protected ArkBusyAjaxButton	enableAllEmptyCells;
	protected ArkBusyAjaxButton	addRack;
	protected ArkBusyAjaxButton	addBox;
	
	protected InventoryLinkTree   tree;
	protected DefaultTreeModel 	treeModel;

	public InventoryTreePanel(String id, FeedbackPanel feedbackPanel, WebMarkupContainer detailContainer, ContainerForm containerForm, DefaultTreeModel treeModel) {
		super(id);
		this.feedbackPanel = feedbackPanel;
		this.detailContainer = detailContainer;
		this.containerForm = containerForm;
		this.treeModel = treeModel;

		tree = new InventoryLinkTree("tree", feedbackPanel, detailContainer, containerForm, treeModel);
		tree.setRootLess(true);
		
	// Study in context
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = null;
		if(sessionStudyId != null) {
			study = iArkCommonService.getStudy(sessionStudyId);
			containerForm.getModelObject().setStudy(study);
		}
		
		setOutputMarkupPlaceholderTag(true);
		initialiseButtons();
		addComponents();

		checkForSelectedNode();
	}
	
	private void checkForSelectedNode() {
		// Check session has contained a node and highlight the node.
		Object prevSelectedNode=ArkSession.get().getNodeObject();
		DefaultMutableTreeNode prevNode = ((DefaultMutableTreeNode) prevSelectedNode);
		if(prevSelectedNode!=null){
			// Added to logically expand/select the node in session
			TreeNode[] nodes = prevNode.getPath();
			for (int i = 0; i < nodes.length; i++) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodes[i];
				if (!node.isLeaf()) {
					if (tree.getTreeState().isNodeExpanded(node)) {
						tree.getTreeState().collapseNode(node);
					}
					else {
						tree.getTreeState().expandNode(node);
					}
				}
			}
			
			tree.getTreeState().selectNode(prevSelectedNode, true);
			tree.updateTree();
			
			DefaultMutableTreeNode node = prevNode;
			if (node.getUserObject() instanceof InvSite) {
				InvSite invSite = (InvSite) node.getUserObject();
				// Get object from database again, to be sure of persistence
				invSite = iInventoryService.getInvSite(invSite.getId());
				containerForm.getModelObject().setInvSite(invSite);
				containerForm.getModelObject().setSelectedStudies(invSite.getStudies());

				SiteDetailPanel detailPanel = new SiteDetailPanel("detailPanel", feedbackPanel, detailContainer, containerForm, this.tree, node);
				detailPanel.initialisePanel();

				detailContainer.addOrReplace(detailPanel);
				detailContainer.setVisible(true);
			}
			if (node.getUserObject() instanceof InvFreezer) {
				InvFreezer invFreezer = (InvFreezer) node.getUserObject();

				containerForm.getModelObject().setInvFreezer(invFreezer);

				FreezerDetailPanel detailPanel = new FreezerDetailPanel("detailPanel", feedbackPanel, detailContainer, containerForm, this.tree, node);
				detailPanel.initialisePanel();

				detailContainer.addOrReplace(detailPanel);
				detailContainer.setVisible(true);
			}
			if (node.getUserObject() instanceof InvRack) {
				InvRack invRack = (InvRack) node.getUserObject();
				// Get object from database again, to be sure of persistence
				invRack = iInventoryService.getInvRack(invRack.getId());
				containerForm.getModelObject().setInvRack(invRack);

				RackDetailPanel detailPanel = new RackDetailPanel("detailPanel", feedbackPanel, detailContainer, containerForm, this.tree, node);
				detailPanel.initialisePanel();

				detailContainer.addOrReplace(detailPanel);
				detailContainer.setVisible(true);
			}
			if (node.getUserObject() instanceof InvBox) {
				InvBox invBox = (InvBox) node.getUserObject();
				// Get object from database again, to be sure of persistence
				invBox = iInventoryService.getInvBox(invBox.getId());
				containerForm.getModelObject().setInvBox(invBox);

				BoxDetailPanel detailPanel = new BoxDetailPanel("detailPanel", feedbackPanel, detailContainer, containerForm, this.tree, node);
				detailPanel.initialisePanel();
				
				// Handle for no cells in InvCell table!
				List<InvCell> invCellList = new ArrayList<InvCell>(0);
				invCellList = invBox.getInvCells();
				int cells = invBox.getNoofcol() * invBox.getNoofrow();
				if(invCellList == null || invCellList.isEmpty()){
					//refresh from db?
					invCellList = iInventoryService.getCellAndBiospecimenListByBox(invBox);
				}
				if (invCellList.size() != cells) {
					this.error("The Box with ID: " + invBox.getId() + " is missing cell information. Please contact support.");
					detailPanel.setEnabled(false);
					AjaxRequestTarget target = getRequestCycle().find(AjaxRequestTarget.class);
					target.add(feedbackPanel);
				}

				detailContainer.addOrReplace(detailPanel);
				detailContainer.setVisible(true);
			}
		}
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
	}

	private void initialiseButtons() {
		addSite = new ArkBusyAjaxButton("addSite") {


			private static final long	serialVersionUID	= 1L;

			@Override
			public boolean isVisible() {
				SecurityManager securityManager = ThreadContext.getSecurityManager();
				Subject currentUser = SecurityUtils.getSubject();
				return securityManager.hasRole(currentUser.getPrincipals(), au.org.theark.core.security.RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR);
			}

			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onAddSiteSubmit(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				this.error("An unexpected error occurred. Unable to process Add Site button.");
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
				this.error("An unexpected error occurred. Unable to process Add Freezer button.");
			}

		};

		enableAllEmptyCells = new ArkBusyAjaxButton("enableAllEmptyCells") {


			private static final long	serialVersionUID	= 1L;

			@Override
			public boolean isVisible() {
				return ArkPermissionHelper.isActionPermitted(Constants.SAVE);
			}

			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onEnableAllEmptyCellsSubmit(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				this.error("An unexpected error occurred. Unable to process Enabling of all empty cells.");
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
				this.error("An unexpected error occurred. Unable to process Add Rack button.");
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
				this.error("An unexpected error occurred. Unable to process Add Box button.");
			}

		};
	}

	private void addComponents() {
		treeForm.addOrReplace(addSite);
		treeForm.addOrReplace(addFreezer);
		treeForm.addOrReplace(enableAllEmptyCells);
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


	public void onEnableAllEmptyCellsSubmit(AjaxRequestTarget target) {
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = null;
		if(sessionStudyId != null) {
			study = iArkCommonService.getStudy(sessionStudyId);
		}
		
		String successFailureMessage = iInventoryService.fillOutAllBoxesWithEmptyInvCellsToCapacity(study);
		log.info("attempted to fill empty cells...this message returned: \n\n" + successFailureMessage);
		
		resetModel();

//		FreezerDetailPanel detailPanel = new FreezerDetailPanel("detailPanel", feedbackPanel, detailContainer, containerForm, tree, null);
//		detailPanel.initialisePanel();
		//TODO : simple message saying DONT DO ANYTHING FOR TEN MINUTES

//		refreshDetailPanel(target, detailPanel);
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
		
		// Study in context
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = null;
		if(sessionStudyId != null) {
			study = iArkCommonService.getStudy(sessionStudyId);
			limsVo.setStudy(study);
		}
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