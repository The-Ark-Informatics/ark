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
package au.org.theark.study.web.component.phone.form;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
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
import au.org.theark.core.model.study.entity.YesNo;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.PhoneVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

/**
 * @author nivedann
 * @author cellis
 * 
 */
public class DetailForm extends AbstractDetailForm<PhoneVO> {

	/**
	 * 
	 */
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

	private DateTextField					dateReceivedDp;
	private DropDownChoice<PhoneType>	phoneTypeChoice;
	private DropDownChoice<PhoneStatus>	phoneStatusChoice;
	private DropDownChoice<YesNo>			silentModeChoice;

	/**
	 * /**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, au.org.theark.study.web.component.phone.form.ContainerForm containerForm) {

		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.feedBackPanel = feedBackPanel;
	}

	public void initialiseDetailForm() {
		phoneIdTxtFld = new TextField<String>("phone.id");
		areaCodeTxtFld = new TextField<String>("phone.areaCode");
		phoneNumberTxtFld = new TextField<String>("phone.phoneNumber");

		source = new TextField<String>("phone.source");
		commentsTxtArea = new TextArea<String>("phone.comment");

		dateReceivedDp = new DateTextField("phone.dateReceived", au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker datePicker = new ArkDatePicker();
		datePicker.bind(dateReceivedDp);
		dateReceivedDp.add(datePicker);

		List<PhoneStatus> phoneStatusSourceList = iArkCommonService.getPhoneStatus();
		ChoiceRenderer<PhoneStatus> phoneStatusRenderer = new ChoiceRenderer<PhoneStatus>(Constants.NAME, Constants.ID);
		phoneStatusChoice = new DropDownChoice<PhoneStatus>("phone.phoneStatus", phoneStatusSourceList, phoneStatusRenderer);

		List<YesNo> yesNoListSource = iArkCommonService.getYesNoList();
		ChoiceRenderer<YesNo> yesNoRenderer = new ChoiceRenderer<YesNo>(Constants.NAME, Constants.ID);
		silentModeChoice = new DropDownChoice<YesNo>("phone.silentMode", yesNoListSource, yesNoRenderer);

		List<PhoneType> phoneTypeList = iArkCommonService.getListOfPhoneType();
		ChoiceRenderer<PhoneType> defaultChoiceRenderer = new ChoiceRenderer<PhoneType>(Constants.NAME, Constants.ID);
		phoneTypeChoice = new DropDownChoice<PhoneType>("phone.phoneType", phoneTypeList, defaultChoiceRenderer);
		phoneTypeChoice.add(new ArkDefaultFormFocusBehavior());
		addDetailFormComponents();
		attachValidators();
	}

	public void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(phoneIdTxtFld.setEnabled(false));
		arkCrudContainerVO.getDetailPanelFormContainer().add(areaCodeTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(phoneNumberTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(phoneTypeChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(phoneStatusChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(commentsTxtArea);
		arkCrudContainerVO.getDetailPanelFormContainer().add(dateReceivedDp);
		arkCrudContainerVO.getDetailPanelFormContainer().add(silentModeChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(source);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		phoneNumberTxtFld.setRequired(true).setLabel((new StringResourceModel("phone.phoneNumber.RequiredValidator", this, new Model<String>("Phone Number"))));
		phoneNumberTxtFld.add(StringValidator.maximumLength(10)).setLabel(new StringResourceModel("phone.phoneNumber.StringValidator.maximum", this, null));
		areaCodeTxtFld.add(StringValidator.maximumLength(10));
		phoneTypeChoice.setRequired(true).setLabel((new StringResourceModel("phone.phoneType.RequiredValidator", this, new Model<String>("Phone Type"))));
		dateReceivedDp.add(DateValidator.maximum(new Date())).setLabel(new StringResourceModel("phone.dateReceived.DateValidator.maximum", this, null));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {
		PhoneVO phoneVO = new PhoneVO();
		containerForm.setModelObject(phoneVO);
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
			studyService.delete(containerForm.getModelObject().getPhone());
			containerForm.info("The Phone record was deleted successfully.");
			editCancelProcess(target);
		}
		catch (ArkSystemException e) {
			this.error("An error occured while processing your delete. Please contact Support");
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
	protected void onSave(Form<PhoneVO> containerForm, AjaxRequestTarget target) {
		// Persist the phone number to the backend, associate the person in context with the phone object
		Long personSessionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		// Get the person and set it on the Phone object.
		try {
			Person person = studyService.getPerson(personSessionId);
			containerForm.getModelObject().getPhone().setPerson(person);
			// Make the area code mandatory only for landline phone (home/work) entries
			String phType = containerForm.getModelObject().getPhone().getPhoneType().getName().toLowerCase();
			boolean saveOk = true;

			if (!phType.equals("mobile")) {
				// must be landline
				if (containerForm.getModelObject().getPhone().getAreaCode() == null || containerForm.getModelObject().getPhone().getAreaCode().length() < 1) {
					this.error("An area code must be entered for landline numbers");
					saveOk = false;
				}
			}
			if (saveOk) {
				// Ok to save...
				String personType = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_TYPE);

				if (personType != null && personType.equalsIgnoreCase(au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_SUBJECT)) {

					LinkSubjectStudy subjectInContext = iArkCommonService.getSubject(personSessionId);// This is fetched basically to display the info
																																	// message along with the Subject UID or Contact ID
					if (containerForm.getModelObject().getPhone().getId() == null) {
						studyService.create(containerForm.getModelObject().getPhone());
						this.info("Phone number was added and linked to Subject UID: " + subjectInContext.getSubjectUID());
					}
					else {
						studyService.update(containerForm.getModelObject().getPhone());
						this.info("Phone number was updated and linked to Subject UID: " + subjectInContext.getSubjectUID());
					}
				}
				else if (personType != null && personType.equalsIgnoreCase(au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_CONTACT)) {
					// TODO: Contact Interface implementation
				}
				
				processErrors(target);
				onSavePostProcess(target);
			}else{
				
				processErrors(target);
				
			}
		}
		catch (ArkUniqueException aue) {
			this.error(aue.getMessage());
		}
		catch (EntityNotFoundException e) {
			this.error("The Subject/Participant is not available in the system anymore");
		}
		catch (ArkSystemException e) {
			this.error("A System Exception has occured please contact Support.");
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
		if (containerForm.getModelObject().getPhone().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}
}
