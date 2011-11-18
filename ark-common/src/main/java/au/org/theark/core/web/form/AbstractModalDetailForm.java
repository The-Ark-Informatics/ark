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
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;

import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.security.PermissionConstants;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.button.EditModeButtonsPanel;
import au.org.theark.core.web.component.button.IEditModeEventHandler;

/**
 * @author cellis
 * 
 */
public abstract class AbstractModalDetailForm<T> extends Form<T> implements  IEditModeEventHandler {
	/**
	 * 
	 */
	private static final long				serialVersionUID	= -4135522738458228329L;

	protected FeedbackPanel					feedbackPanel;
	protected CompoundPropertyModel<T>	cpModel;

	// Add a visitor class for required field marking/validation/highlighting
	protected ArkFormVisitor				formVisitor			= new ArkFormVisitor();

	// Use this for the model where WebMarkupContainers are set inside this VO
	protected ArkCrudContainerVO			arkCrudContainerVo;

	protected WebMarkupContainer			buttonsPanelWMC;

	

	public AbstractModalDetailForm(String id, FeedbackPanel feedbackPanel, ArkCrudContainerVO arkCrudContainerVo, CompoundPropertyModel<T> cpModel) {
		super(id, cpModel);
		this.feedbackPanel = feedbackPanel;
		this.arkCrudContainerVo = arkCrudContainerVo;
		this.cpModel = cpModel;
		//TODO: Shouldn't really have to do this, as there is not FileUpload on the modalWindow's, but needed to get this working!
		setMultiPart(true);
		initialiseForm();
	}


	private void initialiseEditButtonsPanel(boolean isNew) {
		EditModeButtonsPanel buttonsPanel = new EditModeButtonsPanel("buttonsPanel", this);
		if (isNew) {
			buttonsPanel.setDeleteButtonVisible(false);
			buttonsPanel.setDeleteButtonEnabled(false);
		}
		buttonsPanelWMC.addOrReplace(buttonsPanel);
	}
	
	private void initialiseEditButtonsPanelForReadOnlyUser(){
		EditModeButtonsPanel buttonsPanel = new EditModeButtonsPanel("buttonsPanel", this);
		buttonsPanel.setDeleteButtonVisible(false);
		buttonsPanel.setDeleteButtonEnabled(false);
		buttonsPanel.setSaveButtonVisible(false);
		buttonsPanel.setSaveButtonEnabled(false);
		buttonsPanelWMC.addOrReplace(buttonsPanel);
	}
	/**
	 * 
	 * Initialise method that is specific to classes that follow the ArkCrudContainerVO Pattern. The code related to each function has been modularised
	 * into protected methods, this is to provide the subclasses to refer to the protected methods without having to re-create/duplicate them when they
	 * extend the classes.
	 */
	protected void initialiseForm() {
		buttonsPanelWMC = new WebMarkupContainer("buttonsPanelWMC");
		buttonsPanelWMC.setOutputMarkupPlaceholderTag(true);
		if (isNew()) {
			// ARK-333: Allow the form go straight into Edit mode for creating a New record
			initialiseEditButtonsPanel(true);
			arkCrudContainerVo.getDetailPanelFormContainer().setEnabled(true);
		}
		else {
			
			SecurityManager securityManager = ThreadContext.getSecurityManager();
			Subject currentUser = SecurityUtils.getSubject();
			if( ArkPermissionHelper.hasEditPermission(securityManager,currentUser) || //User can UPDATE
				ArkPermissionHelper.hasNewPermission(securityManager, currentUser) || //User can CREATE
				ArkPermissionHelper.hasDeletePermission(securityManager, currentUser)){ //User can DELETE

				initialiseEditButtonsPanel(false);
				arkCrudContainerVo.getDetailPanelFormContainer().setEnabled(true);

				
			}else{
				
				initialiseEditButtonsPanelForReadOnlyUser();
				
			}
			
			
		}

		addComponentsToForm();
	}

	protected void addComponentsToForm() {
		add(arkCrudContainerVo.getDetailPanelFormContainer());
		addOrReplace(buttonsPanelWMC);
	}

	public void onBeforeRender() {
		super.onBeforeRender();
		visitChildren(formVisitor);
	}

