/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.address.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.AddressType;
import au.org.theark.core.model.study.entity.Country;
import au.org.theark.core.model.study.entity.CountryState;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.AddressVO;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

/**
 * @author nivedann
 *
 */
public class DetailForm  extends AbstractDetailForm<AddressVO>{

	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;

	
	private TextField<String> streetAddressTxtFld;
	private TextField<String> cityTxtFld;
	private TextField<String> postCodeTxtFld;
	private DropDownChoice<Country> countryChoice;
	private DropDownChoice<CountryState> stateChoice;
	private DropDownChoice<AddressType> addressTypeChoice;
	private WebMarkupContainer countryStateSelector;
	private RadioChoice<Boolean> addressStatusRadioChoice;
	
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
	public DetailForm(String id, FeedbackPanel feedBackPanel,
			WebMarkupContainer resultListContainer,
			WebMarkupContainer detailPanelContainer,
			WebMarkupContainer detailPanelFormContainer,
			WebMarkupContainer searchPanelContainer,
			WebMarkupContainer viewButtonContainer,
			WebMarkupContainer editButtonContainer,
			Form<AddressVO> containerForm) {
		
		super(id, feedBackPanel, resultListContainer, detailPanelContainer,
				detailPanelFormContainer, searchPanelContainer, viewButtonContainer,
				editButtonContainer, containerForm);
		
	}
	
	
	public void initialiseDetailForm(){
		streetAddressTxtFld = new TextField<String>("address.streetAddress");
		cityTxtFld = new TextField<String>("address.city");
		postCodeTxtFld = new TextField<String>("address.postCode");
		initialiaseCountryDropDown();
		initialiseCountrySelector();
		initialiseAddressTypeDropDown();
		initialiseRadioButton();
		addDetailFormComponents();
	}
	
	
	public void addDetailFormComponents(){
		detailPanelFormContainer.add(streetAddressTxtFld);
		detailPanelFormContainer.add(cityTxtFld);
		detailPanelFormContainer.add(postCodeTxtFld);
		detailPanelFormContainer.add(countryChoice);
		detailPanelFormContainer.add(countryStateSelector);//This contains the drop-downn for State
		detailPanelFormContainer.add(addressTypeChoice);
		detailPanelFormContainer.add(addressStatusRadioChoice);
	}
	
	
	private void initialiseAddressTypeDropDown(){
		List<AddressType> addressTypeList = iArkCommonService.getAddressTypes();
		ChoiceRenderer<AddressType> defaultChoiceRenderer = new ChoiceRenderer<AddressType>(Constants.NAME, Constants.ID);
		addressTypeChoice = new DropDownChoice<AddressType>(Constants.ADDRESS_ADDRESSTYPE,addressTypeList,defaultChoiceRenderer);
	}
	
	/**
	 * The MarkupContainer for The State DropDOwn control
	 */
	private void initialiseCountrySelector(){
		
		countryStateSelector = new WebMarkupContainer("countryStateSelector");
		countryStateSelector.setOutputMarkupPlaceholderTag(true);
		//Get the value selected in Country
		Country selectedCountry  = countryChoice.getModelObject();
		
		//If there is no country selected, back should default to current country and pull the states
		List<CountryState> countryStateList  = iArkCommonService.getStates(selectedCountry);
		ChoiceRenderer<CountryState> defaultStateChoiceRenderer = new ChoiceRenderer<CountryState>("state", Constants.ID);
		stateChoice = new DropDownChoice<CountryState>(Constants.ADDRESS_COUNTRYSTATE_STATE,countryStateList,defaultStateChoiceRenderer);
		//Add the Country State Dropdown into the WebMarkupContainer - countrySelector
		countryStateSelector.add(stateChoice);
	}

