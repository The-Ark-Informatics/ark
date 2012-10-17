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
package au.org.theark.report.web.component.dataextraction.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.model.report.entity.Search;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.SearchVO;
import au.org.theark.core.web.form.AbstractSearchForm;

/**
 * @author nivedann
 * 
 */
public class SearchForm extends AbstractSearchForm<SearchVO> {


	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService				iArkCommonService;
	
	private static final long				serialVersionUID	= 1L;
	private ArkCrudContainerVO				arkCrudContainerVO;
	private TextField<String>				searchIdTxtFld;
	private TextField<String>				searchNameTxtFld;
	private PageableListView<Search>		listView;

	/**
	 * 
	 * @param id
	 * @param cpmModel
	 * @param arkCrudContainerVO
	 * @param feedBackPanel
	 * @param listView
	 */
	public SearchForm(String id, CompoundPropertyModel<SearchVO> cpmModel, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel, PageableListView<Search> listView) {

		super(id, cpmModel, feedBackPanel, arkCrudContainerVO);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedBackPanel;
		this.listView = listView;

		initialiseSearchForm();
		addSearchComponentsToForm();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		disableSearchForm(sessionStudyId, "There is no study in context. Please select a Study.");
	}

	protected void addSearchComponentsToForm() {
		add(searchIdTxtFld);
		add(searchNameTxtFld);
	}

	protected void initialiseSearchForm() {

		searchIdTxtFld = new TextField<String>(Constants.SEARCH_ID);
		searchNameTxtFld = new TextField<String>(Constants.SEARCH_NAME);
	//	descriptionTxtArea = new TextArea<String>(Constants.STUDY_COMPONENT_DESCRIPTION);
//		keywordTxtArea = new TextArea<String>(Constants.STUDY_COMPONENT_KEYWORD);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.form.AbstractSearchForm#onNew(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onNew(AjaxRequestTarget target) {
//		getModelObject().setMode(Constants.MODE_NEW);
//		getModelObject().getStudyComponent().setId(null);
		preProcessDetailPanel(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.form.AbstractSearchForm#onSearch(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSearch(AjaxRequestTarget target) {

		target.add(feedbackPanel);
	//	try {

			List<Search> resultList = iArkCommonService.getSearchesForThisStudy(getModelObject().getSearch().getStudy());

			if (resultList != null && resultList.size() == 0) {
				this.info("Study Component with the specified criteria does not exist in the system.");
				target.add(feedbackPanel);
			}

			getModelObject().setListOfSearchesForResultList(resultList);
			listView.removeAll();

			arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
			target.add(arkCrudContainerVO.getSearchResultPanelContainer());
	//	}
	//	catch (ArkSystemException arkEx) {
	//		this.error("A system error has occured. Please try after sometime.");
	//	}

	}

}
