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
package au.org.theark.study.web.component.correspondence;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.Correspondences;
import au.org.theark.core.vo.CorrespondenceVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.component.correspondence.form.ContainerForm;

public class CorrespondenceContainerPanel extends AbstractContainerPanel<CorrespondenceVO> {

	/**
	 * 
	 */
	private static final long						serialVersionUID	= 1L;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService							studyService;

	// container form
	private ContainerForm							containerForm;
	// panels
	private SearchPanel								searchPanel;
	private DetailPanel								detailPanel;
	private PageableListView<Correspondences>	pageableListView;

	public CorrespondenceContainerPanel(String id) {

		super(id);
		cpModel = new CompoundPropertyModel<CorrespondenceVO>(new CorrespondenceVO());
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
		add(containerForm);

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

		// get the person in context
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);

		// todo remove / evaluate unused code
		// String sessionPersonType = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_TYPE);

		try {
			// initialize the correspondence list
			Collection<Correspondences> personCorrespondenceList = new ArrayList<Correspondences>();
			// can be a subject or contact
			if (sessionPersonId != null) {
				containerForm.getModelObject().getCorrespondence().setPerson(studyService.getPerson(sessionPersonId));
				personCorrespondenceList = studyService.getPersonCorrespondenceList(sessionPersonId, containerForm.getModelObject().getCorrespondence());
			}

			// add the corresponden\ce items related to the person if one found in session, or an empty list
			cpModel.getObject().setCorrespondenceList(personCorrespondenceList);
			searchPanel = new SearchPanel("searchPanel", feedBackPanel, pageableListView, arkCrudContainerVO);
			searchPanel.initialisePanel(cpModel);
			arkCrudContainerVO.getSearchPanelContainer().add(searchPanel);

		}
		catch (EntityNotFoundException ex) {
			ex.printStackTrace();
		}
		catch (ArkSystemException ex) {
			ex.printStackTrace();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return arkCrudContainerVO.getSearchPanelContainer();
	}

	@Override
	protected WebMarkupContainer initialiseSearchResults() {

		SearchResultListPanel searchResultPanel = new SearchResultListPanel("searchResults", containerForm, arkCrudContainerVO);

		iModel = new LoadableDetachableModel<Object>() {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load() {

				// get the personId in session and get the correspondenceList from the backend
				Collection<Correspondences> correspondenceList = new ArrayList<Correspondences>();
				Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);

				try {
					if (isActionPermitted()) {
						if (sessionPersonId != null) {
							correspondenceList = studyService.getPersonCorrespondenceList(sessionPersonId, containerForm.getModelObject().getCorrespondence());
						}
					}
				}
				catch (ArkSystemException ex) {
					ex.printStackTrace();
				}

				pageableListView.removeAll();
				return correspondenceList;
			}
		};

		pageableListView = searchResultPanel.buildPageableListView(iModel);
		pageableListView.setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("correspondenceNavigator", pageableListView);
		searchResultPanel.add(pageNavigator);
		searchResultPanel.add(pageableListView);
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultPanel);

		return arkCrudContainerVO.getSearchResultPanelContainer();
	}

}
