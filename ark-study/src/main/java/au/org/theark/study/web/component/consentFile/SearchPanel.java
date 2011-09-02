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
package au.org.theark.study.web.component.consentFile;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.study.entity.ConsentFile;
import au.org.theark.core.vo.ConsentVO;
import au.org.theark.study.web.component.consentFile.form.ContainerForm;
import au.org.theark.study.web.component.consentFile.form.SearchForm;

/**
 * @author cellis
 * 
 */
@SuppressWarnings("serial")
public class SearchPanel extends Panel {

	private FeedbackPanel						feedBackPanel;
	private WebMarkupContainer					searchMarkupContainer;
	private WebMarkupContainer					listContainer;
	private WebMarkupContainer					detailsContainer;
	private WebMarkupContainer					viewButtonContainer;
	private WebMarkupContainer					editButtonContainer;
	private WebMarkupContainer					detailFormContainer;
	private PageableListView<ConsentFile>	pageableListView;

	/* Constructor */
	public SearchPanel(String id, FeedbackPanel feedBackPanel, WebMarkupContainer searchMarkupContainer, PageableListView<ConsentFile> listView, WebMarkupContainer resultListContainer,
			WebMarkupContainer detailPanelContainer, DetailPanel detail, ContainerForm containerForm, WebMarkupContainer viewButtonContainer, WebMarkupContainer editButtonContainer,
			WebMarkupContainer detailPanelFormContainer) {
		super(id);
		this.searchMarkupContainer = searchMarkupContainer;
		this.pageableListView = listView;
		this.feedBackPanel = feedBackPanel;
		this.listContainer = resultListContainer;
		this.detailsContainer = detailPanelContainer;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.detailFormContainer = detailPanelFormContainer;
	}

	public void initialisePanel(CompoundPropertyModel<ConsentVO> consentVOCpm) {

		SearchForm searchForm = new SearchForm(au.org.theark.core.Constants.SEARCH_FORM, consentVOCpm, pageableListView, feedBackPanel, listContainer, searchMarkupContainer, detailsContainer,
				detailFormContainer, viewButtonContainer, editButtonContainer);

		add(searchForm);
	}
}
