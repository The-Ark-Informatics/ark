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
package au.org.theark.study.web.component.contact.form;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.DateValidator;
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PhoneStatus;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.YesNo;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.DateFromToValidator;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.ContactVO;
import au.org.theark.core.vo.PhoneVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.component.audit.button.HistoryButtonPanel;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

/**
 * @author nivedann
 * @author cellis
 * 
 */
public class PhoneDetailForm extends AbstractDetailForm<ContactVO> {


	private static final long				serialVersionUID	= -5784184438113767249L;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>		iArkCommonService;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService					studyService;

	private TextField<String>				phoneIdTxtFld;
	private TextField<String>				areaCodeTxtFld;
	private TextField<String>				phoneNumberTxtFld;
	private TextField<String>				source;
	private TextArea<String>				commentsTxtArea;
	private CheckBox						preferredPhoneNumberChkBox;
	private DateTextField					dateReceivedDp;
	private DropDownChoice<PhoneType>		phoneTypeChoice;
	private DropDownChoice<PhoneStatus>		phoneStatusChoice;
	private DropDownChoice<YesNo>			silentModeChoice;
	
	private FeedbackPanel 					feedBackPanel;
	private ArkCrudContainerVO 				arkCrudContainerVO;
	
	private DateTextField 					dateValidFrom;
	private DateTextField 					dateValidTo;
	private HistoryButtonPanel historyButtonPanel;

