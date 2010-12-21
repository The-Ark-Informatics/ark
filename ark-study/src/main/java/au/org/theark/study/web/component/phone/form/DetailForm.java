/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.phone.form;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.PhoneVO;
import au.org.theark.core.web.form.AbstractDetailForm;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

/**
 * @author nivedann
 *
 */
public class DetailForm extends AbstractDetailForm<PhoneVO>{

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService iArkCommonService;

	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	private TextField<String> areaCodeTxtFld;
	private TextField<String> phoneNumberTxtFld;
	private DropDownChoice<PhoneType> phoneTypeChoice;

	
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
			WebMarkupContainer editButtonContainer, Form<PhoneVO> containerForm) {
		
		
			super(id, 
					feedBackPanel, 
					resultListContainer, 
					detailPanelContainer,
					detailPanelFormContainer, 
					searchPanelContainer, 
					viewButtonContainer,
					editButtonContainer, 
					containerForm);

	}
	
	
	public void initialiseDetailForm(){
		areaCodeTxtFld = new TextField<String>("phone.areaCode");
		phoneNumberTxtFld = new TextField<String>("phone.phoneNumber");
		List<PhoneType> phoneTypeList = iArkCommonService.getListOfPhoneType();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.ID);
		phoneTypeChoice = new DropDownChoice("phone.phoneType",phoneTypeList,defaultChoiceRenderer);
		addDetailFormComponents();
		attachValidators();
	}

	
	public void addDetailFormComponents(){
		detailPanelFormContainer.add(areaCodeTxtFld);
		detailPanelFormContainer.add(phoneNumberTxtFld);
		detailPanelFormContainer.add(phoneTypeChoice);
	}


	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#attachValidators()
	 */
	@Override
	protected void attachValidators() {
		phoneNumberTxtFld.setRequired(true);
		areaCodeTxtFld.setRequired(true);
	}


	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onCancel(org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onCancel(AjaxRequestTarget target) {
		PhoneVO phoneVO = new PhoneVO();
		containerForm.setModelObject(phoneVO);
		onCancelPostProcess(target);
	}


	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onDeleteConfirmed(org.apache.wicket.ajax.AjaxRequestTarget, java.lang.String, org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow)
	 */
	@Override
	protected void onDeleteConfirmed(AjaxRequestTarget target,	String selection, ModalWindow selectModalWindow) {
		selectModalWindow.close(target);
		PhoneVO phoneVO = new PhoneVO();
		containerForm.setModelObject(phoneVO);
		onCancel(target);
	}


	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#onSave(org.apache.wicket.markup.html.form.Form, org.apache.wicket.ajax.AjaxRequestTarget)
	 */
	@Override
	protected void onSave(Form<PhoneVO> containerForm, AjaxRequestTarget target) {
		System.out.println("onSave invoked");
		//Persist the phone number to the backend, associate the person in context with the phone object
		Long personSessionId =(Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.PERSON_CONTEXT_ID);
		//Get the person and set it on the Phone object.
		try {
			Person person = studyService.getPerson(personSessionId);
			containerForm.getModelObject().getPhone().setPerson(person);
			if(containerForm.getModelObject().getPhone().getId() == null ){
				studyService.create(containerForm.getModelObject().getPhone());
				this.info("Phone number was added and linked to " + containerForm.getModelObject().getPhone().getPerson().getFirstName() + " " + containerForm.getModelObject().getPhone().getPerson().getLastName());
				processErrors(target);
				//Call the create
			}else{
				studyService.update(containerForm.getModelObject().getPhone());
				this.info("Phone number was updated and linked to " + containerForm.getModelObject().getPhone().getPerson().getFirstName() + " " + containerForm.getModelObject().getPhone().getPerson().getLastName());
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
