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
package au.org.theark.study.web.component.consent.form;

import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.ConsentType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyCompStatus;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.ConsentVO;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

/**
 * @author Nivedan
 * 
 */
public class SearchForm extends AbstractSearchForm<ConsentVO> {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	protected IArkCommonService						iArkCommonService;

	@SpringBean(name = Constants.STUDY_SERVICE)
	protected IStudyService								studyService;

	protected PageableListView<Consent>				pageableListView;
	protected CompoundPropertyModel<ConsentVO>	cpmModel;

	/**
	 * Form Components
	 */
	protected TextField<String>						consentedBy;
	protected DateTextField								consentedDatePicker;
	protected DateTextField								endConsentedDatePicker;
	protected DropDownChoice<StudyComp>				studyComponentChoice;
	protected DropDownChoice<StudyCompStatus>		studyComponentStatusChoice;
	protected DropDownChoice<ConsentStatus>		consentStatusChoice;
	protected DropDownChoice<ConsentType>			consentTypeChoice;

	public SearchForm(String id, PageableListView<Consent> listView, FeedbackPanel feedBackPanel, 
							CompoundPropertyModel<ConsentVO> model, ArkCrudContainerVO arkCrudContainerVO) {
		super(id, model, feedBackPanel, arkCrudContainerVO);

		this.cpmModel = model;
		this.pageableListView = listView;
		initialiseSearchForm();
		addSearchComponentsToForm();
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		disableSearchForm(sessionPersonId, "There is no subject or contact in context. Please select a Subject or Contact.");
	}

	protected void initialiseSearchForm() {
		consentedBy = new TextField<String>(Constants.CONSENT_CONSENTED_BY);
		consentedDatePicker = new DateTextField(Constants.CONSENT_CONSENT_DATE, au.org.theark.core.Constants.DD_MM_YYYY);

		ArkDatePicker datePicker = new ArkDatePicker();
		datePicker.bind(consentedDatePicker);
		consentedDatePicker.add(datePicker);

		endConsentedDatePicker = new DateTextField("consentDateEnd", au.org.theark.core.Constants.DD_MM_YYYY);

		ArkDatePicker datePicker2 = new ArkDatePicker();
		datePicker2.bind(endConsentedDatePicker);
		endConsentedDatePicker.add(datePicker2);

		initialiseConsentTypeChoice();
		initialiseConsentStatusChoice();
		initialiseComponentChoice();
		initialiseComponentStatusChoice();
	}

	/**
	 * Initialise the Consent Type Drop Down Choice Control
	 */
	protected void initialiseConsentTypeChoice() {
		List<ConsentType> consentTypeList = iArkCommonService.getConsentType();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		consentTypeChoice = new DropDownChoice(Constants.CONSENT_CONSENT_TYPE, consentTypeList, defaultChoiceRenderer);
	}

	/**
	 * Initialise the Consent Status Drop Down Choice Control
	 */
	protected void initialiseConsentStatusChoice() {
		List<ConsentStatus> consentStatusList = iArkCommonService.getRecordableConsentStatus();
		ChoiceRenderer<ConsentType> defaultChoiceRenderer = new ChoiceRenderer<ConsentType>(Constants.NAME, Constants.ID);
		consentStatusChoice = new DropDownChoice(Constants.CONSENT_CONSENT_STATUS, consentStatusList, defaultChoiceRenderer);
	}

	/**
	 * Initialise the Consent StudyComp Drop Down Choice Control
	 */
	protected void initialiseComponentChoice() {
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Study study = iArkCommonService.getStudy(sessionStudyId);
		List<StudyComp> studyCompList = iArkCommonService.getStudyComponentByStudy(study);
		ChoiceRenderer<StudyComp> defaultChoiceRenderer = new ChoiceRenderer<StudyComp>(Constants.NAME, Constants.ID);
		studyComponentChoice = new DropDownChoice(Constants.CONSENT_STUDY_COMP, studyCompList, defaultChoiceRenderer);
	}

	protected void initialiseComponentStatusChoice() {

		List<StudyCompStatus> studyCompList = iArkCommonService.getStudyComponentStatus();
		ChoiceRenderer<StudyCompStatus> defaultChoiceRenderer = new ChoiceRenderer<StudyCompStatus>(Constants.NAME, Constants.ID);
		studyComponentStatusChoice = new DropDownChoice(Constants.CONSENT_STUDY_COMP_STATUS, studyCompList, defaultChoiceRenderer);

	}

	protected void addSearchComponentsToForm() {
		add(consentTypeChoice);
		add(consentStatusChoice);
		add(studyComponentChoice);
		add(studyComponentStatusChoice);
		add(consentedDatePicker);
		add(endConsentedDatePicker);
		add(consentedBy);
	}

	@Override
	protected void onSearch(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		String sessionPersonType = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_TYPE);// Subject or
																																														// Contact:
																																														// Denotes
																																														// if it was
																																														// a subject
																																														// or
																																														// contact
																																														// placed in
																																														// session
		Consent consent = getModelObject().getConsent();

		try {

			Study study = iArkCommonService.getStudy(sessionStudyId);
			// Person subject = studyService.getPerson(sessionPersonId);

			// consent.setSubject(subject);
			// TODO Replace the above line with a call to LinkSubjectStudy
			consent.setStudy(study);

			// Look up based on criteria via back end.
			// Collection<Consent> consentList = studyService.searchConsent(getModelObject().getConsent());
			LinkSubjectStudy subjectInContext = studyService.getSubjectLinkedToStudy(sessionPersonId, study);
			consent.setLinkSubjectStudy(subjectInContext);
			Collection<Consent> consentList = studyService.searchConsent(getModelObject());

			if (consentList != null && consentList.size() == 0) {
				this.info("There are no consents for the specified criteria.");
				target.add(feedbackPanel);
			}

			getModelObject().setConsentList(consentList);
			pageableListView.removeAll();
			
			arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
			target.add(arkCrudContainerVO.getSearchResultPanelContainer());
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
		preProcessDetailPanel(target);
	}

}
