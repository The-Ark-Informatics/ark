package au.org.theark.lims.web.component.inventory.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.markup.html.tree.LinkIconPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.lims.entity.InvBox;
import au.org.theark.core.model.lims.entity.InvFreezer;
import au.org.theark.core.model.lims.entity.InvRack;
import au.org.theark.core.model.lims.entity.InvSite;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.inventory.form.ContainerForm;
import au.org.theark.lims.web.component.inventory.panel.box.BoxDetailPanel;
import au.org.theark.lims.web.component.inventory.panel.freezer.FreezerDetailPanel;
import au.org.theark.lims.web.component.inventory.panel.rack.RackDetailPanel;
import au.org.theark.lims.web.component.inventory.panel.site.SiteDetailPanel;

public class InventoryNodePanel extends LinkIconPanel {
	private static final PackageResourceReference	STUDY_ICON				= new PackageResourceReference(InventoryNodePanel.class, "study.gif");
	private static final PackageResourceReference	SITE_ICON				= new PackageResourceReference(InventoryNodePanel.class, "site.gif");
	private static final PackageResourceReference	EMPTY_FREEZER_ICON	= new PackageResourceReference(InventoryNodePanel.class, "empty_tank.gif");
	private static final PackageResourceReference	GREEN_FREEZER_ICON	= new PackageResourceReference(InventoryNodePanel.class, "green_tank.gif");
	private static final PackageResourceReference	YELLOW_FREEZER_ICON	= new PackageResourceReference(InventoryNodePanel.class, "yellow_tank.gif");
	private static final PackageResourceReference	FULL_FREEZER_ICON		= new PackageResourceReference(InventoryNodePanel.class, "full_tank.gif");

	private static final PackageResourceReference	EMPTY_BOX_ICON			= new PackageResourceReference(InventoryNodePanel.class, "empty_box.gif");
	private static final PackageResourceReference	GREEN_BOX_ICON			= new PackageResourceReference(InventoryNodePanel.class, "green_box.gif");
	private static final PackageResourceReference	YELLOW_BOX_ICON		= new PackageResourceReference(InventoryNodePanel.class, "yellow_box.gif");
	private static final PackageResourceReference	FULL_BOX_ICON			= new PackageResourceReference(InventoryNodePanel.class, "full_box.gif");

	private FeedbackPanel									feedbackPanel;
	private WebMarkupContainer								detailContainer;
	private ContainerForm									containerForm;

	@SpringBean(name = Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService								iInventoryService;
	private InventoryLinkTree tree;


	private static final long								serialVersionUID		= 1L;

	public InventoryNodePanel(String id, IModel<Object> model, InventoryLinkTree tree, FeedbackPanel feedbackPanel, WebMarkupContainer detailContainer, ContainerForm containerForm) {
		super(id, model, tree);
		setOutputMarkupPlaceholderTag(true);

		this.tree = tree;
		this.feedbackPanel = feedbackPanel;
		this.detailContainer = detailContainer;
		this.containerForm = containerForm;

		// Add tooltip to the node/panel
		addToolTipToNode(model.getObject());
	}

	/**
	 * Adds a tooltip to the tree node/panel
	 * 
	 * @param object
	 */
	private void addToolTipToNode(Object object) {
		final DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) object;
		final StringBuffer stringBuffer = new StringBuffer();

		if (treeNode.getUserObject() instanceof InvSite) {
			InvSite nodeObject = (InvSite) treeNode.getUserObject();
			nodeObject = iInventoryService.getInvSite(nodeObject.getId());
			stringBuffer.append(nodeObject.getName());
			stringBuffer.append("\t");
		}
		if (treeNode.getUserObject() instanceof InvFreezer) {
			InvFreezer nodeObject = (InvFreezer) treeNode.getUserObject();
			nodeObject = iInventoryService.getInvFreezer(nodeObject.getId());
			stringBuffer.append(nodeObject.getName());
			stringBuffer.append("\t");
			stringBuffer.append(percentUsed(nodeObject.getAvailable(), nodeObject.getCapacity()));
		}
		if (treeNode.getUserObject() instanceof InvRack) {
			InvRack nodeObject = (InvRack) treeNode.getUserObject();
			nodeObject = iInventoryService.getInvRack(nodeObject.getId());
			stringBuffer.append(nodeObject.getName());
			stringBuffer.append("\t");
			stringBuffer.append(percentUsed(nodeObject.getAvailable(), nodeObject.getCapacity()));
		}
		if (treeNode.getUserObject() instanceof InvBox) {
			InvBox nodeObject = (InvBox) treeNode.getUserObject();
			nodeObject = iInventoryService.getInvBox(nodeObject.getId());

			stringBuffer.append(nodeObject.getName());
			stringBuffer.append("\t");
			stringBuffer.append(percentUsed(nodeObject.getAvailable(), nodeObject.getCapacity()));
		}

		String toolTip = stringBuffer.toString();
		add(new AttributeModifier("showtooltip", new Model<Boolean>(true)));
		add(new AttributeModifier("title", new Model<String>(toolTip)));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Component newContentComponent(String componentId, BaseTree tree, IModel model) {
		final DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) model.getObject();
		String name = new String();

		if (treeNode.getUserObject() instanceof InvSite) {
			InvSite invSite = (InvSite) treeNode.getUserObject();
			invSite = iInventoryService.getInvSite(invSite.getId());
			name = invSite.getName();
		}
		if (treeNode.getUserObject() instanceof InvFreezer) {
			InvFreezer invFreezer = (InvFreezer) treeNode.getUserObject();
			invFreezer = iInventoryService.getInvFreezer(invFreezer.getId());
			name = invFreezer.getName();
		}
		if (treeNode.getUserObject() instanceof InvRack) {
			InvRack invRack = (InvRack) treeNode.getUserObject();
			invRack = iInventoryService.getInvRack(invRack.getId());
			name = invRack.getName();
		}
		if (treeNode.getUserObject() instanceof InvBox) {
			InvBox invBox = (InvBox) treeNode.getUserObject();
			invBox = iInventoryService.getInvBox(invBox.getId());
			name = invBox.getName();
		}

		return new Label(componentId, name);
	}

