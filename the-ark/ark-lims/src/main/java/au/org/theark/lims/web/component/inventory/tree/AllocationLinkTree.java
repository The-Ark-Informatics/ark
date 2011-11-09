/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.lims.web.component.inventory.tree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.markup.html.tree.LinkIconPanel;
import org.apache.wicket.markup.html.tree.LinkTree;
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
import au.org.theark.lims.model.InventoryModel;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.web.Constants;

/**
 * 
 * @author cellis
 *
 */
public abstract class AllocationLinkTree<T> extends LinkTree {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6241958621459969476L;
	private static final PackageResourceReference	STUDY_ICON			= new PackageResourceReference(AllocationLinkTree.class, "study.gif");
	private static final PackageResourceReference	SITE_ICON			= new PackageResourceReference(AllocationLinkTree.class, "site.gif");
	private static final PackageResourceReference	EMPTY_FREEZER_ICON	= new PackageResourceReference(AllocationLinkTree.class, "empty_tank.gif");
	private static final PackageResourceReference	GREEN_FREEZER_ICON	= new PackageResourceReference(AllocationLinkTree.class, "green_tank.gif");
	private static final PackageResourceReference	YELLOW_FREEZER_ICON	= new PackageResourceReference(AllocationLinkTree.class, "yellow_tank.gif");
	private static final PackageResourceReference	FULL_FREEZER_ICON		= new PackageResourceReference(AllocationLinkTree.class, "full_tank.gif");

	private static final PackageResourceReference	EMPTY_BOX_ICON		= new PackageResourceReference(AllocationLinkTree.class, "empty_box.gif");
	private static final PackageResourceReference	GREEN_BOX_ICON		= new PackageResourceReference(AllocationLinkTree.class, "green_box.gif");
	private static final PackageResourceReference	YELLOW_BOX_ICON	= new PackageResourceReference(AllocationLinkTree.class, "yellow_box.gif");
	private static final PackageResourceReference	FULL_BOX_ICON		= new PackageResourceReference(AllocationLinkTree.class, "full_box.gif");
	
