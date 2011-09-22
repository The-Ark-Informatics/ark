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
package au.org.theark.lims.web.component.subjectlims.subject.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.ILimsSubjectService;
import au.org.theark.lims.web.Constants;

/**
 * @author cellis
 * 
 */
public class SearchForm extends AbstractSearchForm<LimsVO> {

	/**
	 * 
	 */
	private static final long					serialVersionUID	= 5853988156214275754L;
	protected static final Logger				log					= LoggerFactory.getLogger(SearchForm.class);

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;

	@SpringBean(name = Constants.LIMS_SUBJECT_SERVICE)
	private ILimsSubjectService				iLimsSubjectService;

	private DropDownChoice<Study>				studyDdc;
	private TextField<String>					subjectUIDTxtFld;
	private TextField<String>					firstNameTxtFld;
	private TextField<String>					middleNameTxtFld;
	private TextField<String>					lastNameTxtFld;
	private DropDownChoice<VitalStatus>		vitalStatusDdc;
	private DropDownChoice<GenderType>		genderTypeDdc;
	private DateTextField						dateOfBirthTxtFld;
	private CompoundPropertyModel<LimsVO>	cpmModel;

	/**
	 * @param id
	 * @param cpmModel
	 */
	public SearchForm(String id, CompoundPropertyModel<LimsVO> cpmModel, PageableListView<LimsVO> listView, FeedbackPanel feedBackPanel, WebMarkupContainer listContainer,
			WebMarkupContainer searchMarkupContainer, WebMarkupContainer detailsContainer, WebMarkupContainer detailPanelFormContainer, WebMarkupContainer viewButtonContainer,
			WebMarkupContainer editButtonContainer) {

		// super(id, cpmModel);
		super(id, cpmModel, detailsContainer, detailPanelFormContainer, viewButtonContainer, editButtonContainer, searchMarkupContainer, listContainer, feedBackPanel);

		this.cpmModel = cpmModel;
		initialiseSearchForm();
		addSearchComponentsToForm();

		// hide New button for Subject in LIMS
		newButton = new AjaxButton(au.org.theark.core.Constants.NEW) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 6539600487179555764L;

			@Override
			public boolean isVisible() {
				return false;
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit();
			}
		};
		addOrReplace(newButton);
	}

	protected void addSearchComponentsToForm() {
		add(studyDdc);
		add(subjectUIDTxtFld);
		add(firstNameTxtFld);
		add(middleNameTxtFld);
		add(lastNameTxtFld);
		add(vitalStatusDdc);
		add(genderTypeDdc);
		add(dateOfBirthTxtFld);
	}

	protected void initialiseSearchForm() {
		subjectUIDTxtFld = new TextField<String>(Constants.SUBJECT_UID);
		firstNameTxtFld = new TextField<String>(Constants.PERSON_FIRST_NAME);
		middleNameTxtFld = new TextField<String>(Constants.PERSON_MIDDLE_NAME);
		lastNameTxtFld = new TextField<String>(Constants.PERSON_LAST_NAME);

		initStudyDdc();
		initVitalStatusDdc();
		initSubjectStatusDdc();
		initGenderTypeDdc();

		dateOfBirthTxtFld = new DateTextField(Constants.PERSON_DOB, au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker dobDatePicker = new ArkDatePicker();
		dobDatePicker.bind(dateOfBirthTxtFld);
		dateOfBirthTxtFld.add(dobDatePicker);
	}

	private void initStudyDdc() {
		CompoundPropertyModel<LimsVO> limsSubjectCpm = cpmModel;
		PropertyModel<Study> studyPm = new PropertyModel<Study>(limsSubjectCpm, "study");
		List<Study> studyListForUser = new ArrayList<Study>(0);
		studyListForUser = getStudyListForUser();
		ChoiceRenderer<Study> studyRenderer = new ChoiceRenderer<Study>(Constants.NAME, Constants.ID);
		studyDdc = new DropDownChoice<Study>("study", studyPm, (List<Study>) studyListForUser, studyRenderer){
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
				this.setChoices(getStudyListForUser());
			}
		};
	}

	private void initVitalStatusDdc() {
		CompoundPropertyModel<LimsVO> limsSubjectCpm = cpmModel;
		PropertyModel<LinkSubjectStudy> linkSubjectStudyPm = new PropertyModel<LinkSubjectStudy>(limsSubjectCpm, "linkSubjectStudy");
		PropertyModel<Person> personPm = new PropertyModel<Person>(linkSubjectStudyPm, "person");
		PropertyModel<VitalStatus> vitalStatusPm = new PropertyModel<VitalStatus>(personPm, Constants.VITAL_STATUS);
		Collection<VitalStatus> vitalStatusList = iArkCommonService.getVitalStatus();
		ChoiceRenderer<VitalStatus> vitalStatusRenderer = new ChoiceRenderer<VitalStatus>(Constants.NAME, Constants.ID);
		vitalStatusDdc = new DropDownChoice<VitalStatus>(Constants.VITAL_STATUS, vitalStatusPm, (List<VitalStatus>) vitalStatusList, vitalStatusRenderer);
	}

	private void initSubjectStatusDdc() {
		CompoundPropertyModel<LimsVO> limsSubjectCpm = cpmModel;
		PropertyModel<LinkSubjectStudy> linkSubjectStudyPm = new PropertyModel<LinkSubjectStudy>(limsSubjectCpm, "linkSubjectStudy");
		PropertyModel<SubjectStatus> subjectStatusPm = new PropertyModel<SubjectStatus>(linkSubjectStudyPm, "subjectStatus");
		List<SubjectStatus> subjectStatusList = iArkCommonService.getSubjectStatus();
		ChoiceRenderer<SubjectStatus> subjectStatusRenderer = new ChoiceRenderer<SubjectStatus>(Constants.NAME, Constants.SUBJECT_STATUS_ID);
		new DropDownChoice<SubjectStatus>(Constants.SUBJECT_STATUS, subjectStatusPm, subjectStatusList, subjectStatusRenderer);
	}

	private void initGenderTypeDdc() {
		CompoundPropertyModel<LimsVO> subjectCpm = cpmModel;
		PropertyModel<LinkSubjectStudy> linkSubjectStudyPm = new PropertyModel<LinkSubjectStudy>(subjectCpm, "linkSubjectStudy");
		PropertyModel<Person> personPm = new PropertyModel<Person>(linkSubjectStudyPm, Constants.PERSON);
		PropertyModel<GenderType> genderTypePm = new PropertyModel<GenderType>(personPm, Constants.GENDER_TYPE);
		Collection<GenderType> genderTypeList = iArkCommonService.getGenderType();
		ChoiceRenderer<GenderType> genderTypeRenderer = new ChoiceRenderer<GenderType>(Constants.NAME, Constants.ID);
		genderTypeDdc = new DropDownChoice<GenderType>(Constants.GENDER_TYPE, genderTypePm, (List<GenderType>) genderTypeList, genderTypeRenderer);
	}

	protected void onNew(AjaxRequestTarget target) {
		// Should never get here since new should never be enabled for Subject Details via LIMS
		log.error("Incorrect application workflow - tried to create a new Subject via LIMS");
	}

	protected void onSearch(AjaxRequestTarget target) {
		target.addComponent(feedbackPanel);

		List<Study> studyList = new ArrayList<Study>(0);

		// Restrict search if Study selected in Search form
		if (cpmModel.getObject().getStudy() != null) {
			studyList.add(cpmModel.getObject().getStudy());
		}
		else {
			studyList = cpmModel.getObject().getStudyList();
			if(studyList.isEmpty()) {
				log.error("StudyList is empty, shouldn't happen!");
				studyList = getStudyListForUser();
			}
		}

		int count = iLimsSubjectService.getSubjectCount(cpmModel.getObject(), studyList);
		if (count == 0) {
			this.info("There are no subjects with the specified criteria.");
			target.addComponent(feedbackPanel);
		}

		listContainer.setVisible(true);// Make the WebMarkupContainer that houses the search results visible
		target.addComponent(listContainer);// For ajax this is required so
	}
	
	/**
	 * Returns a list of Studies the user is permitted to access
	 * 
	 * @return
	 */
	private List<Study> getStudyListForUser() {
		List<Study> studyListForUser = new ArrayList<Study>(0);
		try {
			Subject currentUser = SecurityUtils.getSubject();
			ArkUser arkUser = iArkCommonService.getArkUser(currentUser.getPrincipal().toString());
			ArkUserVO arkUserVo = new ArkUserVO();
			arkUserVo.setArkUserEntity(arkUser);
			
			Long sessionArkModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);
			ArkModule arkModule = null;
			arkModule = iArkCommonService.getArkModuleById(sessionArkModuleId);
			studyListForUser = iArkCommonService.getStudyListForUserAndModule(arkUserVo, arkModule);
		}
		catch (EntityNotFoundException e) {
			log.error(e.getMessage());
		}
		return studyListForUser;
	}
}