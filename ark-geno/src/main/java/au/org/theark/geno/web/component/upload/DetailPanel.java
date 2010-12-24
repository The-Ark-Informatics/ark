package au.org.theark.geno.web.component.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.io.IOUtils;

import au.org.theark.geno.model.entity.FileFormat;
import au.org.theark.geno.model.vo.UploadCollectionVO;
import au.org.theark.geno.service.IGenoService;
import au.org.theark.geno.web.component.upload.form.ContainerForm;


@SuppressWarnings("serial")
public class DetailPanel extends Panel {
	
	private UploadContainerPanel uploadContainerPanel;
	private ContainerForm containerForm;
	private DetailForm detailForm;

	public DetailPanel(	String id,
					UploadContainerPanel genoCollectionContainerPanel,
					ContainerForm containerForm)
	{
		super(id);
		this.uploadContainerPanel = genoCollectionContainerPanel;
		this.containerForm = containerForm;
	}

	/**
	 * NB: Call this after the a new DetailPanel, but not within its constructor
	 */
	public void initialisePanel()
	{
		detailForm = new DetailForm("detailForm");
		
		detailForm.initialiseDetailForm();
		add(detailForm);
	}

	/*
	 * DetailForm inner class
	 */
	protected class DetailForm extends Form<UploadCollectionVO>
	{

		@SpringBean(name = au.org.theark.geno.service.Constants.GENO_SERVICE)
		private IGenoService genoService;
		

		private int mode;
		
		private WebMarkupContainer detailPanelFormContainer;
		private WebMarkupContainer viewButtonContainer;
		private WebMarkupContainer editButtonContainer;
		
		private AjaxButton saveButton;
		private AjaxButton cancelButton;
		private AjaxButton deleteButton;
		private AjaxButton editButton;
		private AjaxButton editCancelButton;

		private ModalWindow selectModalWindow;
		
		private TextField<String> idTxtFld;
		private FileUploadField fileUploadField;
//		private TextField<String> filenameTxtFld;
		private DropDownChoice<FileFormat> fileFormatDdc;
//		private TextArea<String> descriptionTxtAreaFld;
//		private DatePicker<Date> startDateTxtFld;
//		private DatePicker<Date> expiryDateTxtFld;

		/**
		 * Constructor
		 * @param id
		 */
		public DetailForm(String id)
		{
			super(id);
		}

		/**
		 * NB: Call this after the a new DetailForm, but not within its constructor 
		 */
		public void initialiseDetailForm()
		{
			initialiseForm();
			
			idTxtFld = new TextField<String>(au.org.theark.geno.service.Constants.UPLOADCOLLECTION_VO_ID);
			fileUploadField = new FileUploadField(au.org.theark.geno.service.Constants.UPLOADCOLLECTION_VO_UPLOAD_FILENAME);
//			filenameTxtFld = new TextField<String>(au.org.theark.geno.service.Constants.UPLOADCOLLECTION_VO_UPLOAD_FILENAME);
//			descriptionTxtAreaFld = new TextArea<String>(au.org.theark.geno.service.Constants.GENO_COLLECTION_VO_DESCRIPTION);
//			startDateTxtFld = new DatePicker<Date>(au.org.theark.geno.service.Constants.GENO_COLLECTION_VO_START_DATE);
//			expiryDateTxtFld = new DatePicker<Date>(au.org.theark.geno.service.Constants.GENO_COLLECTION_VO_EXPIRY_DATE);

//			startDateTxtFld.setDateFormat(au.org.theark.core.Constants.DATE_PICKER_DD_MM_YY);
//			expiryDateTxtFld.setDateFormat(au.org.theark.core.Constants.DATE_PICKER_DD_MM_YY);

			// Initialise Drop Down Choices
			initUploadFileFormatDdc();

			attachValidators();
			addComponents();
		}
		
