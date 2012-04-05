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
package au.org.theark.study.web.component.address;

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
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.vo.AddressVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.address.form.ContainerForm;

/**
 * @author nivedann
 * 
 */
public class AddressContainerPanel extends AbstractContainerPanel<AddressVO> {

	/**
	 * 
	 */
	private static final long				serialVersionUID	= 1L;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService					studyService;

	private ContainerForm					containerForm;

	// Panels
	private SearchPanel						searchPanel;
	private DetailPanel						detailPanel;
	private PageableListView<Address>	listView;

	/**
	 * @param id
	 */
	public AddressContainerPanel(String id) {
		super(id);
		cpModel = new CompoundPropertyModel<AddressVO>(new AddressVO());
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

		detailPanel = new DetailPanel("detailPanel", feedBackPanel, arkCrudContainerVO, containerForm);
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
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		// if it was
		// Set the person who this address should be associated with
		Collection<Address> addressList = new ArrayList<Address>();

		try {
			if (sessionPersonId != null) {
				containerForm.getModelObject().getAddress().setPerson(studyService.getPerson(sessionPersonId));
				addressList = studyService.getPersonAddressList(sessionPersonId, containerForm.getModelObject().getAddress());
			}

			cpModel.getObject().setAddresses(addressList);
			searchPanel = new SearchPanel("searchComponentPanel", arkCrudContainerVO, feedBackPanel, containerForm, listView);

			searchPanel.initialisePanel(cpModel);
			arkCrudContainerVO.getSearchPanelContainer().add(searchPanel);
		}
		catch (EntityNotFoundException e) {
			// Report this to the user
		}
		catch (ArkSystemException e) {
			// Logged by the back end. Report this to the user
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

		SearchResultListPanel searchResultPanel = new SearchResultListPanel("searchResults", arkCrudContainerVO, containerForm);

		iModel = new LoadableDetachableModel<Object>() {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load() {
				// Get the PersonId from session and get the phoneList from backend
				Collection<Address> addressList = new ArrayList<Address>();
				Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
				try {
					if (isActionPermitted()) {
						if (sessionPersonId != null) {
							addressList = studyService.getPersonAddressList(sessionPersonId, containerForm.getModelObject().getAddress());
						}
					}
				}
				catch (ArkSystemException e) {
					containerForm.error("A System Exception has occured please contact support.");
				}
				listView.removeAll();
				return addressList;
			}
		};

		listView = searchResultPanel.buildPageableListView(iModel);
		listView.setReuseItems(true);
		// TODO: use Ajax paging navigator
		PagingNavigator pageNavigator = new PagingNavigator("addressNavigator", listView);
		searchResultPanel.add(pageNavigator);
		searchResultPanel.add(listView);
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultPanel);
		return arkCrudContainerVO.getSearchResultPanelContainer();
	}

}
