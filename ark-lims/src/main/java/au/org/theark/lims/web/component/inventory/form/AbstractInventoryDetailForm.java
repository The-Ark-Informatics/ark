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
package au.org.theark.lims.web.component.inventory.form;

import java.util.Iterator;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.model.StringResourceModel;

import au.org.theark.core.Constants;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.web.component.button.AjaxDeleteButton;
import au.org.theark.core.web.component.button.ArkBusyAjaxButton;
import au.org.theark.core.web.form.ArkFormVisitor;

/**
 * <p>
 * Abstract class for Inventory Detail Form sub-classes. It provides some common functionality that sub-classes inherit. 
 * Provides the skeleton methods for onSave, onDelete, onCancel etc. Defines the core buttons like save, delete, cancel, and edit. 
 * Provides method to toggle the view from read only to edit mode which is usually a common behavior the sub-classes can re-use.
 * </p>
 * 
 * @author cellis
 * @param <T>
 * 
 */
public abstract class AbstractInventoryDetailForm<T> extends Form<T> {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4043985976208735564L;
	
	protected Form<T>					containerForm;
	
	protected FeedbackPanel			feedbackPanel;
	protected WebMarkupContainer	detailContainer;
	protected WebMarkupContainer	detailFormContainer;
	protected WebMarkupContainer	viewButtonContainer;
	protected WebMarkupContainer	editButtonContainer;

	protected AjaxButton				saveButton;
	protected AjaxButton				cancelButton;
	protected AjaxButton				deleteButton;
	protected AjaxButton				editButton;

	// Add a visitor class for required field marking/validation/highlighting
	protected ArkFormVisitor		formVisitor			= new ArkFormVisitor();

	protected BaseTree 				tree;
	protected DefaultMutableTreeNode node;
	
	/**
	 * 
	 * @param id
	 * @param feedbackPanel
	 * @param detailContainer
	 * @param containerForm
	 * @param tree
	 * @param node 
	 */
	public AbstractInventoryDetailForm(String id, FeedbackPanel feedbackPanel, WebMarkupContainer detailContainer, Form<T> containerForm, BaseTree tree, DefaultMutableTreeNode node) {
		super(id, containerForm.getModel());
		this.feedbackPanel = feedbackPanel;
		this.detailContainer = detailContainer;
		this.containerForm = containerForm;
		this.tree = tree;
		this.node = node;
		setOutputMarkupPlaceholderTag(true);
		setModelObject(containerForm.getModelObject());
		initialiseForm();
	}

	/**
	 * Initialise the form, with the general edit/save/cancel/delete buttons
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
					// On Cancel from new, simply hide the detailPanel/Container
					detailContainer.setVisible(false);
					target.add(detailContainer);
				}
				else {
					onCancelPostProcess(target);
				}
				processErrors(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedbackPanel);
			}
		};

		saveButton = new ArkBusyAjaxButton(Constants.SAVE, new StringResourceModel("saveKey", this, null)) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -423605230448635419L;

			@Override
			public boolean isVisible() {
				return ArkPermissionHelper.isActionPermitted(Constants.SAVE);
			}

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onSave(containerForm, target);
				target.add(detailFormContainer);
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
				onDeleteConfirmed(target);
				target.add(feedbackPanel);
				detailContainer.setVisible(false);
				target.add(detailContainer);
				tree.updateTree(target);
				target.add(tree);
			}

			@Override
			public boolean isVisible() {
				return (ArkPermissionHelper.isActionPermitted(Constants.DELETE) && !isNew());
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedbackPanel);
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
				detailFormContainer.setEnabled(true);
				target.add(viewButtonContainer);
				target.add(editButtonContainer);
				target.add(detailFormContainer);
			}

			public void onError(AjaxRequestTarget target, Form<?> form) {
				processErrors(target);
			}

			@Override
			public boolean isVisible() {
				return ArkPermissionHelper.isActionPermitted(Constants.EDIT);
			}
		};
		
		editButton.setDefaultFormProcessing(false);
		cancelButton.setDefaultFormProcessing(false);
		
		/* Defines a Read-Only Mode */
		viewButtonContainer = new WebMarkupContainer("viewButtonContainer");
		viewButtonContainer.setOutputMarkupPlaceholderTag(true);
		viewButtonContainer.setVisible(!isNew());

		/* Defines a edit mode */
		editButtonContainer = new WebMarkupContainer("editButtonContainer");
		editButtonContainer.setOutputMarkupPlaceholderTag(true);
		editButtonContainer.setVisible(isNew());
		
		// Initialise in read only
		detailFormContainer = new WebMarkupContainer("detailFormContainer");
		detailFormContainer.setOutputMarkupPlaceholderTag(true);
		detailFormContainer.setEnabled(isNew());

		addComponentsToForm();
	}

	/**
	 * Add all the components to the Detail form
	 */
	protected void addComponentsToForm() {
		editButtonContainer.add(saveButton);
		editButtonContainer.add(cancelButton.setDefaultFormProcessing(false));
		editButtonContainer.add(deleteButton.setDefaultFormProcessing(false));

		viewButtonContainer.add(editButton);

		add(editButtonContainer);
		add(viewButtonContainer);
	}

	@Override
	public void onBeforeRender() {
		super.onBeforeRender();
		visitChildren(formVisitor);
	}

	/**
	 * Method to handle Save action, and handle for any errors, placing forcus on any component in error
	 * 
	 * @param target
	 */
	@SuppressWarnings("unchecked")
	protected void saveOnErrorProcess(AjaxRequestTarget target) {
		boolean setFocusError = false;
		WebMarkupContainer wmc = getDetailContainer();
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

	private WebMarkupContainer getDetailContainer() {
		return this.detailContainer;
	}

	/**
	 * Method to handle Cancel action when in Edit mode, disabling the Detail form, and hiding the "edit" button container
	 * 
	 * @param target
	 */
	protected void onCancelPostProcess(AjaxRequestTarget target) {
		detailContainer.setVisible(true);
		viewButtonContainer.setVisible(true);
		viewButtonContainer.setEnabled(true);
		detailFormContainer.setEnabled(false);
		editButtonContainer.setVisible(false);

		target.add(feedbackPanel);
		target.add(detailFormContainer);
		target.add(viewButtonContainer);
		target.add(editButtonContainer);
	}

	/**
	 * A helper method that will allow the toggle of panels and buttons. This method can be invoked by sub-classes as part of the onSave()
	 * implementation.Once the user has pressed Save either to create a new entity or update, invoking this method will place the new/edited record
	 * panel in View/Read only mode.
	 * 
	 * @param target
	 */
	protected void onSavePostProcess(AjaxRequestTarget target) {
		detailFormContainer.setEnabled(false);
		viewButtonContainer.setVisible(true);
		viewButtonContainer.setEnabled(true);
		editButtonContainer.setVisible(false);

		target.add(detailFormContainer);
		target.add(viewButtonContainer);
		target.add(editButtonContainer);
		
		// Refresh tree panel
		target.add(tree.getParent());
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
				detailContainer.setEnabled(false);
				this.error(errorMessage);
			}
			else {
				detailContainer.setEnabled(true);
			}
		}
		else {
			detailContainer.setEnabled(false);
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
	 */
	abstract protected void onDeleteConfirmed(AjaxRequestTarget target);

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
