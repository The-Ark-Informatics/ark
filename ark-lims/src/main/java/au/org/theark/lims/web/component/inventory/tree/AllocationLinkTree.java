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

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.lims.entity.InvBox;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.web.Constants;

/**
 * 
 * @author cellis
 *
 */
public abstract class AllocationLinkTree extends InventoryLinkTree {


	private static final long	serialVersionUID	= -6241958621459969476L;
	
	@SpringBean(name = Constants.LIMS_INVENTORY_SERVICE)
	private IInventoryService					iInventoryService;

	public AllocationLinkTree(String id) {
		super(id, null, null, null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Component newNodeComponent(String id, IModel model) {
		InventoryNodePanel panel = new InventoryNodePanel(id, model, AllocationLinkTree.this, null, null, null){

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onNodeLinkClicked(Object object, BaseTree tree, AjaxRequestTarget target) {
				final DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;

				if (node.getUserObject() instanceof InvBox) {
					InvBox invBox = (InvBox) node.getUserObject();
					// Get object from database again, to be sure of persistence
					invBox = iInventoryService.getInvBox(invBox.getId());
					boxNodeClicked(target, invBox);
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
			}
		};
		return panel;
	}

	public abstract void boxNodeClicked(AjaxRequestTarget target, InvBox invBox);
}