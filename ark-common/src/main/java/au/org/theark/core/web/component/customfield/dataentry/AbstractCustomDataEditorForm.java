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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.model.study.entity.ICustomFieldData;
import au.org.theark.core.web.component.button.EditModeButtonsPanel;
import au.org.theark.core.web.component.button.IEditModeEventHandler;
import au.org.theark.core.web.component.button.IViewModeEventHandler;
import au.org.theark.core.web.component.button.ViewModeButtonsPanel;
import au.org.theark.core.web.form.ArkFormVisitor;

/**
 * @author elam
 * 
 */
public abstract class AbstractCustomDataEditorForm<T extends CustomDataVO<? extends ICustomFieldData>> 
							extends Form<T> implements IViewModeEventHandler, IEditModeEventHandler {

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
		dataViewWMC.setEnabled(false);	//default to View mode
		this.add(dataViewWMC);
		
		buttonsPanelWMC = new WebMarkupContainer("buttonsPanelWMC");
		buttonsPanelWMC.setOutputMarkupPlaceholderTag(true);
		initialiseViewButtonsPanel();
		this.add(buttonsPanelWMC);
		
		return this;
	}

	private void initialiseViewButtonsPanel() {
		ViewModeButtonsPanel buttonsPanel = new ViewModeButtonsPanel("buttonsPanel", this);
		buttonsPanel.setCancelButtonVisible(false);	//hide the Cancel button in view mode
		buttonsPanel.setCancelButtonEnabled(false);	//disable the Cancel button in view mode
		buttonsPanelWMC.addOrReplace(buttonsPanel);
	}

	public WebMarkupContainer getDataViewWMC() {
		return dataViewWMC;
	}

	public void onEditCancel(AjaxRequestTarget target, Form<?> form) {
		target.add(feedbackPanel);
		
		initialiseViewButtonsPanel();	//put View mode buttons back
		dataViewWMC.setEnabled(false);
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
	
	public void onViewCancel(AjaxRequestTarget target, Form<?> form) {
		// Should never get here
		log.debug("Internal error: arriving here at CustomDataEditorForm.onViewCancel() should be imposible");
	}

	public void onViewCancelError(AjaxRequestTarget target, Form<?> form) {
		// Should never get here
		log.debug("Internal error: arriving here at CustomDataEditorForm.onViewCancelError() should be imposible");
	}

	public void onViewEdit(AjaxRequestTarget target, Form<?> form) {
		//put Edit mode buttons in
		EditModeButtonsPanel buttonsPanel = new EditModeButtonsPanel("buttonsPanel", this);
		buttonsPanel.setDeleteButtonEnabled(false);	// delete button not used in data entry
		buttonsPanel.setDeleteButtonVisible(false);
		buttonsPanelWMC.addOrReplace(buttonsPanel);
		dataViewWMC.setEnabled(true);	//allow fields to be edited
		target.add(dataViewWMC);
		target.add(buttonsPanelWMC);
	}

	public void onViewEditError(AjaxRequestTarget target, Form<?> form) {
		this.error("Couldn't go into edit mode.  If this persists, please contact your System Administrator.");
		target.add(feedbackPanel);
		log.error("Unknown error: Couldn't go into edit mode.");
	}
	
}
