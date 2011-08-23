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
package au.org.theark.geno.web.component.genoCollection;

import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.model.geno.entity.Status;
import au.org.theark.core.web.component.ArkDatePicker;
import au.org.theark.geno.model.vo.GenoCollectionVO;
import au.org.theark.geno.service.IGenoService;
import au.org.theark.geno.web.component.genoCollection.form.ContainerForm;


@SuppressWarnings("serial")
public class DetailPanel extends Panel {
	
	private GenoCollectionContainerPanel genoCollectionContainerPanel;
	private ContainerForm containerForm;
	private DetailForm detailForm;

	public DetailPanel(	String id,
					GenoCollectionContainerPanel genoCollectionContainerPanel,
					ContainerForm containerForm)
	{
		super(id);
		this.genoCollectionContainerPanel = genoCollectionContainerPanel;
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
	protected class DetailForm extends Form<GenoCollectionVO>
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
		private TextField<String> nameTxtFld;
		private DropDownChoice<Status> statusDdc;
		private TextArea<String> descriptionTxtAreaFld;
		private DateTextField startDateTxtFld;
		private DateTextField expiryDateTxtFld;

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
			
			idTxtFld = new TextField<String>(au.org.theark.geno.service.Constants.GENO_COLLECTION_VO_ID);
			nameTxtFld = new TextField<String>(au.org.theark.geno.service.Constants.GENO_COLLECTION_VO_NAME);
			descriptionTxtAreaFld = new TextArea<String>(au.org.theark.geno.service.Constants.GENO_COLLECTION_VO_DESCRIPTION);
			startDateTxtFld = new DateTextField(au.org.theark.geno.service.Constants.GENO_COLLECTION_VO_START_DATE, Constants.DD_MM_YYYY);
			ArkDatePicker datePicker = new ArkDatePicker();
			datePicker.bind(startDateTxtFld);
			startDateTxtFld.add(datePicker);
			
			expiryDateTxtFld = new DateTextField(au.org.theark.geno.service.Constants.GENO_COLLECTION_VO_EXPIRY_DATE, Constants.DD_MM_YYYY);
			ArkDatePicker datePicker2 = new ArkDatePicker();
			datePicker2.bind(expiryDateTxtFld);
			expiryDateTxtFld.add(datePicker2);

			// Initialise Drop Down Choices
			initStatusDdc();

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
			
			genoCollectionContainerPanel.setDetailPanelFormContainer(detailPanelFormContainer);
			genoCollectionContainerPanel.setViewButtonContainer(viewButtonContainer);
			genoCollectionContainerPanel.setEditButtonContainer(editButtonContainer);
			
			cancelButton = new AjaxButton(au.org.theark.core.Constants.CANCEL,  new StringResourceModel("cancelKey", this, null))
			{

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					
//					resultListContainer.setVisible(false); //Hide the Search Result List Panel via the WebMarkupContainer
//					detailPanelContainer.setVisible(false); //Hide the Detail Panle via the WebMarkupContainer
//					target.addComponent(detailPanelContainer);//Attach the Detail WebMarkupContainer to be re-rendered using Ajax
//					target.addComponent(resultListContainer);//Attach the resultListContainer WebMarkupContainer to be re-rendered using Ajax
					genoCollectionContainerPanel.showSearch(target);
					onCancel(target);//Invoke a onCancel() that the sub-class can use to build anything more specific
				}
			};
			
			saveButton = new AjaxButton(au.org.theark.core.Constants.SAVE, new StringResourceModel("saveKey", this, null))
			{
				public void onSubmit(AjaxRequestTarget target, Form<?> form) {
					onSave(containerForm, target);
//					target.addComponent(detailPanelContainer);
					genoCollectionContainerPanel.refreshDetail(target);
				}
				
				public void onError(AjaxRequestTarget target, Form<?> form){
					processErrors(target);
				}
			};
			
			deleteButton = new AjaxButton(au.org.theark.core.Constants.DELETE, new StringResourceModel("deleteKey", this, null))
			{
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
//					target.addComponent(detailPanelContainer);
					genoCollectionContainerPanel.refreshDetail(target);
					showConfirmModalWindow(target);
				}
				
