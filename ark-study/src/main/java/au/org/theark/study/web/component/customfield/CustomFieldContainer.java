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
package au.org.theark.study.web.component.customfield;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.SubjectCustmFld;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.customfield.form.ContainerForm;

/**
 * @author nivedann
 * 
 */
public class CustomFieldContainer extends AbstractContainerPanel<CustomFieldVO> {

	private ContainerForm							containerForm;

	private SearchPanel								searchPanel;
	private SearchResultListPanel					searchResultListPanel;
	private DetailPanel								detailPanel;
	private PageableListView<SubjectCustmFld>	pageableListView;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	protected IArkCommonService					iArkCommonService;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService							studyService;

	/**
	 * @param id
	 */
	public CustomFieldContainer(String id) {

		super(id, true);// call the new constructor

		cpModel = new CompoundPropertyModel<CustomFieldVO>(new CustomFieldVO());
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
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
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseSearchPanel()
	 */
	@Override
	protected WebMarkupContainer initialiseSearchPanel() {

		SearchPanel searchPanel = new SearchPanel("searchComponentPanel", feedBackPanel, pageableListView, containerForm, detailPanel, arkCrudContainerVO);

		arkCrudContainerVO.getSearchPanelContainer().add(searchPanel);
		return arkCrudContainerVO.getSearchPanelContainer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseSearchResults()
	 */
	@Override
	protected WebMarkupContainer initialiseSearchResults() {

		searchResultListPanel = new SearchResultListPanel("searchResults", containerForm, arkCrudContainerVO);

		iModel = new LoadableDetachableModel<Object>() {
			@Override
			protected Object load() {
				Collection<SubjectCustmFld> fieldList = new ArrayList<SubjectCustmFld>();

				Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				Study study = iArkCommonService.getStudy(sessionStudyId);
				// Get the list of Study Related Custom Fields
				containerForm.getModelObject().getSubjectCustomField().setStudy(study);
				SubjectCustmFld customField = containerForm.getModelObject().getSubjectCustomField();
				fieldList = studyService.searchStudyFields(customField);
				pageableListView.removeAll();
				return fieldList;
			}
		};

		pageableListView = searchResultListPanel.buildPageableListView(iModel);
		pageableListView.setReuseItems(true);

		// Build Navigator
		PagingNavigator pageNavigator = new PagingNavigator("navigator", pageableListView);
		searchResultListPanel.add(pageNavigator);
		searchResultListPanel.add(pageableListView);
		arkCrudContainerVO.getSearchResultPanelContainer().add(searchResultListPanel);
		return arkCrudContainerVO.getSearchResultPanelContainer();
	}

}
