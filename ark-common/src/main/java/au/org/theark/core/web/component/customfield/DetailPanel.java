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
package au.org.theark.core.web.component.customfield;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.web.component.customfield.form.DetailForm;

/**
 * @author elam
 * 
 */
public class DetailPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private DetailForm			detailForm;
	private FeedbackPanel		feedbackPanel;

	private CompoundPropertyModel<CustomFieldVO> cpModel;
	private ArkCrudContainerVO arkCrudContainerVO;

	public DetailPanel(String id, FeedbackPanel feedbackPanel, CompoundPropertyModel<CustomFieldVO> cpModel, ArkCrudContainerVO arkCrudContainerVO) {
		super(id, cpModel);
		this.feedbackPanel = feedbackPanel;
		this.cpModel = cpModel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.setOutputMarkupPlaceholderTag(true);
		
		initialisePanel();
	}

	public void initialisePanel() {
		detailForm = new DetailForm("detailForm", cpModel, feedbackPanel, arkCrudContainerVO);
		detailForm.initialiseDetailForm();
		add(detailForm);
	}

	public DetailForm getDetailForm() {
		return detailForm;
	}

	public void setDetailForm(DetailForm detailsForm) {
		this.detailForm = detailsForm;
	}
}
