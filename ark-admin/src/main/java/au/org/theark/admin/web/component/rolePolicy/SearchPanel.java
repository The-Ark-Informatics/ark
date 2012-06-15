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
package au.org.theark.admin.web.component.rolePolicy;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.admin.web.component.ContainerForm;
import au.org.theark.admin.web.component.rolePolicy.form.SearchForm;
import au.org.theark.core.Constants;
import au.org.theark.core.vo.ArkCrudContainerVO;

public class SearchPanel extends Panel {

	private static final long					serialVersionUID	= -2689739291154679914L;
	private ContainerForm						containerForm;
	private FeedbackPanel						feedBackPanel;
	private CompoundPropertyModel<AdminVO>	cpmModel;
	private ArkCrudContainerVO					arkCrudContainerVo;

	/**
	 * Constructor That uses the VO
	 * 
	 * @param id
	 * @param studyCrudContainerVO
	 */
	public SearchPanel(String id, FeedbackPanel feedbackPanel, ContainerForm containerForm, CompoundPropertyModel<AdminVO> cpmModel, ArkCrudContainerVO arkCrudContainerVo) {

		super(id);
		this.containerForm = containerForm;
		this.feedBackPanel = feedbackPanel;
		this.cpmModel = cpmModel;
		this.arkCrudContainerVo = arkCrudContainerVo;
	}

	public void initialisePanel() {
		SearchForm searchForm = new SearchForm(Constants.SEARCH_FORM, cpmModel, arkCrudContainerVo, feedBackPanel, containerForm);
		add(searchForm);
	}
}
