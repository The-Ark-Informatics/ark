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
package au.org.theark.study.web.component.contact;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.iterator.ComponentHierarchyIterator;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.State;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.AddressVO;
import au.org.theark.core.vo.ContactVO;
import au.org.theark.core.vo.EmailAccountVo;
import au.org.theark.core.vo.PhoneVO;
import au.org.theark.core.web.component.AbstractContainerPanel;
import au.org.theark.core.web.component.button.ArkBusyAjaxButton;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;
import au.org.theark.study.web.component.contact.form.ContainerForm;

/**
 * @author nivedann
 * 
 */
public class ContactContainerPanel extends AbstractContainerPanel<ContactVO> {

	private static final long				serialVersionUID	= 1L;
	@SpringBean(name = Constants.STUDY_SERVICE)
	private IStudyService					studyService;
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService					iArkCommonService;
	private ContainerForm					containerForm;
	private PhoneDetailPanel				phoneDetailPanel;
	private AddressDetailPanel			addressDetailPanel;
	private EmailDetailPanel			emailDetailPanel;
	private PhoneListPanel 				phoneResultPanel; 
	private AddressListPanel 			addressResultPanel;
	private EmailListPanel				emailResultPanel;
	protected ArkBusyAjaxButton		newPhoneButton;
	protected ArkBusyAjaxButton		newAddressButton;
	protected ArkBusyAjaxButton		newEmailButton;

