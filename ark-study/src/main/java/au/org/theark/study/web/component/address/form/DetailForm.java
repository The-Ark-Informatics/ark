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
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
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
import org.apache.wicket.validation.validator.StringValidator.LengthBetweenValidator;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.AddressStatus;
import au.org.theark.core.model.study.entity.AddressType;
import au.org.theark.core.model.study.entity.Country;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.State;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.AddressVO;
import au.org.theark.core.vo.ArkCrudContainerVO;
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
public class DetailForm extends AbstractDetailForm<AddressVO> {

	private static final long					serialVersionUID	= 1423759632793367263L;

	@SuppressWarnings("unchecked")
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService					iArkCommonService;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService						iStudyService;

	private TextField<String>					streetAddressTxtFld;
	private TextField<String>					cityTxtFld;
	private TextField<String>					postCodeTxtFld;
	private DropDownChoice<Country>			countryChoice;
	private DropDownChoice<State>	stateChoice;
	private DropDownChoice<AddressType>		addressTypeChoice;
	private WebMarkupContainer					stateSelector;
	private DropDownChoice<AddressStatus>	addressStatusChoice;
	private CheckBox								preferredMailingAddressChkBox;
	private DateTextField						dateReceivedDp;
	private TextArea<String>					commentsTxtArea;
	private TextField<String>					sourceTxtFld;
	private TextField<String>					addressLineOneTxtFld;

	protected TextField<String>				otherState;
	protected Label								otherStateInvalidError;

	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public DetailForm(String id, FeedbackPanel feedBackPanel, ArkCrudContainerVO arkCrudContainerVO, ContainerForm containerForm) {
		super(id, feedBackPanel, containerForm, arkCrudContainerVO);
		this.feedBackPanel = feedBackPanel;
	}

