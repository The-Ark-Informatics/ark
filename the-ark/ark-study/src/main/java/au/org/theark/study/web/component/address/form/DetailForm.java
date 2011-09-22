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
package au.org.theark.study.web.component.address.form;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
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
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.AddressStatus;
import au.org.theark.core.model.study.entity.AddressType;
import au.org.theark.core.model.study.entity.Country;
import au.org.theark.core.model.study.entity.CountryState;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.AddressVO;
import au.org.theark.core.web.behavior.ArkDefaultFormFocusBehavior;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.core.web.form.AbstractContainerForm;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

/**
 * @author nivedann
 * 
 */
public class DetailForm extends AbstractDetailForm<AddressVO> {

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService					iArkCommonService;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService						studyService;

	private TextField<String>					streetAddressTxtFld;
	private TextField<String>					cityTxtFld;
	private TextField<String>					postCodeTxtFld;
	private DropDownChoice<Country>			countryChoice;
	private DropDownChoice<CountryState>	stateChoice;
	private DropDownChoice<AddressType>		addressTypeChoice;
	private WebMarkupContainer					countryStateSelector;
	private DropDownChoice<AddressStatus>	addressStatusChoice;
	private CheckBox								preferredMailingAddressChkBox;
	private DateTextField						dateReceivedDp;
	private TextArea<String>					commentsTxtArea;
	private TextField<String>					sourceTxtFld;
	private TextField<String>					addressLineOneTxtFld;

	protected TextField<String>				otherState;

	/**
	 * @param id
	 * @param feedBackPanel
	 * @param resultListContainer
	 * @param detailPanelContainer
	 * @param detailPanelFormContainer
	 * @param searchPanelContainer
	 * @param viewButtonContainer
	 * @param editButtonContainer
	 * @param containerForm
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel, WebMarkupContainer resultListContainer, WebMarkupContainer detailPanelContainer, WebMarkupContainer detailPanelFormContainer,
			WebMarkupContainer searchPanelContainer, WebMarkupContainer viewButtonContainer, WebMarkupContainer editButtonContainer, AbstractContainerForm<AddressVO> containerForm) {

		super(id, feedBackPanel, resultListContainer, detailPanelContainer, detailPanelFormContainer, searchPanelContainer, viewButtonContainer, editButtonContainer, containerForm);

	}

	public void initialiseDetailForm() {
		streetAddressTxtFld = new TextField<String>("address.streetAddress");
		streetAddressTxtFld.add(new ArkDefaultFormFocusBehavior());
		cityTxtFld = new TextField<String>("address.city");
		postCodeTxtFld = new TextField<String>("address.postCode");
		commentsTxtArea = new TextArea<String>("address.comments");
		otherState = new TextField<String>("address.otherState");
		sourceTxtFld = new TextField<String>("address.source");
		addressLineOneTxtFld = new TextField<String>("address.addressLineOne");
		initialiaseCountryDropDown();
		initialiseStateSelector();
		initialiseAddressTypeDropDown();
		initialiseAddressStatusDropDown();
		initialisePreferredMailingAddressDropDown();
		initialiseDatePicker();
		attachValidators();
		addDetailFormComponents();
	}

	public void addDetailFormComponents() {
		detailPanelFormContainer.add(streetAddressTxtFld);
		detailPanelFormContainer.add(cityTxtFld);
		detailPanelFormContainer.add(postCodeTxtFld);
		detailPanelFormContainer.add(countryChoice);
		detailPanelFormContainer.add(countryStateSelector);// This contains the drop-down for State
		detailPanelFormContainer.add(addressTypeChoice);
		detailPanelFormContainer.add(addressStatusChoice);
		detailPanelFormContainer.add(dateReceivedDp);
		detailPanelFormContainer.add(commentsTxtArea);
		detailPanelFormContainer.add(preferredMailingAddressChkBox);
		detailPanelFormContainer.add(sourceTxtFld);
		detailPanelFormContainer.add(addressLineOneTxtFld);

	}

	private void initialiseAddressTypeDropDown() {
		List<AddressType> addressTypeList = iArkCommonService.getAddressTypes();
		ChoiceRenderer<AddressType> defaultChoiceRenderer = new ChoiceRenderer<AddressType>(Constants.NAME, Constants.ID);
		addressTypeChoice = new DropDownChoice<AddressType>(Constants.ADDRESS_ADDRESSTYPE, addressTypeList, defaultChoiceRenderer);
	}

	private void initialisePreferredMailingAddressDropDown() {
		preferredMailingAddressChkBox = new CheckBox(Constants.ADDRESS_PREFERRED_MAILING);
	}

	/**
	 * The MarkupContainer for The State DropDown control
	 */
	private void initialiseStateSelector() {

		countryStateSelector = new WebMarkupContainer("countryStateSelector");
		countryStateSelector.setOutputMarkupPlaceholderTag(true);
		// Get the value selected in Country
		Country selectedCountry = countryChoice.getModelObject();

		// If there is no country selected, back should default to current country and pull the states
		List<CountryState> countryStateList = iArkCommonService.getStates(selectedCountry);
		ChoiceRenderer<CountryState> defaultStateChoiceRenderer = new ChoiceRenderer<CountryState>("state", Constants.ID);
		stateChoice = new DropDownChoice<CountryState>(Constants.ADDRESS_COUNTRYSTATE_STATE, countryStateList, defaultStateChoiceRenderer);
		// Add the Country State Dropdown into the WebMarkupContainer - countrySelector
		countryStateSelector.add(stateChoice);
		countryStateSelector.add(otherState);
		if (countryStateList.size() > 0) {
			otherState.setVisible(false);
			stateChoice.setVisible(true);
		}
		else {
			otherState.setVisible(true);
			stateChoice.setVisible(false);
		}
	}