		protected void initialiseForm() {
			//Contains the controls of the details
			detailPanelFormContainer = new WebMarkupContainer("detailFormContainer");
			detailPanelFormContainer.setOutputMarkupPlaceholderTag(true);
			detailPanelFormContainer.setEnabled(false);
			
			/* Defines a Read-Only Mode */
			viewButtonContainer = new WebMarkupContainer("viewButtonContainer");
			viewButtonContainer.setOutputMarkupPlaceholderTag(true);
			viewButtonContainer.setVisible(false);
			
			/* Defines a edit mode */
			editButtonContainer = new WebMarkupContainer("editButtonContainer");
			editButtonContainer.setOutputMarkupPlaceholderTag(true);
			editButtonContainer.setVisible(false);
			
			uploadContainerPanel.setDetailPanelFormContainer(detailPanelFormContainer);
			uploadContainerPanel.setViewButtonContainer(viewButtonContainer);
			uploadContainerPanel.setEditButtonContainer(editButtonContainer);
			
			cancelButton = new AjaxButton(au.org.theark.core.Constants.CANCEL,  new StringResourceModel("cancelKey", this, null))
			{

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					
					uploadContainerPanel.showSearch(target);
					onCancel(target);//Invoke a onCancel() that the sub-class can use to build anything more specific
				}
			};
			
			saveButton = new AjaxButton(au.org.theark.core.Constants.SAVE, new StringResourceModel("saveKey", this, null))
			{
				public void onSubmit(AjaxRequestTarget target, Form<?> form) {
					onSave(containerForm, target);
					uploadContainerPanel.refreshDetail(target);
				}
				
				public void onError(AjaxRequestTarget target, Form<?> form){
					processErrors(target);
				}
			};
			
			deleteButton = new AjaxButton(au.org.theark.core.Constants.DELETE, new StringResourceModel("deleteKey", this, null))
			{
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					uploadContainerPanel.refreshDetail(target);
					showConfirmModalWindow(target);
				}
			};
			
			editButton = new AjaxButton(au.org.theark.core.Constants.EDIT, new StringResourceModel("editKey", this, null))
			{
				public void onSubmit(AjaxRequestTarget target, Form<?> form)
				{
					uploadContainerPanel.showEditDetail(target);
				}
				
				public void onError(AjaxRequestTarget target, Form<?> form){
					processErrors(target);
				}
			};
			
			editCancelButton = new AjaxButton(au.org.theark.core.Constants.EDIT_CANCEL, new StringResourceModel("editCancelKey", this, null))
			{
				public void onSubmit(AjaxRequestTarget target, Form<?> form)
				{	
					onCancel(target);
				}
				public void onError(AjaxRequestTarget target, Form<?> form){
					processErrors(target);
				}
			};
			
			uploadContainerPanel.setDetailPanelButtons(saveButton,
																cancelButton,
																deleteButton,
																editButton,
																editCancelButton);

			selectModalWindow = initialiseModalWindow();

