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
package au.org.theark.study.web.component.subjectFile.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyCompStatus;
import au.org.theark.core.model.study.entity.SubjectFile;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.subjectFile.DetailPanel;

/**
 * @author cellis
 * 
 */
@SuppressWarnings({ "serial" })
public class SearchForm extends AbstractSearchForm<SubjectVO> {

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	protected IArkCommonService						iArkCommonService;

	@SpringBean(name = Constants.STUDY_SERVICE)
	protected IStudyService								studyService;

	protected DetailPanel								detailPanel;
	protected PageableListView<SubjectFile>		pageableListView;
	protected CompoundPropertyModel<SubjectVO>	cpmModel;

	/**
	 * Form Components
	 */
	protected TextField<String>						subjectFileId;
	protected TextField<String>						subjectFileName;
	private DropDownChoice<StudyCompStatus>		studyComponentChoice;
	private ArkCrudContainerVO				arkCrudContainerVO;
	
	public SearchForm(String id,CompoundPropertyModel<SubjectVO> model,ArkCrudContainerVO arkCrudContainerVO,FeedbackPanel feedBackPanel,PageableListView<SubjectFile> listView ){
		
		super(id,model,feedBackPanel,arkCrudContainerVO);
		this.cpmModel = model;
		this.pageableListView = listView;
		this.arkCrudContainerVO = arkCrudContainerVO;
		initialiseSearchForm();
		addSearchComponentsToForm();
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		disableSearchForm(sessionPersonId, "There is no subject or contact in context. Please select a Subject or Contact.",arkCrudContainerVO);
	}

	@SuppressWarnings("unchecked")
	private void initialiseDropDownChoices() {
		// Initialise Drop Down Choices
		List<StudyComp> studyCompList = iArkCommonService.getStudyComponent();
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
		target.addComponent(feedbackPanel);
		SubjectFile subjectFile = new SubjectFile();
		subjectFile = getModelObject().getSubjectFile();
		Collection<SubjectFile> subjectFileList = new ArrayList<SubjectFile>();

		try {
			LinkSubjectStudy linkSubjectStudy = null;
			Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
			linkSubjectStudy = iArkCommonService.getSubject(sessionPersonId);
			subjectFile.setLinkSubjectStudy(linkSubjectStudy);
		}
		catch (EntityNotFoundException e1) {
			this.error("There is no subject in context.");
			target.addComponent(feedbackPanel);
		}

		try {
			// Look up based on criteria via back end.
			subjectFileList = studyService.searchSubjectFile(subjectFile);

			if (subjectFileList != null && subjectFileList.size() == 0) {
				this.info("There are no subject files for the specified criteria.");
				target.addComponent(feedbackPanel);
			}

			getModelObject().setSubjectFileList(subjectFileList);
			pageableListView.removeAll();
			arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
			target.addComponent(arkCrudContainerVO.getSearchResultPanelContainer());

		}
		catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ArkSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onNew(AjaxRequestTarget target) {
		// ARK-108:: no longer do full reset to VO
		getModelObject().getSubjectFile().setId(null); // only reset ID (not
		// user definable)

		preProcessDetailPanel(target,arkCrudContainerVO);
	}

}
