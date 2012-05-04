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
package au.org.theark.lims.web.component.subjectlims.subject;

import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.subjectlims.subject.form.ContainerForm;
import au.org.theark.lims.web.component.subjectlims.subject.form.SearchForm;

/**
 * 
 * @author cellis
 *
 */
public class SearchPanel extends Panel {


	private static final long	serialVersionUID	= 5699910763818733372L;
	private FeedbackPanel					feedBackPanel;
	private PageableListView<LimsVO>		listView;
	private ArkCrudContainerVO				arkCrudContainerVO;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param listView
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public SearchPanel(String id, FeedbackPanel feedBackPanel, PageableListView<LimsVO> listView, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {
		super(id);
		this.listView = listView;
		this.feedBackPanel = feedBackPanel;
		this.arkCrudContainerVO = arkCrudContainerVO;
	}

	public void initialisePanel(CompoundPropertyModel<LimsVO> limsVoCpm) {
		SearchForm searchForm = new SearchForm(Constants.SEARCH_FORM, limsVoCpm, listView, feedBackPanel, arkCrudContainerVO);
		add(searchForm);
	}
}
