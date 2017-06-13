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
package au.org.theark.report.web.component.dataextraction.filter;


import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.QueryFilterListVO;
import au.org.theark.report.web.component.dataextraction.filter.form.QueryFilterForm;

public class QueryFilterPanel extends Panel {

	private static final long						serialVersionUID	= 7224168117680252835L;
	protected FeedbackPanel							feedbackPanel;
	private QueryFilterForm					form;

	public QueryFilterPanel(String id, FeedbackPanel feedbackPanel, IModel<QueryFilterListVO> model, ModalWindow modalWindow,ArkCrudContainerVO arkCrudContainerVO) {
		super(id, model);
		this.feedbackPanel = feedbackPanel;
		initialisePanel(model, modalWindow,arkCrudContainerVO);
		setOutputMarkupPlaceholderTag(true);
	}

	public void initialisePanel(IModel<QueryFilterListVO> model, ModalWindow modalWindow,ArkCrudContainerVO arkCrudContainerVO) {
		form = new QueryFilterForm("queryFilterForm", model, modalWindow,arkCrudContainerVO);
		form.initialiseForm();
		add(form);
	}

	/**
	 * @return the listDetailForm
	 */
	public QueryFilterForm getListDetailForm() {
		return form;
	}

	/**
	 * @param listDetailForm the listDetailForm to set
	 */
	public void setListDetailForm(QueryFilterForm listDetailForm) {
		this.form = listDetailForm;
	}
}