	private void initialiaseCountryDropDown(){
		
		List<Country> countryList = iArkCommonService.getCountries();
		ChoiceRenderer<Country> defaultChoiceRenderer = new ChoiceRenderer<Country>(Constants.NAME, Constants.ID);
		countryChoice = new DropDownChoice<Country>(Constants.ADDRESS_COUNTRY, countryList, defaultChoiceRenderer);
		
		//Attach a behavior, so when it changes it does something
		countryChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				updateCountryStateChoices(countryChoice.getModelObject());
				target.addComponent(countryStateSelector);
			}
		});
	}
	
	/**
	 * A method that will refresh the choices in the State drop down choice based on
	 * what was selected in the Country Dropdown. It uses the country as the argument and invokes
	 * the back-end to fetch relative states.
	 */
	private void updateCountryStateChoices(Country country){
		
		List<CountryState> countryStateList = iArkCommonService.getStates(country);
		stateChoice.getChoices().clear();
		stateChoice.setChoices(countryStateList);
	}
	
	private void initialiseRadioButton(){
		
		//The list that the Radio Button will use
		List<Boolean> list = new ArrayList<Boolean>();
		list.add(Boolean.TRUE);
		list.add(Boolean.FALSE);
		
		IChoiceRenderer<Boolean> radioChoiceRender = new IChoiceRenderer<Boolean>() {
			public Object getDisplayValue(final Boolean choice){
				
				String displayValue="Inactive";
				
				if(choice !=null && choice.booleanValue()){
					displayValue = "Active";
				}
				return displayValue;
			}
			
			public String getIdValue(final Boolean object,final int index){
				return object.toString();
			}
		};
		
		PropertyModel<Boolean> addressStatusModel = new PropertyModel<Boolean>(containerForm.getModelObject().getAddress(),"addressStatus");
		addressStatusRadioChoice = new RadioChoice<Boolean>("addressStatus",addressStatusModel,list,radioChoiceRender);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		// TODO Auto-generated method stub
		//studyNameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.study.name.required", this, new Model<String>("Name")));
		cityTxtFld.setRequired(true).setLabel( new StringResourceModel("error.city.required", this, new Model<String>("City")));
		streetAddressTxtFld.setRequired(true).setLabel(new StringResourceModel("error.street.address.required", this, new Model<String>("Street Address")));
		streetAddressTxtFld.add(StringValidator.maximumLength(255));
		
		postCodeTxtFld.setRequired(true).setLabel(new StringResourceModel("error.postcode.required", this, new Model<String>("Post Code")));
		postCodeTxtFld.add(StringValidator.maximumLength(10)).setLabel(new StringResourceModel("error.postcode.max.length", this , new Model<String>("Post Code Max Length")));
		postCodeTxtFld.add(StringValidator.minimumLength(4));
		
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {
		AddressVO addressVO = new AddressVO();
		containerForm.setModelObject(addressVO);
		onCancelPostProcess(target);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onDeleteConfirmed(org.apache.wicket.ajax.AjaxRequestTarget, java.lang.String, org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow)
	 */
	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target,	String selection, ModalWindow selectModalWindow) {
		try {
			
			studyService.delete(containerForm.getModelObject().getAddress());
		} catch (ArkSystemException e) {
			this.error("An error occured while processing your delete. Please contact Support");
			//TODO Need to work out more on how user will contact support (Level 1..etc) a generic message with contact info plus logs to be emailed to admin
			e.printStackTrace();
		}
		selectModalWindow.close(target);
		AddressVO addressVO = new AddressVO();
		containerForm.setModelObject(addressVO);
		onCancel(target);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<AddressVO> containerForm,AjaxRequestTarget target) {
		
		Long personSessionId =(Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		//Get the person and set it on the Phone object.
		try {
			Person person = studyService.getPerson(personSessionId);
			containerForm.getModelObject().getAddress().setPerson(person);
			if(containerForm.getModelObject().getAddress().getId() == null ){
				studyService.create(containerForm.getModelObject().getAddress());
				this.info("Address was successfully added and linked to Subject:" + person.getFirstName() + " " + person.getLastName());
				processErrors(target);
				//Call the create
			}else{
				
				studyService.update(containerForm.getModelObject().getAddress());
				this.info("Address was successfully updated and linked to Subject:" + person.getFirstName() + " " + person.getLastName());
				processErrors(target);
				//Update 
			}
			
			onSavePostProcess(target);
			//Invoke backend to persist the phone
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ArkSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#processErrors(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void processErrors(AjaxRequestTarget target) {
		target.addComponent(feedBackPanel);
		
	}

}
