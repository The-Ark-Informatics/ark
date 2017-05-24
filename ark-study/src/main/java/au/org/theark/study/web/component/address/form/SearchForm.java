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

import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
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

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.AddressType;
import au.org.theark.core.model.study.entity.Country;
import au.org.theark.core.model.study.entity.State;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.AddressVO;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

/**
 * @author nivedann, travis
 * 
 */
public class SearchForm extends AbstractSearchForm<AddressVO> {


	private static final long					serialVersionUID	= 1L;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService					iArkCommonService;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService						studyService;

	private ArkCrudContainerVO					arkCrudContainerVO;

	private PageableListView<Address>		listView;

	private TextField<String>					streetAddressTxtFld;
	private TextField<String>					cityTxtFld;
	private TextField<String>					postCodeTxtFld;
	private DropDownChoice<Country>			countryChoice;
	private DropDownChoice<State>	stateChoice;
	private DropDownChoice<AddressType>		addressTypeChoice;

	private WebMarkupContainer					stateSelector;

	/**
	 * 
	 * @param id
	 * @param cpmModel
	 * @param arkCrudContainerVO
	 * @param feedBackPanel
	 * @param listView
	 */
	public SearchForm(String id, CompoundPropertyModel<AddressVO> cpmModel, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel, PageableListView<Address> listView) {

		super(id, cpmModel, feedBackPanel, arkCrudContainerVO);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedBackPanel;
		this.listView = listView;

		Label generalTextLbl = new Label("generalLbl", new StringResourceModel("search.panel.text", new Model()));
		add(generalTextLbl);
		resetButton.setVisible(false);
		searchButton.setVisible(false);

		// initialiseSearchForm();
		// addSearchComponentsToForm();
		// TODO: Use Subject UID when they are not just contacts
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		disableSearchForm(sessionPersonId, "There is no subject or contact selected. Please select a subject or contact.");
	}

	/**
	 * Initialise all the form components for the search
	 */
	protected void initialiseSearchForm() {

		streetAddressTxtFld = new TextField<String>("address.streetAddress");
		cityTxtFld = new TextField<String>("address.city");
		postCodeTxtFld = new TextField<String>("address.postCode");
		initialiaseCountryDropDown();

		initialiseCountrySelector();
		initialiseAddressTypeDropDown();
	}

	protected void addSearchComponentsToForm() {
		add(streetAddressTxtFld);
		add(cityTxtFld);
		add(postCodeTxtFld);
		add(countryChoice);
		add(stateSelector);// This contains the dropdrop for State
		add(addressTypeChoice);
	}

	/**
	 * The MarkupContainer for The State DropDOwn control
	 */
	private void initialiseCountrySelector() {

		stateSelector = new WebMarkupContainer("stateSelector");
		stateSelector.setOutputMarkupPlaceholderTag(true);
		// Get the value selected in Country
		Country selectedCountry = countryChoice.getModelObject();

		// If there is no country selected, back should default to current country and pull the states
		List<State> stateList = iArkCommonService.getStates(selectedCountry);
		ChoiceRenderer<State> defaultStateChoiceRenderer = new ChoiceRenderer<State>("name", Constants.ID);
		stateChoice = new DropDownChoice<State>("address.state", stateList, defaultStateChoiceRenderer);
		// Add the Country State Dropdown into the WebMarkupContainer - countrySelector
		stateSelector.add(stateChoice);
	}

	private void setDefaultCountry() {

		Address defaultedCountryAddress = new Address();
		defaultedCountryAddress.setCountry(iArkCommonService.getCountry(au.org.theark.core.Constants.DEFAULT_COUNTRY_CODE));
		AddressVO newAddressVO = getModelObject();
		newAddressVO.setAddress(defaultedCountryAddress);
		setModelObject(newAddressVO);
	}

