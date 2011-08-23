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
package au.org.theark.study.web.component.phone;

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
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.PhoneVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.phone.form.ContainerForm;

/**
 * @author nivedann
 * 
 */
public class PhoneContainerPanel extends AbstractContainerPanel<PhoneVO> {

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService			iArkCommonService;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService				studyService;

	// Container Form
	private ContainerForm				containerForm;
	// Panels
	private SearchResultListPanel		searchResultListPanel;
	private SearchPanel					searchPanel;
	private DetailPanel					detailPanel;
	private PageableListView<Phone>	pageableListView;

	/**
	 * Constructor
	 * 
	 * @param id
	 */
	public PhoneContainerPanel(String id) {

		super(id);
		cpModel = new CompoundPropertyModel<PhoneVO>(new PhoneVO());
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

		detailPanel = new DetailPanel("detailsPanel", feedBackPanel, searchResultPanelContainer, detailPanelContainer, detailPanelFormContainer, searchPanelContainer, viewButtonContainer,
				editButtonContainer, containerForm);
		detailPanel.initialisePanel();
		detailPanelContainer.add(detailPanel);
		return detailPanelContainer;
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
		String sessionPersonType = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_TYPE);// Subject or
																																														// Contact:
																																														// Denotes
																																														// if it was
																																														// a subject
																																														// or
																																														// contact
																																														// placed in
																																														// session

		try {
			// Initialise the phoneList;
			Collection<Phone> personPhoneList = new ArrayList<Phone>();

			if (sessionPersonId != null) {
				containerForm.getModelObject().getPhone().setPerson(studyService.getPerson(sessionPersonId));// Can be a Subject or Contact
				personPhoneList = studyService.getPersonPhoneList(sessionPersonId, containerForm.getModelObject().getPhone());
			}

			// All the phone items related to the person if one found in session or an empty list
			cpModel.getObject().setPhoneList(personPhoneList);
			searchPanel = new SearchPanel("searchComponentPanel", feedBackPanel, searchPanelContainer, pageableListView, searchResultPanelContainer, detailPanelContainer, detailPanel, containerForm,
					viewButtonContainer, editButtonContainer, detailPanelFormContainer);
			searchPanel.initialisePanel(cpModel);
			searchPanelContainer.add(searchPanel);

		}
		catch (EntityNotFoundException entityNotFoundException) {
			// Report this to the user

		}
		catch (ArkSystemException arkSystemException) {
			// Logged by the back end. Report this to the user
		}

		return searchPanelContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseSearchResults()
	 */
	@Override
	protected WebMarkupContainer initialiseSearchResults() {

		SearchResultListPanel searchResultPanel = new SearchResultListPanel("searchResults", detailPanelContainer, detailPanelFormContainer, searchPanelContainer, searchResultPanelContainer,
				viewButtonContainer, editButtonContainer, containerForm);
		iModel = new LoadableDetachableModel<Object>() {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load() {
				// Get the PersonId from session and get the phoneList from backend
				Collection<Phone> personPhoneList = new ArrayList<Phone>();
				try {
					Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
					if (isActionPermitted() && sessionPersonId != null) {
						personPhoneList = studyService.getPersonPhoneList(sessionPersonId, containerForm.getModelObject().getPhone());
					}
				}
				catch (EntityNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (ArkSystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pageableListView.removeAll();
				return personPhoneList;
			}
		};

		pageableListView = searchResultPanel.buildPageableListView(iModel);
		pageableListView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("phoneNavigator", pageableListView);
		searchResultPanel.add(pageNavigator);
		searchResultPanel.add(pageableListView);
		searchResultPanelContainer.add(searchResultPanel);
		return searchResultPanelContainer;

	}

}
