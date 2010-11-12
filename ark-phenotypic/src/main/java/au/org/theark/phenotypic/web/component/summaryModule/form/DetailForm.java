/**
 * 
 * This is a new file
 *
 *
 */
package au.org.theark.phenotypic.web.component.summaryModule.form;

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

import au.org.theark.phenotypic.model.entity.Status;
import au.org.theark.phenotypic.model.vo.CollectionVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.phenoCollection.Detail;

/**
 * @author nivedann
 * 
 */
@SuppressWarnings( { "serial", "unchecked", "unused" })
public class DetailForm extends Form<CollectionVO>
{
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService				phenotypicService;

	private WebMarkupContainer				resultListContainer;
	private WebMarkupContainer				detailPanelContainer;
	private WebMarkupContainer				detailFormContainer;
	private WebMarkupContainer 			viewButtonContainer;
	private WebMarkupContainer 			editButtonContainer;
	private ContainerForm					phenoCollectionContainerForm;

	private int									mode;

	private TextField<String>				idTxtFld;
	private TextField<String>				nameTxtFld;
	private DropDownChoice<Status>		statusDdc;
	private TextArea<String>				descriptionTxtAreaFld;
	private TextField<String>				startDateTxtFld;
	private TextField<String>				expiryDateTxtFld;

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
		this.phenoCollectionContainerForm = containerForm;
		this.resultListContainer = listContainer;
		this.detailPanelContainer = detailsContainer;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.detailFormContainer = detailFormContainer;
		
		editButton = new AjaxButton(au.org.theark.core.Constants.EDIT, new StringResourceModel("editKey", this, null))
		{

			public void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				onEdit(phenoCollectionContainerForm.getModelObject(), target);
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
				onSave(phenoCollectionContainerForm.getModelObject(), target);
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
				onDelete(phenoCollectionContainerForm.getModelObject(), target);
				target.addComponent(detailPanelContainer);
			}

			public void onError(AjaxRequestTarget target, Form<?> form)
			{
				processErrors(target);
			}
		};
	}
	
	private void initStatusDdc()
	{
		 java.util.Collection<Status> statusCollection = phenotypicService.getStatus();
		 ChoiceRenderer statusRenderer = new ChoiceRenderer(au.org.theark.phenotypic.web.Constants.STATUS_NAME, au.org.theark.phenotypic.web.Constants.STATUS_ID);
		 statusDdc = new DropDownChoice<Status>(au.org.theark.phenotypic.web.Constants.COLLECTIONVO_COLLECTION_STATUS, (List) statusCollection, statusRenderer);
	}

	public void initialiseForm()
	{
		idTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.COLLECTIONVO_COLLECTION_ID);
		nameTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.COLLECTIONVO_COLLECTION_NAME);
		descriptionTxtAreaFld = new TextArea<String>(au.org.theark.phenotypic.web.Constants.COLLECTIONVO_COLLECTION_DESCRIPTION);
		startDateTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.COLLECTIONVO_COLLECTION_START_DATE);
		expiryDateTxtFld = new TextField<String>(au.org.theark.phenotypic.web.Constants.COLLECTIONVO_COLLECTION_EXPIRY_DATE);
		
		// Initialise Drop Down Choices
		initStatusDdc();

		attachValidators();
		addComponents();
	}

	private void attachValidators()
	{
		nameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.phenoCollection.name.required", this, new Model<String>("Name")));;
		statusDdc.setRequired(true).setLabel(new StringResourceModel("error.phenoCollection.status.required", this, new Model<String>("Status")));;;
	}

	private void addComponents()
	{
		detailFormContainer.add(idTxtFld);
		detailFormContainer.add(nameTxtFld);
		detailFormContainer.add(descriptionTxtAreaFld);
		detailFormContainer.add(statusDdc);
		detailFormContainer.add(startDateTxtFld);
		detailFormContainer.add(expiryDateTxtFld);
		
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

	protected void onSave(CollectionVO collectionVo, AjaxRequestTarget target)
	{

	}

	protected void onCancel(AjaxRequestTarget target)
	{

	}
	
	protected void onEdit(CollectionVO collectionVo, AjaxRequestTarget target)
	{

	}
	
	protected void onDelete(CollectionVO collectionVo, AjaxRequestTarget target)
	{
		
	}

	protected void processErrors(AjaxRequestTarget target)
	{

	}
	
	public TextField<String> getIdTxtFld()
	{
		return idTxtFld;
	}

	public void setIdTxtFld(TextField<String> idTxtFld)
	{
		this.idTxtFld = idTxtFld;
	}

	public TextField<String> getNameTxtFld()
	{
		return nameTxtFld;
	}

	public void setNameTxtFld(TextField<String> nameTxtFld)
	{
		this.nameTxtFld = nameTxtFld;
	}

	public DropDownChoice<Status> getStatusDdc()
	{
		return statusDdc;
	}

	public void setStatusDdc(DropDownChoice<Status> statusDdc)
	{
		this.statusDdc = statusDdc;
	}

	public TextArea<String> getDescriptionTxtAreaFld()
	{
		return descriptionTxtAreaFld;
	}

	public void setDescriptionTxtAreaFld(TextArea<String> descriptionTxtAreaFld)
	{
		this.descriptionTxtAreaFld = descriptionTxtAreaFld;
	}

	public TextField<String> getStartDateTxtFld()
	{
		return startDateTxtFld;
	}

	public void setStartDateTxtFld(TextField<String> startDateTxtFld)
	{
		this.startDateTxtFld = startDateTxtFld;
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