	private void initialiaseCountryDropDown() {

		List<Country> countryList = iArkCommonService.getCountries();
		ChoiceRenderer<Country> defaultChoiceRenderer = new ChoiceRenderer<Country>(Constants.NAME, Constants.ID);
		countryChoice = new DropDownChoice<Country>("address.country", countryList, defaultChoiceRenderer);
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

	private void initialiseAddressTypeDropDown() {
		List<AddressType> addressTypeList = iArkCommonService.getAddressTypes();
		ChoiceRenderer<AddressType> defaultChoiceRenderer = new ChoiceRenderer<AddressType>(Constants.NAME, Constants.ID);
		addressTypeChoice = new DropDownChoice<AddressType>("address.addressType", addressTypeList, defaultChoiceRenderer);
	}

	/**
	 * A method that will refresh the choices in the State drop down choice based on what was selected in the Country Dropdown. It uses the country as
	 * the argument and invokes the back-end to fetch relative states.
	 */
	private void updateStateChoices(Country country) {

		List<State> stateList = iArkCommonService.getStates(country);
		stateChoice.getChoices().clear();
		stateChoice.setChoices(stateList);
	}

	@Override
	protected void onSearch(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		// Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		// String sessionPersonType = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_TYPE);// Subject
		// or contact placed insession
		try {

			// if(sessionPersonType.equalsIgnoreCase(au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_SUBJECT)){
			//
			// }else if(sessionPersonType.equalsIgnoreCase(au.org.theark.core.Constants.PERSON_CONTEXT_TYPE_CONTACT)){
			//
			// }
			Address address = getModelObject().getAddress();
			address.setPerson(studyService.getPerson(sessionPersonId));
			Collection<Address> addressList = studyService.getPersonAddressList(sessionPersonId, address);
			if (addressList != null && addressList.size() == 0) {
				this.info("No records match the specified search criteria.");
				target.add(feedbackPanel);
			}

			getModelObject().setAddresses(addressList);
			listView.removeAll();
			arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
			target.add(arkCrudContainerVO.getSearchResultPanelContainer());
		}
		catch (EntityNotFoundException entityNotFoundException) {
			this.warn("There are no addresses available for the specified search criteria.");
			target.add(feedbackPanel);

		}
		catch (ArkSystemException arkException) {
			this.error("The Ark Application has encountered a system error. Please contact the system administrator.");
			target.add(feedbackPanel);
		}

	}

	@Override
	protected void onNew(AjaxRequestTarget target) {
		// ARK-108:: no longer do full reset to VO
		// Set a default Country on new when the Country field is empty
		if (getModelObject().getAddress() == null || getModelObject().getAddress().getCountry() == null) {
			// final List<Country> countryList = iArkCommonService.getCountries();
			setDefaultCountry();
		}

		// Force new address to be preferred if totally new address
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		try {
			getModelObject().setAddresses(studyService.getPersonAddressList(sessionPersonId, null));
		}
		catch (ArkSystemException e) {
			e.printStackTrace();
		}
		if(getModelObject().getAddresses().size() == 0) {
			getModelObject().getAddress().setPreferredMailingAddress(true);
		}

		updateDetailFormPrerender(getModelObject().getAddress());

		preProcessDetailPanel(target);
	}

	public void updateDetailFormPrerender(Address address) {
		// Ensure we update the CountyStateChoices in DetailsForm
		// like what happens via DetailForm's updateStateChoices(..)
		List<State> stateList = iArkCommonService.getStates(address.getCountry());
		WebMarkupContainer wmcStateSelector = (WebMarkupContainer) arkCrudContainerVO.getDetailPanelFormContainer().get(Constants.STATE_SELECTOR_WMC);
		DropDownChoice<State> detailStateSelector = (DropDownChoice<State>) wmcStateSelector.get("address.state");
		Label otherStateInvalidError = (Label) wmcStateSelector.get("address.otherStateInvalidError");
		TextField<String> otherState = (TextField<String>) wmcStateSelector.get("address.otherState");

		if (stateList != null && stateList.size() > 0) {
			detailStateSelector.getChoices().clear();
			detailStateSelector.setChoices(stateList);
			detailStateSelector.setVisible(true);
			otherState.setVisible(false);
			//ARK-748 
			//Ignore the new Address objects from state validation and hide the other state validation label
			if(getModelObject().getAddress().getId() !=null  
					&& otherState.getModelObject()!=null && !otherState.getModelObject().isEmpty()){
				//alert the user
				otherStateInvalidError =new Label("address.otherStateInvalidError", "Previously uploaded value " + otherState.getModelObject() + " was invalid.");
				otherStateInvalidError.add(new Behavior(){
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 7964297415151363039L;

					@Override
					public void onComponentTag(Component component, ComponentTag tag) {
						super.onComponentTag(component, tag);
						tag.put("class", "fieldErrorMessage");//or something like ("style"; "color-red")
					}
				});
				wmcStateSelector.addOrReplace(otherStateInvalidError);
			}
			else{
				otherStateInvalidError.setVisible(false);
			}
		}
		else {
			// hide it
			detailStateSelector.setVisible(false);
			otherState.setVisible(true);
			otherStateInvalidError.setVisible(false);
		}
	}
}
