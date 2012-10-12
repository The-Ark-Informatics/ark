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
package au.org.theark.report.web.component.dataextraction;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.SearchVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.report.web.component.dataextraction.form.ContainerForm;
import au.org.theark.report.web.Constants;

public class StudyComponentContainerPanel extends AbstractContainerPanel<SearchVO> {

	private static final long				serialVersionUID	= 1L;

	// Panels
	private SearchPanel						searchComponentPanel;
	private SearchResultListPanel			searchResultPanel;
	private DetailPanel						detailsPanel;

	private PageableListView<StudyComp>	pageableListView;

	private ContainerForm					containerForm;

//	@SpringBean(name = Constants.STUDY_SERVICE)
//	private IStudyService					studyService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService				iArkCommonService;

	public StudyComponentContainerPanel(String id) {
		super(id);

		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<SearchVO>(new SearchVO());

		/* Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", cpModel);

		containerForm.add(initialiseFeedBackPanel());

		containerForm.add(initialiseDetailPanel());

		containerForm.add(initialiseSearchResults());

		containerForm.add(initialiseSearchPanel());

		add(containerForm);

	}

	protected WebMarkupContainer initialiseSearchResults() {

		searchResultPanel = new SearchResultListPanel("searchResults", arkCrudContainerVO, containerForm);

		iModel = new LoadableDetachableModel<Object>() {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load() {
/*
				try {
					Long studySessionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
					if (isActionPermitted() && studySessionId != null) {
						Study studyInContext = iArkCommonService.getStudy(studySessionId);
						containerForm.getModelObject().getStudyComponent().setStudy(studyInContext);
						containerForm.getModelObject().setStudyCompList(studyService.searchStudyComp(containerForm.getModelObject().getStudyComponent()));
					}

				}
				catch (ArkSystemException e) {
					containerForm.error("A System Exception has occured please contact Support");
				}
				pageableListView.removeAll();
*/
				return containerForm.getModelObject(); //.getStudyCompList();
			}
		};

		pageableListView = searchResultPanel.buildPageableListView(iModel);
		pageableListView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", pageableListView);
		searchResultPanel.add(pageNavigator);
		searchResultPanel.add(pageableListView);
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultPanel);
		return arkCrudContainerVO.getSearchResultPanelContainer();
	}

	protected WebMarkupContainer initialiseDetailPanel() {
		detailsPanel = new DetailPanel("detailPanel", feedBackPanel, arkCrudContainerVO, containerForm);
		detailsPanel.initialisePanel();
		arkCrudContainerVO.getDetailPanelContainer().add(detailsPanel);
		return arkCrudContainerVO.getDetailPanelContainer();
	}

	protected WebMarkupContainer initialiseSearchPanel() {
		SearchVO searchVO = new SearchVO();
/*
		// Get a result-set by default
		List<StudyComp> resultList = new ArrayList<StudyComp>();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		try {
			if (sessionStudyId != null && sessionStudyId > 0) {
				resultList = studyService.searchStudyComp(searchVO.getStudyComponent());
			}
		}
		catch (ArkSystemException e) {
			this.error("A System error occured  while initializing Search Panel");
		}
*/
		List<SearchVO> searchThatWouldUltimatelyComeFromSomeQuery = new ArrayList<SearchVO>();
		//cpModel.getObject().setSearch(searchThatWouldUltimatelyComeFromSomeQuery);//(resultList);
		searchComponentPanel = new SearchPanel("searchComponentPanel", arkCrudContainerVO, feedBackPanel, containerForm, pageableListView);
		searchComponentPanel.initialisePanel(cpModel);
		arkCrudContainerVO.getSearchPanelContainer().add(searchComponentPanel);
		return arkCrudContainerVO.getSearchPanelContainer();

	}

}
