/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.study.web.component.subject.form;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.study.model.entity.PhoneType;
import au.org.theark.study.model.vo.SubjectVO;
import au.org.theark.study.service.IStudyService;
import au.org.theark.study.web.Constants;

/**
 * @author nivedann
 *
 */
public class PhoneForm extends Form{
	
	private ContainerForm phoneContainerForm;
	
	private TextField<String> areaCodeTxtFld;
	private TextField<String> phoneNumberTxtFld;
	private TextField<String> phoneIdTxtFld;
	private DropDownChoice<PhoneType> phoneTypeChoice;
	
	private WebMarkupContainer listContainer;
	
	private AjaxButton deleteButton;
	private AjaxButton saveButton;
	private AjaxButton cancelButton;
	
	
	@SpringBean( name = Constants.STUDY_SERVICE)
	private IStudyService studyService;
	
	/**
	 * @param id
	 */
	public PhoneForm(String id,ContainerForm containerForm, WebMarkupContainer phoneListContainer) {
		super(id);
		this.phoneContainerForm = containerForm;
		this.listContainer  = phoneListContainer;
		
		cancelButton = new AjaxButton(Constants.CANCEL,  new StringResourceModel("cancelKey", this, null))
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				
			}
		};
		
		saveButton = new AjaxButton(Constants.SAVE,  new StringResourceModel("cancelKey", this, null))
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onSave(phoneContainerForm.getModelObject(), target);
				//Hide the Detail Panel for Phone and make the List display with the new result
			}
			
			public void onError(AjaxRequestTarget target, Form<?> form){
				//Display the error message and allow user to correct it or cancel
				processFeedback(target);
			}
		};
		
		deleteButton = new AjaxButton(Constants.DELETE,  new StringResourceModel("cancelKey", this, null))
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				
			}
		};
		
		
	}
	
	protected void onSave(SubjectVO subjectVO, AjaxRequestTarget target){
		
	}
	
	protected void processFeedback(AjaxRequestTarget target){
		
	}
	
	public void initialiseForm(){
		
		phoneIdTxtFld = new TextField<String>("phone.phoneKey");
		areaCodeTxtFld = new TextField<String>("phone.areaCode");
		phoneNumberTxtFld = new TextField<String>("phone.phoneNumber");

		//Initalise the dropdown choice with a list
		List<PhoneType> phoneTypeList = studyService.getListOfPhoneType();
		ChoiceRenderer defaultChoiceRenderer = new ChoiceRenderer(Constants.NAME, Constants.PHONE_TYPE_ID);
		phoneTypeChoice = new DropDownChoice("phone.phoneType",phoneTypeList,defaultChoiceRenderer);
	}
	
	private void addComponents(){
		
		add(phoneIdTxtFld);
		add(areaCodeTxtFld);
		add(phoneNumberTxtFld);
		add(phoneTypeChoice);
	}
	
	private void attachValidators(){
		
	}
	private void decorateComponents(){
		
	}

}
