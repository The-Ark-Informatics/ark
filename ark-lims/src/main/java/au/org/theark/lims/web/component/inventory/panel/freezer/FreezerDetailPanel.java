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
package au.org.theark.lims.web.component.inventory.panel.freezer;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.model.lims.entity.InvRack;
import au.org.theark.core.web.component.button.ArkBusyAjaxButton;
import au.org.theark.lims.web.component.inventory.form.ContainerForm;
import au.org.theark.lims.web.component.inventory.form.FreezerDetailForm;
import au.org.theark.lims.web.component.inventory.panel.rack.RackDetailPanel;
import au.org.theark.lims.web.component.inventory.tree.InventoryLinkTree;

@SuppressWarnings("serial")
public class FreezerDetailPanel extends Panel {
	private FeedbackPanel				feedbackPanel;
	private WebMarkupContainer			detailContainer;
	private FreezerDetailForm				detailForm;
	private ContainerForm				containerForm;
	private InventoryLinkTree			tree;
	private DefaultMutableTreeNode	node;
	private AjaxButton					addRack;

	public FreezerDetailPanel(String id, FeedbackPanel feedbackPanel, WebMarkupContainer detailContainer, ContainerForm containerForm, InventoryLinkTree tree, DefaultMutableTreeNode node) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
		this.feedbackPanel = feedbackPanel;
		this.detailContainer = detailContainer;
		this.containerForm = containerForm;
		this.tree = tree;
		this.node = node;
	}

	public void initialisePanel() {
		detailForm = new FreezerDetailForm("detailForm", feedbackPanel, detailContainer, containerForm, tree, node);
		detailForm.initialiseDetailForm();
		
		addRack = new ArkBusyAjaxButton("addRack") {
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				containerForm.getModelObject().setInvRack(new InvRack());
				containerForm.getModelObject().getInvRack().setInvFreezer(containerForm.getModelObject().getInvFreezer());
				RackDetailPanel rackDetailPanel = new RackDetailPanel("detailPanel", feedbackPanel, detailContainer, containerForm, tree, node); 
				rackDetailPanel.initialisePanel();
				
				FreezerDetailPanel.this.replaceWith(rackDetailPanel);
				target.add(detailContainer);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}
			
			@Override
			public boolean isEnabled() {
				return containerForm.getModelObject().getInvFreezer().getAvailable() != null && containerForm.getModelObject().getInvFreezer().getAvailable() > 0;
			}
		};
		
		add(detailForm);
		add(addRack);
	}

	public FreezerDetailForm getDetailForm() {
		return detailForm;
	}

	public void setDetailForm(FreezerDetailForm detailForm) {
		this.detailForm = detailForm;
	}
}