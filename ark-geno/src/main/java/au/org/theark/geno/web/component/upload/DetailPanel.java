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
package au.org.theark.geno.web.component.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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

import au.org.theark.core.model.geno.entity.DelimiterType;
import au.org.theark.core.model.geno.entity.FileFormat;
import au.org.theark.core.model.geno.entity.GenoCollection;
import au.org.theark.core.model.geno.entity.UploadCollection;
import au.org.theark.core.security.ArkSecurityManager;
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
		detailForm.setMultiPart(true);

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

		private TextField<String> idTxtFld;
		private FileUploadField fileUploadField;
		private DropDownChoice<FileFormat> fileFormatDdc;
		private DropDownChoice<DelimiterType> delimiterTypeDdc;

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

			// Initialise Drop Down Choices
			initUploadDropDownChoices();

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

				@Override
				protected void onError(AjaxRequestTarget arg0, Form<?> arg1) {
					// TODO Auto-generated method stub
					
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
					//TODO Show Modal Window To Confirm
				}
				
				public void onError(AjaxRequestTarget target, Form<?> form) {
					processErrors(target);
				}
			};
			
			editButton = new AjaxButton(au.org.theark.core.Constants.EDIT, new StringResourceModel("editKey", this, null))
			{
				public void onSubmit(AjaxRequestTarget target, Form<?> form)
				{
//					fileUploadField.setRequired(false);
//					fileUploadField.setEnabled(false);
//					fileUploadField.setVisible(false);
					//target.addComponent(fileUploadField);
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

			addComponentsToForm();
		}
		
		protected void addComponentsToForm(){
			
			add(detailPanelFormContainer);

			editButtonContainer.add(saveButton);
			editButtonContainer.add(cancelButton.setDefaultFormProcessing(false));
			editButtonContainer.add(deleteButton.setDefaultFormProcessing(false));
			
			viewButtonContainer.add(editButton);
			viewButtonContainer.add(editCancelButton.setDefaultFormProcessing(false));
			
			add(editButtonContainer);
			add(viewButtonContainer);
			
		}

		protected void attachValidators()
		{
			fileUploadField.setRequired(true).setLabel(new StringResourceModel("error.uploadCollection.filename.required", this, new Model<String>("Filename")));
			fileFormatDdc.setRequired(true).setLabel(new StringResourceModel("error.uploadCollection.fileFormat.required", this, new Model<String>("File Format")));
			delimiterTypeDdc.setRequired(true).setLabel(new StringResourceModel("error.uploadCollection.delimiterType.required", this, new Model<String>("Delimiter")));
		}

		private void addComponents()
		{
			detailPanelFormContainer.add(idTxtFld.setEnabled(false));
			detailPanelFormContainer.add(fileUploadField);
			detailPanelFormContainer.add(fileFormatDdc);
			detailPanelFormContainer.add(delimiterTypeDdc);
//			detailPanelFormContainer.add(startDateTxtFld);
//			detailPanelFormContainer.add(expiryDateTxtFld);

			add(detailPanelFormContainer);
		}

		private void initUploadDropDownChoices()
		{
			java.util.Collection<FileFormat> fileFormatCollection = genoService.getFileFormatCollection();
			ChoiceRenderer fileFormatRenderer = new ChoiceRenderer(au.org.theark.geno.service.Constants.FILEFORMAT_NAME, 
																au.org.theark.geno.service.Constants.FILEFORMAT_ID);
			fileFormatDdc = new DropDownChoice<FileFormat>(au.org.theark.geno.service.Constants.UPLOADCOLLECTION_VO_UPLOAD_FILEFORMAT, 
															(List) fileFormatCollection, fileFormatRenderer);

			java.util.Collection<DelimiterType> delimiterTypeCollection = genoService.getDelimiterTypeCollection();
			ChoiceRenderer delimiterTypeRenderer = new ChoiceRenderer(au.org.theark.geno.service.Constants.DELIMITERTYPE_NAME, 
																au.org.theark.geno.service.Constants.DELIMITERTYPE_ID);
			delimiterTypeDdc = new DropDownChoice<DelimiterType>(au.org.theark.geno.service.Constants.UPLOADCOLLECTION_VO_UPLOAD_DELIMITERTYPE, 
															(List) delimiterTypeCollection, delimiterTypeRenderer);
		}

		private void createDirectoryIfNeeded(String directoryName)
		{
		  File theDir = new File(directoryName);

		  // if the directory does not exist, create it
		  if (!theDir.exists())
		  {
		    System.out.println("creating directory: " + directoryName);
		    theDir.mkdir();
		  }
		}

		protected void onSave(Form<UploadCollectionVO> containerForm, AjaxRequestTarget target)
		{
			//NB: We are dealing with the GenoCollection (not Marker)

			// create new UploadCollection that is linked with the new Upload
			if (containerForm.getModelObject().getUploadCollection().getId() == null)
			{				
				// Retrieve file and store in temp folder
				//TODO: AJAX-ified and asynchronous and hit database
				FileUpload fileUpload = fileUploadField.getFileUpload();
				//TODO: Load the temporary directory from the application configuration instead
				String tempDir = System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID().toString();
				createDirectoryIfNeeded(tempDir);
				String filePath = tempDir + File.separator + fileUpload.getClientFileName();
				File file = new File(filePath);
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(file);
					IOUtils.copy(fileUpload.getInputStream(), fos);
					fos.close();
					System.out.println("Successfully stored a temporary file: " + filePath);
				} catch (IOException ioe) {
					System.out.println("Failed to save the uploaded file:" + ioe);
				} finally {
					if (fos != null) {
						fos = null;
					}
				}
				// Save a new Upload
				UploadCollection uploadCol = containerForm.getModelObject().getUploadCollection();

				Long genoColctnId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.geno.web.Constants.SESSION_GENO_COLLECTION_ID);
				GenoCollection genoCol = genoService.getCollection(genoColctnId);
				uploadCol.setCollection(genoCol);
				//set the proper filename (getUpload().getFilename() returned an address to an object)
				uploadCol.getUpload().setFilename(fileUpload.getClientFileName());
				genoService.createUploadCollection(uploadCol);

				this.info("Geno upload " + uploadCol.getUpload().getFilename() + " was created successfully");
				processErrors(target);
			}
			// Updates on Uploads should not be permitted
//			else
//			{	
//				// Update existing Upload
//				genoService.updateUpload(containerForm.getModelObject().getUpload());
//				this.info("Geno upload " + containerForm.getModelObject().getUpload().getFilename() + " was updated successfully");
//				processErrors(target);
//			}
			
						
			uploadContainerPanel.showViewDetail(target);
			//TODO:(CE) To handle Business and System Exceptions here
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

			//TODO: Must be study admin to do this!
			ArkSecurityManager arkSecurityManager = ArkSecurityManager.getInstance();
			Subject currentUser = SecurityUtils.getSubject();
			
			if (arkSecurityManager.subjectHasRole("GENO Administrator")) {
				genoService.deleteUploadCollection(containerForm.getModelObject().getUploadCollection());
				this.info("Geno upload " + containerForm.getModelObject().getUploadCollection().getUpload().getFilename() + " was deleted successfully");	
			   	// Display delete confirmation message
				uploadContainerPanel.refreshFeedback(target);
			}
			else {
				this.info("Operation not permitted - Only a Study Admin can delete an upload");	
			   	// Display insufficient privileges message
				uploadContainerPanel.refreshFeedback(target);
			}
	   		
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
		

	}
}