	@SpringBean(name = Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService					iInventoryService;
	
	private FeedbackPanel feedbackPanel;
	private IModel<T> propertyModel;

	public AllocationLinkTree(String id, TreeModel model, FeedbackPanel feedbackPanel, IModel<T> propertyModel) {
		super(id, model);
		this.feedbackPanel = feedbackPanel;
		this.propertyModel = propertyModel;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Component newNodeComponent(String id, IModel model) {
		// Override standard node creation so we can setup our own component
		// This kind of blows if the standard LinkTree implementation changes
		final LinkIconPanel panel = new LinkIconPanel(id, model, this) {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onNodeLinkClicked(Object node, BaseTree tree, AjaxRequestTarget target) {
				AllocationLinkTree.this.onNodeLinkClicked(node, tree, target);
			}

			@Override
			protected Component newContentComponent(String componentId, BaseTree tree, IModel model) {
				return new Label(componentId, getNodeTextModel(model));
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

			@Override
			protected void addComponents(final IModel model, final BaseTree tree) {
				MarkupContainer link = new ArkBusyAjaxLink("iconLink"){
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1857552996891982138L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						onNodeLinkClicked(model.getObject(), tree, target);
					}
				};
				add(link);
				
				link.add(newImageComponent("icon", tree, model));

				link = new ArkBusyAjaxLink("contentLink"){
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1857552996891982138L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						onNodeLinkClicked(model.getObject(), tree, target);
					}
				};
				add(link);
				
				link.add(newContentComponent("content", tree, model));
			}
		};
		
		// Add tooltip to the node
		addToolTipToNode(panel, model.getObject()); 

		return panel;
	}

	/**
	 * Adds a tooltip to the tree node/panel
	 * @param panel
	 * @param object
	 */
	@SuppressWarnings("unchecked")
	private void addToolTipToNode(LinkIconPanel panel, Object object) {
		final DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) object;
		final StringBuffer stringBuffer = new StringBuffer();

		if (defaultMutableTreeNode.getUserObject() instanceof InventoryModel) {
			final InventoryModel inventoryModel = (InventoryModel) defaultMutableTreeNode.getUserObject();

			if (inventoryModel.getObject() instanceof InvSite) {
				InvSite nodeObject = (InvSite) inventoryModel.getObject();
				stringBuffer.append(nodeObject.getName()); 
				stringBuffer.append("\t");
			}
			if (inventoryModel.getObject() instanceof InvFreezer) {
				InvFreezer nodeObject = (InvFreezer) inventoryModel.getObject();
				stringBuffer.append(nodeObject.getName());
				stringBuffer.append("\t");
				stringBuffer.append(percentUsed(nodeObject.getAvailable(), nodeObject.getCapacity()));
			}
			if (inventoryModel.getObject() instanceof InvRack) {
				InvRack nodeObject = (InvRack) inventoryModel.getObject();
				stringBuffer.append(nodeObject.getName());
				stringBuffer.append("\t");
				stringBuffer.append(percentUsed(nodeObject.getAvailable(), nodeObject.getCapacity()));
			}
			if (inventoryModel.getObject() instanceof InvBox) {
				InvBox nodeObject = (InvBox) inventoryModel.getObject();
				stringBuffer.append(nodeObject.getName());
				stringBuffer.append("\t");
				stringBuffer.append(percentUsed(nodeObject.getAvailable(), nodeObject.getCapacity()));
			}
		}
		
		String toolTip = stringBuffer.toString();
		panel.add(new AttributeModifier("showtooltip", new Model<Boolean>(true)));
		panel.add(new AttributeModifier("title", new Model<String>(toolTip)));
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
	 * @param available
	 * @param capacity
	 * @return
	 */
	private float calculatePercentUsed(float available, float capacity) {
		float percentUsed = ((capacity - available) / capacity) * 100;
		percentUsed = Math.round(percentUsed);
		return percentUsed;
	}

	/**
	 * Expand or collapse a node if the user clicks on a node (in addition to the +/- signs)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void onNodeLinkClicked(Object object, BaseTree tree, AjaxRequestTarget target) {
		final DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
		final LimsVO limsVo = (LimsVO) propertyModel.getObject();
		
		if (node.getUserObject() instanceof InventoryModel) {
			final InventoryModel inventoryModel = (InventoryModel) node.getUserObject();

			
			if (inventoryModel.getObject() instanceof InvBox) {
				InvBox invBox = (InvBox) inventoryModel.getObject();
				// Get object from database again, to be sure of persistence
				invBox = iInventoryService.getInvBox(invBox.getId());
				limsVo.setInvBox(invBox);
				boxNodeClicked(target, invBox);
			}
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
	}
	
	public abstract void boxNodeClicked(AjaxRequestTarget target, InvBox invBox);

	/**
	 * Determine what icon to display on the node
	 * 
	 * @param object
	 *           the reference object of the node
	 * @return resourceReference to the icon for the node in question
	 */
	@SuppressWarnings("unchecked")
	private ResourceReference getIconResourceReference(Object object) {
		final DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) object;
		ResourceReference resourceReference = STUDY_ICON;

		if (defaultMutableTreeNode.getUserObject() instanceof InventoryModel) {
			final InventoryModel inventoryModel = (InventoryModel) defaultMutableTreeNode.getUserObject();

			if (inventoryModel.getObject() instanceof InvSite) {
				resourceReference = SITE_ICON;
			}
			if (inventoryModel.getObject() instanceof InvFreezer) {
				InvFreezer invTank = (InvFreezer) inventoryModel.getObject();
				resourceReference = getTankIcon(invTank.getAvailable(), invTank.getCapacity());
			}
			if (inventoryModel.getObject() instanceof InvRack) {
				InvRack invTray = (InvRack) inventoryModel.getObject();
				resourceReference = getTankIcon(invTray.getAvailable(), invTray.getCapacity());
			}
			if (inventoryModel.getObject() instanceof InvBox) {
				InvBox invBox = (InvBox) inventoryModel.getObject();
				resourceReference = getBoxIcon(invBox.getAvailable(), invBox.getCapacity());
			}
		}
		return resourceReference;
	}

	/**
	 * Get the Tank/Site empty/half/nearly full/full icon, determined by the (available/capacity) ratio
	 * @param available
	 * @param capacity
	 * @return
	 */
	protected ResourceReference getTankIcon(float available, float capacity) {
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
}