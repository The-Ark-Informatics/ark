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
package au.org.theark.phenotypic.web.component.phenofielduploader;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.Upload;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.phenotypic.model.vo.PhenoFieldUploadVO;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.phenofielduploader.form.ContainerForm;

public class FieldUploadContainerPanel extends AbstractContainerPanel<PhenoFieldUploadVO> {
	private static final long					serialVersionUID				= 1L;

	private SearchPanel							searchPanel;
	private SearchResultListPanel				searchResultListPanel;
	private Panel									detailPanel;
	private WizardPanel							wizardPanel;
	private PageableListView<Upload>	listView;
	private ContainerForm						containerForm;

	@SpringBean(name = "phenotypicService")
	private IPhenotypicService					serviceInterface;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;

	public FieldUploadContainerPanel(String id, ArkFunction arkFunction) {
		super(id);

		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<PhenoFieldUploadVO>(new PhenoFieldUploadVO());
		cpModel.getObject().getUpload().setArkFunction(arkFunction);	//set the relevant arkFunction  

		/* Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseWizardPanel());
		containerForm.add(initialiseSearchResults());

		add(containerForm);
	}

	private WebMarkupContainer initialiseWizardPanel() {
		wizardPanel = new WizardPanel("wizardPanel", feedBackPanel, containerForm, arkCrudContainerVO);
		wizardPanel.initialisePanel();
		arkCrudContainerVO.getWizardPanelContainer().setVisible(true);
		arkCrudContainerVO.getWizardPanelContainer().add(wizardPanel);
		return arkCrudContainerVO.getWizardPanelContainer();
	}

	protected WebMarkupContainer initialiseSearchResults() {
		searchResultListPanel = new SearchResultListPanel("searchResults", arkCrudContainerVO, listView, containerForm);

		iModel = new LoadableDetachableModel<Object>() {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load() {
				// Return all Uploads for the study in context
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);

				listView.removeAll();

				if (sessionStudyId != null) {
					Upload searchPhenoUpload = new Upload();
					Study study = iArkCommonService.getStudy(sessionStudyId);
					searchPhenoUpload.setStudy(study);
					searchPhenoUpload.setArkFunction(containerForm.getModelObject().getUpload().getArkFunction());

					java.util.Collection<Upload> phenoUploads = iArkCommonService.searchUploads(searchPhenoUpload);
					return phenoUploads;
				}
				else {
					return null;
				}
			}
		};

		listView = searchResultListPanel.buildPageableListView(iModel);
		listView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", listView);
		searchResultListPanel.add(pageNavigator);
		searchResultListPanel.add(listView);
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultListPanel);
		searchResultListPanel.setVisible(true);

		return arkCrudContainerVO.getSearchResultPanelContainer();
	}

	@Override
	protected WebMarkupContainer initialiseDetailPanel() {
		detailPanel = new EmptyPanel("detailPanel");
		arkCrudContainerVO.getDetailPanelContainer().add(detailPanel);
		return arkCrudContainerVO.getDetailPanelContainer();
	}

	@Override
	protected WebMarkupContainer initialiseSearchPanel() {
		searchPanel = new SearchPanel("searchPanel", feedBackPanel, listView, containerForm, arkCrudContainerVO);
		searchPanel.initialisePanel();
		searchPanel.setVisible(false);
		arkCrudContainerVO.getSearchPanelContainer().add(searchPanel);
		return arkCrudContainerVO.getSearchPanelContainer();
	}
}