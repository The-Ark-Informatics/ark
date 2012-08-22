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
package au.org.theark.lims.web.component.inventory.panel.site;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.Constants;
import au.org.theark.core.model.lims.entity.InvFreezer;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.web.component.button.ArkBusyAjaxButton;
import au.org.theark.lims.web.component.inventory.form.ContainerForm;
import au.org.theark.lims.web.component.inventory.form.SiteDetailForm;
import au.org.theark.lims.web.component.inventory.panel.freezer.FreezerDetailPanel;
import au.org.theark.lims.web.component.inventory.tree.InventoryLinkTree;

@SuppressWarnings("serial")
public class SiteDetailPanel extends Panel {
	private FeedbackPanel				feedbackPanel;
	private WebMarkupContainer			detailContainer;
	private SiteDetailForm				detailForm;
	private ContainerForm				containerForm;
	private InventoryLinkTree			tree;
	private DefaultMutableTreeNode	node;
	private AjaxButton					addFreezer;

	public SiteDetailPanel(String id, FeedbackPanel feedbackPanel, WebMarkupContainer detailContainer, ContainerForm containerForm, InventoryLinkTree tree, DefaultMutableTreeNode node) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
		this.feedbackPanel = feedbackPanel;
		this.detailContainer = detailContainer;
		this.containerForm = containerForm;
		this.tree = tree;
		this.node = node;
	}

	public void initialisePanel() {
		detailForm = new SiteDetailForm("detailForm", feedbackPanel, detailContainer, containerForm, tree, node);
		detailForm.initialiseDetailForm();
		
		addFreezer = new ArkBusyAjaxButton("addFreezer") {
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				containerForm.getModelObject().setInvFreezer(new InvFreezer());
				containerForm.getModelObject().getInvFreezer().setInvSite(containerForm.getModelObject().getInvSite());
				FreezerDetailPanel freezerDetailPanel = new FreezerDetailPanel("detailPanel", feedbackPanel, detailContainer, containerForm, tree, node); 
				freezerDetailPanel.initialisePanel();
				
				SiteDetailPanel.this.replaceWith(freezerDetailPanel);
				target.add(detailContainer);
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}
			
			@Override
			public boolean isVisible() {
				return super.isVisible() && ArkPermissionHelper.isActionPermitted(Constants.SAVE);
			}
		};
		add(detailForm);
		add(addFreezer);
	}

	public SiteDetailForm getDetailForm() {
		return detailForm;
	}

	public void setDetailForm(SiteDetailForm detailForm) {
		this.detailForm = detailForm;
	}
}