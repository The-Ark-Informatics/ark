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
package au.org.theark.lims.web.component.subjectlims.subject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsService;
import au.org.theark.lims.service.ILimsSubjectService;
import au.org.theark.lims.web.Constants;
import au.org.theark.lims.web.component.subjectlims.subject.form.ContainerForm;

/**
 * 
 * @author cellis
 * 
 */
@SuppressWarnings("unchecked")
public class SubjectContainerPanel extends AbstractContainerPanel<LimsVO> {
	/**
	 * 
	 */
	private static final long																			serialVersionUID	= -2956968644138345497L;
	private static final Logger																		log					= LoggerFactory.getLogger(SubjectContainerPanel.class);
	private SearchPanel																					searchPanel;
	private SearchResultListPanel																		searchResultsPanel;
	private DetailPanel																					detailsPanel;
	private PageableListView<LimsVO>														pageableListView;
	private ContainerForm																				containerForm;

	private WebMarkupContainer																			arkContextMarkup;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService																			iArkCommonService;

	@SpringBean(name = Constants.LIMS_SERVICE)
	private ILimsService																					iLimsService;

	@SpringBean(name = Constants.LIMS_SUBJECT_SERVICE)
	private ILimsSubjectService																		iLimsSubjectService;

	private DataView<LinkSubjectStudy>																dataView;
	private ArkDataProvider2<LimsVO, LinkSubjectStudy, ILimsSubjectService>	subjectProvider;

	/**
	 * @param id
	 */
	public SubjectContainerPanel(String id, WebMarkupContainer arkContextMarkup) {

		super(id);
		this.arkContextMarkup = arkContextMarkup;
		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<LimsVO>(new LimsVO());
		containerForm = new ContainerForm("containerForm", cpModel);

		// Set study list user should see
		containerForm.getModelObject().setStudyList(getStudyListForUser());

		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
		containerForm.add(initialiseDetailPanel());

		prerenderContextCheck();

		add(containerForm);
	}

	protected void prerenderContextCheck() {
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);

		if ((sessionPersonId != null)) {
			String sessionPersonType = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_TYPE);
			if (sessionPersonType.equals(au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_SUBJECT)) {
				Person person;
				boolean contextLoaded = false;
				try {
					person = iLimsService.getPerson(sessionPersonId);
					LimsVO limsVo = new LimsVO();
					limsVo.getLinkSubjectStudy().setPerson(person);
					limsVo.setLinkSubjectStudy(iArkCommonService.getSubjectByUID(limsVo.getLinkSubjectStudy().getSubjectUID()));
					containerForm.setModelObject(limsVo);
					contextLoaded = true;
				}
				catch (EntityNotFoundException e) {
					log.error(e.getMessage());
				}
				catch (ArkSystemException e) {
					log.error(e.getMessage());
				}

				if (contextLoaded) {
					// Put into Detail View mode
					searchPanelContainer.setVisible(false);
					searchResultPanelContainer.setVisible(false);
					detailPanelContainer.setVisible(true);
					detailPanelFormContainer.setEnabled(false);
					viewButtonContainer.setVisible(true);
					editButtonContainer.setVisible(false);
				}
			}
		}
	}

	protected WebMarkupContainer initialiseSearchPanel() {
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if (sessionStudyId != null && sessionStudyId > 0) {
			containerForm.getModelObject().getLinkSubjectStudy().setStudy(iArkCommonService.getStudy(sessionStudyId));
		}

		searchPanel = new SearchPanel("searchComponentPanel", feedBackPanel, searchPanelContainer, pageableListView, searchResultPanelContainer, detailPanelContainer, detailPanelFormContainer,
				viewButtonContainer, editButtonContainer, detailsPanel, containerForm);

		searchPanel.initialisePanel(cpModel);
		searchPanelContainer.add(searchPanel);
		return searchPanelContainer;
	}

	protected WebMarkupContainer initialiseDetailPanel() {

		detailsPanel = new DetailPanel("detailsPanel", feedBackPanel, searchResultPanelContainer, detailPanelContainer, detailPanelFormContainer, searchPanelContainer, viewButtonContainer,
				editButtonContainer, arkContextMarkup, containerForm);
		detailsPanel.initialisePanel();
		detailPanelContainer.add(detailsPanel);
		return detailPanelContainer;
	}

	protected WebMarkupContainer initialiseSearchResults() {

		searchResultsPanel = new SearchResultListPanel("searchResults", detailPanelContainer, detailPanelFormContainer, searchPanelContainer, searchResultPanelContainer, viewButtonContainer,
				editButtonContainer, arkContextMarkup, containerForm);

		subjectProvider = new ArkDataProvider2<LimsVO, LinkSubjectStudy, ILimsSubjectService>(iLimsSubjectService) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			public int size() {
				List<Study> studyList = new ArrayList<Study>(0);

				// Restrict search if Study selected in Search form
				if (criteriaModel.getObject().getStudy() != null && criteriaModel.getObject().getStudy().getId() != null) {
					studyList.add(criteriaModel.getObject().getStudy());
				}
				else {
					studyList = criteriaModel.getObject().getStudyList();
				}

				return service.getSubjectCount(criteriaModel.getObject(), studyList);
			}

			public Iterator<LinkSubjectStudy> iterator(int first, int count) {
				List<LinkSubjectStudy> listSubjects = new ArrayList<LinkSubjectStudy>(0);

				// Restrict search if Study selected in Search form
				List<Study> studyList = new ArrayList<Study>(0);
				if (criteriaModel.getObject().getStudy() != null && criteriaModel.getObject().getStudy().getId() != null) {
					studyList.add(criteriaModel.getObject().getStudy());
				}
				else {
					studyList = criteriaModel.getObject().getStudyList();
				}

				if (isActionPermitted()) {
					listSubjects = iLimsSubjectService.searchPageableSubjects(criteriaModel.getObject(), studyList, first, count);
				}
				return listSubjects.iterator();
			}
		};
		subjectProvider.setCriteriaModel(this.cpModel);

		dataView = searchResultsPanel.buildDataView(subjectProvider);
		dataView.setItemsPerPage(au.org.theark.core.Constants.ROWS_PER_PAGE);

		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("navigator", dataView) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.addComponent(searchResultPanelContainer);
			}
		};
		searchResultsPanel.add(pageNavigator);
		searchResultsPanel.add(dataView);
		searchResultPanelContainer.add(searchResultsPanel);
		return searchResultPanelContainer;
	}

	public void resetDataProvider() {
	}

	public void setContextUpdateLimsWMC(WebMarkupContainer limsContainerWMC) {
		containerForm.setContextUpdateLimnsWMC(limsContainerWMC);
	}

	/**
	 * Returns a list of Studies the user is permitted to access
	 * 
	 * @return
	 */
	private List<Study> getStudyListForUser() {
		List<Study> studyListForUser = new ArrayList<Study>(0);
		try {
			Subject currentUser = SecurityUtils.getSubject();
			ArkUser arkUser = iArkCommonService.getArkUser(currentUser.getPrincipal().toString());
			ArkUserVO arkUserVo = new ArkUserVO();
			arkUserVo.setArkUserEntity(arkUser);
			studyListForUser = iArkCommonService.getStudyListForUser(arkUserVo);
		}
		catch (EntityNotFoundException e) {
			log.error(e.getMessage());
		}
		return studyListForUser;
	}
}