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
package au.org.theark.study.web.component.managestudy.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.StudyCrudContainerVO;
import au.org.theark.core.vo.StudyModelVO;
import au.org.theark.core.web.StudyHelper;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.managestudy.DetailPanel;

public class SearchForm extends AbstractSearchForm<StudyModelVO> {
	private static final long							serialVersionUID	= -5468677674413992897L;
	private static final Logger						log					= LoggerFactory.getLogger(SearchForm.class);
	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService							iArkCommonService;

	/* The Input Components that will be part of the Search Form */
	private TextField<String>							studyIdTxtFld;
	private TextField<String>							studyNameTxtFld;
	private DateTextField								dateOfApplicationDp;
	private TextField<String>							principalContactTxtFld;
	private DropDownChoice<StudyStatus>				studyStatusDpChoices;
	private List<StudyStatus>							studyStatusList;
	private CompoundPropertyModel<StudyModelVO>	cpmModel;

	private StudyCrudContainerVO						studyCrudContainerVO;
	private Container										containerForm;
	private FeedbackPanel								feedbackPanel;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param model
	 * @param studyCrudContainerVO
	 * @param containerForm
	 */
	public SearchForm(String id, CompoundPropertyModel<StudyModelVO> studyModelVOCpm, StudyCrudContainerVO studyCrudContainerVO, FeedbackPanel feedbackPanel, Container containerForm) {

		super(id, studyModelVOCpm, feedbackPanel, studyCrudContainerVO);

		this.containerForm = containerForm;
		this.studyCrudContainerVO = studyCrudContainerVO;
		this.feedbackPanel = feedbackPanel;
		setMultiPart(true);

		cpmModel = studyModelVOCpm;
		initialiseSearchForm();
		addSearchComponentsToForm();
	}

	@SuppressWarnings("unchecked")
	protected void initialiseSearchForm() {
		studyIdTxtFld = new TextField<String>(Constants.STUDY_SEARCH_KEY);

		studyNameTxtFld = new TextField<String>(Constants.STUDY_SEARCH_NAME);
		dateOfApplicationDp = new DateTextField(Constants.STUDY_SEARCH_DOA, au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker datePicker = new ArkDatePicker();
		datePicker.bind(dateOfApplicationDp);
		dateOfApplicationDp.add(datePicker);

		principalContactTxtFld = new TextField<String>(Constants.STUDY_SEARCH_CONTACT);
		this.studyStatusList = iArkCommonService.getListOfStudyStatus();

		CompoundPropertyModel<StudyModelVO> studyCmpModel = (CompoundPropertyModel<StudyModelVO>) cpmModel;
		// Create a propertyModel to bind the components of this form, the root which is StudyContainer
		PropertyModel<Study> pm = new PropertyModel<Study>(studyCmpModel, "study");
		// Another PropertyModel for rendering the DropDowns and pass in the Property Model instance of type Study
		PropertyModel<StudyStatus> pmStudyStatus = new PropertyModel<StudyStatus>(pm, "studyStatus");
		initStudyStatusDropDown(pmStudyStatus);
	}

	@SuppressWarnings("unchecked")
	protected void onSearch(AjaxRequestTarget target) {
		try {
			List<Study> studyListForUser = new ArrayList<Study>(0);
			// Search study
			Study searchStudyCriteria = containerForm.getModelObject().getStudy();
			Subject currentUser = SecurityUtils.getSubject();
			ArkUser arkUser = iArkCommonService.getArkUser(currentUser.getPrincipal().toString());
			ArkUserVO arkUserVo = new ArkUserVO();
			arkUserVo.setArkUserEntity(arkUser);
			arkUserVo.setStudy(searchStudyCriteria);
			
			studyListForUser = iArkCommonService.getStudyListForUser(arkUserVo);

			if (studyListForUser.size() == 0) {
				containerForm.getModelObject().setStudyList(studyListForUser);
				this.info("There are no records that matched your query. Please modify your filter");
				target.add(feedbackPanel);
			}
			containerForm.getModelObject().setStudyList(studyListForUser);
			studyCrudContainerVO.getPageableListView().removeAll();
			studyCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
			target.add(studyCrudContainerVO.getSearchResultPanelContainer());
		}
		catch (EntityNotFoundException e) {
			log.error(e.getMessage());
			this.error("There are no records that matched your query. Please modify your filter");
			target.add(feedbackPanel);
		}
	}

	private void addSearchComponentsToForm() {
		add(studyIdTxtFld);
		add(studyNameTxtFld);
		add(dateOfApplicationDp);
		add(principalContactTxtFld);
		add(studyStatusDpChoices);
	}

	@SuppressWarnings( { "unchecked" })
	private void initStudyStatusDropDown(PropertyModel<StudyStatus> pmStudyStatus) {
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.STUDY_STATUS_KEY);
		studyStatusDpChoices = new DropDownChoice(Constants.STUDY_DROP_DOWN_CHOICE, pmStudyStatus, studyStatusList, defaultChoiceRenderer);
	}

