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
package au.org.theark.core.web.form;

import java.util.Iterator;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.button.AjaxDeleteButton;

/**
 * <p>
 * Abstract class for Detail Form sub-classes. It provides some common functionality that sub-classes inherit. Provides the skeleton methods for
 * onSave,onDelete,onCancel etc.Defines the core buttons like save,delete,cancel, edit and editCancel. Provides method to toggle the view from read
 * only to edit mode which is usually a common behavior the sub-classes can re-use.
 * </p>
 * 
 * @author nivedann
 * @param <T>
 * 
 */
public abstract class AbstractDetailForm<T> extends Form<T> {
	/**
	 * 
	 */
	private static final long		serialVersionUID	= -1818001425244673832L;
	protected FeedbackPanel			feedBackPanel;
	protected Form<T>					containerForm;

	protected AjaxButton				saveButton;
	protected AjaxButton				cancelButton;
	protected AjaxButton				deleteButton;
	protected AjaxButton				editButton;
	protected AjaxButton				editCancelButton;

	// Add a visitor class for required field marking/validation/highlighting
	protected ArkFormVisitor		formVisitor			= new ArkFormVisitor();

	// Use this for the model where WebMarkupContainers are set inside this VO
	protected ArkCrudContainerVO	arkCrudContainerVO;
	protected CompoundPropertyModel<T> cpModel;
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;

	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param containerForm
	 * @param arkCrudContainerVO
	 */
	public AbstractDetailForm(String id, FeedbackPanel feedBackPanel, Form<T> containerForm, ArkCrudContainerVO arkCrudContainerVO) {
		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.containerForm = containerForm;
		this.feedBackPanel = feedBackPanel;
		initialiseForm();
	}
	
	/**
	 * 
	 * @param id
	 * @param feedBackPanel
	 * @param containerForm
	 * @param arkCrudContainerVO
	 */
	public AbstractDetailForm(String id, FeedbackPanel feedBackPanel, CompoundPropertyModel<T> cpModel, ArkCrudContainerVO arkCrudContainerVO) {
		super(id, cpModel);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.cpModel = cpModel;
		this.feedBackPanel = feedBackPanel;
		initialiseForm();
	}

	/**
	 * Initialise method that is specific to classes that follow the CrudContainerVO Pattern. The code related to each function has been modularised
	 * into protected methods, this is to provide the subclasses to refer to the protected methods without having to re-create/duplicate them when they
	 * extend the classes.
	 * 
	 * @param isArkCrudContainerVOPattern
	 */
	protected void initialiseForm() {
		cancelButton = new AjaxButton(Constants.CANCEL, new StringResourceModel("page.cancel", this, null)) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1684005199059571017L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if (isNew()) {
					editCancelProcess(target);
				}
				else {
					editCancelProcessForUpdate(target);
				}
			}