			addComponentsToForm();
		}
		
		protected void addComponentsToForm(){
			
			detailPanelFormContainer.add(selectModalWindow);
			add(detailPanelFormContainer);

			editButtonContainer.add(saveButton);
			editButtonContainer.add(cancelButton.setDefaultFormProcessing(false));
			editButtonContainer.add(deleteButton);
			
			viewButtonContainer.add(editButton);
			viewButtonContainer.add(editCancelButton.setDefaultFormProcessing(false));
			
			add(editButtonContainer);
			add(viewButtonContainer);
			
		}

		protected void attachValidators()
		{
//			filenameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.genoCollection.name.required", this, new Model<String>("Name")));
			fileFormatDdc.setRequired(true).setLabel(new StringResourceModel("error.genoCollection.status.required", this, new Model<String>("Status")));
		}

		private void addComponents()
		{
			detailPanelFormContainer.add(idTxtFld.setEnabled(false));
			detailPanelFormContainer.add(fileUploadField);
//			detailPanelFormContainer.add(filenameTxtFld);
//			detailPanelFormContainer.add(descriptionTxtAreaFld);
			detailPanelFormContainer.add(fileFormatDdc);
//			detailPanelFormContainer.add(startDateTxtFld);
//			detailPanelFormContainer.add(expiryDateTxtFld);

			add(detailPanelFormContainer);
		}

		private void initUploadFileFormatDdc()
		{
			java.util.Collection<FileFormat> fileFormatCollection = genoService.getFileFormatCollection();
			ChoiceRenderer fileFormatRenderer = new ChoiceRenderer(au.org.theark.geno.service.Constants.FILEFORMAT_NAME, 
																au.org.theark.geno.service.Constants.FILEFORMAT_ID);
			fileFormatDdc = new DropDownChoice<FileFormat>(au.org.theark.geno.service.Constants.UPLOADCOLLECTION_VO_UPLOAD_FILEFORMAT, 
															(List) fileFormatCollection, fileFormatRenderer);
		}

		protected void showConfirmModalWindow(AjaxRequestTarget target){
			selectModalWindow.show(target);
			target.addComponent(selectModalWindow);
		}
		
		protected void onSave(Form<UploadCollectionVO> containerForm, AjaxRequestTarget target)
		{
			setMultiPart(true);
			
//			if (containerForm.getModelObject().getGenoCollection().getId() == null)
//			{
//				// Save a new GenoCollection
//				genoService.createCollection(containerForm.getModelObject().getGenoCollection());
//				this.info("Genotypic collection " + containerForm.getModelObject().getGenoCollection().getName() + " was created successfully");
//				processErrors(target);
//			}
//			else
//			{
//				// Update existing GenoCollection
//				genoService.updateCollection(containerForm.getModelObject().getGenoCollection());
//				this.info("Genotypic collection " + containerForm.getModelObject().getGenoCollection().getName() + " was updated successfully");
//				processErrors(target);
//			}
			
//			containerForm.getModelObject().getUpload()
			//TODO: AJAX-ified and asynchronous and hit database
			FileUpload fileUpload = fileUploadField.getFileUpload();
			File file = new File("/tmp/" + fileUpload.getClientFileName());
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				IOUtils.copy(fileUpload.getInputStream(), fos);
				fos.close();
			} catch (IOException ioe) {
				System.out.println("Failed to save the uploaded file:" + ioe);
			} finally {
				if (fos != null) {
					fos = null;
				}
			}
			
			uploadContainerPanel.showViewDetail(target);
			//TODO:(CE) To handle Business and System Exceptions here
			
			setMultiPart(false);
		}

		protected void onCancel(AjaxRequestTarget target)
		{
			// reset the collection VO
			UploadCollectionVO uploadCollectionVO = new UploadCollectionVO();
			containerForm.setModelObject(uploadCollectionVO);
			
			uploadContainerPanel.showSearch(target);
		}
		
		protected void processErrors(AjaxRequestTarget target)
		{
//			target.addComponent(feedBackPanel);
			uploadContainerPanel.refreshFeedback(target);
		}

		protected  void onDeleteConfirmed(AjaxRequestTarget target, String selection, ModalWindow selectModalWindow){
			//TODO:(CE) To handle Business and System Exceptions here
			genoService.deleteCollection(containerForm.getModelObject().getGenoCollection());
			this.info("Genotypic collection " + containerForm.getModelObject().getGenoCollection().getName() + " was deleted successfully");
	   		
		   	// Display delete confirmation message
			uploadContainerPanel.refreshFeedback(target);
		   	//TODO Implement Exceptions in PhentoypicService
			//  } catch (UnAuthorizedOperation e) { this.error("You are not authorised to manage study components for the given study " +
			//  study.getName()); processFeedback(target); } catch (ArkSystemException e) {
			//  this.error("A System error occured, we will have someone contact you."); processFeedback(target); }
	     
			// Close the confirm modal window
		   	selectModalWindow.close(target);
		   	// Move focus back to Search form
			onCancel(target);
		}
		
		/*  
		 * Confirmation modal window
		 */
		protected  void onDeleteCancel(AjaxRequestTarget target, ModalWindow selectModalWindow){
			selectModalWindow.close(target);
		}
		
		protected ModalWindow initialiseModalWindow(){
		
			// The ModalWindow, showing some choices for the user to select.
			selectModalWindow = new au.org.theark.core.web.component.SelectModalWindow("modalwindow"){

				protected void onSelect(AjaxRequestTarget target, String selection){
					onDeleteConfirmed(target,selection, selectModalWindow);
			    }
		
			    protected void onCancel(AjaxRequestTarget target){
			    	onDeleteCancel(target,selectModalWindow);
			    }
			};
			
			return selectModalWindow;

		}

	}
}