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
package au.org.theark.geno.web.component.genoCollection;

import java.util.Collection;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import au.org.theark.core.model.geno.entity.GenoCollection;
import au.org.theark.geno.web.component.genoCollection.form.ContainerForm;

/**
 * @author elam
 *
 */
public class GenoCRUDWebUIMgr {
	
	/* Panels */
	protected FeedbackPanel feedbackPanel;
	protected DetailPanel detailPanel;
	protected SearchPanel searchComponentPanel;
	protected SearchResultListPanel searchResultPanel;
	
	/*Web Markup Containers */
	protected ContainerForm containerForm;
	protected WebMarkupContainer searchPanelContainer;
	protected WebMarkupContainer searchResultPanelContainer;
	protected WebMarkupContainer detailPanelContainer;
	protected WebMarkupContainer detailPanelFormContainer;
	protected WebMarkupContainer viewButtonContainer;
	protected WebMarkupContainer editButtonContainer;
	
	/* Pageable-List view */
	protected PageableListView<GenoCollection> pageableListView;
	
	
	public void ShowSearchResults(Collection<GenoCollection> genoCollectionCol, AjaxRequestTarget target) {
		containerForm.getModelObject().setGenoCollectionCollection(genoCollectionCol);
		pageableListView.removeAll();
		searchResultPanelContainer.setVisible(true);// Make the WebMarkupContainer that houses the search results visible
		target.addComponent(searchResultPanelContainer);// For ajax this is required so
	}
	
	public void HideSearchResults() {
		
	}

	public void RefreshFeedback(AjaxRequestTarget target) {
		target.addComponent(feedbackPanel);
	}
}