				public void onError(AjaxRequestTarget target, Form<?> form){
					processErrors(target);
				}
				
			};
			
			editButton = new AjaxButton(au.org.theark.core.Constants.EDIT, new StringResourceModel("editKey", this, null))
			{
				public void onSubmit(AjaxRequestTarget target, Form<?> form)
				{
//					deleteButton.setEnabled(true);
//					deleteButton.setVisible(true);
//					viewButtonContainer.setVisible(false);
//					editButtonContainer.setVisible(true);
//					detailPanelFormContainer.setEnabled(true);
//					target.addComponent(viewButtonContainer);
//					target.addComponent(editButtonContainer);
//					target.addComponent(detailPanelFormContainer);
					
					genoCollectionContainerPanel.showEditDetail(target);
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
			
			genoCollectionContainerPanel.setDetailPanelButtons(saveButton,
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
			editButtonContainer.add(deleteButton.setDefaultFormProcessing(false));
			
			viewButtonContainer.add(editButton);
			viewButtonContainer.add(editCancelButton.setDefaultFormProcessing(false));
			
			add(editButtonContainer);
			add(viewButtonContainer);
			
		}

		protected void attachValidators()
		{
			nameTxtFld.setRequired(true).setLabel(new StringResourceModel("error.genoCollection.name.required", this, new Model<String>("Name")));
			statusDdc.setRequired(true).setLabel(new StringResourceModel("error.genoCollection.status.required", this, new Model<String>("Status")));
		}

		private void addComponents()
		{
			detailPanelFormContainer.add(idTxtFld.setEnabled(false));
			detailPanelFormContainer.add(nameTxtFld);
			detailPanelFormContainer.add(descriptionTxtAreaFld);
			detailPanelFormContainer.add(statusDdc);
			detailPanelFormContainer.add(startDateTxtFld);
			detailPanelFormContainer.add(expiryDateTxtFld);

			add(detailPanelFormContainer);
		}

		private void initStatusDdc()
		{
			java.util.Collection<Status> statusCollection = genoService.getStatusCollection();
			ChoiceRenderer statusRenderer = new ChoiceRenderer(au.org.theark.geno.service.Constants.STATUS_NAME, 
																au.org.theark.geno.service.Constants.STATUS_ID);
			statusDdc = new DropDownChoice<Status>(au.org.theark.geno.service.Constants.GENO_COLLECTION_VO_STATUS, (List) statusCollection, statusRenderer);
		}
		
		protected void showConfirmModalWindow(AjaxRequestTarget target){
			selectModalWindow.show(target);
			target.addComponent(selectModalWindow);
		}
		
		protected void onSave(Form<GenoCollectionVO> containerForm, AjaxRequestTarget target)
		{

			if (containerForm.getModelObject().getGenoCollection().getId() == null)
			{
				// Save a new GenoCollection
				genoService.createCollection(containerForm.getModelObject().getGenoCollection());
				this.info("Genotypic collection " + containerForm.getModelObject().getGenoCollection().getName() + " was created successfully");
				processErrors(target);
			}
			else
			{
				// Update existing GenoCollection
				genoService.updateCollection(containerForm.getModelObject().getGenoCollection());
				this.info("Genotypic collection " + containerForm.getModelObject().getGenoCollection().getName() + " was updated successfully");
				processErrors(target);
			}
			
//			onSavePostProcess(target);
			genoCollectionContainerPanel.showViewDetail(target);
			//TODO:(CE) To handle Business and System Exceptions here
		}

		protected void onCancel(AjaxRequestTarget target)
		{
			// reset the collection VO
			GenoCollectionVO genoCollectionVO = new GenoCollectionVO();
			containerForm.setModelObject(genoCollectionVO);
			
//			onCancelPostProcess(target);	//very similar to the required behaviour of the normal cancel
			genoCollectionContainerPanel.showSearch(target);
		}
		
		protected void processErrors(AjaxRequestTarget target)
		{
//			target.addComponent(feedBackPanel);
			genoCollectionContainerPanel.refreshFeedback(target);
		}

		protected  void onDeleteConfirmed(AjaxRequestTarget target, String selection, ModalWindow selectModalWindow){
			//TODO:(CE) To handle Business and System Exceptions here
			genoService.deleteCollection(containerForm.getModelObject().getGenoCollection());
			this.info("Genotypic collection " + containerForm.getModelObject().getGenoCollection().getName() + " was deleted successfully");
	   		
		   	// Display delete confirmation message
//		   	target.addComponent(feedBackPanel);
			genoCollectionContainerPanel.refreshFeedback(target);
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