	private void initialiaseCountryDropDown() {

		List<Country> countryList = iArkCommonService.getCountries();
		ChoiceRenderer<Country> defaultChoiceRenderer = new ChoiceRenderer<Country>(Constants.NAME, Constants.ID);
		countryChoice = new DropDownChoice<Country>(Constants.ADDRESS_COUNTRY, countryList, defaultChoiceRenderer);

		// Attach a behavior, so when it changes it does something
		countryChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				updateCountryStateChoices(countryChoice.getModelObject());
				target.addComponent(countryStateSelector);
			}
		});
	}

	/**
	 * A method that will refresh the choices in the State drop down choice based on what was selected in the Country Dropdown. It uses the country as
	 * the argument and invokes the back-end to fetch relative states.
	 */
	private void updateCountryStateChoices(Country country) {

		List<CountryState> countryStateList = iArkCommonService.getStates(country);
		if (countryStateList != null && countryStateList.size() > 0) {
			stateChoice.setVisible(true);
			stateChoice.getChoices().clear();
			stateChoice.setChoices(countryStateList);
			otherState.setVisible(false);
		}
		else {
			// hide it
			stateChoice.setVisible(false);
			otherState.setVisible(true);
		}

	}

	private void initialiseAddressStatusDropDown() {
		List<AddressStatus> statusList = iArkCommonService.getAddressStatuses();
		ChoiceRenderer<AddressStatus> defaultChoiceRenderer = new ChoiceRenderer<AddressStatus>(Constants.NAME, Constants.ID);
		addressStatusChoice = new DropDownChoice<AddressStatus>(Constants.ADDRESS_ADDRESSSTATUS, statusList, defaultChoiceRenderer);

	}

	private void initialiseDatePicker() {
		// Create new DateTextField and assign date format
		dateReceivedDp = new DateTextField(Constants.ADDRESS_DATE_RECEIVED, au.org.theark.core.Constants.DD_MM_YYYY);
		ArkDatePicker datePicker = new ArkDatePicker();
		datePicker.bind(dateReceivedDp);
		dateReceivedDp.add(datePicker);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {

		addressLineOneTxtFld.add(StringValidator.maximumLength(255));
		sourceTxtFld.add(StringValidator.maximumLength(255));
		streetAddressTxtFld.setRequired(true).setLabel(new StringResourceModel("street.address", this, new Model<String>("Street Address")));
		streetAddressTxtFld.add(StringValidator.maximumLength(255));
		cityTxtFld.setRequired(true).setLabel(new StringResourceModel("city", this, new Model<String>("City")));
		postCodeTxtFld.setRequired(true).setLabel(new StringResourceModel("postcode", this, new Model<String>("Post Code")));
		postCodeTxtFld.add(StringValidator.maximumLength(10)).setLabel(new StringResourceModel("postcode", this, new Model<String>("Post Code Max Length")));
		postCodeTxtFld.add(StringValidator.minimumLength(4));
		dateReceivedDp.add(DateValidator.maximum(new Date())).setLabel(new StringResourceModel("error.study.date.max.range", this, null));
		addressTypeChoice.setRequired(true).setLabel(new StringResourceModel("addressType", this, new Model<String>("Address Type")));
		addressStatusChoice.setRequired(true).setLabel(new StringResourceModel("addressStatus", this, new Model<String>("Address Status")));
		stateChoice.setRequired(true).setLabel(new StringResourceModel("state", this, new Model<String>("State")));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {
		AddressVO addressVO = new AddressVO();
		containerForm.setModelObject(addressVO);
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

			studyService.delete(containerForm.getModelObject().getAddress());
			containerForm.info("The Address has been deleted successfully.");
			editCancelProcess(target);
		}
		catch (ArkSystemException e) {
			this.error("An error occured while processing your delete. Please contact Support");
			// TODO Need to work out more on how user will contact support (Level 1..etc) a generic message with contact info plus logs to be emailed to
			// admin
			e.printStackTrace();
		}
		onCancel(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<AddressVO> containerForm, AjaxRequestTarget target) {

		Long personSessionId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);

		// Get the person and set it on the AddressVO.
		try {
			Person person = studyService.getPerson(personSessionId);

			boolean hasPreferredMailing = studyService.personHasPreferredMailingAddress(person, containerForm.getModelObject().getAddress().getId());
			boolean preferredMailingAdressIsYes = false;

			if (containerForm.getModelObject().getAddress().getPreferredMailingAddress() != null) {
				preferredMailingAdressIsYes = (containerForm.getModelObject().getAddress().getPreferredMailingAddress() == true);
			}

			// Check if other address already set to preferredMailingAddress
			if (hasPreferredMailing && preferredMailingAdressIsYes) {
				containerForm.error("The person has already specified a Preferred Mailing address. This address cannot be set as Preferred Mailing address.");
				processErrors(target);
			}
			else {
				containerForm.getModelObject().getAddress().setPerson(person);
				if (containerForm.getModelObject().getAddress().getId() == null) {
					studyService.create(containerForm.getModelObject().getAddress());
					this.info("Address was successfully added and linked to Subject:" + person.getFirstName() + " " + person.getLastName());
					processErrors(target);
				}
				else {

					studyService.update(containerForm.getModelObject().getAddress());
					this.info("Address was successfully updated and linked to Subject:" + person.getFirstName() + " " + person.getLastName());
					processErrors(target);
				}

				onSavePostProcess(target);
			}
			// Invoke backend to persist the AddressVO
		}
		catch (EntityNotFoundException e) {
			containerForm.error("The Specified subject is not available any more in the system. Please re-do the operation");

		}
		catch (ArkSystemException e) {
			containerForm.error("A system error has occured, Pleas contact support.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#processErrors(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.addComponent(feedBackPanel);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#isNew()
	 */
	@Override
	protected boolean isNew() {
		if (containerForm.getModelObject().getAddress().getId() == null) {
			return true;
		}
		else {
			return false;
		}

	}
}
