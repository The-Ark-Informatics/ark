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
package au.org.theark.admin.web.component.role.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.admin.service.IAdminService;
import au.org.theark.admin.web.component.ContainerForm;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractDetailForm;

public class DetailForm extends AbstractDetailForm<AdminVO> {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4117355038874906668L;

	protected transient Logger		log					= LoggerFactory.getLogger(DetailForm.class);

	@SpringBean(name = au.org.theark.admin.service.Constants.ARK_ADMIN_SERVICE)
	private IAdminService<Void>	iAdminService;

	private TextField<String>		idTxtFld;
	private TextField<String>		nameTxtFld;
	private TextArea<String>		descriptionTxtAreaFld;

	/**
	 * Constructor
	 * @param id
	 * @param feedbackPanel
	 * @param containerForm
	 * @param arkCrudContainerVo
	 */
	public DetailForm(String id, FeedbackPanel feedbackPanel, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVo) {
		super(id, feedbackPanel, containerForm, arkCrudContainerVo);
		this.containerForm = containerForm;
		arkCrudContainerVO = arkCrudContainerVo;
		setMultiPart(true);
	}
	
	@Override
	public void onBeforeRender() {
		super.onBeforeRender();
		// Do not allow deletes
		deleteButton.setEnabled(false);
	}

	public void initialiseDetailForm() {
		idTxtFld = new TextField<String>("arkRole.id");
		idTxtFld.setEnabled(false);
		
		nameTxtFld = new TextField<String>("arkRole.name") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void onBeforeRender() {
				super.onBeforeRender();
				setEnabled(isNew());
			}
		};
		
		descriptionTxtAreaFld = new TextArea<String>("arkRole.description");

		attachValidators();
		addDetailFormComponents();
	}

	@Override
	protected void attachValidators() {
		// Set required field here
		nameTxtFld.setRequired(true);
	}
	
	/* (non-Javadoc)
	 * @see au.org.theark.core.web.form.AbstractDetailForm#addDetailFormComponents()
	 */
	@Override
	protected void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(idTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(nameTxtFld);
		arkCrudContainerVO.getDetailPanelFormContainer().add(descriptionTxtAreaFld);

		add(arkCrudContainerVO.getDetailPanelFormContainer());
	}

	protected void onSave(Form<AdminVO> containerForm, AjaxRequestTarget target) {
		// Save or update
		iAdminService.createOrUpdateArkRole(containerForm.getModelObject());

		this.info("Ark Role: " + containerForm.getModelObject().getArkRole().getName() + " was created/updated successfully.");
		target.add(feedBackPanel);
		onSavePostProcess(target);
	}

	protected void onCancel(AjaxRequestTarget target) {
		containerForm.setModelObject(new AdminVO());
	}

	protected void onDeleteConfirmed(AjaxRequestTarget target, String selectionO) {
		// Delete
		iAdminService.deleteArkModule(containerForm.getModelObject());

		this.info("Ark Role: " + containerForm.getModelObject().getArkModule().getName() + " was deleted successfully.");
		editCancelProcess(target);
	}

	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);
	}

	protected boolean isNew() {
		if (containerForm.getModelObject().getArkRole().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}
}
