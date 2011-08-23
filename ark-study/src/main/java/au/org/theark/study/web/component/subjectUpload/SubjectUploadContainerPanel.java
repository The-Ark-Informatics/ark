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
package au.org.theark.study.web.component.subjectUpload;

import java.util.ArrayList;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.StudyUpload;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.component.subjectUpload.form.ContainerForm;

public class SubjectUploadContainerPanel extends AbstractContainerPanel<UploadVO> {
	@SpringBean(name = au.org.theark.core.Constants.STUDY_SERVICE)
	private IStudyService						studyService;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService					iArkCommonService;

	private static final long					serialVersionUID	= 1L;

	// Panels
	private SearchPanel							searchComponentPanel;
	private SearchResultListPanel				searchResultPanel;
	private DetailPanel							detailPanel;
	private WizardPanel							wizardPanel;
	private PageableListView<StudyUpload>	listView;
	private ContainerForm						containerForm;

	public SubjectUploadContainerPanel(String id) {
		super(id);

		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<UploadVO>(new UploadVO());

		initialiseMarkupContainers();

		/* Bind the CPM to the Form */
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		// containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseWizardPanel());
		containerForm.add(initialiseSearchResults());
		// containerForm.add(initialiseSearchPanel());
		containerForm.setMultiPart(true);
		add(containerForm);
	}

	private WebMarkupContainer initialiseWizardPanel() {
		wizardPanel = new WizardPanel("wizardPanel", searchResultPanelContainer, feedBackPanel, wizardPanelContainer, searchPanelContainer, wizardPanelFormContainer, containerForm);
		wizardPanel.initialisePanel();
		wizardPanelContainer.setVisible(true);
		wizardPanelContainer.add(wizardPanel);
		return wizardPanelContainer;
	}

	protected WebMarkupContainer initialiseSearchResults() {
		searchResultPanel = new SearchResultListPanel("searchResults", feedBackPanel, detailPanelContainer, searchPanelContainer, containerForm, searchResultPanelContainer, detailPanel,
				viewButtonContainer, editButtonContainer, detailPanelFormContainer);

		iModel = new LoadableDetachableModel<Object>() {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load() {
				// Return all Uploads for the Study in context
				java.util.Collection<StudyUpload> studyUploads = new ArrayList<StudyUpload>();
				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				if (isActionPermitted() && sessionStudyId != null) {
					StudyUpload studyUpload = new StudyUpload();
					studyUpload.setStudy(iArkCommonService.getStudy(sessionStudyId));
					studyUploads = studyService.searchUpload(studyUpload);

				}
				listView.removeAll();
				return studyUploads;
			}
		};

		listView = searchResultPanel.buildPageableListView(iModel);
		listView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", listView);
		searchResultPanel.add(pageNavigator);
		searchResultPanel.add(listView);
		searchResultPanelContainer.add(searchResultPanel);
		searchResultPanel.setVisible(true);

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
		searchComponentPanel = new SearchPanel("searchPanel", feedBackPanel, searchPanelContainer, listView, searchResultPanelContainer, wizardPanelContainer, wizardPanel, containerForm,
				viewButtonContainer, editButtonContainer, wizardPanelFormContainer);
		searchComponentPanel.initialisePanel();
		searchComponentPanel.setVisible(false);
		searchPanelContainer.add(searchComponentPanel);
		return searchPanelContainer;
	}
}
