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
package au.org.theark.phenotypic.web.component.summaryModule;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;

import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.web.component.summaryModule.form.ContainerForm;

public class SummaryContainerPanel extends AbstractContainerPanel<PhenoCollectionVO> {
	private static final long						serialVersionUID	= 1L;

	// Panels
	private SearchPanel								searchComponentPanel;
	private SearchResultListPanel					searchResultPanel;
	private DetailPanel								detailPanel;
	private PageableListView<PhenoCollection>	listView;
	private ContainerForm							containerForm;

	public SummaryContainerPanel(String id) {
		super(id);

		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<PhenoCollectionVO>(new PhenoCollectionVO());

		initialiseMarkupContainers();

		/* Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
		add(containerForm);
	}

	protected WebMarkupContainer initialiseSearchResults() {
		searchResultPanel = new SearchResultListPanel("searchResults", detailPanelContainer, searchPanelContainer, containerForm, searchResultPanelContainer, detailPanel, viewButtonContainer,
				editButtonContainer, detailPanelFormContainer);

		iModel = new LoadableDetachableModel<Object>() {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load() {
				return containerForm.getModelObject().getPhenoCollectionCollection();
			}
		};

		listView = searchResultPanel.buildPageableListView(iModel);
		listView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", listView);
		searchResultPanel.add(pageNavigator);
		searchResultPanel.add(listView);
		searchResultPanelContainer.add(searchResultPanel);

		// for Summary Module, disable search result list
		searchResultPanel.setVisible(false);

		return searchResultPanelContainer;
	}

	protected WebMarkupContainer initialiseDetailPanel() {
		detailPanel = new DetailPanel("detailPanel", searchResultPanelContainer, feedBackPanel, detailPanelContainer, searchPanelContainer, containerForm, viewButtonContainer, editButtonContainer,
				detailPanelFormContainer);
		detailPanel.initialisePanel();
		detailPanelContainer.add(detailPanel);
		return detailPanelContainer;
	}

	protected WebMarkupContainer initialiseSearchPanel() {
		// For Summary Module, no results ever displayed
		searchComponentPanel = new SearchPanel("searchPanel", feedBackPanel, searchPanelContainer, listView, searchResultPanelContainer, detailPanelContainer, detailPanel, containerForm,
				viewButtonContainer, editButtonContainer, detailPanelFormContainer);
		searchComponentPanel.initialisePanel();

		searchPanelContainer.add(searchComponentPanel);
		return searchPanelContainer;
	}
}