	/**
	 * /**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	
	public PhoneDetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.arkCrudContainerVO=arkCrudContainerVO;
		this.feedBackPanel = feedBackPanel;
	}
	@Override
	public void onBeforeRender() {
		// Disable preferred phone for new phone and if no others exist
		boolean enabled = !(isNew() && containerForm.getModelObject().getPhoneVo().getPhoneList().size() == 0);
		preferredPhoneNumberChkBox.setEnabled(enabled);
		historyButtonPanel.setVisible(!isNew());
		super.onBeforeRender();
	}

	public void initialiseDetailForm() {
		phoneIdTxtFld = new TextField<String>("phoneVo.phone.id");
		areaCodeTxtFld = new TextField<String>("phoneVo.phone.areaCode");
		preferredPhoneNumberChkBox = new CheckBox("phoneVo.phone.preferredPhoneNumber");
		phoneNumberTxtFld = new TextField<String>("phoneVo.phone.phoneNumber");
		source = new TextField<String>("phoneVo.phone.source");
		commentsTxtArea = new TextArea<String>("phoneVo.phone.comment");
		dateReceivedDp = new DateTextField("phoneVo.phone.dateReceived", new PatternDateConverter(au.org.theark.core.Constants.DD_MM_YYYY,false));
		ArkDatePicker dPDateReceived = new ArkDatePicker();
		dPDateReceived.bind(dateReceivedDp);
		dateReceivedDp.add(dPDateReceived);
		
		//Valid From
		dateValidFrom=new DateTextField("phoneVo.phone.validFrom", new PatternDateConverter(au.org.theark.core.Constants.DD_MM_YYYY,false));
		ArkDatePicker dPDateValidFrom = new ArkDatePicker();
		dPDateValidFrom.bind(dateValidFrom);
		dateValidFrom.add(dPDateValidFrom);

		//Valid To
		dateValidTo=new DateTextField("phoneVo.phone.validTo", new PatternDateConverter(au.org.theark.core.Constants.DD_MM_YYYY,false));
		ArkDatePicker dPDateValidTo = new ArkDatePicker();
		dPDateValidTo.bind(dateValidTo);
		dateValidTo.add(dPDateValidTo);
	
		List<PhoneStatus> phoneStatusSourceList = iArkCommonService.getPhoneStatus();
		ChoiceRenderer<PhoneStatus> phoneStatusRenderer = new ChoiceRenderer<PhoneStatus>(Constants.NAME, Constants.ID);
		phoneStatusChoice = new DropDownChoice<PhoneStatus>("phoneVo.phone.phoneStatus", phoneStatusSourceList, phoneStatusRenderer);

		List<YesNo> yesNoListSource = iArkCommonService.getYesNoList();
		ChoiceRenderer<YesNo> yesNoRenderer = new ChoiceRenderer<YesNo>(Constants.NAME, Constants.ID);
		silentModeChoice = new DropDownChoice<YesNo>("phoneVo.phone.silentMode", yesNoListSource, yesNoRenderer);

		List<PhoneType> phoneTypeList = iArkCommonService.getListOfPhoneType();
		ChoiceRenderer<PhoneType> defaultChoiceRenderer = new ChoiceRenderer<PhoneType>(Constants.NAME, Constants.ID);
		phoneTypeChoice = new DropDownChoice<PhoneType>("phoneVo.phone.phoneType", phoneTypeList, defaultChoiceRenderer);
		phoneTypeChoice.add(new ArkDefaultFormFocusBehavior());
		historyButtonPanel = new HistoryButtonPanel(containerForm, arkCrudContainerVO.getEditButtonContainer(), arkCrudContainerVO.getDetailPanelFormContainer(),feedBackPanel);
		addDetailFormComponents();
		attachValidators();
	}

	public void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(phoneIdTxtFld.setEnabled(false));
		arkCrudContainerVO.getDetailPanelFormContainer().add(areaCodeTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(preferredPhoneNumberChkBox);
		arkCrudContainerVO.getDetailPanelFormContainer().add(phoneNumberTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(phoneTypeChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(phoneStatusChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(commentsTxtArea);
		arkCrudContainerVO.getDetailPanelFormContainer().add(dateReceivedDp);
		arkCrudContainerVO.getDetailPanelFormContainer().add(silentModeChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(source);
		arkCrudContainerVO.getDetailPanelFormContainer().add(dateValidFrom);
		arkCrudContainerVO.getDetailPanelFormContainer().add(dateValidTo);
		arkCrudContainerVO.getEditButtonContainer().add(historyButtonPanel);
		this.add(new DateFromToValidator(dateValidFrom, dateValidTo,"Valid from date","Valid to date"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		areaCodeTxtFld.add(StringValidator.maximumLength(10));
		phoneTypeChoice.setRequired(true).setLabel((new StringResourceModel("phone.phoneType.required", this, new Model<String>("Phone Type"))));
		phoneNumberTxtFld.setRequired(true).setLabel((new StringResourceModel("phone.phoneNumber.required", this, new Model<String>("Phone Number"))));
		phoneStatusChoice.setRequired(true).setLabel((new StringResourceModel("phone.phoneStatus.required", this, new Model<String>("Phone Status"))));
		phoneNumberTxtFld.add(StringValidator.maximumLength(20));
		dateReceivedDp.add(DateValidator.maximum(new Date())).setLabel(new StringResourceModel("phone.dateReceived.DateValidator.maximum", this, null));
		source.add(StringValidator.maximumLength(au.org.theark.core.Constants.GENERAL_FIELD_COMMENTS_MAX_LENGTH_500));
		commentsTxtArea.add(StringValidator.maximumLength(au.org.theark.core.Constants.GENERAL_FIELD_COMMENTS_MAX_LENGTH_500));
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {
		ContactVO contactVO=new ContactVO();
		contactVO.setPhoneVo(new PhoneVO());
		containerForm.setModelObject(contactVO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onDeleteConfirmed(org.apache.wicket.ajax.AjaxRequestTarget, java.lang.String,
	 * org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow)
	 */
	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		try {
			studyService.delete(containerForm.getModelObject().getPhoneVo().getPhone());
			this.deleteInformation();
			//containerForm.info("The Phone record was deleted successfully.");
			editCancelProcess(target);
		}
		catch (ArkSystemException e) {
			this.error("An error occured while processing your delete request. Please contact the system administrator.");
			// TODO Need to work out more on how user will contact support (Level 1..etc) a generic message with contact info plus logs to be emailed to
			// admin
		}
		onCancel(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<ContactVO> containerForm, AjaxRequestTarget target) {
		// Persist the phone number to the backend, associate the person in context with the phone object
		Long personSessionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		// Get the person and set it on the Phone object.
		try {
			//boolean saveOk = true;
			Person person = studyService.getPerson(personSessionId);
			containerForm.getModelObject().getPhoneVo().getPhone().setPerson(person);
			//if (saveOk) {
				// Ok to save...
				String personType = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_TYPE);
				if (personType != null && personType.equalsIgnoreCase(au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_SUBJECT)) {
					Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
					Study study = iArkCommonService.getStudy(studyId);
					LinkSubjectStudy subjectInContext = iArkCommonService.getSubject(personSessionId, study);// This is fetched basically to display the
					// message along with the Subject UID or Contact ID
					if (containerForm.getModelObject().getPhoneVo().getPhone().getId() == null) {
						if(containerForm.getModelObject().getPhoneVo().getPhone().getPreferredPhoneNumber()){
							// Update any other preferredPhoneNumber to false
							studyService.setPreferredPhoneNumberToFalse(person);
						}
						studyService.create(containerForm.getModelObject().getPhoneVo().getPhone());
						this.saveInformation();
						//this.info("Phone number was added and linked to Subject UID: " + subjectInContext.getSubjectUID());
					}else {
						if(containerForm.getModelObject().getPhoneVo().getPhone().getPreferredPhoneNumber()){
							// Update any other preferredMailingAddresses to false
							studyService.setPreferredPhoneNumberToFalse(person);
						}
						studyService.update(containerForm.getModelObject().getPhoneVo().getPhone());
						this.updateInformation();
						//this.info("Phone number was updated and linked to Subject UID: " + subjectInContext.getSubjectUID());
						}
				}else if (personType != null && personType.equalsIgnoreCase(au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_CONTACT)) {
					// TODO: Contact Interface implementation
				}
				processErrors(target);
				onSavePostProcess(target);
			//}
			/*else {
				processErrors(target);
			}*/
		}
		catch (ArkUniqueException aue) {
			this.error(aue.getMessage());
		}
		catch (EntityNotFoundException e) {
			this.error("The Subject/Participant is no longer available in the system.");
		}
		catch (ArkSystemException e) {
			this.error("A system exception has occurred. Please contact the system administrator.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#processErrors(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#getMode()
	 */
	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getPhoneVo().getPhone().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}

	
}