	@Override
	protected Component newImageComponent(String componentId, final BaseTree tree, final IModel<Object> model) {
		return new Image(componentId) {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected ResourceReference getImageResourceReference() {
				return getIconResourceReference(model.getObject());
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addComponents(final IModel model, final BaseTree tree) {
		MarkupContainer link = new ArkBusyAjaxLink("iconLink") {

			private static final long	serialVersionUID	= 1857552996891982138L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				onNodeLinkClicked(model.getObject(), tree, target);
			}
		};
		add(link);

		link.add(newImageComponent("icon", tree, model));

		link = new ArkBusyAjaxLink<String>("contentLink") {

			private static final long	serialVersionUID	= 1857552996891982138L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				onNodeLinkClicked(model.getObject(), tree, target);
			}
		};
		add(link);

		link.add(newContentComponent("content", this.tree, model));
	}

	/**
	 * Expand or collapse a node if the user clicks on a node (in addition to the +/- signs)
	 */
	@Override
	protected void onNodeLinkClicked(Object object, BaseTree tree, AjaxRequestTarget target) {
		final DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;

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

			detailContainer.addOrReplace(detailPanel);
			detailContainer.setVisible(true);
		}

		if (!node.isLeaf()) {
			if (tree.getTreeState().isNodeExpanded(node)) {
				tree.getTreeState().collapseNode(node);
			}
			else {
				tree.getTreeState().expandNode(node);
			}
		}

		// Handle node clicked (highlight)
		tree.getTreeState().selectNode(node, true);
		tree.updateTree(target);

		target.add(feedbackPanel);
		target.add(detailContainer);
	}

	/**
	 * Determine what icon to display on the node
	 * 
	 * @param object
	 *           the reference object of the node
	 * @return resourceReference to the icon for the node in question
	 */
	private ResourceReference getIconResourceReference(Object object) {
		final DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
		ResourceReference resourceReference = STUDY_ICON;

		if (node.getUserObject() instanceof InvSite) {
			resourceReference = SITE_ICON;
		}
		if (node.getUserObject() instanceof InvFreezer) {
			InvFreezer invFreezer = (InvFreezer) node.getUserObject();
			invFreezer = iInventoryService.getInvFreezer(invFreezer.getId());
			if(invFreezer.getId() != null) {
				resourceReference = getFreezerIcon(invFreezer.getAvailable(), invFreezer.getCapacity());
			}
		}
		if (node.getUserObject() instanceof InvRack) {
			InvRack invRack = (InvRack) node.getUserObject();
			invRack = iInventoryService.getInvRack(invRack.getId());
			if(invRack.getId() != null) {
				resourceReference = getFreezerIcon(invRack.getAvailable(), invRack.getCapacity());
			}
		}
		if (node.getUserObject() instanceof InvBox) {
			InvBox invBox = (InvBox) node.getUserObject();
			invBox = iInventoryService.getInvBox(invBox.getId());
			if(invBox.getId() != null) {
				resourceReference = getBoxIcon(invBox.getAvailable(), invBox.getCapacity());
			}
		}
		return resourceReference;
	}

	/**
	 * Get the Tank/Site empty/half/nearly full/full icon, determined by the (available/capacity) ratio
	 * 
	 * @param available
	 * @param capacity
	 * @return
	 */
	protected ResourceReference getFreezerIcon(float available, float capacity) {
		float result = available / capacity;

		if (result == 0) {
			return FULL_FREEZER_ICON;
		}
		else if ((result > 0) && (result < 0.5)) {
			return YELLOW_FREEZER_ICON;
		}
		else if ((result >= 0.5) && (result < 1)) {
			return GREEN_FREEZER_ICON;
		}
		else {
			return EMPTY_FREEZER_ICON;
		}
	}

	/**
	 * Get the Box empty/half/nearly full/full icon, determined by the (available/capacity) ratio
	 * 
	 * @param available
	 * @param capacity
	 * @return
	 */
	protected ResourceReference getBoxIcon(float available, float capacity) {
		float result = available / capacity;

		if (result == 0) {
			return FULL_BOX_ICON;
		}
		else if ((result > 0) && (result < 0.5)) {
			return YELLOW_BOX_ICON;
		}
		else if ((result >= 0.5) && (result < 1)) {
			return GREEN_BOX_ICON;
		}
		else {
			return EMPTY_BOX_ICON;
		}
	}

	/**
	 * 
	 * @param available
	 * @param capacity
	 * @return
	 */
	private String percentUsed(float available, float capacity) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("(Used: ");
		stringBuffer.append("\t");
		stringBuffer.append(calculatePercentUsed(available, capacity));
		stringBuffer.append("%)");
		return stringBuffer.toString();
	}

	/**
	 * Calculates the percent used of the tank/shelf/box
	 * 
	 * @param available
	 * @param capacity
	 * @return
	 */
	private float calculatePercentUsed(float available, float capacity) {
		float percentUsed = ((capacity - available) / capacity) * 100;
		percentUsed = Math.round(percentUsed);
		return percentUsed;
	}
}
