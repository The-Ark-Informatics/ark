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
package au.org.theark.lims.web.component.inventory.panel.tank;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.tree.BaseTree;

import au.org.theark.lims.web.component.inventory.form.ContainerForm;
import au.org.theark.lims.web.component.inventory.form.TankDetailForm;

@SuppressWarnings("serial")
public class TankDetailPanel extends Panel {
	private FeedbackPanel		feedbackPanel;
	private WebMarkupContainer	detailContainer;
	private TankDetailForm		detailForm;
	private ContainerForm		containerForm;
	private BaseTree 				tree;

	public TankDetailPanel(String id, FeedbackPanel feedbackPanel, WebMarkupContainer detailContainer, ContainerForm containerForm, BaseTree tree) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
		this.feedbackPanel = feedbackPanel;
		this.detailContainer = detailContainer;
		this.containerForm = containerForm;
		this.tree = tree;
	}

	public void initialisePanel() {
		detailForm = new TankDetailForm("detailForm", feedbackPanel, detailContainer, containerForm, tree);
		detailForm.initialiseDetailForm();
		add(detailForm);
	}

	public TankDetailForm getDetailForm() {
		return detailForm;
	}

	public void setDetailForm(TankDetailForm detailForm) {
		this.detailForm = detailForm;
	}
}