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

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;

import au.org.theark.core.Constants;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.button.AjaxDeleteButton;
import au.org.theark.core.web.component.button.ArkBusyAjaxButton;

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
	protected WebMarkupContainer	resultListContainer;
	protected WebMarkupContainer	detailPanelContainer;
	protected WebMarkupContainer	searchPanelContainer;
	protected WebMarkupContainer	viewButtonContainer;
	protected WebMarkupContainer	editButtonContainer;
	protected WebMarkupContainer	detailPanelFormContainer;
	protected FeedbackPanel			feedBackPanel;
	protected Form<T>					containerForm;

	protected AjaxButton				saveButton;
	protected AjaxButton				cancelButton;
	protected AjaxButton				deleteButton;
	protected AjaxButton				editButton;
	protected AjaxButton				editCancelButton;

	protected ModalWindow			selectModalWindow;

	// Add a visitor class for required field marking/validation/highlighting
	protected ArkFormVisitor		formVisitor			= new ArkFormVisitor();

	// Use this for the model where WebMarkupContainers are set inside this VO
	protected ArkCrudContainerVO	arkCrudContainerVO;
	protected CompoundPropertyModel<T> cpModel;

	/**
	 * Constructor for AbstractDetailForm class
	 * 
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
	public AbstractDetailForm(String id, FeedbackPanel feedBackPanel, WebMarkupContainer resultListContainer, WebMarkupContainer detailPanelContainer, WebMarkupContainer detailPanelFormContainer,
			WebMarkupContainer searchPanelContainer, WebMarkupContainer viewButtonContainer, WebMarkupContainer editButtonContainer, Form<T> containerForm) {
		super(id);
		this.resultListContainer = resultListContainer;
		this.detailPanelContainer = detailPanelContainer;
		this.feedBackPanel = feedBackPanel;
		this.searchPanelContainer = searchPanelContainer;
		this.editButtonContainer = editButtonContainer;
		this.viewButtonContainer = viewButtonContainer;
		this.detailPanelFormContainer = detailPanelFormContainer;
		this.containerForm = containerForm;

		initialiseForm();
	}

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
		setMultiPart(true);// Make sure this is required.

		initialiseForm(true);// For CRUD VO Pattern
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
//		setMultiPart(true);// Make sure this is required.

		initialiseForm(true);// For CRUD VO Pattern
	}

	/**
	 * Initialise method that is specific to classes that do not follow the ArkCrudContainerVO Pattern. The code related to each function has been
	 * modularised into protected methods, this is to provide the subclasses to refer to the protected methods without having to re-create/duplicate
	 * them when they extend the classes.
	 * 
	 */
	protected void initialiseForm() {
		cancelButton = new AjaxButton(Constants.CANCEL, new StringResourceModel("cancelKey", this, null)) {
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
					resultListContainer.setVisible(false); // Hide the Search Result List Panel via the WebMarkupContainer
					detailPanelContainer.setVisible(false); // Hide the Detail Panel via the WebMarkupContainer
					target.addComponent(detailPanelContainer);// Attach the Detail WebMarkupContainer to be re-rendered using Ajax
					target.addComponent(resultListContainer);// Attach the resultListContainer WebMarkupContainer to be re-rendered using Ajax
					onCancelPostProcess(target);
				}
			}
		};

		saveButton = new ArkBusyAjaxButton(Constants.SAVE, new StringResourceModel("saveKey", this, null)) {
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
				target.addComponent(detailPanelContainer);
			}

			@SuppressWarnings("unchecked")
			public void onError(AjaxRequestTarget target, Form<?> form) {
				boolean setFocusError = false;
				WebMarkupContainer wmc = (WebMarkupContainer) form.get("detailFormContainer");
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
		};

		deleteButton = new AjaxDeleteButton(Constants.DELETE, new StringResourceModel("confirmDelete", this, null), new StringResourceModel(Constants.DELETE, this, null)) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 4005032637149080009L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onDeleteConfirmed(target, null, selectModalWindow);
			}

			@Override
			public boolean isVisible() {
				// calling super.isVisible() will allow an external setVisible() to override visibility
				return super.isVisible() && ArkPermissionHelper.isActionPermitted(Constants.DELETE);
			}
		};

		editButton = new AjaxButton("edit", new StringResourceModel("editKey", this, null)) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -6282464357368710796L;

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				deleteButton.setEnabled(true);
				viewButtonContainer.setVisible(false);
				editButtonContainer.setVisible(true);
				detailPanelFormContainer.setEnabled(true);
				target.addComponent(viewButtonContainer);
				target.addComponent(editButtonContainer);
				target.addComponent(detailPanelFormContainer);
			}

			public void onError(AjaxRequestTarget target, Form<?> form) {
				processErrors(target);
			}

			@Override
			public boolean isVisible() {
				// calling super.isVisible() will allow an external setVisible() to override visibility
				return super.isVisible() && ArkPermissionHelper.isActionPermitted(Constants.EDIT);
			}
		};

		editCancelButton = new ArkBusyAjaxButton("editCancel", new StringResourceModel("editCancelKey", this, null)) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 5457464178392550628L;

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				editCancelProcess(target);
			}

			public void onError(AjaxRequestTarget target, Form<?> form) {
				processErrors(target);
			}
		};

		selectModalWindow = initialiseModalWindow();

		addComponentsToForm();
	}

	/**
	 * Initialise method that is specific to classes that follow the CrudContainerVO Pattern. The code related to each function has been modularised
	 * into protected methods, this is to provide the subclasses to refer to the protected methods without having to re-create/duplicate them when they
	 * extend the classes.
	 * 
	 * @param isArkCrudContainerVOPattern
	 */
	protected void initialiseForm(Boolean isArkCrudContainerVOPattern) {
		cancelButton = new AjaxButton(Constants.CANCEL, new StringResourceModel("cancelKey", this, null)) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1684005199059571017L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if (isNew()) {
					editCancelProcess(target, true);
				}
				else {
					editCancelProcessForUpdate(target);
				}
			}
		};

		saveButton = new AjaxButton(Constants.SAVE, new StringResourceModel("saveKey", this, null)) {
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
				target.addComponent(arkCrudContainerVO.getDetailPanelContainer());
			}

			public void onError(AjaxRequestTarget target, Form<?> form) {
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
				onDeleteConfirmed(target, null, selectModalWindow);
			}

			@Override
			public boolean isVisible() {
				// calling super.isVisible() will allow an external setVisible() to override visibility
				return super.isVisible() && ArkPermissionHelper.isActionPermitted(Constants.DELETE);
			}
		};

		editButton = new AjaxButton("edit", new StringResourceModel("editKey", this, null)) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -6282464357368710796L;

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				editButtonProcess(target);
			}

			public void onError(AjaxRequestTarget target, Form<?> form) {
				processErrors(target);
			}

			@Override
			public boolean isVisible() {
				// calling super.isVisible() will allow an external setVisible() to override visibility
				return super.isVisible() && ArkPermissionHelper.isActionPermitted(Constants.EDIT);
			}
		};

		editCancelButton = new AjaxButton("editCancel", new StringResourceModel("editCancelKey", this, null)) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 5457464178392550628L;

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				editCancelProcess(target, true);
			}

			public void onError(AjaxRequestTarget target, Form<?> form) {
				processErrors(target);
			}
		};

		selectModalWindow = initialiseModalWindow();

		addComponentsToForm(true);
	}

	/**
	 * Add all the components to the Detail form
	 */
	protected void addComponentsToForm() {
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

	/**
	 * Add all the components to the Detail form Overloaded for when sub-classes use the ArkCrudContainerVO pattern
	 * 
	 * @param isArkCrudContainerVoPattern
	 */
	protected void addComponentsToForm(boolean isArkCrudContainerVoPattern) {
		// TODO: Changed from 'add' to 'addOrReplace' for components added to the editButtonContainer and viewButtonContainer
		// to support instantiating the DetailForm more than once.  Need to fix this when revising the abstraction pattern.
		// (both are only instantiated once in the top-level container as per original abstraction pattern and thus causes  
		// issues for further re-instantiations of the DetailPanel/DetailForm).
		arkCrudContainerVO.getEditButtonContainer().addOrReplace(saveButton);
		arkCrudContainerVO.getEditButtonContainer().addOrReplace(cancelButton.setDefaultFormProcessing(false));
		arkCrudContainerVO.getEditButtonContainer().addOrReplace(deleteButton.setDefaultFormProcessing(false));

		arkCrudContainerVO.getViewButtonContainer().addOrReplace(editButton);
		arkCrudContainerVO.getViewButtonContainer().addOrReplace(editCancelButton.setDefaultFormProcessing(false));

		arkCrudContainerVO.getDetailPanelFormContainer().addOrReplace(selectModalWindow);

		add(arkCrudContainerVO.getDetailPanelFormContainer());
		add(arkCrudContainerVO.getViewButtonContainer());
		add(arkCrudContainerVO.getEditButtonContainer());
	}

	@SuppressWarnings("unchecked")
	public void onBeforeRender() {
		super.onBeforeRender();
		visitChildren(formVisitor);
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
		target.addComponent(arkCrudContainerVO.getSearchResultPanelContainer());
		target.addComponent(arkCrudContainerVO.getDetailPanelContainer());
		onCancelPostProcess(target, true);
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
		target.addComponent(arkCrudContainerVO.getViewButtonContainer());
		target.addComponent(arkCrudContainerVO.getEditButtonContainer());
		target.addComponent(arkCrudContainerVO.getDetailPanelFormContainer());
	}

	/**
	 * Method to handle Cancel action when in Edit mode, disabling the Detail form, and hiding the "edit" button container
	 * 
	 * @param target
	 */
	protected void onCancelPostProcess(AjaxRequestTarget target) {
		detailPanelContainer.setVisible(true);
		viewButtonContainer.setVisible(true);
		viewButtonContainer.setEnabled(true);
		detailPanelFormContainer.setEnabled(false);
		resultListContainer.setVisible(false);
		searchPanelContainer.setVisible(false);
		editButtonContainer.setVisible(false);

		target.addComponent(feedBackPanel);
		target.addComponent(searchPanelContainer);
		target.addComponent(detailPanelContainer);
		target.addComponent(resultListContainer);
		target.addComponent(viewButtonContainer);
		target.addComponent(detailPanelFormContainer);
		target.addComponent(editButtonContainer);
	}

	/**
	 * Method to handle Cancel action when in Edit mode, disabling the Detail form, and hiding the "edit" button container Overloaded
	 * onCancelPostProcess. Use this when you use the ArkCrudContainerVO to manage the WebMarkupContainers.
	 * 
	 * @param target
	 * @param isArkCrudContainerVOPattern
	 */
	protected void onCancelPostProcess(AjaxRequestTarget target, Boolean isArkCrudContainerVOPattern) {
		arkCrudContainerVO.getViewButtonContainer().setVisible(true);
		arkCrudContainerVO.getViewButtonContainer().setEnabled(true);
		arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
		arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(false);
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
		arkCrudContainerVO.getSearchPanelContainer().setVisible(false);
		arkCrudContainerVO.getEditButtonContainer().setVisible(false);

		target.addComponent(feedBackPanel);
		target.addComponent(arkCrudContainerVO.getSearchPanelContainer());
		target.addComponent(arkCrudContainerVO.getSearchResultPanelContainer());
		target.addComponent(arkCrudContainerVO.getDetailPanelContainer());
		target.addComponent(arkCrudContainerVO.getDetailPanelFormContainer());

		target.addComponent(arkCrudContainerVO.getViewButtonContainer());
		target.addComponent(arkCrudContainerVO.getEditButtonContainer());
	}

	/**
	 * Method to handle the EditCancel/New Search action, hiding the Detail form/panel and showing the Search/Results panels
	 * 
	 * @param target
	 */
	protected void editCancelProcess(AjaxRequestTarget target) {
		resultListContainer.setVisible(true);
		detailPanelContainer.setVisible(false);
		searchPanelContainer.setVisible(true);

		target.addComponent(feedBackPanel);
		target.addComponent(searchPanelContainer);
		target.addComponent(detailPanelContainer);
		target.addComponent(resultListContainer);
		onCancel(target);
	}

	/**
	 * Method to handle the EditCancel/New Search action, hiding the Detail form/panel and showing the Search/Results panels Overloaded method for
	 * sub-classes that implement the ArkCrudContainerVO pattern
	 * 
	 * @param target
	 */
	protected void editCancelProcess(AjaxRequestTarget target, boolean isArkCrudContainerVoPattern) {
		arkCrudContainerVO.getSearchResultPanelContainer().setVisible(true);
		arkCrudContainerVO.getDetailPanelContainer().setVisible(false);
		arkCrudContainerVO.getSearchPanelContainer().setVisible(true);

		target.addComponent(feedBackPanel);
		target.addComponent(arkCrudContainerVO.getSearchPanelContainer());
		target.addComponent(arkCrudContainerVO.getDetailPanelContainer());
		target.addComponent(arkCrudContainerVO.getSearchResultPanelContainer());
		onCancel(target);
	}

	/**
	 * A helper method that will allow the toggle of panels and buttons. This method can be invoked by sub-classes as part of the onSave()
	 * implementation.Once the user has pressed Save either to create a new entity or update, invoking this method will place the new/edited record
	 * panel in View/Read only mode.
	 * 
	 * @param target
	 */
	protected void onSavePostProcess(AjaxRequestTarget target) {
		detailPanelContainer.setVisible(true);
		viewButtonContainer.setVisible(true);
		viewButtonContainer.setEnabled(true);
		detailPanelFormContainer.setEnabled(false);
		resultListContainer.setVisible(false);
		searchPanelContainer.setVisible(false);
		editButtonContainer.setVisible(false);

		target.addComponent(resultListContainer);
		target.addComponent(detailPanelContainer);
		target.addComponent(detailPanelFormContainer);
		target.addComponent(searchPanelContainer);
		target.addComponent(viewButtonContainer);
		target.addComponent(editButtonContainer);
	}

	/**
	 * A helper method that will allow the toggle of panels and buttons. This method can be invoked by sub-classes as part of the onSave()
	 * implementation.Once the user has pressed Save either to create a new entity or update, invoking this method will place the new/edited record
	 * panel in View/Read only mode. Overloaded method for sub-classes that implement the ArkCrudContainerVO pattern
	 * 
	 * @param target
	 */
	protected void onSavePostProcess(AjaxRequestTarget target, ArkCrudContainerVO crudVO) {
		// Visibility
		crudVO.getDetailPanelContainer().setVisible(true);
		crudVO.getViewButtonContainer().setVisible(true);
		crudVO.getSearchResultPanelContainer().setVisible(false);
		crudVO.getSearchPanelContainer().setVisible(false);
		crudVO.getEditButtonContainer().setVisible(false);

		// Enable
		crudVO.getDetailPanelFormContainer().setEnabled(false);
		crudVO.getViewButtonContainer().setEnabled(true);

		target.addComponent(crudVO.getSearchResultPanelContainer());
		target.addComponent(crudVO.getDetailPanelContainer());
		target.addComponent(crudVO.getDetailPanelFormContainer());
		target.addComponent(crudVO.getSearchPanelContainer());
		target.addComponent(crudVO.getViewButtonContainer());
		target.addComponent(crudVO.getEditButtonContainer());
		target.addComponent(feedBackPanel);
	}

	/**
	 * A helper method that handles the press of the Delete button, thus displaying a modal pop-up that required user selection
	 * 
	 * @param containerForm
	 * @param target
	 */
	protected void onDelete(Form<T> containerForm, AjaxRequestTarget target) {
		selectModalWindow.show(target);
		target.addComponent(selectModalWindow);
	}

	/**
	 * A helper method that handles the press of the Cancel button within the modal pop-up. ie Closes the modal pop-up
	 * 
	 * @param target
	 * @param selectModalWindow
	 */
	protected void onDeleteCancel(AjaxRequestTarget target, ModalWindow selectModalWindow) {
		selectModalWindow.close(target);
	}

	/**
	 * A helper method that initialises the modal window for delete confirmation
	 */
	protected ModalWindow initialiseModalWindow() {
		// The ModalWindow, showing some choices for the user to select.
		selectModalWindow = new au.org.theark.core.web.component.SelectModalWindow("modalwindow") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -1116985092871743122L;

			protected void onSelect(AjaxRequestTarget target, String selection) {
				onDeleteConfirmed(target, selection, selectModalWindow);
			}

			protected void onCancel(AjaxRequestTarget target) {
				onDeleteCancel(target, selectModalWindow);
			}
		};

		return selectModalWindow;
	}

	/**
	 * Method that sub-classes may call to disable the entire Detail form, and display a reason
	 * 
	 * @param sessionId
	 * @param errorMessage
	 */
	protected void disableDetailForm(Long sessionId, String errorMessage) {
		if (ArkPermissionHelper.isModuleFunctionAccessPermitted()) {
			if (sessionId == null) {
				detailPanelContainer.setEnabled(false);
				this.error(errorMessage);
			}
			else {
				detailPanelContainer.setEnabled(true);
			}
		}
		else {
			detailPanelContainer.setEnabled(false);
			this.error(au.org.theark.core.Constants.MODULE_NOT_ACCESSIBLE_MESSAGE);
		}
	}

	/**
	 * Method that sub-classes may call to disable the entire Detail form, and display a reason Overloaded method for those sub-classes that implement
	 * the ArkCrudContainerVO pattern
	 * 
	 * @param sessionId
	 * @param errorMessage
	 * @param arkCrudContainerVO
	 */
	protected void disableDetailForm(Long sessionId, String errorMessage, ArkCrudContainerVO arkCrudContainerVO) {
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
	abstract protected void onDeleteConfirmed(AjaxRequestTarget target, String selection, ModalWindow selectModalWindow);

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
}
