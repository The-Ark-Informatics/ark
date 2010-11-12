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

import mx4j.log.Log;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.datepicker.DatePicker;

import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldType;
import au.org.theark.phenotypic.model.vo.FieldVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.field.Detail;

/**
 * @author nivedann
 * 
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class DetailForm extends Form<FieldVO>
{
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService				phenotypicService;

	private WebMarkupContainer				resultListContainer;
	private WebMarkupContainer				detailPanelContainer;
	private WebMarkupContainer				detailFormContainer;
	private WebMarkupContainer 			viewButtonContainer;
	private WebMarkupContainer 			editButtonContainer;
	private ContainerForm					fieldContainerForm;

	private int									mode;

	private TextField<String>				fieldIdTxtFld;
	private TextField<String>				fieldNameTxtFld;
	private DropDownChoice<FieldType>	fieldTypeDdc;
	private TextArea<String>				fieldDescriptionTxtAreaFld;
	private TextField<String>				fieldUnitsTxtFld;
	private TextField<String>				fieldSeqNumTxtFld;
	private TextField<String>				fieldMinValueTxtFld;
	private TextField<String>				fieldMaxValueTxtFld;
	private TextField<String>				fieldDiscreteValuesTxtFld;

	private AjaxButton						editButton;
	private AjaxButton						editCancelButton;
	private AjaxButton						deleteButton;
	private AjaxButton						saveButton;
	private AjaxButton						cancelButton;

	/**
	 * Default constructor
	 * 
	 * @param id
	 */
	public DetailForm(String id)
	{
		super(id);
	}

	/**
	 * DetailForm constructor
	 * 
	 * @param id
	 */
	public DetailForm(String id, Detail detailPanel, WebMarkupContainer listContainer, WebMarkupContainer detailsContainer, ContainerForm containerForm,
			WebMarkupContainer viewButtonContainer,
			WebMarkupContainer editButtonContainer,
			WebMarkupContainer detailFormContainer)
	{
		super(id);
		this.fieldContainerForm = containerForm;
		this.resultListContainer = listContainer;
		this.detailPanelContainer = detailsContainer;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.detailFormContainer = detailFormContainer;
		
		editButton = new AjaxButton(au.org.theark.core.Constants.EDIT, new StringResourceModel("editKey", this, null))
		{

			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				onEdit(fieldContainerForm.getModelObject(), target);
				target.addComponent(detailPanelContainer);
			}

			public void onError(AjaxRequestTarget target, Form<?> form)
			{
				processErrors(target);
			}
		};
		
		editCancelButton = new AjaxButton(au.org.theark.core.Constants.EDIT_CANCEL, new StringResourceModel("editCancelKey", this, null))
		{
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				onCancel(target);
			}
		};

		cancelButton = new AjaxButton(au.org.theark.core.Constants.CANCEL, new StringResourceModel("cancelKey", this, null))
		{

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				onCancel(target);
			}
		};

		saveButton = new AjaxButton(au.org.theark.core.Constants.SAVE, new StringResourceModel("saveKey", this, null))
		{

			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				onSave(fieldContainerForm.getModelObject(), target);
				target.addComponent(detailPanelContainer);
			}

			public void onError(AjaxRequestTarget target, Form<?> form)
			{
				processErrors(target);
			}
		};
		
		deleteButton = new AjaxButton(au.org.theark.core.Constants.DELETE, new StringResourceModel("deleteKey", this, null))
		{
			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				onDelete(fieldContainerForm.getModelObject(), target);
				target.addComponent(detailPanelContainer);
			}

			public void onError(AjaxRequestTarget target, Form<?> form)
			{
				processErrors(target);
			}
		};
	}
	
	private void initFieldTypeDdc()
	{
		 java.util.Collection<FieldType> fieldTypeCollection = phenotypicService.getFieldTypes();
		 ChoiceRenderer fieldTypeRenderer = new ChoiceRenderer(au.org.theark.phenotypic.web.Constants.FIELD_TYPE_NAME, au.org.theark.phenotypic.web.Constants.FIELD_TYPE_ID);
		 fieldTypeDdc = new DropDownChoice<FieldType>(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_FIELD_TYPE, (List) fieldTypeCollection, fieldTypeRenderer);
	}

	public void initialiseForm()
	{
		fieldIdTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_ID);
		fieldNameTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_NAME);
		fieldDescriptionTxtAreaFld = new TextArea<String>(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_DESCRIPTION);
		fieldUnitsTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_UNITS);
		fieldSeqNumTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_SEQ_NUM);
		fieldMinValueTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_MIN_VALUE);
		fieldMaxValueTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_MAX_VALUE);
		fieldDiscreteValuesTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_DISCRETE_VALUES);
		
		// Initialise Drop Down Choices
		initFieldTypeDdc();

		attachValidators();
		addComponents();
	}

	private void attachValidators()
	{
		fieldNameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.phenotypic.name.required", this, new Model<String>("Name")));;
		fieldTypeDdc.setRequired(true).setLabel(new StringResourceModel("error.phenotypic.fieldType.required", this, new Model<String>("Field Type")));;;
	}

	private void addComponents()
	{
		detailFormContainer.add(fieldIdTxtFld);
		detailFormContainer.add(fieldNameTxtFld);
		detailFormContainer.add(fieldDescriptionTxtAreaFld);
		detailFormContainer.add(fieldTypeDdc);
		detailFormContainer.add(fieldSeqNumTxtFld);
		detailFormContainer.add(fieldUnitsTxtFld);
		detailFormContainer.add(fieldMinValueTxtFld);
		detailFormContainer.add(fieldMaxValueTxtFld);
		detailFormContainer.add(fieldDiscreteValuesTxtFld);
		
		add(detailFormContainer);
		
		// View has Edit and Cancel
		viewButtonContainer.add(editButton);
		viewButtonContainer.add(editCancelButton.setDefaultFormProcessing(false));
		
		// Edit has Save, Delete and Cancel
		editButtonContainer.add(saveButton);
		editButtonContainer.add(deleteButton);
		editButtonContainer.add(cancelButton.setDefaultFormProcessing(false));
		
		// Button containers
		add(viewButtonContainer);
		add(editButtonContainer);
	}

	protected void onSave(FieldVO fieldVo, AjaxRequestTarget target)
	{

	}

	protected void onCancel(AjaxRequestTarget target)
	{

	}
	
	protected void onEdit(FieldVO fieldVo, AjaxRequestTarget target)
	{

	}
	
	protected void onDelete(FieldVO fieldVo, AjaxRequestTarget target)
	{
		
	}

	protected void processErrors(AjaxRequestTarget target)
	{

	}

	public TextField<String> getFieldIdTxtFld()
	{
		return fieldIdTxtFld;
	}

	public void setfieldIdTxtFld(TextField<String> fieldIdTxtFld)
	{
		this.fieldIdTxtFld = fieldIdTxtFld;
	}
	
	public TextField<String> getFieldNameTxtFld()
	{
		return fieldNameTxtFld;
	}

	public void setFieldNameTxtFld(TextField<String> fieldNameTxtFld)
	{
		this.fieldNameTxtFld = fieldNameTxtFld;
	}
	
	public AjaxButton getEditButton()
	{
		return editButton;
	}

	public void setEditButton(AjaxButton editButton)
	{
		this.editButton = editButton;
	}

	public AjaxButton getEditCancelButton()
	{
		return editCancelButton;
	}

	public void setEditCancelButton(AjaxButton editCancelButton)
	{
		this.editCancelButton = editCancelButton;
	}

	public AjaxButton getDeleteButton()
	{
		return deleteButton;
	}

	public void setDeleteButton(AjaxButton deleteButton)
	{
		this.deleteButton = deleteButton;
	}

	public AjaxButton getSaveButton()
	{
		return saveButton;
	}

	public void setSaveButton(AjaxButton saveButton)
	{
		this.saveButton = saveButton;
	}

	public AjaxButton getCancelButton()
	{
		return cancelButton;
	}

	public void setCancelButton(AjaxButton cancelButton)
	{
		this.cancelButton = cancelButton;
	}
}