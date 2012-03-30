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
package au.org.theark.study.web.component.attachments;

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
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.SubjectFile;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.attachments.form.ContainerForm;

/**
 * 
 * @author nivedann
 * @author elam
 * @author cellis
 *
 */
public class AttachmentsContainerPanel extends AbstractContainerPanel<SubjectVO> {

	private static final long					serialVersionUID	= 1L;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService						studyService;

	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService					iArkCommonService;

	// Container Form
	private ContainerForm						containerForm;
	private SearchPanel							searchPanel;
	private DetailPanel							detailPanel;
	private PageableListView<SubjectFile>	pageableListView;

	/**
	 * Constructor
	 * 
	 * @param id
	 */
	public AttachmentsContainerPanel(String id) {

		super(id);
		cpModel = new CompoundPropertyModel<SubjectVO>(new SubjectVO());
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
		
		detailPanel = new  DetailPanel("detailPanel",feedBackPanel,arkCrudContainerVO,containerForm);
		detailPanel.initialisePanel();;
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
		//Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		Collection<SubjectFile> subjectFileList = new ArrayList<SubjectFile>();

		// All the subject file items related to the subject if one found in session or an empty list
		cpModel.getObject().setSubjectFileList(subjectFileList);
		searchPanel = new SearchPanel("searchComponentPanel",feedBackPanel,arkCrudContainerVO,pageableListView);
		searchPanel.initialisePanel(cpModel);
		arkCrudContainerVO.getSearchPanelContainer().add(searchPanel);
		return arkCrudContainerVO.getSearchPanelContainer(); //searchPanelContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseSearchResults()
	 */
	@Override
	protected WebMarkupContainer initialiseSearchResults() {
		SearchResultListPanel searchResultPanel = new SearchResultListPanel("searchResults",arkCrudContainerVO,containerForm);
		iModel = new LoadableDetachableModel<Object>() {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load() {
				// Get the PersonId from session and get the subjectFileList from back end
				Collection<SubjectFile> subjectFileList = new ArrayList<SubjectFile>();
				try {
					if (isActionPermitted()) {
						SubjectFile subjectFile = containerForm.getModelObject().getSubjectFile();
						Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
						Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
						Study study = iArkCommonService.getStudy(studyId);
						
						if(sessionPersonId != null){
							LinkSubjectStudy linkSubjectStudy = iArkCommonService.getSubject(sessionPersonId, study);
							subjectFile.setLinkSubjectStudy(linkSubjectStudy);
							subjectFileList = studyService.searchSubjectFile(subjectFile);
						}
					}
				}
				catch (EntityNotFoundException e) {
					containerForm.error("The person ID/Subject in context does not exist in the system.");
				}
				catch (ArkSystemException e) {
					containerForm.error("A System error has occured. Please contact Support");
				}

				pageableListView.removeAll();
				return subjectFileList;
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
