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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.security.ArkPermissionHelper;
import au.org.theark.core.web.component.listeditor.AbstractListEditor;

/**
 * s
 * 
 * @author cellis
 * 
 */
public abstract class AbstractListDetailForm<T> extends Form<T> {
	/**
	 * 
	 */
	private static final long			serialVersionUID	= -4025065993634203645L;

	protected transient Logger			log					= LoggerFactory.getLogger(AbstractListDetailForm.class);

	protected AbstractListEditor<?>	listEditor			= null;
	protected FeedbackPanel				feedbackPanel;
	protected Form<T>						containerForm;

	// Add a visitor class for required field marking/validation/highlighting
	protected ArkFormVisitor			formVisitor			= new ArkFormVisitor();

	protected AjaxButton					newButton;

	public AbstractListDetailForm(String id) {
		super(id);
		setOutputMarkupPlaceholderTag(true);
	}

	public AbstractListDetailForm(String id, IModel<T> model) {
		super(id, model);
		setOutputMarkupPlaceholderTag(true);
	}

	public AbstractListDetailForm(String id, FeedbackPanel feedbackPanel, IModel<T> model) {
		super(id, model);
		this.feedbackPanel = feedbackPanel;
		setOutputMarkupPlaceholderTag(true);
	}

	/**
	 * Initialise method that is specific to classes that follow the ArkCrudContainerVO Pattern. The code related to each function has been modularised
	 * into protected methods, this is to provide the subclasses to refer to the protected methods without having to re-create/duplicate them when they
	 * extend the classes.
	 */
	public void initialiseForm() {
		addComponentsToForm();
	}

	protected void addComponentsToForm() {
		newButton = new AjaxButton("listNewButton", new StringResourceModel("listNewKey", this, null)) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -8505652280527122102L;

			@Override
			public boolean isVisible() {
				return ArkPermissionHelper.isActionPermitted(au.org.theark.core.Constants.NEW);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				// Sub class to implement New button press
				onNew(target, containerForm);
			}
		};

		newButton.setDefaultFormProcessing(false);

		addOrReplace(newButton);
		addOrReplace(initialiseListEditor());
	}

	/**
	 * @param listEditor
	 *           the listEditor to set
	 */
	public void setListEditor(AbstractListEditor<?> listEditor) {
		this.listEditor = listEditor;
	}

	/**
	 * @return the listEditor
	 */
	public AbstractListEditor<?> getListEditor() {
		return listEditor;
	}

	@SuppressWarnings("unchecked")
	public void onBeforeRender() {
		super.onBeforeRender();
		visitChildren(formVisitor);
	}

	abstract protected void attachValidators();

	/**
	 * Initialises the listEditor within the form
	 * 
	 * @return
	 */
	abstract protected AbstractListEditor<?> initialiseListEditor();

	/**
	 * Sub-class implements the action when New button clicked
	 * 
	 * @param target
	 * @param form
	 */
	abstract protected void onNew(AjaxRequestTarget target, Form<T> form);

	/**
	 * Show the modal window that contains the detail form to create/edit/delete
	 * 
	 * @param target
	 * @param form
	 */
	abstract protected void showModalWindow(AjaxRequestTarget target, Form<T> form);
}
