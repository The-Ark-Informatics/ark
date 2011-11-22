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
package au.org.theark.core.web.component.customfield.dataentry;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ICustomFieldData;
import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.button.ArkAjaxButton;
import au.org.theark.core.web.component.button.EditModeButtonsPanel;
import au.org.theark.core.web.component.button.IEditModeEventHandler;
import au.org.theark.core.web.form.ArkFormVisitor;

/**
 * @author elam
 * 
 */
public abstract class AbstractCustomDataEditorForm<T extends CustomDataVO<? extends ICustomFieldData>> 
							extends Form<T> implements  IEditModeEventHandler {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private static final Logger log = LoggerFactory.getLogger(AbstractCustomDataEditorForm.class);

	protected CompoundPropertyModel<T>			cpModel;
	
	protected FeedbackPanel	feedbackPanel;
	protected WebMarkupContainer dataViewWMC;
	protected WebMarkupContainer buttonsPanelWMC;
	
	// Add a visitor class for required field marking/validation/highlighting
	protected ArkFormVisitor formVisitor = new ArkFormVisitor();
	
	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService<Void>			iArkCommonService;

	public AbstractCustomDataEditorForm(String id, CompoundPropertyModel<T> cpModel, FeedbackPanel feedbackPanel) {
		super(id, cpModel);
		this.cpModel = cpModel;
		this.feedbackPanel = feedbackPanel;
		setOutputMarkupPlaceholderTag(true);
	}

	public AbstractCustomDataEditorForm<T> initialiseForm() {
		dataViewWMC = new WebMarkupContainer("dataViewWMC") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			// this WMC must have the visitor since the form itself isn't always repainted
			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
				visitChildren(formVisitor);	// automatically mark the required fields
			}
		};
		dataViewWMC.setOutputMarkupId(true);
		
		dataViewWMC.setEnabled(true);	//default to View mode
		this.add(dataViewWMC);
		
		buttonsPanelWMC = new WebMarkupContainer("buttonsPanelWMC");
		buttonsPanelWMC.setOutputMarkupPlaceholderTag(true);
		
		EditModeButtonsPanel buttonsPanel = new EditModeButtonsPanel("buttonsPanel", this);
		buttonsPanel.setDeleteButtonEnabled(false);	// delete button not used in data entry
		buttonsPanel.setDeleteButtonVisible(false);
		buttonsPanelWMC.addOrReplace(buttonsPanel);
		this.add(buttonsPanelWMC);
		
		return this;
	}

	public void onBeforeRender() {
		super.onBeforeRender();
		visitChildren(formVisitor);
		
		SecurityManager securityManager = ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();
		if( ArkPermissionHelper.hasEditPermission(securityManager,currentUser) || //User can UPDATE
			ArkPermissionHelper.hasNewPermission(securityManager, currentUser) || //User can CREATE
			ArkPermissionHelper.hasDeletePermission(securityManager, currentUser)){ //User can DELETE
			
			dataViewWMC.setOutputMarkupId(true);
			dataViewWMC.setEnabled(true);
			this.add(dataViewWMC);
	
		}else{
			dataViewWMC.setOutputMarkupId(true);
			dataViewWMC.setEnabled(false);	//default to View mode
			this.add(dataViewWMC);
		}
		
		Long arkFunctionId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_FUNCTION_KEY);
		ArkFunction arkFunction  = iArkCommonService.getArkFunctionById(arkFunctionId);
		if(arkFunction.getName().equalsIgnoreCase(au.org.theark.core.Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_DATA)){
			EditModeButtonsPanel buttonPanel = (EditModeButtonsPanel)buttonsPanelWMC.get("buttonsPanel");
			if(buttonPanel != null){
				((ArkAjaxButton)buttonPanel.get("cancel")).setVisible(false);
			}
			
		}
	}
	

	public WebMarkupContainer getDataViewWMC() {
		return dataViewWMC;
	}

	public void onEditCancel(AjaxRequestTarget target, Form<?> form) {
		target.add(feedbackPanel);
		dataViewWMC.setEnabled(true);
		target.add(dataViewWMC);
		target.add(buttonsPanelWMC);
	}

	public void onEditCancelError(AjaxRequestTarget target, Form<?> form) {
		this.info("Error occurred with cancelling the edit.  If this persists, please contact your System Administrator.");
		target.add(feedbackPanel);
		log.error("Unknown error: Couldn't cancel the edit.");
	}

	public void onEditDelete(AjaxRequestTarget target, Form<?> form) {
		// Should never get here
		log.debug("Internal error: arriving here at CustomDataEditorForm.onEditDelete() should be imposible");
	}

	public void onEditDeleteError(AjaxRequestTarget target, Form<?> form) {
		// Should never get here
		log.debug("Internal error: arriving here at CustomDataEditorForm.onEditDeleteError() should be imposible");
	}

	abstract public void onEditSave(AjaxRequestTarget target, Form<?> form);
	
	public void onEditSaveError(AjaxRequestTarget target, Form<?> form) {
		target.add(feedbackPanel);
	}
}
