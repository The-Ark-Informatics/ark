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
package au.org.theark.lims.web.component.inventory.panel.box;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.tree.BaseTree;

import au.org.theark.core.model.lims.entity.InvBox;
import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.lims.web.component.inventory.form.BoxDetailForm;
import au.org.theark.lims.web.component.inventory.form.ContainerForm;
import au.org.theark.lims.web.component.inventory.panel.box.display.GridBoxPanel;

public class BoxDetailPanel extends Panel {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7132411215567811297L;
	private FeedbackPanel					feedbackPanel;
	private WebMarkupContainer				detailContainer;
	private BoxDetailForm					detailForm;
	private ContainerForm					containerForm;
	private BaseTree							tree;
	private DefaultMutableTreeNode		node;
	private GridBoxPanel						gridBoxPanel;
	private AbstractDetailModalWindow	modalWindow;

	public BoxDetailPanel(String id, FeedbackPanel feedbackPanel, WebMarkupContainer detailContainer, ContainerForm containerForm, BaseTree tree, DefaultMutableTreeNode node) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
		this.feedbackPanel = feedbackPanel;
		this.detailContainer = detailContainer;
		this.containerForm = containerForm;
		this.tree = tree;
		this.node = node;
	}

	public void initialisePanel() {		
		detailForm = new BoxDetailForm("detailForm", feedbackPanel, detailContainer, containerForm, tree, node);
		detailForm.initialiseDetailForm();

		modalWindow = new AbstractDetailModalWindow("detailModalWindow") {

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onCloseModalWindow(AjaxRequestTarget target) {
				// Repaint gridBoxPanel
				target.add(gridBoxPanel);
			}
		};
		
		InvBox invBox = containerForm.getModelObject().getInvBox();
		gridBoxPanel = new GridBoxPanel("gridBoxPanel", invBox, invBox.getName(), modalWindow);
		
		add(detailForm);
		add(gridBoxPanel);
		add(modalWindow);
	}

	public BoxDetailForm getDetailForm() {
		return detailForm;
	}

	public void setDetailForm(BoxDetailForm detailForm) {
		this.detailForm = detailForm;
	}
}