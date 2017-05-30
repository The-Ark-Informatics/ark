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
package au.org.theark.study.web.component.global.subject.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.OtherID;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.ContextHelper;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.StudyHelper;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

/**
 * @author nivedann
 * 
 */
public class SearchForm extends AbstractSearchForm<SubjectVO> {
	private static final long						serialVersionUID	= 1L;
	protected static final Logger				log					= LoggerFactory.getLogger(SearchForm.class);

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService						iArkCommonService;

	@SpringBean(name = au.org.theark.core.Constants.STUDY_SERVICE)
	private IStudyService							iStudyService;
	
	protected WebMarkupContainer arkContextMarkup;
	protected WebMarkupContainer studyNameMarkup;
	protected WebMarkupContainer studyLogoMarkup;

	private DropDownChoice<Study>					studyDdc;
	private TextField<String>						subjectUIDTxtFld;
	private TextField<String>						firstNameTxtFld;
	private TextField<String>						middleNameTxtFld;
	private TextField<String>						lastNameTxtFld;
	private DropDownChoice<VitalStatus>			vitalStatusDdc;
	private DropDownChoice<GenderType>			genderTypeDdc;
	private DropDownChoice<SubjectStatus>		subjectStatusDdc;
	private DateTextField							dateOfBirthTxtFld;
	private TextField<String> otherIDTxtFld;

	private List<Study> studyListForUser = new ArrayList<Study>(0);
	
	// TODO get explanation never accessed, yet we can set it - maybe wicket can access?
	//private PageableListView<SubjectVO>			listView;
	private CompoundPropertyModel<SubjectVO>	cpmModel;

	/**
	 * @param id
	 * @param cpmModel
	 * @param arkContextMarkup 
	 * @param studyNameMarkup s
	 * @param studyLogoMarkup
	 */
	public SearchForm(String id, CompoundPropertyModel<SubjectVO> cpmModel, PageableListView<SubjectVO> listView, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, WebMarkupContainer arkContextMarkup, WebMarkupContainer studyNameMarkup, WebMarkupContainer studyLogoMarkup) {
		// super(id, cpmModel);
		super(id, cpmModel, feedBackPanel, arkCrudContainerVO);

		this.cpmModel = cpmModel;
		//this.listView = listView;
		this.arkContextMarkup = arkContextMarkup;
		this.studyNameMarkup = studyNameMarkup;
		this.studyLogoMarkup = studyLogoMarkup;
		
		initialiseSearchForm();
		addSearchComponentsToForm();
//		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
//		disableSearchForm(sessionStudyId, "There is no study in context. Please select a study");
	}

	protected void addSearchComponentsToForm() {
		add(studyDdc);
		add(subjectUIDTxtFld);
		add(firstNameTxtFld);
		add(middleNameTxtFld);
		add(lastNameTxtFld);
		add(vitalStatusDdc);
		add(subjectStatusDdc);
		add(genderTypeDdc);
		add(dateOfBirthTxtFld);
		add(otherIDTxtFld);
	}

	protected void initialiseSearchForm() {
		initStudyDdc();
		subjectUIDTxtFld = new TextField<String>(Constants.SUBJECT_UID);
		firstNameTxtFld = new TextField<String>(Constants.PERSON_FIRST_NAME);
		middleNameTxtFld = new TextField<String>(Constants.PERSON_MIDDLE_NAME);
		lastNameTxtFld = new TextField<String>(Constants.PERSON_LAST_NAME);
		otherIDTxtFld = new TextField<String>("otherID", new Model(""));
		initVitalStatusDdc();
		initSubjectStatusDdc();
		initGenderTypeDdc();

		dateOfBirthTxtFld = new DateTextField(Constants.PERSON_DOB, new PatternDateConverter(au.org.theark.core.Constants.DD_MM_YYYY,false));
		ArkDatePicker dobDatePicker = new ArkDatePicker();
		dobDatePicker.bind(dateOfBirthTxtFld);
		dateOfBirthTxtFld.add(dobDatePicker);
		
		remove(newButton);
	}
	
