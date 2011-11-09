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
package au.org.theark.phenotypic.web.component.fieldData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.pheno.entity.FieldData;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.core.web.component.ArkDataProvider;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.fieldData.form.ContainerForm;

public class FieldDataContainerPanel extends AbstractContainerPanel<PhenoCollectionVO> {
	private static final long													serialVersionUID	= 1L;

	// Panels
	private SearchPanel															searchComponentPanel;
	private SearchResultListPanel												searchResultPanel;
	private DetailPanel															detailPanel;
	private PageableListView<FieldData>										listView;

	private ContainerForm														containerForm;

	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService													iPhenotypicService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService													iArkCommonService;

	private DataView<PhenoCollectionVO>										dataView;
	private ArkDataProvider<PhenoCollectionVO, IPhenotypicService>	fieldDataProvider;

	public FieldDataContainerPanel(String id) {
		super(id);

		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<PhenoCollectionVO>(new PhenoCollectionVO());

		/* Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());

		add(containerForm);
	}

	protected WebMarkupContainer initialiseSearchResults() {

		searchResultPanel = new SearchResultListPanel("searchResults", arkCrudContainerVO,listView, containerForm);

		// Data providor to paginate resultList
		fieldDataProvider = new ArkDataProvider<PhenoCollectionVO, IPhenotypicService>(iPhenotypicService) {

			public int size() {
				return iPhenotypicService.getStudyFieldDataCount(model.getObject());
			}

			public Iterator<PhenoCollectionVO> iterator(int first, int count) {
				List<PhenoCollectionVO> listFieldData = new ArrayList<PhenoCollectionVO>();
				listFieldData = iPhenotypicService.searchPageableFieldData(model.getObject(), first, count);
				return listFieldData.iterator();
			}
		};

		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		containerForm.getModelObject().setStudy(study);
		fieldDataProvider.setModel(this.cpModel);

		dataView = searchResultPanel.buildDataView(fieldDataProvider);
		dataView.setItemsPerPage(au.org.theark.core.Constants.ROWS_PER_PAGE);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", dataView);
		searchResultPanel.add(pageNavigator);
		searchResultPanel.add(dataView);
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultPanel);
		return arkCrudContainerVO.getSearchResultPanelContainer();
	}

	protected WebMarkupContainer initialiseDetailPanel() {

		detailPanel = new DetailPanel("detailPanel", feedBackPanel,arkCrudContainerVO, containerForm);
		detailPanel.initialisePanel();
		arkCrudContainerVO.getDetailPanelContainer().add(detailPanel);
		return arkCrudContainerVO.getDetailPanelContainer();
		
	}

	protected WebMarkupContainer initialiseSearchPanel() {

		// Get a collection of fieldData's for the study in context by default
		Collection<FieldData> fieldDataCollection = new ArrayList<FieldData>();
		containerForm.getModelObject().setFieldDataCollection(fieldDataCollection);
		searchComponentPanel = new SearchPanel("searchPanel", arkCrudContainerVO, feedBackPanel, listView, containerForm);

		searchComponentPanel.initialisePanel();
		arkCrudContainerVO.getSearchPanelContainer().add(searchComponentPanel);
		return arkCrudContainerVO.getSearchPanelContainer();
		
	}
}