	@SuppressWarnings("unchecked")
	protected void saveOnErrorProcess(AjaxRequestTarget target) {
		boolean setFocusError = false;
		WebMarkupContainer wmc = arkCrudContainerVo.getDetailPanelFormContainer();
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
	 * A helper method that will allow the toggle of panels and buttons. This method can be invoked by sub-classes as part of the onSave()
	 * implementation.Once the user has pressed Save either to create a new entity or update, invoking this method will place the new/edited record
	 * panel in View/Read only mode.
	 * 
	 * @param target
	 */
	protected void onSavePostProcess(AjaxRequestTarget target) {
		arkCrudContainerVo.getDetailPanelContainer().setVisible(true);
		arkCrudContainerVo.getDetailPanelFormContainer().setEnabled(true);
		initialiseEditButtonsPanel(false);	// restore the Delete button
		target.add(buttonsPanelWMC);
		target.add(arkCrudContainerVo.getDetailPanelContainer());
		target.add(arkCrudContainerVo.getDetailPanelFormContainer());
	}

	protected void disableModalDetailForm(Long sessionId, String errorMessage) {
		if (sessionId == null) {
			arkCrudContainerVo.getDetailPanelContainer().setEnabled(false);
			this.error(errorMessage);
		}
		else {
			arkCrudContainerVo.getDetailPanelContainer().setEnabled(true);
		}
	}

	protected void disableModalDetailForm(Long sessionId, String errorMessage, ArkCrudContainerVO arkCrudContainerVo) {
		SecurityManager securityManager = ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();

		if (!securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.CREATE) && !securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.UPDATE)
				&& !securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.READ) && !securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.DELETE)) {

			arkCrudContainerVo.getDetailPanelContainer().setEnabled(false);
			this.error("You do not have the required security privileges to work with this function. Please see your Administrator.");
		}
		else {

			if (sessionId == null) {
				arkCrudContainerVo.getDetailPanelContainer().setEnabled(false);
				this.error(errorMessage);
			}
			else {
				arkCrudContainerVo.getDetailPanelContainer().setEnabled(true);
			}
		}
	}

	abstract protected void attachValidators();

	abstract protected void onSave(AjaxRequestTarget target);

	abstract protected void onClose(AjaxRequestTarget target);

	abstract protected void onDeleteConfirmed(AjaxRequestTarget target, Form<?> form);

	abstract protected void processErrors(AjaxRequestTarget target);

	abstract protected boolean isNew();

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.component.button.IEditModeEventHandler#onEditCancel(org.apache.wicket.ajax.AjaxRequestTarget,
	 * org.apache.wicket.markup.html.form.Form)
	 */
	public void onEditCancel(AjaxRequestTarget target, Form<?> form) {
		onClose(target);
		processErrors(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.component.button.IEditModeEventHandler#onEditCancelError(org.apache.wicket.ajax.AjaxRequestTarget,
	 * org.apache.wicket.markup.html.form.Form)
	 */
	public void onEditCancelError(AjaxRequestTarget target, Form<?> form) {
		processErrors(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.component.button.IEditModeEventHandler#onEditDelete(org.apache.wicket.ajax.AjaxRequestTarget,
	 * org.apache.wicket.markup.html.form.Form)
	 */
	public void onEditDelete(AjaxRequestTarget target, Form<?> form) {
		onDeleteConfirmed(target, form);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.component.button.IEditModeEventHandler#onEditDeleteError(org.apache.wicket.ajax.AjaxRequestTarget,
	 * org.apache.wicket.markup.html.form.Form)
	 */
	public void onEditDeleteError(AjaxRequestTarget target, Form<?> form) {
		processErrors(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.component.button.IEditModeEventHandler#onEditSave(org.apache.wicket.ajax.AjaxRequestTarget,
	 * org.apache.wicket.markup.html.form.Form)
	 */
	public void onEditSave(AjaxRequestTarget target, Form<?> form) {
		onSave(target);
		target.add(arkCrudContainerVo.getDetailPanelContainer());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.component.button.IEditModeEventHandler#onEditSaveError(org.apache.wicket.ajax.AjaxRequestTarget,
	 * org.apache.wicket.markup.html.form.Form)
	 */
	public void onEditSaveError(AjaxRequestTarget target, Form<?> form) {
		saveOnErrorProcess(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.component.button.IViewModeEventHandler#onViewCancel(org.apache.wicket.ajax.AjaxRequestTarget,
	 * org.apache.wicket.markup.html.form.Form)
	 */
	public void onViewCancel(AjaxRequestTarget target, Form<?> form) {
		onClose(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.component.button.IViewModeEventHandler#onViewCancelError(org.apache.wicket.ajax.AjaxRequestTarget,
	 * org.apache.wicket.markup.html.form.Form)
	 */
	public void onViewCancelError(AjaxRequestTarget target, Form<?> form) {
		processErrors(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.component.button.IViewModeEventHandler#onViewEdit(org.apache.wicket.ajax.AjaxRequestTarget,
	 * org.apache.wicket.markup.html.form.Form)
	 */
	public void onViewEdit(AjaxRequestTarget target, Form<?> form) {
		// switch from View mode buttons to Edit mode buttons 
		initialiseEditButtonsPanel(false);
		arkCrudContainerVo.getDetailPanelFormContainer().setEnabled(true);

		target.add(arkCrudContainerVo.getDetailPanelFormContainer());
		target.add(buttonsPanelWMC);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.component.button.IViewModeEventHandler#onViewEditError(org.apache.wicket.ajax.AjaxRequestTarget,
	 * org.apache.wicket.markup.html.form.Form)
	 */
	public void onViewEditError(AjaxRequestTarget target, Form<?> form) {
		processErrors(target);
	}
}
