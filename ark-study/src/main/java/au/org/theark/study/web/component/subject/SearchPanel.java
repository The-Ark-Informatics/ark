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

import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.subject.form.ContainerForm;
import au.org.theark.study.web.component.subject.form.SearchForm;

/**
 * @author nivedann
 * 
 */
public class SearchPanel extends Panel {

	private FeedbackPanel					feedBackPanel;
	private PageableListView<SubjectVO>	listView;
	private ArkCrudContainerVO arkCrudContainerVO;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param searchMarkupContainer
	 * @param listView
	 * @param resultListContainer
	 * @param detailPanelContainer
	 * @param detailFormContainer
	 * @param viewButtonContainer
	 * @param editButtonContainer
	 * @param detailPanel
	 * @param containerForm
	 */
	public SearchPanel(String id, FeedbackPanel feedBackPanel,PageableListView<SubjectVO> listView,ArkCrudContainerVO arkCrudContainerVO,	ContainerForm containerForm) {
		super(id);
		this.listView = listView;
		this.feedBackPanel = feedBackPanel;
		this.arkCrudContainerVO = arkCrudContainerVO;
	}

	public void initialisePanel(CompoundPropertyModel<SubjectVO> subjectVoCpm) {

		SearchForm searchStudyCompForm = new SearchForm(Constants.SEARCH_FORM, subjectVoCpm, listView, feedBackPanel,arkCrudContainerVO);
		add(searchStudyCompForm);

	}

}
