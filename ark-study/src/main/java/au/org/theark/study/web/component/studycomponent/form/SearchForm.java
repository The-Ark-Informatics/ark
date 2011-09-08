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
package au.org.theark.study.web.component.studycomponent.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.study.model.vo.StudyCompVo;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

/**
 * @author nivedann
 * 
 */
public class SearchForm extends AbstractSearchForm<StudyCompVo> {

	private TextField<String>				studyCompIdTxtFld;
	private TextField<String>				compNameTxtFld;
	private TextArea<String>				descriptionTxtArea;
	private TextArea<String>				keywordTxtArea;
	private PageableListView<StudyComp>	listView;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService					studyService;

	protected void addSearchComponentsToForm() {
		add(studyCompIdTxtFld);
		add(compNameTxtFld);
		add(keywordTxtArea);
	}

	protected void initialiseSearchForm() {

		studyCompIdTxtFld = new TextField<String>(Constants.STUDY_COMPONENT_ID);
		compNameTxtFld = new TextField<String>(Constants.STUDY_COMPONENT_NAME);
		descriptionTxtArea = new TextArea<String>(Constants.STUDY_COMPONENT_DESCRIPTION);
		keywordTxtArea = new TextArea<String>(Constants.STUDY_COMPONENT_KEYWORD);
	}

	/**
	 * @param id
	 * @param cpmModel
	 */
	public SearchForm(String id, CompoundPropertyModel<StudyCompVo> cpmModel, PageableListView<StudyComp> listView, FeedbackPanel feedBackPanel, WebMarkupContainer listContainer,
			WebMarkupContainer searchMarkupContainer, WebMarkupContainer detailsContainer, WebMarkupContainer detailPanelFormContainer, WebMarkupContainer viewButtonContainer,
			WebMarkupContainer editButtonContainer) {

		// super(id, cpmModel);
		super(id, cpmModel, detailsContainer, detailPanelFormContainer, viewButtonContainer, editButtonContainer, searchMarkupContainer, listContainer, feedBackPanel);

		this.listView = listView;
		initialiseSearchForm();
		addSearchComponentsToForm();
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		disableSearchForm(sessionStudyId, "There is no study in context. Please select a Study.");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.form.AbstractSearchForm#onNew(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onNew(AjaxRequestTarget target) {

		StudyCompVo studyCompVo = new StudyCompVo();
		studyCompVo.setMode(Constants.MODE_NEW);
		setModelObject(studyCompVo);
		// processDetail(target);
		preProcessDetailPanel(target);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.form.AbstractSearchForm#onSearch(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSearch(AjaxRequestTarget target) {

		target.addComponent(feedbackPanel);
		try {

			List<StudyComp> resultList = studyService.searchStudyComp(getModelObject().getStudyComponent());

			if (resultList != null && resultList.size() == 0) {
				this.info("Study Component with the specified criteria does not exist in the system.");
				target.addComponent(feedbackPanel);
			}
			getModelObject().setStudyCompList(resultList);
			listView.removeAll();
			listContainer.setVisible(true);// Make the WebMarkupContainer that houses the search results visible
			target.addComponent(listContainer);// For ajax this is required so
		}
		catch (ArkSystemException arkEx) {
			this.error("A system error has occured. Please try after sometime.");
		}

	}

}
