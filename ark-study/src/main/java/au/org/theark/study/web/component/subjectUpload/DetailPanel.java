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
package au.org.theark.study.web.component.subjectUpload;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.study.web.component.subjectUpload.form.ContainerForm;
import au.org.theark.study.web.component.subjectUpload.form.DetailForm;

@SuppressWarnings("serial")
public class DetailPanel extends Panel {
	
	private ArkCrudContainerVO arkCrudContainerVO;
	private DetailForm			detailForm;
	private FeedbackPanel		feedBackPanel;
	private ContainerForm		containerForm;
	private ArkFunction arkFunction;

	public DetailPanel(String id, FeedbackPanel feedBackPanel, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO,ArkFunction arkFunction) {
		super(id);
		this.feedBackPanel = feedBackPanel;
		this.containerForm = containerForm;
		this.arkCrudContainerVO = arkCrudContainerVO;
	}

	public void initialisePanel() {
		detailForm = new DetailForm("detailForm", feedBackPanel, containerForm, arkCrudContainerVO,arkFunction);

		detailForm.initialiseDetailForm();
		add(detailForm);
	}
}