	@Override
	public void onBeforeRender() {
		// Disable preferred mailing for new address and no others exist
		boolean enabled = !(isNew() && containerForm.getModelObject().getAddresses().size() == 0);
		preferredMailingAddressChkBox.setEnabled(enabled);
		super.onBeforeRender();
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
		arkCrudContainerVO.getDetailPanelFormContainer().add(streetAddressTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(cityTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(postCodeTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(countryChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(stateSelector);// This contains the drop-down for State
		arkCrudContainerVO.getDetailPanelFormContainer().add(addressTypeChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(addressStatusChoice);
		arkCrudContainerVO.getDetailPanelFormContainer().add(dateReceivedDp);
		arkCrudContainerVO.getDetailPanelFormContainer().add(commentsTxtArea);
		arkCrudContainerVO.getDetailPanelFormContainer().add(preferredMailingAddressChkBox);
		arkCrudContainerVO.getDetailPanelFormContainer().add(sourceTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(addressLineOneTxtFld);
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
		stateSelector = new WebMarkupContainer("stateSelector");
		stateSelector.setOutputMarkupPlaceholderTag(true);
		// Get the value selected in Country
		Country selectedCountry = countryChoice.getModelObject();

		// If there is no country selected, back should default to current country and pull the states
		List<State> stateList = iArkCommonService.getStates(selectedCountry);
		ChoiceRenderer<State> defaultStateChoiceRenderer = new ChoiceRenderer<State>("name", Constants.ID);
		stateChoice = new DropDownChoice<State>(Constants.ADDRESS_STATE, stateList, defaultStateChoiceRenderer);
		// Add the Country State Dropdown into the WebMarkupContainer - countrySelector
		otherStateInvalidError = new Label("address.otherStateInvalidError", "");
		otherStateInvalidError.setOutputMarkupPlaceholderTag(true);
		otherStateInvalidError.add(new Behavior(){
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -6756543741833275627L;

			@Override
			public void onComponentTag(Component component, ComponentTag tag) {
				super.onComponentTag(component, tag);
				tag.put("class", "fieldErrorMessage");//or something like ("style"; "color-red")
			}
		});
		
		stateSelector.add(stateChoice);
		stateSelector.add(otherState);
		stateSelector.add(otherStateInvalidError);
		
		if (stateList.size() > 0) {
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

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				updateStateChoices(countryChoice.getModelObject());
				target.add(stateSelector);
			}
		});
	}

	/**
	 * A method that will refresh the choices in the State drop down choice based on what was selected in the Country Dropdown. It uses the country as
	 * the argument and invokes the back-end to fetch relative states.
	 */
	private void updateStateChoices(Country country) {
		List<State> stateList = iArkCommonService.getStates(country);
		if (stateList != null && stateList.size() > 0) {
			stateChoice.setVisible(true);
			stateChoice.getChoices().clear();
			stateChoice.setChoices(stateList);
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

		streetAddressTxtFld.setRequired(true).setLabel(new StringResourceModel("address.streetAddress.RequiredValidator", this, new Model<String>("Street Address")));

		streetAddressTxtFld.add(LengthBetweenValidator.maximumLength(255));

		cityTxtFld.setRequired(true).setLabel(new StringResourceModel("address.city.RequiredValidator", this, new Model<String>("City")));

		postCodeTxtFld.setRequired(true).setLabel(new StringResourceModel("address.postCode.RequiredValidator", this, new Model<String>("Post Code")));
		// TODO User Centric ones for Max and Min
		postCodeTxtFld.add(LengthBetweenValidator.maximumLength(10));
		postCodeTxtFld.add(LengthBetweenValidator.minimumLength(4));

		dateReceivedDp.add(DateValidator.maximum(new Date())).setLabel(new StringResourceModel("address.dateReceived.DateValidator.maximum", this, null));

		addressTypeChoice.setRequired(true).setLabel(new StringResourceModel("address.addressType.RequiredValidator", this, new Model<String>("Address Type")));
		addressStatusChoice.setRequired(true).setLabel(new StringResourceModel("address.addressStatus.RequiredValidator", this, new Model<String>("Address Status")));
		stateChoice.setRequired(true).setLabel(new StringResourceModel("address.state.RequiredValidator", this, new Model<String>("State")));
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
			iStudyService.delete(containerForm.getModelObject().getAddress());
			this.info("The Address has been deleted successfully.");
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
		StringBuffer feedBackMessageStr = new StringBuffer();
		// Get the person and set it on the AddressVO.
		try {
			Person person = iStudyService.getPerson(personSessionId);
			
			List<State> statesForThisCountry = iArkCommonService.getStates(containerForm.getModelObject().getAddress().getCountry()) ;
			if(statesForThisCountry.isEmpty()){
				containerForm.getModelObject().getAddress().setState(null);
			}
			else{
				containerForm.getModelObject().getAddress().setOtherState(null);
			}
			otherStateInvalidError.setVisible(false);
			
			containerForm.getModelObject().getAddress().setPerson(person);
			if (containerForm.getModelObject().getAddress().getId() == null) {
				if(containerForm.getModelObject().getAddress().getPreferredMailingAddress()){
					// Update any other preferredMailingAddresses to false
					iStudyService.setPreferredMailingAdressToFalse(person);
				}
				
				iStudyService.create(containerForm.getModelObject().getAddress());
				feedBackMessageStr.append("Address was successfully added and linked to Subject: ");
			}
			else {
				if(containerForm.getModelObject().getAddress().getPreferredMailingAddress()){
					// Update any other preferredMailingAddresses to false
					iStudyService.setPreferredMailingAdressToFalse(person);
				}
				
				iStudyService.update(containerForm.getModelObject().getAddress());
				feedBackMessageStr.append("Address was successfully updated and linked to Subject: ");
			}

			if (person.getFirstName() != null && person.getLastName() != null) {
				feedBackMessageStr.append(person.getFirstName() + " " + person.getLastName());
			}
			else {
				Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
				Study study = iArkCommonService.getStudy(studyId);
				String uid = iArkCommonService.getSubject(person.getId(), study).getSubjectUID();
				feedBackMessageStr.append(uid);
			}
			this.info(feedBackMessageStr.toString());
			processErrors(target);
			onSavePostProcess(target);
			// Invoke backend to persist the AddressVO
		}
		catch (EntityNotFoundException e) {
			this.error("The Specified subject is not available any more in the system. Please re-do the operation");
		}
		catch (ArkSystemException e) {
			this.error("A system error has occured, Pleas contact support.");
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
