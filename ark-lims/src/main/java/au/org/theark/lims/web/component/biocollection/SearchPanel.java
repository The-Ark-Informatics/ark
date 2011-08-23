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
package au.org.theark.lims.web.component.biocollection;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.web.component.biocollection.form.ContainerForm;
import au.org.theark.lims.web.component.biocollection.form.SearchForm;

/**
 * @author cellis
 * 
 */
@SuppressWarnings("serial")
public class SearchPanel extends Panel {
	private FeedbackPanel							feedBackPanel;
	private WebMarkupContainer						searchMarkupContainer;
	private WebMarkupContainer						listContainer;
	private WebMarkupContainer						detailContainer;
	private PageableListView<BioCollection>	listView;
	private ContainerForm							containerForm;
	private DetailPanel								detailPanel;
	private WebMarkupContainer						viewButtonContainer;
	private WebMarkupContainer						editButtonContainer;
	private WebMarkupContainer						detailPanelFormContainer;
	private WebMarkupContainer						arkContextMarkup;

	/* Constructor */
	public SearchPanel(String id, FeedbackPanel feedBackPanel, WebMarkupContainer searchMarkupContainer, PageableListView<BioCollection> listView, WebMarkupContainer resultListContainer,
			WebMarkupContainer detailPanelContainer, DetailPanel detail, ContainerForm containerForm, WebMarkupContainer viewButtonContainer, WebMarkupContainer editButtonContainer,
			WebMarkupContainer detailPanelFormContainer, WebMarkupContainer arkContextMarkup) {
		super(id);
		this.searchMarkupContainer = searchMarkupContainer;
		this.listView = listView;
		this.feedBackPanel = feedBackPanel;
		this.detailContainer = detailPanelContainer;
		this.detailPanel = detail;
		this.containerForm = containerForm;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.detailPanelFormContainer = detailPanelFormContainer;
		this.arkContextMarkup = arkContextMarkup;
		listContainer = resultListContainer;
	}

	public void initialisePanel() {
		SearchForm searchForm = new SearchForm(au.org.theark.core.Constants.SEARCH_FORM, (CompoundPropertyModel<LimsVO>) containerForm.getModel(), listView, feedBackPanel, detailPanel, listContainer,
				searchMarkupContainer, detailContainer, detailPanelFormContainer, viewButtonContainer, editButtonContainer, arkContextMarkup);

		add(searchForm);
	}
}
