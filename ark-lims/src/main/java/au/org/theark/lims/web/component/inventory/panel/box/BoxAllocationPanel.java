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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.web.component.AbstractDetailModalWindow;
import au.org.theark.lims.web.component.inventory.form.BoxAllocationDetailForm;
import au.org.theark.lims.web.component.inventory.form.ContainerForm;
import au.org.theark.lims.web.component.inventory.panel.box.display.GridBoxPanel;
import au.org.theark.lims.web.component.inventory.tree.InventoryLinkTree;

/**
 * 
 * @author cellis
 * 
 */
public class BoxAllocationPanel extends Panel {

	private static final long						serialVersionUID	= -1L;
	private static final Logger					log					= LoggerFactory.getLogger(BoxAllocationPanel.class);

	private FeedbackPanel					feedbackPanel;
	private WebMarkupContainer				detailContainer;
	private BoxAllocationDetailForm		detailForm;
	private ContainerForm					containerForm;
	private InventoryLinkTree				tree;
	private DefaultMutableTreeNode		node;
	private GridBoxPanel						gridBoxPanel;
	private AbstractDetailModalWindow	modalWindow;

	public BoxAllocationPanel(String id, FeedbackPanel feedbackPanel, WebMarkupContainer detailContainer, ContainerForm containerForm, InventoryLinkTree tree, DefaultMutableTreeNode node) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
		this.feedbackPanel = feedbackPanel;
		this.detailContainer = detailContainer;
		this.containerForm = containerForm;
		this.tree = tree;
		this.node = node;
	}

	public void initialisePanel() {
		modalWindow = new AbstractDetailModalWindow("detailModalWindow") {


			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onCloseModalWindow(AjaxRequestTarget target) {
				target.add(feedbackPanel);
				target.add(detailForm);
				target.add(gridBoxPanel);
			}
		};
		gridBoxPanel = new GridBoxPanel("gridBoxPanel", containerForm.getModelObject(), modalWindow, false);
		detailForm = new BoxAllocationDetailForm("detailForm", feedbackPanel, detailContainer, containerForm, tree, node, gridBoxPanel, modalWindow);
		detailForm.initialiseDetailForm();
		
		add(detailForm);
		add(gridBoxPanel);
		add(modalWindow);
	}

	public BoxAllocationDetailForm getDetailForm() {
		return detailForm;
	}

	public void setDetailForm(BoxAllocationDetailForm detailForm) {
		this.detailForm = detailForm;
	}

	public static Logger getLog() {
		return log;
	}
}