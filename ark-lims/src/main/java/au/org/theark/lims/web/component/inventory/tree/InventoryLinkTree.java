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

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.tree.LinkTree;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.lims.entity.InvBox;
import au.org.theark.core.model.lims.entity.InvFreezer;
import au.org.theark.core.model.lims.entity.InvRack;
import au.org.theark.core.model.lims.entity.InvSite;
import au.org.theark.core.model.lims.entity.InvTreeNode;
import au.org.theark.lims.model.InventoryModel;
import au.org.theark.lims.model.TreeNodeModel;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.inventory.form.ContainerForm;

public class InventoryLinkTree extends LinkTree {
	/**
	 * 
	 */
	private static final long					serialVersionUID	= -3736908668279170191L;
	private static final Logger	log					= LoggerFactory.getLogger(InventoryLinkTree.class);
	
	@SpringBean(name = Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService					iInventoryService;
	
	private List<InvSite>			invSites				= new ArrayList<InvSite>(0);
	
	private FeedbackPanel feedbackPanel;
	private WebMarkupContainer detailContainer;
	private ContainerForm containerForm;

	public InventoryLinkTree(String id, TreeModel model, FeedbackPanel feedbackPanel, WebMarkupContainer detailContainer, ContainerForm containerForm) {
		super(id, model);
		this.feedbackPanel = feedbackPanel;
		this.detailContainer = detailContainer;
		this.containerForm = containerForm;
		createTreeModel();
	}
	
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
		MutableTreeNode rootNode = new MutableTreeNode(new TreeNodeModel("ROOT"));
		//add(rootNode, invSites);
		addSites(rootNode, invSites);
		model = new DefaultTreeModel(rootNode);
		return model;
	}

	void addSites(DefaultMutableTreeNode parentNode, List<InvSite> sites) {
		for (InvSite site : sites){
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(site);
			parentNode.add(childNode);
			addFreezers(childNode, site.getInvFreezers());
		}
	}
	
	void addFreezers(DefaultMutableTreeNode parentNode, List<InvFreezer> freezers){
		for (InvFreezer freezer : freezers){
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(freezer);
			parentNode.add(childNode);
			addRacks(childNode, freezer.getInvRacks());
		}
	}
	
	void addRacks(DefaultMutableTreeNode parentNode, List<InvRack> racks){
		for (InvRack rack : racks){
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(rack);
			parentNode.add(childNode);
			addBoxes(childNode, rack.getInvBoxes());
		}
	}

	void addBoxes(DefaultMutableTreeNode parentNode, List<InvBox> boxes){
		for (InvBox box : boxes){
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(box);
			parentNode.add(childNode);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Component newNodeComponent(String id, IModel model) {
		InventoryNodePanel panel = new InventoryNodePanel(id, model, InventoryLinkTree.this, feedbackPanel, detailContainer, containerForm);
		return panel;
	}	
}