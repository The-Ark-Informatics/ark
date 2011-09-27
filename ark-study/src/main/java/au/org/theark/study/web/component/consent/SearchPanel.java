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
package au.org.theark.study.web.component.consent;

import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.ConsentVO;
import au.org.theark.study.web.component.consent.form.SearchForm;

/**
 * @author Nivedan
 * 
 */
@SuppressWarnings("serial")
public class SearchPanel extends Panel {

	private ArkCrudContainerVO				arkCrudContainerVO;

	private FeedbackPanel					feedBackPanel;
	private PageableListView<Consent>	pageableListView;

	/* Constructor */
	public SearchPanel(String id, FeedbackPanel feedBackPanel, PageableListView<Consent> pageableListView, ArkCrudContainerVO arkCrudContainerVO) {
		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.pageableListView = pageableListView;
		this.feedBackPanel = feedBackPanel;
	}

	public void initialisePanel(CompoundPropertyModel<ConsentVO> consentVOCpm) {

		SearchForm searchForm = new SearchForm(au.org.theark.core.Constants.SEARCH_FORM, pageableListView, feedBackPanel, consentVOCpm, arkCrudContainerVO);

		add(searchForm);
	}
}
