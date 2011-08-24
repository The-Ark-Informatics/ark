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
import javax.swing.tree.TreeNode;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.markup.html.tree.LinkIconPanel;
import org.apache.wicket.markup.html.tree.LinkTree;
import org.apache.wicket.model.IModel;

import au.org.theark.core.model.lims.entity.InvBox;
import au.org.theark.core.model.lims.entity.InvSite;
import au.org.theark.core.model.lims.entity.InvTank;
import au.org.theark.core.model.lims.entity.InvTray;
import au.org.theark.lims.model.InventoryModel;
import au.org.theark.lims.web.component.inventory.form.ContainerForm;
import au.org.theark.lims.web.component.inventory.panel.box.BoxDetailPanel;
import au.org.theark.lims.web.component.inventory.panel.site.SiteDetailPanel;
import au.org.theark.lims.web.component.inventory.panel.tank.TankDetailPanel;
import au.org.theark.lims.web.component.inventory.panel.tray.TrayDetailPanel;

public class InventoryLinkTree extends LinkTree {
	/**
	 * 
	 */
	private static final long					serialVersionUID	= -3736908668279170191L;

	private static final ResourceReference	STUDY_ICON			= new ResourceReference(InventoryLinkTree.class, "study.gif");
	private static final ResourceReference	SITE_ICON			= new ResourceReference(InventoryLinkTree.class, "site.gif");
	private static final ResourceReference	EMPTY_TANK_ICON	= new ResourceReference(InventoryLinkTree.class, "empty_tank.gif");
	private static final ResourceReference	GREEN_TANK_ICON	= new ResourceReference(InventoryLinkTree.class, "green_tank.gif");
	private static final ResourceReference	YELLOW_TANK_ICON	= new ResourceReference(InventoryLinkTree.class, "yellow_tank.gif");
	private static final ResourceReference	FULL_TANK_ICON		= new ResourceReference(InventoryLinkTree.class, "full_tank.gif");

	private static final ResourceReference	EMPTY_BOX_ICON		= new ResourceReference(InventoryLinkTree.class, "empty_box.gif");
	private static final ResourceReference	GREEN_BOX_ICON		= new ResourceReference(InventoryLinkTree.class, "green_box.gif");
	private static final ResourceReference	YELLOW_BOX_ICON	= new ResourceReference(InventoryLinkTree.class, "yellow_box.gif");
	private static final ResourceReference	FULL_BOX_ICON		= new ResourceReference(InventoryLinkTree.class, "full_box.gif");
	
	private FeedbackPanel feedbackPanel;
	private WebMarkupContainer detailContainer;
	private ContainerForm containerForm;

	public InventoryLinkTree(String id, TreeModel model, FeedbackPanel feedbackPanel, WebMarkupContainer detailContainer, ContainerForm containerForm) {
		super(id, model);
		this.feedbackPanel = feedbackPanel;
		this.detailContainer = detailContainer;
		this.containerForm = containerForm;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Component newNodeComponent(String id, IModel model) {

		// Override standard node creation so we can setup our own component
		// This kind of blows since if the standard LinkTree implementation changes we
		// may be hooped, pooched, or even twaddled
		final LinkIconPanel panel = new LinkIconPanel(id, model, this) {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onNodeLinkClicked(Object node, BaseTree tree, AjaxRequestTarget target) {
				super.onNodeLinkClicked(node, tree, target);
				InventoryLinkTree.this.onNodeLinkClicked(node, tree, target);
			}

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

			/**
			 * Determine what icon to display on the node
			 * 
			 * @param object
			 *           the referece object of the node
			 * @return resourceReference to the icon for the node in question
			 */
			private ResourceReference getIconResourceReference(Object object) {
				final DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) object;
				ResourceReference resourceReference = STUDY_ICON;

				if (defaultMutableTreeNode.getUserObject() instanceof InventoryModel) {
					final InventoryModel inventoryModel = (InventoryModel) defaultMutableTreeNode.getUserObject();

					if (inventoryModel.getObject() instanceof InvSite) {
						resourceReference = SITE_ICON;
					}
					if (inventoryModel.getObject() instanceof InvTank) {
						InvTank invTank = (InvTank) inventoryModel.getObject();
						resourceReference = getTankIcon(invTank.getAvailable(), invTank.getCapacity());
					}
					if (inventoryModel.getObject() instanceof InvTray) {
						InvTray invTray = (InvTray) inventoryModel.getObject();
						resourceReference = getTankIcon(invTray.getAvailable(), invTray.getCapacity());
					}
					if (inventoryModel.getObject() instanceof InvBox) {
						InvBox invBox = (InvBox) inventoryModel.getObject();
						resourceReference = getBoxIcon(invBox.getAvailable(), invBox.getCapacity());
					}
				}
				return resourceReference;
			}

			protected ResourceReference getTankIcon(float available, float capacity) {
				float result = available / capacity;
				
				if (result == 0) {
					return FULL_TANK_ICON;
				}
				else if ((result > 0) && (result < 0.5)) {
					return YELLOW_TANK_ICON;
				}
				else if ((result >= 0.5) && (result < 1)) {
					return GREEN_TANK_ICON;
				}
				else {
					return EMPTY_TANK_ICON;
				}
			}

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

			protected void addComponents(final IModel model, final BaseTree tree) {
				final BaseTree.ILinkCallback callback = new BaseTree.ILinkCallback() {
					private static final long	serialVersionUID	= 1L;

					public void onClick(AjaxRequestTarget target) {
						onNodeLinkClicked(model.getObject(), tree, target);
					}
				};

				MarkupContainer link = tree.newLink("iconLink", callback);
				add(link);
				link.add(newImageComponent("icon", tree, model));

				link = tree.newLink("contentLink", callback);
				add(link);
				link.add(newContentComponent("content", tree, model));
			}
		};

		return panel;
	}

