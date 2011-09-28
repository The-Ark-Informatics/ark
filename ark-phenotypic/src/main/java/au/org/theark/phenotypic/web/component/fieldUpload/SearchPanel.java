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
package au.org.theark.phenotypic.web.component.fieldUpload;

import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.model.pheno.entity.PhenoUpload;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.web.component.fieldUpload.form.ContainerForm;
import au.org.theark.phenotypic.web.component.fieldUpload.form.SearchForm;

/**
 * @author cellis
 * 
 */
@SuppressWarnings("serial")
public class SearchPanel extends Panel {
	private FeedbackPanel						feedBackPanel;
	private PageableListView<PhenoUpload>	listView;
	private ContainerForm						containerForm;
	private ArkCrudContainerVO					arkCrudContainerVO;

	public SearchPanel(String id, FeedbackPanel feedBackPanel, PageableListView<PhenoUpload> listView, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id);
		this.listView = listView;
		this.feedBackPanel = feedBackPanel;
		this.containerForm = containerForm;
		this.arkCrudContainerVO = arkCrudContainerVO;
	}

	public void initialisePanel() {
		SearchForm searchForm = new SearchForm(au.org.theark.core.Constants.SEARCH_FORM, 
														(CompoundPropertyModel<UploadVO>) containerForm.getModel(), arkCrudContainerVO, feedBackPanel, listView);

		add(searchForm);
	}
}
