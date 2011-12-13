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
package au.org.theark.phenotypic.web.component.phenodatauploader;

import java.util.ArrayList;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyUpload;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.phenotypic.model.vo.PhenoFieldDataUploadVO;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.phenodatauploader.form.ContainerForm;


@SuppressWarnings({ "unused" })
public class PhenoDataUploadContainerPanel extends AbstractContainerPanel<PhenoFieldDataUploadVO> {
	
	private static final long					serialVersionUID				= 1L;

	private transient Logger					log								= LoggerFactory.getLogger(PhenoDataUploadContainerPanel.class);
	
	private SearchResultListPanel				searchResultPanel;
	private DetailPanel							detailPanel;
	private PageableListView<StudyUpload>		listView;
	private ContainerForm						containerForm;

	@SpringBean(name = "phenotypicService")
	private IPhenotypicService					iPhenotypicService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;

	
	private boolean								phenoCollectionInContext	= false;
	
	public void setDefaultVisibility() {
		arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
		arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(true);
		arkCrudContainerVO.getEditButtonContainer().setVisible(true);
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
	}
	
	
	
	public PhenoDataUploadContainerPanel(String id) {
		super(id);
		setDefaultVisibility();
		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<PhenoFieldDataUploadVO>(new PhenoFieldDataUploadVO());

		/* Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		add(containerForm);
	}

	protected WebMarkupContainer initialiseSearchResults() {
		searchResultPanel = new SearchResultListPanel("searchResults", listView, containerForm, arkCrudContainerVO);

		iModel = new LoadableDetachableModel<Object>() {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load() {
				// Set study in context
				Study study = new Study();
				Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);

				listView.removeAll();

				if (studyId != null) {
					study = iArkCommonService.getStudy(studyId);
					StudyUpload studyUpload = new StudyUpload();
					studyUpload.setStudy(study);
					ArkFunction arkFunction = iArkCommonService.getArkFunctionByName(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_FIELD_DATA_UPLOAD);
					studyUpload.setArkFunction(arkFunction);
					java.util.Collection<StudyUpload> questionniareFieldDataUploads = new ArrayList<StudyUpload>();
					questionniareFieldDataUploads = iPhenotypicService.searchUpload(studyUpload);
					return questionniareFieldDataUploads;
				}
				else {
					return null;//TODO? Why return null
				}
			}
		};

		listView = searchResultPanel.buildPageableListView(iModel);
		listView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", listView);
		searchResultPanel.add(pageNavigator);
		searchResultPanel.add(listView);
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultPanel);
		searchResultPanel.setVisible(true);

		return arkCrudContainerVO.getSearchResultPanelContainer();
	}
	
	

	@Override
	protected WebMarkupContainer initialiseDetailPanel() {
		detailPanel = new DetailPanel("detailsPanel", feedBackPanel, containerForm, arkCrudContainerVO);
		detailPanel.initialisePanel();
		arkCrudContainerVO.getDetailPanelContainer().add(detailPanel);
		return arkCrudContainerVO.getDetailPanelContainer();
	}

	@Override
	protected WebMarkupContainer initialiseSearchPanel() {
		return arkCrudContainerVO.getSearchPanelContainer();
	}
}