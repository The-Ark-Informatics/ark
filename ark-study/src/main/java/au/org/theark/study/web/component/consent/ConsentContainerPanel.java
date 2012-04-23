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
package au.org.theark.study.web.component.consent;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ConsentVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.consent.form.ContainerForm;

public class ConsentContainerPanel extends AbstractContainerPanel<ConsentVO> {
	private static final long				serialVersionUID	= 1L;

	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService				iArkCommonService;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService					studyService;

	// Container Form
	private ContainerForm					containerForm;

	private SearchPanel						searchPanel;
	private DetailPanel						detailPanel;
	private PageableListView<Consent>	pageableListView;

	/**
	 * Constructor
	 * 
	 * @param id
	 */
	public ConsentContainerPanel(String id) {

		super(id);
		cpModel = new CompoundPropertyModel<ConsentVO>(new ConsentVO());
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
		add(containerForm);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseDetailPanel()
	 */
	@Override
	protected WebMarkupContainer initialiseDetailPanel() {
		detailPanel = new DetailPanel("detailPanel", feedBackPanel, containerForm, arkCrudContainerVO);
		detailPanel.initialisePanel();
		arkCrudContainerVO.getDetailPanelContainer().add(detailPanel);
		return arkCrudContainerVO.getDetailPanelContainer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseSearchPanel()
	 */
	@Override
	protected WebMarkupContainer initialiseSearchPanel() {
		// Get the Person in Context and determine the Person Type
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
				
		try {
			// Initialise the phoneList;
			Collection<Consent> consentList = new ArrayList<Consent>();

			// The LinkSubjectStudy item should be fetched and stored in the model
			Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			Study study = iArkCommonService.getStudy(sessionStudyId);

			if (sessionPersonId != null) {
				LinkSubjectStudy linkSubjectStudy = studyService.getSubjectLinkedToStudy(sessionPersonId, study);
				containerForm.getModelObject().getConsent().setLinkSubjectStudy(linkSubjectStudy);
			}

			// All the phone items related to the person if one found in session or an empty list
			cpModel.getObject().setConsentList(consentList);
			searchPanel = new SearchPanel("searchPanel", feedBackPanel, pageableListView, arkCrudContainerVO);
			searchPanel.initialisePanel(cpModel);
			arkCrudContainerVO.getSearchPanelContainer().add(searchPanel);
		}
		catch (EntityNotFoundException entityNotFoundException) {
			containerForm.error("The Person/Subject cannot be found in the system. Please contact Support.");

		}
		catch (ArkSystemException arkSystemException) {
			containerForm.error("A System error/exception has occured. Please contact Support.");
		}

		return arkCrudContainerVO.getSearchPanelContainer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseSearchResults()
	 */
	@Override
	protected WebMarkupContainer initialiseSearchResults() {
		// TODO: Should eventually remove containerForm and instead pass over the cpModel
		SearchResultListPanel searchResultPanel = new SearchResultListPanel("searchResults", containerForm, arkCrudContainerVO);
		iModel = new LoadableDetachableModel<Object>() {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load() {

				// Get the PersonId from session and get the phoneList from back end
				Collection<Consent> consentList = new ArrayList<Consent>();

				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);

				try {
					if (isActionPermitted()) {
						if (sessionPersonId != null) {
							Study study = iArkCommonService.getStudy(sessionStudyId);
							LinkSubjectStudy linkSubjectStudy = studyService.getSubjectLinkedToStudy(sessionPersonId, study);
							containerForm.getModelObject().getConsent().setLinkSubjectStudy(linkSubjectStudy);
							containerForm.getModelObject().getConsent().setStudy(study);
							consentList = studyService.searchConsent(containerForm.getModelObject());
						}
					}
				}
				catch (EntityNotFoundException e) {
					containerForm.error("Subject is not available in the system");
				}
				catch (ArkSystemException e) {
					containerForm.error("A System Error has occured please contact Support");
				}

				pageableListView.removeAll();
				return consentList;
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
}