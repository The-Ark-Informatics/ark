/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.phenotypic.web.component.field.form;

import java.util.Collection;
import java.util.Date;
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
import org.odlabs.wiquery.ui.datepicker.DatePicker;

import au.org.theark.phenotypic.model.entity.FieldType;
import au.org.theark.phenotypic.model.vo.FieldVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.field.Detail;

/**
 * @author nivedann
 *
 */
@SuppressWarnings({ "serial", "unchecked", "unused" })
public class DetailForm extends Form{

	@SpringBean( name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService phenotypicService;
	
	public DetailForm(String id)
	{
		super(id);
		// TODO Auto-generated constructor stub
	}
	private WebMarkupContainer resultListContainer;
	private WebMarkupContainer detailPanelContainer;
	
	private ContainerForm fieldContainerForm;
	
	private TextField<String> fieldIdTxtFld;
	private TextField<String> fieldNameTxtFld;
	private TextField<String> fieldDescriptionTxtFld;
	private TextField<String> fieldUnitsTxtFld;
	
	private DatePicker<Date> dateOfBirth;
	
	//Reference Data 
	private DropDownChoice<FieldType> fieldTypeDdc;
	
	private AjaxButton deleteButton;
	private AjaxButton saveButton;
	private AjaxButton cancelButton;
	
	/**
	 * @param id
	 */
	public DetailForm(	String id,
						Detail detailPanel, 
						WebMarkupContainer listContainer,
						WebMarkupContainer detailsContainer,
						ContainerForm containerForm) {
		super(id);
		this.fieldContainerForm = containerForm;
		this.resultListContainer = listContainer;
		this.detailPanelContainer = detailsContainer;
		this.fieldContainerForm = containerForm;
		
		
		cancelButton = new AjaxButton(au.org.theark.core.Constants.CANCEL,  new StringResourceModel("cancelKey", this, null))
		{

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				
				onCancel(target);
			}
		};
		
		saveButton = new AjaxButton(au.org.theark.core.Constants.SAVE, new StringResourceModel("saveKey", this, null))
		{

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onSave(fieldContainerForm.getModelObject(), target);
				target.addComponent(detailPanelContainer);
			}
			
			public void onError(AjaxRequestTarget target, Form<?> form){
				processErrors(target);
			}
		};
		
	}
	
	public void initialiseForm(){
		
		fieldNameTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELD_NAME);
		fieldDescriptionTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELD_DESCRIPTION);
		
		//Initialise Drop Down Choices 
		//Title We can also have the reference data populated on Application start and refer to a static list instead of hitting the database
		Collection<FieldType> fieldTypeList = phenotypicService.getFieldTypes();
		ChoiceRenderer<FieldType> defaultChoiceRenderer = new ChoiceRenderer<FieldType>(au.org.theark.phenotypic.web.Constants.FIELD_NAME,au.org.theark.phenotypic.web.Constants.FIELD_ID);
		fieldTypeDdc = new DropDownChoice<FieldType>(au.org.theark.phenotypic.web.Constants.FIELD_TYPE,(List)fieldTypeList,defaultChoiceRenderer);

		attachValidators();
		addComponents();
	}
	
	private void attachValidators(){
		
		fieldNameTxtFld.setRequired(true);
	}
	
	private void addComponents(){

		add(fieldIdTxtFld);
		add(fieldTypeDdc);
		add(saveButton);
		add(cancelButton.setDefaultFormProcessing(false));
	}
	
	protected void onSave(FieldVO fieldVo, AjaxRequestTarget target){
		
	}
	
	protected  void onCancel(AjaxRequestTarget target){
		
	}
	
	protected void processErrors(AjaxRequestTarget target){
		
	}

	public TextField<String> getFieldIdTxtFld() {
		return fieldIdTxtFld;
	}

	public void setfieldIdTxtFld(TextField<String> fieldIdTxtFld) {
		this.fieldIdTxtFld = fieldIdTxtFld;
	}

}
