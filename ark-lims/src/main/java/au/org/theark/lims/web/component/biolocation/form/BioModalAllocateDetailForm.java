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
package au.org.theark.lims.web.component.biolocation.form;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.lims.entity.InvBox;
import au.org.theark.core.model.lims.entity.InvSite;
import au.org.theark.core.model.lims.entity.InvTreeNode;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.lims.model.InventoryModel;
import au.org.theark.lims.model.TreeNodeModel;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.web.component.inventory.panel.box.display.GridBoxPanel;
import au.org.theark.lims.web.component.inventory.tree.AllocationLinkTree;

/**
 * Detail form for Biospecimen allocation, as displayed within a modal window
 * 
 * @author cellis
 */
public class BioModalAllocateDetailForm extends Form<LimsVO> {
	/**
	 * 
	 */
	private static final long				serialVersionUID	= 9211020895417833151L;
	private static final Logger			log					= LoggerFactory.getLogger(BioModalAllocateDetailForm.class);

	@SpringBean(name = au.org.theark.lims.web.Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService				iInventoryService;

	private CompoundPropertyModel<LimsVO> cpModel;
	private AbstractDetailModalWindow modalWindow;
	private final BaseTree					tree;
	private List<InvSite>					invSites				= new ArrayList<InvSite>(0);
	private Panel								gridBoxPanel;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVo
	 * @param modalWindow
	 * @param listDetailPanel
	 */
	public BioModalAllocateDetailForm(String id, FeedbackPanel feedBackPanel, final ArkCrudContainerVO arkCrudContainerVo, final AbstractDetailModalWindow modalWindow,
			final CompoundPropertyModel<LimsVO> cpModel) {
		super(id, cpModel);
		this.cpModel = cpModel;
		this.modalWindow = modalWindow;

		tree = new AllocationLinkTree<LimsVO>("tree", createTreeModel(), feedBackPanel, cpModel) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public void boxNodeClicked(AjaxRequestTarget target, InvBox invBox) {
				cpModel.getObject().setInvBox(invBox);
				repaintGridBoxPanel(target);
			}
		};
		tree.getTreeState().collapseAll();
		tree.setRootLess(true);
		
		gridBoxPanel = new GridBoxPanel("gridBoxPanel", cpModel.getObject(), modalWindow, true);
		gridBoxPanel.setVisible(false);
	}
	
	public void initialiseDetailForm() {
		addComponents();
	}

	private void addComponents() {
		add(tree);
		add(gridBoxPanel);
	}

	protected void repaintGridBoxPanel(AjaxRequestTarget target) {
		gridBoxPanel = new GridBoxPanel("gridBoxPanel", cpModel.getObject(), modalWindow, true);
		gridBoxPanel.setVisible(true);
		addOrReplace(gridBoxPanel);
		target.add(gridBoxPanel);
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
}