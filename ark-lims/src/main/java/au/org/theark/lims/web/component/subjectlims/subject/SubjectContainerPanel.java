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
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.ArkLdapRealm;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.lims.model.vo.LimsVO;
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

	private static final long									serialVersionUID	= -2956968644138345497L;
	private static final Logger								log					= LoggerFactory.getLogger(SubjectContainerPanel.class);
	private SearchPanel											searchPanel;
	private SearchResultListPanel								searchResultListPanel;
	private DetailPanel											detailsPanel;
	private PageableListView<LimsVO>							pageableListView;
	private ContainerForm										containerForm;

	private WebMarkupContainer									arkContextMarkup;
	private WebMarkupContainer									studyNameMarkup;
	private WebMarkupContainer									studyLogoMarkup;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService									iArkCommonService;

	@SpringBean(name = Constants.LIMS_SUBJECT_SERVICE)
	private ILimsSubjectService								iLimsSubjectService;

	private DataView<LinkSubjectStudy>						dataView;
	private ArkDataProvider2<LimsVO, LinkSubjectStudy>	subjectProvider;

	/**
	 * 
	 * @param id
	 * @param arkContextMarkup
	 */
	public SubjectContainerPanel(String id, WebMarkupContainer arkContextMarkup, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup) {
		super(id);
		this.arkContextMarkup = arkContextMarkup;
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;
		
		/* Initialise the CPM */
		cpModel = new CompoundPropertyModel<LimsVO>(new LimsVO());
		containerForm = new ContainerForm("containerForm", cpModel);
		
		// Added to handle for odd bug in Wicket 1.5.1...shouldn't be needed!
		containerForm.setMultiPart(true);

		// Set study list user should see
		containerForm.getModelObject().setStudyList(containerForm.getStudyListForUser());

		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
		containerForm.add(initialiseDetailPanel());
		prerenderContextCheck();
		add(containerForm);
	}

	protected void prerenderContextCheck() {
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		String sessionSubjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
		
		// Force clearing of Cache to re-load roles for the user for the study
		arkLdapRealm.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
		aafRealm.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());

		if ((sessionStudyId != null) && (sessionSubjectUID != null)) {
			LinkSubjectStudy subjectFromBackend = new LinkSubjectStudy();
			Study study = null;
			boolean contextLoaded = false;
			
			study = iArkCommonService.getStudy(sessionStudyId);
			
			try {
				subjectFromBackend = iArkCommonService.getSubjectByUID(sessionSubjectUID, study);

				// Set SubjectUID into context
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.SUBJECTUID, subjectFromBackend.getSubjectUID());
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID, subjectFromBackend.getStudy().getId());

				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID, subjectFromBackend.getPerson().getId());
				SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.PERSON_TYPE, au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_SUBJECT);
			}
			catch (EntityNotFoundException e) {
				log.error(e.getMessage());
			}

			if (study != null && subjectFromBackend != null) {
				contextLoaded = true;
				LimsVO limsVo = new LimsVO();
				limsVo.setLinkSubjectStudy(subjectFromBackend);
				limsVo.setStudy(subjectFromBackend.getStudy());
				containerForm.setModelObject(limsVo);
			}

			if (contextLoaded) {
				// Put into Detail View mode
				arkCrudContainerVO.getSearchPanelContainer().setVisible(false);
				arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
				arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
				arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(false);
				//arkCrudContainerVO.getViewButtonContainer().setVisible(true);
				arkCrudContainerVO.getEditButtonContainer().setVisible(false);
			}
		}
	}

	protected WebMarkupContainer initialiseSearchPanel() {
		searchPanel = new SearchPanel("searchComponentPanel", feedBackPanel, pageableListView, arkCrudContainerVO, containerForm);
		searchPanel.initialisePanel(cpModel);
		arkCrudContainerVO.getSearchPanelContainer().add(searchPanel);
		return arkCrudContainerVO.getSearchPanelContainer();
	}

	protected WebMarkupContainer initialiseDetailPanel() {
		detailsPanel = new DetailPanel("detailsPanel", feedBackPanel, arkContextMarkup, containerForm, arkCrudContainerVO);
		detailsPanel.initialisePanel();
		arkCrudContainerVO.getDetailPanelContainer().add(detailsPanel);
		return arkCrudContainerVO.getDetailPanelContainer();
	}

	protected WebMarkupContainer initialiseSearchResults() {
		searchResultListPanel = new SearchResultListPanel("searchResults", arkContextMarkup, containerForm, arkCrudContainerVO, studyNameMarkup, studyLogoMarkup);
		subjectProvider = new ArkDataProvider2<LimsVO, LinkSubjectStudy>() {

			private static final long	serialVersionUID	= 1L;

			public int size() {
				List<Study> studyList = new ArrayList<Study>(0);

				// Restrict search if Study selected in Search form
				if (criteriaModel.getObject().getStudy() != null && criteriaModel.getObject().getStudy().getId() != null) {
					studyList.add(criteriaModel.getObject().getStudy());
				}
				else {
					studyList = criteriaModel.getObject().getStudyList();
					if (studyList.isEmpty()) {
						studyList = containerForm.getStudyListForUser();
					}
				}

				return iLimsSubjectService.getSubjectCount(criteriaModel.getObject(), studyList);
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
					if (studyList.isEmpty()) {
						studyList = containerForm.getStudyListForUser();
					}
				}

				listSubjects = iLimsSubjectService.searchPageableSubjects(criteriaModel.getObject(), studyList, first, count);
				return listSubjects.iterator();
			}
		};
		subjectProvider.setCriteriaModel(this.cpModel);

		dataView = searchResultListPanel.buildDataView(subjectProvider);
		dataView.setItemsPerPage(au.org.theark.core.Constants.ROWS_PER_PAGE);

		AjaxPagingNavigator pageNavigator = new AjaxPagingNavigator("navigator", dataView) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onAjaxEvent(AjaxRequestTarget target) {
				target.add(arkCrudContainerVO.getSearchResultPanelContainer());
			}
		};
		searchResultListPanel.add(pageNavigator);
		searchResultListPanel.add(dataView);
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultListPanel);
		return arkCrudContainerVO.getSearchResultPanelContainer();
	}

	public void resetDataProvider() {
	}

	public void setContextUpdateLimsWMC(WebMarkupContainer limsContainerWMC) {
		containerForm.setContextUpdateLimsWMC(limsContainerWMC);
	}
}