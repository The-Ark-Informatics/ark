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
package au.org.theark.admin.web.component.role;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.admin.web.component.ContainerForm;
import au.org.theark.admin.web.component.role.form.DetailForm;
import au.org.theark.core.vo.ArkCrudContainerVO;

public class DetailPanel extends Panel {

	private static final long	serialVersionUID	= -2562093040192063621L;
	private FeedbackPanel		feedbackPanel;
	private ContainerForm		containerForm;
	private DetailForm			detailForm;
	private ArkCrudContainerVO	arkCrudContainerVo;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedbackPanel
	 * @param studyCrudContainerVO
	 * @param containerForm
	 */
	public DetailPanel(String id, FeedbackPanel feedbackPanel, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVo) {
		super(id);
		this.feedbackPanel = feedbackPanel;
		this.containerForm = containerForm;
		this.arkCrudContainerVo = arkCrudContainerVo;
	}

	public void initialisePanel() {
		detailForm = new DetailForm("detailForm", feedbackPanel, containerForm, arkCrudContainerVo);
		detailForm.initialiseDetailForm();
		add(detailForm); // Add the form to the panel
	}
}
