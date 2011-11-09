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

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.pheno.entity.PhenoUpload;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.fieldDataUpload.form.ContainerForm;

@SuppressWarnings({ "unused" })
public class FieldDataUploadContainerPanel extends AbstractContainerPanel<UploadVO> {
	private static final long					serialVersionUID				= 1L;

	private SearchPanel							searchPanel;
	private SearchResultListPanel				searchResultPanel;
	private DetailPanel							detailPanel;
	private WizardPanel							wizardPanel;
	private PageableListView<PhenoUpload>	listView;
	private ContainerForm						containerForm;

	@SpringBean(name = "phenotypicService")
	private IPhenotypicService					iPhenotypicService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;

	private transient Logger					log								= LoggerFactory.getLogger(FieldDataUploadContainerPanel.class);
	private boolean								phenoCollectionInContext	= false;

	public FieldDataUploadContainerPanel(String id) {
		super(id);

		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<UploadVO>(new UploadVO());

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
					PhenoUpload phenoUpload = new PhenoUpload();
					phenoUpload.setStudy(study);

					// Only show data uploads, not data dictionary uploads "FIELD"
					phenoUpload.setUploadType("FIELD_DATA");

					java.util.Collection<PhenoUpload> phenoUploads = iPhenotypicService.searchUpload(phenoUpload);
					return phenoUploads;
				}
				else {
					return null;
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
		detailPanel = new DetailPanel("detailPanel", feedBackPanel, containerForm, arkCrudContainerVO);
		detailPanel.initialisePanel();
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