	/**
	 * Expand or collapse a node if the user clicks on a node (in addition to the +/- signs)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void onNodeLinkClicked(Object object, BaseTree tree, AjaxRequestTarget target) {
		final TreeNode node = (TreeNode) object;
		final DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) object;
		
		if (defaultMutableTreeNode.getUserObject() instanceof InventoryModel) {
			final InventoryModel inventoryModel = (InventoryModel) defaultMutableTreeNode.getUserObject();

			if (inventoryModel.getObject() instanceof InvSite) {
				InvSite invSite = (InvSite) inventoryModel.getObject();
				
				containerForm.getModelObject().setInvSite(invSite);
				
				SiteDetailPanel detailPanel = new SiteDetailPanel("detailPanel", feedbackPanel, detailContainer, containerForm, tree);
				detailPanel.initialisePanel();
				
				detailContainer.addOrReplace(detailPanel);
				detailContainer.setVisible(true);
			}
			if (inventoryModel.getObject() instanceof InvTank) {
				InvTank invTank = (InvTank) inventoryModel.getObject();
				
				containerForm.getModelObject().setInvTank(invTank);
				
				TankDetailPanel detailPanel = new TankDetailPanel("detailPanel", feedbackPanel, detailContainer, containerForm, tree);
				detailPanel.initialisePanel();
				
				detailContainer.addOrReplace(detailPanel);
				detailContainer.setVisible(true);
			}
			if (inventoryModel.getObject() instanceof InvTray) {
				InvTray invTray = (InvTray) inventoryModel.getObject();
				
				containerForm.getModelObject().setInvTray(invTray);
				
				TrayDetailPanel detailPanel = new TrayDetailPanel("detailPanel", feedbackPanel, detailContainer, containerForm, tree);
				detailPanel.initialisePanel();
				
				detailContainer.addOrReplace(detailPanel);
				detailContainer.setVisible(true);
			}
			if (inventoryModel.getObject() instanceof InvBox) {
				InvBox invBox = (InvBox) inventoryModel.getObject();
				
				containerForm.getModelObject().setInvBox(invBox);
				
				BoxDetailPanel detailPanel = new BoxDetailPanel("detailPanel", feedbackPanel, detailContainer, containerForm, tree);
				detailPanel.initialisePanel();
				
				detailContainer.addOrReplace(detailPanel);
				detailContainer.setVisible(true);
			}
		}

		if (!node.isLeaf()) {
			if (tree.getTreeState().isNodeExpanded(node)) {
				tree.getTreeState().collapseNode(node);
			}
			else {
				tree.getTreeState().expandNode(node);
			}
			// and else here would handle your leaf node
			tree.updateTree(target);
		}
		
		target.addComponent(feedbackPanel);
		target.addComponent(detailContainer);
	}
}