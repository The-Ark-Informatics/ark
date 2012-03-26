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
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
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
import au.org.theark.core.model.study.entity.CountryState;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.AddressVO;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractSearchForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.address.DetailPanel;

/**
 * @author nivedann
 * 
 */
public class SearchForm extends AbstractSearchForm<AddressVO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService					iArkCommonService;

	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService						studyService;

	private ArkCrudContainerVO				arkCrudContainerVO;
	private DetailPanel							detailPanel;
	private PageableListView<Address>		listView;

	private TextField<String>					streetAddressTxtFld;
	private TextField<String>					cityTxtFld;
	private TextField<String>					postCodeTxtFld;
	private DropDownChoice<Country>			countryChoice;
	private DropDownChoice<CountryState>	stateChoice;
	private DropDownChoice<AddressType>		addressTypeChoice;

	private WebMarkupContainer					countryStateSelector;


	/**
	 * 
	 * @param id
	 * @param cpmModel
	 * @param arkCrudContainerVO
	 * @param feedBackPanel
	 * @param listView
	 */
	public SearchForm(String id,CompoundPropertyModel<AddressVO> cpmModel, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel, PageableListView<Address> listView){
		
		super(id,cpmModel,feedBackPanel,arkCrudContainerVO);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedBackPanel;
		this.listView = listView;

		Label generalTextLbl = new Label("generalLbl", new StringResourceModel("search.panel.text", new Model() ));
		add(generalTextLbl);
		resetButton.setVisible(false);
		searchButton.setVisible(false);
		
		//initialiseSearchForm();
		//addSearchComponentsToForm();
		//TODO: Use Subject UID when they are not just contacts
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		disableSearchForm(sessionPersonId, "There is no subject or contact in context. Please select a Subject or Contact.");
	}
	
	/**
	 * Initialise all the form components for the search
	 */
	protected void initialiseSearchForm() {

		streetAddressTxtFld = new TextField<String>(Constants.ADDRESS_STREET_ADDRESS);
		cityTxtFld = new TextField<String>(Constants.ADDRESS_CITY);
		postCodeTxtFld = new TextField<String>(Constants.ADDRESS_POST_CODE);
		initialiaseCountryDropDown();

		initialiseCountrySelector();
		initialiseAddressTypeDropDown();
	}

	protected void addSearchComponentsToForm() {
		add(streetAddressTxtFld);
		add(cityTxtFld);
		add(postCodeTxtFld);
		add(countryChoice);
		add(countryStateSelector);// This contains the dropdrop for State
		add(addressTypeChoice);
	}

	/**
	 * The MarkupContainer for The State DropDOwn control
	 */
	private void initialiseCountrySelector() {

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
		countryChoice = new DropDownChoice<Country>(Constants.ADDRESS_COUNTRY, countryList, defaultChoiceRenderer);
		// Attach a behavior, so when it changes it does something
		countryChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				updateCountryStateChoices(countryChoice.getModelObject());
				target.add(countryStateSelector);
			}
		});
	}

	private void initialiseAddressTypeDropDown() {
		List<AddressType> addressTypeList = iArkCommonService.getAddressTypes();
		ChoiceRenderer<AddressType> defaultChoiceRenderer = new ChoiceRenderer<AddressType>(Constants.NAME, Constants.ID);
		addressTypeChoice = new DropDownChoice<AddressType>(Constants.ADDRESS_ADDRESSTYPE, addressTypeList, defaultChoiceRenderer);
	}

	/**
	 * A method that will refresh the choices in the State drop down choice based on what was selected in the Country Dropdown. It uses the country as
	 * the argument and invokes the back-end to fetch relative states.
	 */
	private void updateCountryStateChoices(Country country) {

		List<CountryState> countryStateList = iArkCommonService.getStates(country);
		stateChoice.getChoices().clear();
		stateChoice.setChoices(countryStateList);
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
				this.info("No records match the specified criteria.");
				target.add(feedbackPanel);
			}

			getModelObject().setAddresses(addressList);
			listView.removeAll();
			arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
			target.add(arkCrudContainerVO.getSearchResultPanelContainer());
		}
		catch (EntityNotFoundException entityNotFoundException) {
			this.warn("There are no addresses available for the specified criteria.");
			target.add(feedbackPanel);

		}
		catch (ArkSystemException arkException) {
			this.error("The Ark Application has encountered a system error.");
			target.add(feedbackPanel);
		}

	}

	@Override
	protected void onNew(AjaxRequestTarget target) {
		// ARK-108:: no longer do full reset to VO
		// Set a default Country on new when the Country field is empty
		if (getModelObject().getAddress() == null || getModelObject().getAddress().getCountry() == null) {
			final List<Country> countryList = iArkCommonService.getCountries();
			setDefaultCountry();
		}
		
		// Force new address to be preferred
		getModelObject().getAddress().setPreferredMailingAddress(true);
		
		updateDetailFormPrerender(getModelObject().getAddress());

		preProcessDetailPanel(target);
	}

	public void updateDetailFormPrerender(Address address) {
		// Ensure we update the CountyStateChoices in DetailsForm
		// like what happens via DetailForm's updateCountryStateChoices(..)
		List<CountryState> countryStateList = iArkCommonService.getStates(address.getCountry());
		WebMarkupContainer wmcStateSelector = (WebMarkupContainer) arkCrudContainerVO.getDetailPanelFormContainer().get(Constants.COUNTRY_STATE_SELECTOR_WMC);
		DropDownChoice<CountryState> detailStateSelector = (DropDownChoice<CountryState>) wmcStateSelector.get("address.countryState");

		TextField<String> otherState = (TextField<String>) wmcStateSelector.get("address.otherState");

		if (countryStateList != null && countryStateList.size() > 0) {
			detailStateSelector.getChoices().clear();
			detailStateSelector.setChoices(countryStateList);
			detailStateSelector.setVisible(true);
			otherState.setVisible(false);
		}
		else {
			// hide it
			detailStateSelector.setVisible(false);
			otherState.setVisible(true);
		}
	}
}