	@SuppressWarnings("unchecked")
	private void initStudyDdc() {
		CompoundPropertyModel<SubjectVO> subjectCpm = cpmModel;
		PropertyModel<Study> studyPm = new PropertyModel<Study>(subjectCpm, "linkSubjectStudy.study");
		try {
			Subject currentUser = SecurityUtils.getSubject();
			ArkUser arkUser = iArkCommonService.getArkUser(currentUser.getPrincipal().toString());
			ArkUserVO arkUserVo = new ArkUserVO();
			arkUserVo.setArkUserEntity(arkUser);
			
			//Long sessionArkModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);
			//ArkModule arkModule = null;
			//arkModule = iArkCommonService.getArkModuleById(sessionArkModuleId);
			//studyListForUser = iArkCommonService.getStudyListForUserAndModule(arkUserVo, arkModule);
			
			Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
			studyListForUser = iArkCommonService.getStudyListForUser(arkUserVo); //getParentAndChildStudies(sessionStudyId);
			cpmModel.getObject().setStudyList(studyListForUser);
		}
		catch (EntityNotFoundException e) {
			log.error(e.getMessage());
		}
		ChoiceRenderer<Study> studyRenderer = new ChoiceRenderer<Study>(Constants.NAME, Constants.ID);
		studyDdc = new DropDownChoice<Study>("study", studyPm, (List<Study>) studyListForUser, studyRenderer);
		studyDdc.setNullValid(true);
		studyDdc.setDefaultModelObject(null);
		studyDdc.add(new AjaxFormComponentUpdatingBehavior("onchange"){

			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				Study study = studyDdc.getModelObject();
				if(study == null) {
					ContextHelper contextHelper = new ContextHelper();
					contextHelper.resetContextLabel(target, arkContextMarkup);
					StudyHelper studyHelper = new StudyHelper();
					studyHelper.setStudyLogo(new Study(), target, studyNameMarkup, studyLogoMarkup,iArkCommonService);
				} else {
					SecurityUtils.getSubject().getSession().setAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID, study.getId());
					ContextHelper contextHelper = new ContextHelper();
					contextHelper.resetContextLabel(target, arkContextMarkup);
					contextHelper.setStudyContextLabel(target, study.getName(), arkContextMarkup);
					StudyHelper studyHelper = new StudyHelper();
					studyHelper.setStudyLogo(study, target, studyNameMarkup, studyLogoMarkup,iArkCommonService);
				}
				target.add(SearchForm.this);
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void initVitalStatusDdc() {
		CompoundPropertyModel<SubjectVO> subjectCpm = cpmModel;
		PropertyModel<LinkSubjectStudy> linkSubjectStudyPm = new PropertyModel<LinkSubjectStudy>(subjectCpm, "linkSubjectStudy");
		PropertyModel<Person> personPm = new PropertyModel<Person>(linkSubjectStudyPm, "person");
		PropertyModel<VitalStatus> vitalStatusPm = new PropertyModel<VitalStatus>(personPm, Constants.VITAL_STATUS);
		Collection<VitalStatus> vitalStatusList = iArkCommonService.getVitalStatus();
		ChoiceRenderer vitalStatusRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		vitalStatusDdc = new DropDownChoice<VitalStatus>(Constants.VITAL_STATUS, vitalStatusPm, (List) vitalStatusList, vitalStatusRenderer);
	}

	@SuppressWarnings("unchecked")
	private void initSubjectStatusDdc() {
		CompoundPropertyModel<SubjectVO> subjectCpm = cpmModel;
		PropertyModel<LinkSubjectStudy> linkSubjectStudyPm = new PropertyModel<LinkSubjectStudy>(subjectCpm, "linkSubjectStudy");
		PropertyModel<SubjectStatus> subjectStatusPm = new PropertyModel<SubjectStatus>(linkSubjectStudyPm, "subjectStatus");
		List<SubjectStatus> subjectStatusList = iArkCommonService.getSubjectStatus();
		ChoiceRenderer subjectStatusRenderer = new ChoiceRenderer(Constants.NAME, Constants.SUBJECT_STATUS_ID);
		subjectStatusDdc = new DropDownChoice<SubjectStatus>(Constants.SUBJECT_STATUS, subjectStatusPm, subjectStatusList, subjectStatusRenderer);
	}

	@SuppressWarnings("unchecked")
	private void initGenderTypeDdc() {
		CompoundPropertyModel<SubjectVO> subjectCpm = cpmModel;
		PropertyModel<LinkSubjectStudy> linkSubjectStudyPm = new PropertyModel<LinkSubjectStudy>(subjectCpm, "linkSubjectStudy");
		PropertyModel<Person> personPm = new PropertyModel<Person>(linkSubjectStudyPm, Constants.PERSON);
		PropertyModel<GenderType> genderTypePm = new PropertyModel<GenderType>(personPm, Constants.GENDER_TYPE);
		Collection<GenderType> genderTypeList = iArkCommonService.getGenderTypes();
		ChoiceRenderer genderTypeRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		genderTypeDdc = new DropDownChoice<GenderType>(Constants.GENDER_TYPE, genderTypePm, (List) genderTypeList, genderTypeRenderer);
	}

	@SuppressWarnings("unchecked")
	protected void onNew(AjaxRequestTarget target) {
		// Disable SubjectUID if auto-generation set
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study studyInContext = iArkCommonService.getStudy(sessionStudyId);
		if ((studyInContext != null) && (studyInContext.getAutoGenerateSubjectUid())) {
			TextField<String> subjectUIDTxtFld = (TextField<String>) arkCrudContainerVO.getDetailPanelFormContainer().get(Constants.SUBJECT_UID);
			getModelObject().getLinkSubjectStudy().setSubjectUID(Constants.SUBJECT_AUTO_GENERATED);
			subjectUIDTxtFld.setEnabled(false);
			target.add(subjectUIDTxtFld);
		}
		else {
			TextField<String> subjectUIDTxtFld = (TextField<String>) arkCrudContainerVO.getDetailPanelFormContainer().get(Constants.SUBJECT_UID);
			subjectUIDTxtFld.setEnabled(true);
			target.add(subjectUIDTxtFld);
		}

		// Available child studies
		List<Study> availableChildStudies = new ArrayList<Study>(0);
		if (studyInContext.getParentStudy() != null) {
			availableChildStudies = iStudyService.getChildStudyListOfParent(studyInContext);
		}
		getModelObject().setAvailableChildStudies(availableChildStudies);

		preProcessDetailPanel(target);
	}

	protected void onSearch(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		//Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		//getModelObject().getLinkSubjectStudy().setStudy(iArkCommonService.getStudy(sessionStudyId));
		
		//getModelObject().getLinkSubjectStudy().getStudy();

		String otherIDSearch = otherIDTxtFld.getValue();
		if(otherIDSearch != null) {
			OtherID o = new OtherID();
			o.setOtherID(otherIDSearch);
//			List<OtherID> otherIDs = new ArrayList<OtherID>();
//			otherIDs.add(o);
			cpmModel.getObject().getLinkSubjectStudy().getPerson().getOtherIDs().clear();//setOtherIDs(otherIDs);
			cpmModel.getObject().getLinkSubjectStudy().getPerson().getOtherIDs().add(o);
		}
		if(cpmModel.getObject().getLinkSubjectStudy().getStudy() == null) {
			cpmModel.getObject().setStudyList(studyListForUser);
		}
		long count = iArkCommonService.getStudySubjectCount(cpmModel.getObject());
		if (count == 0L) {
			this.info("There are no subjects with the specified criteria.");
			target.add(feedbackPanel);
		}
		
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
		target.add(arkCrudContainerVO.getSearchResultPanelContainer());// For ajax this is required so
	}

	@Override
	protected void onBeforeRender() {
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
//		Study study = iArkCommonService.getStudy(sessionStudyId);
//		cpmModel.getObject().getLinkSubjectStudy().setStudy(study);
//		boolean parentStudy = (study.getParentStudy() == null || (study.getParentStudy() == study)) && cpmModel.getObject().isEnableNewButton();
//		newButton.setEnabled(parentStudy);
		newButton.setEnabled(false);
		super.onBeforeRender();
	}
}