			@Override
			protected void onError(AjaxRequestTarget arg0, Form<?> arg1) {
				
			}
		};

		saveButton = new AjaxButton(Constants.SAVE, new StringResourceModel("page.save", this, null)) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -423605230448635419L;

			@Override
			public boolean isVisible() {
				// calling super.isVisible() will allow an external setVisible() to override visibility
				return super.isVisible() && ArkPermissionHelper.isActionPermitted(Constants.SAVE);
			}

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onSave(containerForm, target);
				target.add(arkCrudContainerVO.getDetailPanelContainer());
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> arg1) {
				saveOnErrorProcess(target);
				
			}
		};

		deleteButton = new AjaxDeleteButton(Constants.DELETE, new StringResourceModel("confirmDelete", this, null), new StringResourceModel(Constants.DELETE, this, null)) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 4005032637149080009L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onDeleteConfirmed(target, null);
			}

			@Override
			public boolean isVisible() {
				// calling super.isVisible() will allow an external setVisible() to override visibility
				return super.isVisible() && ArkPermissionHelper.isActionPermitted(Constants.DELETE);
			}

			@Override
			protected void onError(AjaxRequestTarget arg0, Form<?> arg1) {
				
			}

		};
		addComponentsToForm();
	}

	/**
	 * Add all the components to the Detail form Overloaded for when sub-classes use the ArkCrudContainerVO pattern
	 * 
	 * @param isArkCrudContainerVoPattern
	 */
	protected void addComponentsToForm() {
		arkCrudContainerVO.getEditButtonContainer().addOrReplace(saveButton);
		arkCrudContainerVO.getEditButtonContainer().addOrReplace(cancelButton.setDefaultFormProcessing(false));
		arkCrudContainerVO.getEditButtonContainer().addOrReplace(deleteButton.setDefaultFormProcessing(false));
		add(arkCrudContainerVO.getDetailPanelFormContainer());
		add(arkCrudContainerVO.getEditButtonContainer());
	}

	public void onBeforeRender() {
		super.onBeforeRender();
		visitChildren(formVisitor);
		Long arkFunctionId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_FUNCTION_KEY);
		ArkFunction arkFunction  = iArkCommonService.getArkFunctionById(arkFunctionId);
		
		if(arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_LIMS_SUBJECT)){
			
			ArkCRUDHelper.onBeforeRenderWithCRDPermissions(arkCrudContainerVO,arkFunction);
			
		}else{
			SecurityManager securityManager = ThreadContext.getSecurityManager();
			Subject currentUser = SecurityUtils.getSubject();
			if( ArkPermissionHelper.hasEditPermission(securityManager,currentUser) || //User can UPDATE
				ArkPermissionHelper.hasNewPermission(securityManager, currentUser) || //User can CREATE
				ArkPermissionHelper.hasDeletePermission(securityManager, currentUser)){ //User can DELETE
				
				//If the logged in user has Create,Update Or Delete then by-pass the View/Read Only Screen and show the Edit Screen
				ArkCRUDHelper.onBeforeRenderWithCRDPermissions(arkCrudContainerVO);
			
			}else{
				
				ArkCRUDHelper.onBeforeRenderWithReadPermission(arkCrudContainerVO);
			}
			
		}
		
		
	}
	
	public void determineViewOrEditMode(){
		
		
	}

	/**
	 * Implement this to add all the form components/objects
	 */
	protected void addFormComponents() {
		add(saveButton);
		add(cancelButton.setDefaultFormProcessing(false));
	}

	protected void editCancelProcessForUpdate(AjaxRequestTarget target) {
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
		arkCrudContainerVO.getDetailPanelContainer().setVisible(false);
		target.add(arkCrudContainerVO.getSearchResultPanelContainer());
		target.add(arkCrudContainerVO.getDetailPanelContainer());
		onCancelPostProcess(target);
	}

	/**
	 * Method to handle Save action, and handle for any errors, placing forcus on any component in error
	 * 
	 * @param target
	 */
	@SuppressWarnings("unchecked")
	protected void saveOnErrorProcess(AjaxRequestTarget target) {
		boolean setFocusError = false;
		WebMarkupContainer wmc = arkCrudContainerVO.getDetailPanelContainer();
		for (Iterator iterator = wmc.iterator(); iterator.hasNext();) {
			Component component = (Component) iterator.next();
			if (component instanceof FormComponent) {
				FormComponent formComponent = (FormComponent) component;

				if (!formComponent.isValid()) {
					if (!setFocusError) {
						// Place focus on field in error (for the first field in error)
						target.focusComponent(formComponent);
						setFocusError = true;
					}
				}
			}
		}

		processErrors(target);
	}

	/**
	 * Method to handle Edit action, enabling the Detail form, and hiding the "view" button container
	 * 
	 * @param target
	 */
	protected void editButtonProcess(AjaxRequestTarget target) {
		deleteButton.setEnabled(true);
		// The visibility of the delete button should not be changed from
		// any of the abstract classes. This allows the implementation
		// to control the visibility of the delete button.
		// NB: SearchForm onNew has the Delete button's setEnabled(false)
		// deleteButton.setVisible(true);
		arkCrudContainerVO.getViewButtonContainer().setVisible(false);
		arkCrudContainerVO.getEditButtonContainer().setVisible(true);
		arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(true);
		target.add(arkCrudContainerVO.getViewButtonContainer());
		target.add(arkCrudContainerVO.getEditButtonContainer());
		target.add(arkCrudContainerVO.getDetailPanelFormContainer());
	}


	/**
	 * Method to handle Cancel action when in Edit mode, disabling the Detail form, and hiding the "edit" button container Overloaded
	 * onCancelPostProcess. Use this when you use the ArkCrudContainerVO to manage the WebMarkupContainers.
	 * 
	 * @param target
	 * @param isArkCrudContainerVOPattern
	 */
	protected void onCancelPostProcess(AjaxRequestTarget target) {
		arkCrudContainerVO.getDetailPanelContainer().setVisible(false);//Go to search page/results
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
		arkCrudContainerVO.getSearchPanelContainer().setVisible(true);

		target.add(feedBackPanel);
		target.add(arkCrudContainerVO.getSearchPanelContainer());
		target.add(arkCrudContainerVO.getSearchResultPanelContainer());
		target.add(arkCrudContainerVO.getDetailPanelContainer());
		target.add(arkCrudContainerVO.getDetailPanelFormContainer());
		target.add(arkCrudContainerVO.getEditButtonContainer());
		onCancel(target);
	}

	/**
	 * Method to handle the EditCancel/New Search action, hiding the Detail form/panel and showing the Search/Results panels Overloaded method for
	 * sub-classes that implement the ArkCrudContainerVO pattern
	 * 
	 * @param target
	 */
	protected void editCancelProcess(AjaxRequestTarget target) {
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
		arkCrudContainerVO.getDetailPanelContainer().setVisible(false);
		arkCrudContainerVO.getSearchPanelContainer().setVisible(true);

		target.add(feedBackPanel);
		target.add(arkCrudContainerVO.getSearchPanelContainer());
		target.add(arkCrudContainerVO.getDetailPanelContainer());
		target.add(arkCrudContainerVO.getSearchResultPanelContainer());
		onCancel(target);
	}

	/**
	 * A helper method that will allow the toggle of panels and buttons. This method can be invoked by sub-classes as part of the onSave()
	 * implementation.Once the user has pressed Save either to create a new entity or update, invoking this method will place the new/edited record
	 * panel in View/Read only mode. Overloaded method for sub-classes that implement the ArkCrudContainerVO pattern
	 * @param target
	 */
	protected void onSavePostProcess(AjaxRequestTarget target) {
		
		if(ArkPermissionHelper.isActionPermitted(Constants.DELETE)){
			AjaxButton ajaxButton = (AjaxButton) arkCrudContainerVO.getEditButtonContainer().get("delete");
			if (ajaxButton != null) {
				ajaxButton.setEnabled(true);
				target.add(ajaxButton);
			}
		}
		
		// Visibility
		arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
		arkCrudContainerVO.getSearchPanelContainer().setVisible(false);
		arkCrudContainerVO.getEditButtonContainer().setVisible(false);
		// Enable
		arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(false);
		target.add(arkCrudContainerVO.getSearchResultPanelContainer());
		target.add(arkCrudContainerVO.getDetailPanelContainer());
		target.add(arkCrudContainerVO.getDetailPanelFormContainer());
		target.add(arkCrudContainerVO.getSearchPanelContainer());
		target.add(arkCrudContainerVO.getEditButtonContainer());
		target.add(feedBackPanel);
	}
	

	/**
	 * Method that sub-classes may call to disable the entire Detail form, and display a reason Overloaded method for those sub-classes that implement
	 * the ArkCrudContainerVO pattern
	 * 
	 * @param sessionId
	 * @param errorMessage
	 * @param arkCrudContainerVO
	 */
	protected void disableDetailForm(Long sessionId, String errorMessage) {
		if (ArkPermissionHelper.isModuleFunctionAccessPermitted()) {
			if (sessionId == null) {
				arkCrudContainerVO.getDetailPanelContainer().setEnabled(false);
				this.error(errorMessage);
			}
			else {
				arkCrudContainerVO.getDetailPanelContainer().setEnabled(true);
			}
		}
		else {
			arkCrudContainerVO.getDetailPanelContainer().setEnabled(false);
			this.error(au.org.theark.core.Constants.MODULE_NOT_ACCESSIBLE_MESSAGE);
		}
	}

	/**
	 * Abstract method for sub-classes to implement their own validation
	 */
	abstract protected void attachValidators();

	/**
	 * Abstract method for sub-classes to implement their own Cancel action
	 * 
	 * @param target
	 */
	abstract protected void onCancel(AjaxRequestTarget target);

	/**
	 * Abstract method for sub-classes to implement their own Save action
	 * 
	 * @param containerForm
	 * @param target
	 */
	abstract protected void onSave(Form<T> containerForm, AjaxRequestTarget target);

	/**
	 * Abstract method for sub-classes to implement their own Delete action
	 * 
	 * @param target
	 * @param selection
	 * @param selectModalWindow
	 */
	abstract protected void onDeleteConfirmed(AjaxRequestTarget target, String selection);

	/**
	 * Abstract method for sub-classes to implement their own processing of any errors
	 * 
	 * @param target
	 */
	abstract protected void processErrors(AjaxRequestTarget target);

	/**
	 * Abstract method for sub-classes to implement, to determine if a the form refers to a new object/entity
	 * 
	 * @return
	 */
	abstract protected boolean isNew();
	
	/**
	 * Delegates this to the subclass to add the Form components defined in it.This enforces this method across all sub-classes and keeps it consistent.
	 */
	abstract protected void addDetailFormComponents();
}