	@SuppressWarnings("unchecked")
	protected void onNew(AjaxRequestTarget target) {
		containerForm.setModelObject(new StudyModelVO());
		Collection<ArkModule> availableArkModules = new ArrayList<ArkModule>();
		availableArkModules = iArkCommonService.getEntityList(ArkModule.class);
		containerForm.getModelObject().setAvailableArkModules(availableArkModules);// ArkModule from database not LDAP.

		// Hide Summary details on new
		studyCrudContainerVO.getSummaryContainer().setVisible(false);
		target.add(studyCrudContainerVO.getSummaryContainer());

		// Show upload item for new Study
		studyCrudContainerVO.getStudyLogoMarkup().setVisible(true);
		studyCrudContainerVO.getStudyLogoUploadContainer().setVisible(true);

		StudyHelper studyHelper = new StudyHelper();
		studyHelper.setStudyLogo(containerForm.getModelObject().getStudy(), target, studyCrudContainerVO.getStudyNameMarkup(), studyCrudContainerVO.getStudyLogoMarkup());
		
		target.add(studyCrudContainerVO.getStudyLogoMarkup());
		target.add(studyCrudContainerVO.getStudyLogoUploadContainer());

		// Clear context items
		ContextHelper contextHelper = new ContextHelper();
		contextHelper.resetContextLabel(target, studyCrudContainerVO.getArkContextMarkup());
		studyNameTxtFld.setEnabled(true);

		// Default boolean selections
		containerForm.getModelObject().getStudy().setAutoGenerateSubjectUid(false);
		containerForm.getModelObject().getStudy().setAutoConsent(false);

		//TODO: Suggest moving some of this "onNew" code into DetailPanel/DetailForm's onBeforeRender(..)
		// Disable SubjectUID pattern fields by default for New study
		WebMarkupContainer wmc = (WebMarkupContainer) studyCrudContainerVO.getDetailPanelContainer();
		DetailPanel detailsPanel = (DetailPanel) wmc.get("detailsPanel");
		DetailForm detailForm = (DetailForm) detailsPanel.get("detailForm");
		WebMarkupContainer autoSubjectUidcontainer = detailForm.getAutoSubjectUidContainer();
		WebMarkupContainer subjectUidcontainer = detailForm.getSubjectUidContainer();

		// Example auto-generated SubjectUID to "AAA-0000000001" on new
		containerForm.getModelObject().setSubjectUidExample(Constants.SUBJECTUID_EXAMPLE);
		Label subjectUidExampleLbl = detailForm.getSubjectUidExampleLbl();
		subjectUidExampleLbl.setDefaultModelObject(containerForm.getModelObject().getSubjectUidExample());
		target.add(subjectUidExampleLbl);

		autoSubjectUidcontainer.setEnabled(true);
		subjectUidcontainer.setEnabled(false);
		target.add(subjectUidcontainer);

		preProcessDetailPanel(target);
	}
}