	/**
	 * @param id
	 */
	public ContactContainerPanel(String id) {
		super(id);
		cpModel = new CompoundPropertyModel<ContactVO>(new ContactVO());
		containerForm = new ContainerForm("containerForm", cpModel);
		containerForm.add(initialiseFeedBackPanel());
		containerForm.add(initialiseSearchResults());
		containerForm.add(initialiseDetailPanel());
		add(containerForm);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.component.AbstractContainerPanel#initialiseDetailPanel()
	 */
	@Override
	protected WebMarkupContainer initialiseDetailPanel() {
		phoneDetailPanel = new PhoneDetailPanel("phoneDetailPanel", feedBackPanel, arkCrudContainerVO, containerForm);
		phoneDetailPanel.setOutputMarkupId(true);
		phoneDetailPanel.initialisePanel();
		arkCrudContainerVO.getDetailPanelContainer().add(phoneDetailPanel);
		addressDetailPanel = new AddressDetailPanel("addressDetailPanel", feedBackPanel, arkCrudContainerVO, containerForm);
		addressDetailPanel.setOutputMarkupId(true);
		addressDetailPanel.initialisePanel();
		arkCrudContainerVO.getDetailPanelContainer().add(addressDetailPanel);
		emailDetailPanel = new EmailDetailPanel("emailDetailPanel", feedBackPanel, arkCrudContainerVO, containerForm);
		emailDetailPanel.setOutputMarkupId(true);
		emailDetailPanel.initialisePanel();
		arkCrudContainerVO.getDetailPanelContainer().add(emailDetailPanel);
		return arkCrudContainerVO.getDetailPanelContainer();
	}
	
	/**
	 * Initialize Search Address Results.
	 * @return
	 */
	private void initialiseSearchAddressResults() {
		 addressResultPanel = new AddressListPanel("addressResults", arkCrudContainerVO, containerForm);
		 addressResultPanel.setOutputMarkupId(true);
		 arkCrudContainerVO.getSearchResultPanelContainer().add(addressResultPanel);
	}
	
	/**
	 * Initialize Phone List for a person.
	 * @return
	 */
	private void initialiseSearchPhoneResults() {
		phoneResultPanel = new PhoneListPanel("phoneResults", arkCrudContainerVO, containerForm);
		phoneResultPanel.setOutputMarkupId(true);
		arkCrudContainerVO.getSearchResultPanelContainer().add(phoneResultPanel);
	}
	
	private void initialiseSearchEmailResults() {
		emailResultPanel = new EmailListPanel("emailResults", arkCrudContainerVO, containerForm);
		emailResultPanel.setOutputMarkupId(true);
		arkCrudContainerVO.getSearchResultPanelContainer().add(emailResultPanel);
	}
	

	/**
	 * There is no Search Panel in the contact container but we can use the 
	 * abstract method to initialize the components 
	 * 1.New phone button
	 * 2.New Address button.
	 * 3.Phone List
	 * 4.Address List
	 * 5. Email List 
	 */
	@Override
	protected WebMarkupContainer initialiseSearchResults() {
		boolean contextLoaded = prerenderContextCheck();
		initialiseNewPhoneButton();
		initialiseNewAddressButton();
		initialiseNewEmailButton();
		initialiseSearchPhoneResults();
		initialiseSearchAddressResults();
		initialiseSearchEmailResults();
		if (!contextLoaded) {
			this.error(au.org.theark.core.Constants.MESSAGE_NO_SUBJECT_IN_CONTEXT);
		}	
		return arkCrudContainerVO.getSearchResultPanelContainer();
	}
	
	/**
	 *  Initialize phone button.
	 */
	private void initialiseNewPhoneButton() {
		newPhoneButton = new ArkBusyAjaxButton("newPhoneButton") {
			private static final long	serialVersionUID	= 1L;
			@Override
			public boolean isVisible() {
				boolean isVisible = true;
				String sessionSubjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
				isVisible = (ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.NEW) && sessionSubjectUID != null);
				return isVisible;
			}
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onPhoneNew(target);
			}
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				this.error("An unexpected error occurred. Unable to proceed with new.");
			}
		};
		newPhoneButton.setDefaultFormProcessing(false);
		arkCrudContainerVO.getSearchResultPanelContainer().add(newPhoneButton);
	}
	
	
	/**
	 *  Initialize address button.
	 */
	private void initialiseNewAddressButton() {
		newAddressButton = new ArkBusyAjaxButton("newAddressButton") {
			private static final long	serialVersionUID	= 1L;
			@Override
			public boolean isVisible() {
				boolean isVisible = true;
				String sessionSubjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
				isVisible = (ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.NEW) && sessionSubjectUID != null);
				return isVisible;
			}
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onAddressNew(target);
			}
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				this.error("An unexpected error occurred. Unable to proceed with new.");
			}
		};
		newAddressButton.setDefaultFormProcessing(false);
		arkCrudContainerVO.getSearchResultPanelContainer().add(newAddressButton);
	}
	
	/**
	 *  Initialize email button.
	 */
	private void initialiseNewEmailButton() {
		newEmailButton = new ArkBusyAjaxButton("newEmailButton") {
			private static final long	serialVersionUID	= 1L;
			@Override
			public boolean isVisible() {
				boolean isVisible = true;
				String sessionSubjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
				isVisible = (ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.NEW) && sessionSubjectUID != null);
				return isVisible;
			}
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onEmailNew(target);
			}
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				this.error("An unexpected error occurred. Unable to proceed with new.");
			}
		};
		newEmailButton.setDefaultFormProcessing(false);
		arkCrudContainerVO.getSearchResultPanelContainer().add(newEmailButton);
	}
	
	/**
	 * Event to press new button under the phoneList.
	 * @param target
	 */
	private void onPhoneNew(AjaxRequestTarget target){
		preSetPhoneFormBeforeVisible();
		switchBetweenPanels(target,au.org.theark.study.web.Constants.PHONE_DETAIL_PANEL);	
	}
	/**
	 * Event to press new button under the phoneList.
	 * @param target
	 */
	private void onAddressNew(AjaxRequestTarget target){
		preSetAdddressFormBeforeVisible();
		switchBetweenPanels(target,au.org.theark.study.web.Constants.ADDRESS_DETAIL_PANEL);
	}
	
	private void onEmailNew(AjaxRequestTarget target){
		preSetEmailFormBeforeVisible();
		switchBetweenPanels(target,au.org.theark.study.web.Constants.EMAIL_DETAIL_PANEL);
	}

	/**
	 * Switching the loaded panel according to the button click.
	 * @param target
	 * @param type
	 */
	private void switchBetweenPanels(AjaxRequestTarget target, String type){
		
		Component  addressDetailPanelComp=arkCrudContainerVO.getDetailPanelContainer().get(au.org.theark.study.web.Constants.ADDRESS_DETAIL_PANEL);
		addressDetailPanelComp.setOutputMarkupId(true);
		Component  phoneDetailPanelComp=arkCrudContainerVO.getDetailPanelContainer().get(au.org.theark.study.web.Constants.PHONE_DETAIL_PANEL);
		phoneDetailPanelComp.setOutputMarkupId(true);
		Component  emailDetailPanelComp=arkCrudContainerVO.getDetailPanelContainer().get(au.org.theark.study.web.Constants.EMAIL_DETAIL_PANEL);
		emailDetailPanelComp.setOutputMarkupId(true);				
		arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
		arkCrudContainerVO.getDetailPanelContainer().setEnabled(true);
		arkCrudContainerVO.getDetailPanelFormContainer().setVisible(true);
		arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(true);
		if(au.org.theark.study.web.Constants.PHONE_DETAIL_PANEL.equals(type)){
			phoneDetailPanelComp.setVisible(true);
			addressDetailPanelComp.setVisible(false);
			emailDetailPanelComp.setVisible(false);
		}else if(au.org.theark.study.web.Constants.ADDRESS_DETAIL_PANEL.equals(type)){
			phoneDetailPanelComp.setVisible(false);
			addressDetailPanelComp.setVisible(true);
			emailDetailPanelComp.setVisible(false);
		}else if(au.org.theark.study.web.Constants.EMAIL_DETAIL_PANEL.equals(type)){
			phoneDetailPanelComp.setVisible(false);
			addressDetailPanelComp.setVisible(false);
			emailDetailPanelComp.setVisible(true);
		}
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
		arkCrudContainerVO.getEditButtonContainer().setVisible(true);
		arkCrudContainerVO.getViewButtonContainer().setVisible(false);
		arkCrudContainerVO.getSearchPanelContainer().setVisible(false);
		target.add(arkCrudContainerVO.getDetailPanelContainer());
		target.add(arkCrudContainerVO.getDetailPanelFormContainer());  
		target.add(addressDetailPanelComp);
		target.add(phoneDetailPanelComp);
		target.add(emailDetailPanelComp);
		target.add(arkCrudContainerVO.getSearchResultPanelContainer());
		target.add(arkCrudContainerVO.getSearchPanelContainer());
		target.add(arkCrudContainerVO.getViewButtonContainer());
		target.add(arkCrudContainerVO.getEditButtonContainer());
		
	}	
	/**
	 * <pre> set the form details before showing to the user.
	 */
	private void preSetAdddressFormBeforeVisible(){
		AddressVO addressVo=cpModel.getObject().getAddressVo();
		if (addressVo.getAddress() == null || addressVo.getAddress().getCountry() == null) {
			// final List<Country> countryList = iArkCommonService.getCountries();
			setDefaultCountry();
		}

		// Force new address to be preferred if totally new address
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		try {
			addressVo.setAddresses(studyService.getPersonAddressList(sessionPersonId, null));
		}
		catch (ArkSystemException e) {
			e.printStackTrace();
		}
		if(addressVo.getAddresses().size() == 0) {
			addressVo.getAddress().setPreferredMailingAddress(true);
		}
		updateDetailFormPrerender(addressVo.getAddress());
		
	}
	/**
	 * Set default country.
	 */
	private void setDefaultCountry() {

		Address defaultedCountryAddress = new Address();
		defaultedCountryAddress.setCountry(iArkCommonService.getCountry(au.org.theark.core.Constants.DEFAULT_COUNTRY_CODE));
		AddressVO newAddressVO = cpModel.getObject().getAddressVo();
		newAddressVO.setAddress(defaultedCountryAddress);
		cpModel.getObject().setAddressVo(newAddressVO);
	}
	/**
	 * 
	 * @param address
	 */
	private void updateDetailFormPrerender(Address address) {
		// Ensure we update the CountyStateChoices in DetailsForm
		// like what happens via DetailForm's updateStateChoices(..)
		List<State> stateList = iArkCommonService.getStates(address.getCountry());

		WebMarkupContainer wmcStateSelector = (WebMarkupContainer) arkCrudContainerVO.getDetailPanelContainer().get("addressDetailPanel").get("addressDetailsForm").get("addressDetailFormContainer").get(Constants.STATE_SELECTOR_WMC);
		DropDownChoice<State> detailStateSelector = (DropDownChoice<State>) wmcStateSelector.get("addressVo.address.state");
		Label otherStateInvalidError = (Label) wmcStateSelector.get("addressVo.address.otherStateInvalidError");
		TextField<String> otherState = (TextField<String>) wmcStateSelector.get("addressVo.address.otherState");

		if (stateList != null && stateList.size() > 0) {
			detailStateSelector.getChoices().clear();
			detailStateSelector.setChoices(stateList);
			detailStateSelector.setVisible(true);
			otherState.setVisible(false);
			//ARK-748 
			//Ignore the new Address objects from state validation and hide the other state validation label
			if(cpModel.getObject().getAddressVo().getAddress().getId() !=null  
					&& otherState.getModelObject()!=null && !otherState.getModelObject().isEmpty()){
				//alert the user
				otherStateInvalidError =new Label("addressVo.address.otherStateInvalidError", "Previously uploaded value " + otherState.getModelObject() + " was invalid.");
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
	/**
	 * This method is not used in the Contact Container panel because of the search panel 
	 * is never used in contact container any more. 
	 */
	@Override
	 protected WebMarkupContainer initialiseSearchPanel() {
		return null;
	}
	/**
	 * 
	 * @return
	 */
	protected boolean prerenderContextCheck() {
		// Get the Study, SubjectUID and ArkModule from Context
		Long sessionStudyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		String sessionSubjectUID = (String) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.SUBJECTUID);
		Long sessionArkModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);
		boolean contextLoaded = false;
		if ((sessionStudyId != null) && (sessionSubjectUID != null)) {
			LinkSubjectStudy linkSubjectStudy = null;
			ArkModule arkModule = null;
			Study study = null;
			try {
				study = iArkCommonService.getStudy(sessionStudyId);
				linkSubjectStudy = iArkCommonService.getSubjectByUID(sessionSubjectUID, study);
				arkModule = iArkCommonService.getArkModuleById(sessionArkModuleId);
				// cpModel.getObject().setArkModule(arkModule);
				if (study != null && linkSubjectStudy != null && arkModule != null) {
					contextLoaded = true;
				}
			}
			catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return contextLoaded;
	}
	
	/**
	 * <pre> set the form details before showing to the user.
	 */
	private void preSetPhoneFormBeforeVisible(){
		PhoneVO phoneVo=cpModel.getObject().getPhoneVo();

		// Force new address to be preferred if totally new address
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		try {
			phoneVo.setPhoneList(studyService.getPersonPhoneList(sessionPersonId, null));
		}
		catch (ArkSystemException e) {
			e.printStackTrace();
		}
		if(phoneVo.getPhoneList().size() == 0) {
			phoneVo.getPhone().setPreferredPhoneNumber(true);
		}
	}
	
	private void preSetEmailFormBeforeVisible(){
		EmailAccountVo emailVo=cpModel.getObject().getEmailAccountVo();

		// Force new address to be preferred if totally new address
		Long sessionPersonId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		try {
			emailVo.setEmailAccountList(studyService.getPersonEmailAccountList(sessionPersonId));
		}
		catch (ArkSystemException e) {
			e.printStackTrace();
		}
		if(emailVo.getEmailAccountList().size() == 0) {
			emailVo.getEmailAccount().setPrimaryAccount(true);
		}
	}
	

}
