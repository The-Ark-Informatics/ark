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
package au.org.theark.phenotypic.web.component.fieldDataUpload;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.pheno.entity.PhenoUpload;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.web.component.fieldDataUpload.form.ContainerForm;
import au.org.theark.phenotypic.web.component.fieldDataUpload.form.SearchForm;

/**
 * @author cellis
 * 
 */
@SuppressWarnings("serial")
public class SearchPanel extends Panel {
	private FeedbackPanel						feedBackPanel;
	private WebMarkupContainer					searchMarkupContainer;
	private WebMarkupContainer					listContainer;
	private WebMarkupContainer					wizardContainer;
	private PageableListView<PhenoUpload>	listView;
	private ContainerForm						containerForm;
	private WizardPanel							wizardPanel;
	private WebMarkupContainer					viewButtonContainer;
	private WebMarkupContainer					editButtonContainer;
	private WebMarkupContainer					wizardPanelFormContainer;

	/* Constructor */
	public SearchPanel(String id, FeedbackPanel feedBackPanel, WebMarkupContainer searchMarkupContainer, PageableListView<PhenoUpload> listView, WebMarkupContainer resultListContainer,
			WebMarkupContainer wizardPanelContainer, WizardPanel wizard, ContainerForm containerForm, WebMarkupContainer viewButtonContainer, WebMarkupContainer editButtonContainer,
			WebMarkupContainer wizardPanelFormContainer) {
		super(id);
		this.searchMarkupContainer = searchMarkupContainer;
		this.listView = listView;
		this.feedBackPanel = feedBackPanel;
		this.wizardContainer = wizardPanelContainer;
		this.wizardPanel = wizard;
		this.containerForm = containerForm;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.wizardPanelFormContainer = wizardPanelFormContainer;
		listContainer = resultListContainer;
	}

	public void initialisePanel() {
		SearchForm searchForm = new SearchForm(au.org.theark.core.Constants.SEARCH_FORM, (CompoundPropertyModel<UploadVO>) containerForm.getModel(), listView, feedBackPanel, wizardPanel, listContainer,
				searchMarkupContainer, wizardContainer, wizardPanelFormContainer, viewButtonContainer, editButtonContainer);
		add(searchForm);
	}
}
