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
package au.org.theark.study.web.component.attachments.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyCompStatus;
import au.org.theark.core.model.study.entity.SubjectFile;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.attachments.DetailPanel;

/**
 * @author cellis
 * 
 */
public class SearchForm extends AbstractSearchForm<SubjectVO> {

	private static final long							serialVersionUID	= 716294824375744799L;
	private transient Logger							log					= LoggerFactory.getLogger(SearchForm.class);
	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	protected IArkCommonService						iArkCommonService;

	@SpringBean(name = Constants.STUDY_SERVICE)
	protected IStudyService								iStudyService;

	protected DetailPanel								detailPanel;
	protected PageableListView<SubjectFile>		pageableListView;
	protected CompoundPropertyModel<SubjectVO>	cpmModel;

	/**
	 * Form Components
	 */
	protected TextField<String>						subjectFileId;
	protected TextField<String>						subjectFileName;
	private DropDownChoice<StudyCompStatus>		studyComponentChoice;
	private ArkCrudContainerVO							arkCrudContainerVO;

	public SearchForm(String id, CompoundPropertyModel<SubjectVO> model, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel, PageableListView<SubjectFile> listView) {
		super(id, model, feedBackPanel, arkCrudContainerVO);
		this.cpmModel = model;
		this.pageableListView = listView;
		this.arkCrudContainerVO = arkCrudContainerVO;

		Label generalTextLbl = new Label("generalLbl", new StringResourceModel("search.panel.text", new Model<String>()));
		add(generalTextLbl);
		resetButton.setVisible(false);
		searchButton.setVisible(false);
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		disableSearchForm(sessionPersonId, au.org.theark.core.Constants.MESSAGE_NO_SUBJECT_IN_CONTEXT);
	}

	@SuppressWarnings("unchecked")
	private void initialiseDropDownChoices() {

		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study studyInContext = iArkCommonService.getStudy(studyId);
		List<StudyComp> studyCompList = iArkCommonService.getStudyComponentByStudy(studyInContext);
		ChoiceRenderer<StudyComp> defaultChoiceRenderer = new ChoiceRenderer<StudyComp>(Constants.NAME, Constants.ID);
		studyComponentChoice = new DropDownChoice(Constants.SUBJECT_FILE_STUDY_COMP, studyCompList, defaultChoiceRenderer);
	}

	protected void initialiseSearchForm() {
		subjectFileId = new TextField<String>(Constants.SUBJECT_FILE_ID);
		subjectFileName = new TextField<String>(Constants.SUBJECT_FILE_FILENAME);
		initialiseDropDownChoices();
	}

	protected void addSearchComponentsToForm() {
		add(subjectFileId);
		add(subjectFileName);
		add(studyComponentChoice);
	}

	@Override
	protected void onSearch(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		SubjectFile subjectFile = new SubjectFile();
		subjectFile = getModelObject().getSubjectFile();
		Collection<SubjectFile> subjectFileList = new ArrayList<SubjectFile>();

		try {
			LinkSubjectStudy linkSubjectStudy = null;
			Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
			Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			Study study = iArkCommonService.getStudy(studyId);
			linkSubjectStudy = iArkCommonService.getSubject(sessionPersonId, study);
			subjectFile.setLinkSubjectStudy(linkSubjectStudy);
		}
		catch (EntityNotFoundException e1) {
			this.error("There is no subject selected.");
			target.add(feedbackPanel);
		}

		try {
			// Look up based on criteria via back end.
			subjectFileList = iStudyService.searchSubjectFile(subjectFile);

			if (subjectFileList != null && subjectFileList.size() == 0) {
				this.info("There are no subject files for the specified search criteria.");
				target.add(feedbackPanel);
			}

			getModelObject().setSubjectFileList(subjectFileList);
			pageableListView.removeAll();
			arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
			target.add(arkCrudContainerVO.getSearchResultPanelContainer());

		}
		catch (EntityNotFoundException e) {
			log.error(e.getMessage());
		}
		catch (ArkSystemException e) {
			log.error(e.getMessage());
		}
	}

	@Override
	protected void onNew(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		getModelObject().getSubjectFile().setId(null);
		preProcessDetailPanel(target);
	}
}
