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
package au.org.theark.study.web.component.subject;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.study.web.component.subject.form.ContainerForm;
import au.org.theark.study.web.component.subject.form.DetailForm;

/**
 * @author nivedann
 * 
 */
public class DetailPanel extends Panel {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5633334040450350186L;
	private DetailForm			detailsForm;
	private FeedbackPanel		feedBackPanel;
	private WebMarkupContainer	arkContextContainer;
	private ContainerForm		containerForm;
	private ArkCrudContainerVO arkCrudContainerVO;

	public DetailPanel(String id, FeedbackPanel feedBackPanel, WebMarkupContainer arkContextContainer, ContainerForm containerForm,ArkCrudContainerVO arkCrudContainerVO) {
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.arkContextContainer = arkContextContainer;
		this.containerForm = containerForm;
		this.arkCrudContainerVO = arkCrudContainerVO;
	}

	public void initialisePanel() {

		detailsForm = new DetailForm("detailsForm", feedBackPanel, arkContextContainer, containerForm,arkCrudContainerVO);
		detailsForm.initialiseDetailForm();
		add(detailsForm);
	}

}
