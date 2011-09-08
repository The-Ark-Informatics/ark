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
package au.org.theark.phenotypic.web.component.summaryModule.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.summaryModule.DetailPanel;

/**
 * @author nivedann
 * 
 */
@SuppressWarnings({ "serial", "unused" })
public class DetailForm extends Form<PhenoCollectionVO> {
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService	phenotypicService;

	private WebMarkupContainer	resultListContainer;
	private WebMarkupContainer	detailPanelContainer;
	private WebMarkupContainer	detailFormContainer;
	private WebMarkupContainer	viewButtonContainer;
	private WebMarkupContainer	editButtonContainer;
	private ContainerForm		phenoCollectionContainerForm;

	private int						mode;

	private AjaxButton			editButton;
	private AjaxButton			editCancelButton;
	private AjaxButton			deleteButton;
	private AjaxButton			saveButton;
	private AjaxButton			cancelButton;

	/**
	 * Default constructor
	 * 
	 * @param id
	 */
	public DetailForm(String id) {
		super(id);
	}

	/**
	 * DetailForm constructor
	 * 
	 * @param id
	 */
	public DetailForm(String id, DetailPanel detailPanel, WebMarkupContainer listContainer, WebMarkupContainer detailsContainer, ContainerForm containerForm, WebMarkupContainer viewButtonContainer,
			WebMarkupContainer editButtonContainer, WebMarkupContainer detailFormContainer) {
		super(id);
		this.phenoCollectionContainerForm = containerForm;
		this.resultListContainer = listContainer;
		this.detailPanelContainer = detailsContainer;
		this.viewButtonContainer = viewButtonContainer;
		this.editButtonContainer = editButtonContainer;
		this.detailFormContainer = detailFormContainer;

		editButton = new AjaxButton(au.org.theark.core.Constants.EDIT, new StringResourceModel("editKey", this, null)) {

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onEdit(phenoCollectionContainerForm.getModelObject(), target);
				target.addComponent(detailPanelContainer);
			}

			public void onError(AjaxRequestTarget target, Form<?> form) {
				processErrors(target);
			}
		};

		editCancelButton = new AjaxButton(au.org.theark.core.Constants.EDIT_CANCEL, new StringResourceModel("editCancelKey", this, null)) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onCancel(target);
			}
		};

		cancelButton = new AjaxButton(au.org.theark.core.Constants.CANCEL, new StringResourceModel("cancelKey", this, null)) {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onCancel(target);
			}
		};

		saveButton = new AjaxButton(au.org.theark.core.Constants.SAVE, new StringResourceModel("saveKey", this, null)) {

			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onSave(phenoCollectionContainerForm.getModelObject(), target);
				target.addComponent(detailPanelContainer);
			}

			public void onError(AjaxRequestTarget target, Form<?> form) {
				processErrors(target);
			}
		};

		deleteButton = new AjaxButton(au.org.theark.core.Constants.DELETE, new StringResourceModel("deleteKey", this, null)) {
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onDelete(phenoCollectionContainerForm.getModelObject(), target);
				target.addComponent(detailPanelContainer);
			}

			public void onError(AjaxRequestTarget target, Form<?> form) {
				processErrors(target);
			}
		};
	}

	public void initialiseForm() {
		attachValidators();
		addComponents();
	}

	private void attachValidators() {
	}

	private void addComponents() {
		add(detailFormContainer);

		// View has Edit and Cancel
		viewButtonContainer.add(editButton);
		viewButtonContainer.add(editCancelButton.setDefaultFormProcessing(false));

		// Edit has Save, Delete and Cancel
		editButtonContainer.add(saveButton);
		editButtonContainer.add(deleteButton);
		editButtonContainer.add(cancelButton.setDefaultFormProcessing(false));

		// Button containers
		add(viewButtonContainer);
		add(editButtonContainer);
	}

	protected void onSave(PhenoCollectionVO collectionVo, AjaxRequestTarget target) {

	}

	protected void onCancel(AjaxRequestTarget target) {

	}

	protected void onEdit(PhenoCollectionVO collectionVo, AjaxRequestTarget target) {

	}

	protected void onDelete(PhenoCollectionVO collectionVo, AjaxRequestTarget target) {

	}

	protected void processErrors(AjaxRequestTarget target) {

	}

	public AjaxButton getEditButton() {
		return editButton;
	}

	public void setEditButton(AjaxButton editButton) {
		this.editButton = editButton;
	}

	public AjaxButton getEditCancelButton() {
		return editCancelButton;
	}

	public void setEditCancelButton(AjaxButton editCancelButton) {
		this.editCancelButton = editCancelButton;
	}

	public AjaxButton getDeleteButton() {
		return deleteButton;
	}

	public void setDeleteButton(AjaxButton deleteButton) {
		this.deleteButton = deleteButton;
	}

	public AjaxButton getSaveButton() {
		return saveButton;
	}

	public void setSaveButton(AjaxButton saveButton) {
		this.saveButton = saveButton;
	}

	public AjaxButton getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(AjaxButton cancelButton) {
		this.cancelButton = cancelButton;
	}
}
