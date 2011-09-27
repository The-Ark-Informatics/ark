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
package au.org.theark.study.web.component.managestudy;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.StudyModelVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.study.web.component.managestudy.form.Container;

public class StudyContainer extends AbstractContainerPanel<StudyModelVO> {
	/**
	 * 
	 */
	private static final long			serialVersionUID	= 6705316114204293307L;
	private static final Logger		log					= LoggerFactory.getLogger(StudyContainer.class);
	private Container						containerForm;
	private Details						detailsPanel;
	private SearchResults				searchResultsPanel;
	private Search							searchStudyPanel;
	private TabbedPanel					moduleTabbedPanel;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>	iArkCommonService;

	private IModel<Object>				iModel;
	private StudyCrudContainerVO		studyCrudContainerVO;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param studyNameMarkup
	 * @param studyLogoMarkup
	 * @param arkContextMarkup
	 */
	public StudyContainer(String id, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup, WebMarkupContainer arkContextMarkup) {

		super(id);
		cpModel = new CompoundPropertyModel<StudyModelVO>(new StudyModelVO());
		// Create the form that will hold the other controls
		containerForm = new Container("containerForm", cpModel);

		/* Initialise the study crud container vo that has all the WebMarkups */
		studyCrudContainerVO = new StudyCrudContainerVO();
		// Set the Markups that was passed in into the VO
		studyCrudContainerVO.setStudyNameMarkup(studyNameMarkup);
		studyCrudContainerVO.setStudyLogoMarkup(studyLogoMarkup);
		studyCrudContainerVO.setArkContextMarkup(arkContextMarkup);

		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
		add(containerForm);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param studyNameMarkup
	 * @param studyLogoMarkup
	 * @param arkContextMarkup
	 * @param moduleTabbedPanel
	 */
	public StudyContainer(String id, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup, WebMarkupContainer arkContextMarkup, TabbedPanel moduleTabbedPanel) {
		super(id);
		this.moduleTabbedPanel = moduleTabbedPanel;

		cpModel = new CompoundPropertyModel<StudyModelVO>(new StudyModelVO());
		// Create the form that will hold the other controls
		containerForm = new Container("containerForm", cpModel);

		/* Initialise the study crud container vo that has all the WebMarkups */
		studyCrudContainerVO = new StudyCrudContainerVO();
		// Set the Markups that was passed in into the VO
		studyCrudContainerVO.setStudyNameMarkup(studyNameMarkup);
		studyCrudContainerVO.setStudyLogoMarkup(studyLogoMarkup);
		studyCrudContainerVO.setArkContextMarkup(arkContextMarkup);

		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseDetailPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseSearchPanel());
		add(containerForm);
	}

	protected WebMarkupContainer initialiseSearchPanel() {
		searchStudyPanel = new Search("searchStudyPanel", studyCrudContainerVO, feedBackPanel, containerForm);
		searchStudyPanel.initialisePanel(cpModel);
		studyCrudContainerVO.getSearchPanelContainer().add(searchStudyPanel);
		return studyCrudContainerVO.getSearchPanelContainer();
	}

	@Override
	protected WebMarkupContainer initialiseSearchResults() {
		searchResultsPanel = new SearchResults("searchResults", studyCrudContainerVO, containerForm, moduleTabbedPanel);
		iModel = new LoadableDetachableModel<Object>() {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load() {
				List<Study> studyListForUser = new ArrayList<Study>(0);
				try {
					Subject currentUser = SecurityUtils.getSubject();
					ArkUser arkUser = iArkCommonService.getArkUser(currentUser.getPrincipal().toString());
					ArkUserVO arkUserVo = new ArkUserVO();
					arkUserVo.setArkUserEntity(arkUser);
					arkUserVo.setStudy(containerForm.getModelObject().getStudy());
					studyListForUser = iArkCommonService.getStudyListForUser(arkUserVo);
					if (studyListForUser.size() == 0) {
						StudyContainer.this.error("You do not have any access permissions to any Study. Please see your Administrator.");
					}
				}
				catch (EntityNotFoundException e) {
					log.error(e.getMessage());
				}
				studyCrudContainerVO.getPageableListView().removeAll();
				return studyListForUser;
			}
		};

		studyCrudContainerVO.setPageableListView(searchResultsPanel.buildPageableListView(iModel, studyCrudContainerVO.getSearchResultPanelContainer()));
		studyCrudContainerVO.getPageableListView().setReuseItems(true);
		PagingNavigator pageNavigator = new PagingNavigator("navigator", studyCrudContainerVO.getPageableListView());
		searchResultsPanel.add(pageNavigator);
		searchResultsPanel.add(studyCrudContainerVO.getPageableListView());
		studyCrudContainerVO.getSearchResultPanelContainer().add(searchResultsPanel);
		return studyCrudContainerVO.getSearchResultPanelContainer();
	}

	@Override
	protected WebMarkupContainer initialiseDetailPanel() {
		detailsPanel = new Details("detailsPanel", feedBackPanel, studyCrudContainerVO, containerForm);
		detailsPanel.initialisePanel();
		studyCrudContainerVO.getDetailPanelContainer().add(detailsPanel);
		return studyCrudContainerVO.getDetailPanelContainer();
	}

	/**
	 * @param moduleTabbedPanel
	 *           the moduleTabbedPanel to set
	 */
	public void setModuleTabbedPanel(TabbedPanel moduleTabbedPanel) {
		this.moduleTabbedPanel = moduleTabbedPanel;
	}

	/**
	 * @return the moduleTabbedPanel
	 */
	public TabbedPanel getModuleTabbedPanel() {
		return moduleTabbedPanel;
	}
}
