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
package au.org.theark.study.web.component.calendar.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.StudyCalendar;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.study.model.vo.StudyCalendarVo;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

/**
 * @author nivedann
 * 
 */
public class SearchForm extends AbstractSearchForm<StudyCalendarVo> {

	private static final long serialVersionUID = 1L;
	private ArkCrudContainerVO arkCrudContainerVO;
	private TextField<String> studyCalendarIdTxtFld;
	private TextField<String> studyCalendarNameTxtFld;
	private DateTextField studyCalendarStartDateFld;
	private DateTextField studyCalendarEndDateFld;
	
	private PageableListView<StudyCalendar> listView;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService studyService;

	/**
	 * 
	 * @param id
	 * @param cpmModel
	 * @param arkCrudContainerVO
	 * @param feedBackPanel
	 * @param listView
	 */
	public SearchForm(String id, CompoundPropertyModel<StudyCalendarVo> cpmModel, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel, PageableListView<StudyCalendar> listView) {

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
		add(studyCalendarIdTxtFld);
		add(studyCalendarNameTxtFld);
		add(studyCalendarStartDateFld);
		add(studyCalendarEndDateFld);

	}

	protected void initialiseSearchForm() {
		studyCalendarIdTxtFld = new TextField<String>(Constants.STUDY_CALENDAR_ID);
		
		studyCalendarNameTxtFld = new TextField<String>(Constants.STUDY_CALENDAR_NAME);
		
		studyCalendarStartDateFld = new DateTextField(Constants.STUDY_CALENDAR_START_DATE, au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker startDatePicker = new ArkDatePicker();
		startDatePicker.bind(studyCalendarStartDateFld);
		studyCalendarStartDateFld.add(startDatePicker);
		
		studyCalendarEndDateFld = new DateTextField(Constants.STUDY_CALENDAR_END_DATE, au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker endDatePicker = new ArkDatePicker();
		endDatePicker.bind(studyCalendarEndDateFld);
		studyCalendarEndDateFld.add(endDatePicker);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * au.org.theark.core.form.AbstractSearchForm#onNew(org.apache.wicket.ajax
	 * .AjaxRequestTarget)
	 */
	@Override
	protected void onNew(AjaxRequestTarget target) {
		getModelObject().setMode(Constants.MODE_NEW);
		getModelObject().getStudyCalendar().setId(null);
		preProcessDetailPanel(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * au.org.theark.core.form.AbstractSearchForm#onSearch(org.apache.wicket
	 * .ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSearch(AjaxRequestTarget target) {

		target.add(feedbackPanel);

		List<StudyCalendar> resultList = studyService.searchStudyCalenderList(getModelObject().getStudyCalendar());

		if (resultList != null && resultList.size() == 0) {
			this.info("Study Component with the specified criteria does not exist in the system.");
			target.add(feedbackPanel);
		}

		getModelObject().setStudyCalendarList(resultList);
		listView.removeAll();

		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
		target.add(arkCrudContainerVO.getSearchResultPanelContainer());

	}

}
