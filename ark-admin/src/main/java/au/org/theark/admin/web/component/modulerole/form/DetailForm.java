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
package au.org.theark.admin.web.component.modulerole.form;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.admin.service.IAdminService;
import au.org.theark.admin.web.component.ContainerForm;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkModuleRole;
import au.org.theark.core.model.study.entity.ArkRole;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.palette.ArkPalette;
import au.org.theark.core.web.form.AbstractDetailForm;

public class DetailForm extends AbstractDetailForm<AdminVO> {


	private static final long				serialVersionUID	= -770595464410732350L;

	protected transient Logger				log					= LoggerFactory.getLogger(DetailForm.class);

	@SpringBean(name = au.org.theark.admin.service.Constants.ARK_ADMIN_SERVICE)
	private IAdminService<Void>			iAdminService;

	private DropDownChoice<ArkModule>	arkModuleDropDown;
	private Palette<ArkModuleRole>		arkModuleRolePalette;

	private int	PALLETTE_ROWS				= 10;

	/**
	 * Constructor
	 * 
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
		arkModuleDropDown.setEnabled(isNew());
		deleteButton.setEnabled(false);
	}

	public void initialiseDetailForm() {
		initArkModuleDropDown();
		initArkModuleRolePalette();

		attachValidators();
		addDetailFormComponents();
	}

	private void initArkModuleDropDown() {
		List<ArkModule> arkModuleList = iAdminService.getArkModuleList();
		ChoiceRenderer<ArkModule> defaultChoiceRenderer = new ChoiceRenderer<ArkModule>("name", "id");
		arkModuleDropDown = new DropDownChoice<ArkModule>("arkModuleRole.arkModule", arkModuleList, defaultChoiceRenderer);
		arkModuleDropDown.setOutputMarkupPlaceholderTag(true);
		arkModuleDropDown.setEnabled(isNew());
	}
	
	@SuppressWarnings("unchecked")
	private void initArkModuleRolePalette() {
		CompoundPropertyModel<AdminVO> cpModel = (CompoundPropertyModel<AdminVO>) containerForm.getModel();
		cpModel.getObject().setAvailableArkRoles(iAdminService.getArkRoleList());
		cpModel.getObject().setSelectedArkRoles(iAdminService.getArkRoleListByArkModule(containerForm.getModelObject().getArkModule()));
		IChoiceRenderer<String> renderer = new ChoiceRenderer<String>("name", "id");
		PropertyModel<Collection<ArkRole>> selectedModPm = new PropertyModel<Collection<ArkRole>>(cpModel, "selectedArkRoles");
		PropertyModel<Collection<ArkRole>> availableModulesPm = new PropertyModel<Collection<ArkRole>>(cpModel, "availableArkRoles");

		arkModuleRolePalette = new ArkPalette("selectedArkModuleRoles", selectedModPm, availableModulesPm, renderer, PALLETTE_ROWS, true);
	}

	@Override
	protected void attachValidators() {
		arkModuleDropDown.setRequired(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#addDetailFormComponents()
	 */
	@Override
	protected void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(arkModuleDropDown);
		arkCrudContainerVO.getDetailPanelFormContainer().add(arkModuleRolePalette);

		add(arkCrudContainerVO.getDetailPanelFormContainer());
	}

	protected void onSave(Form<AdminVO> containerForm, AjaxRequestTarget target) {
		if (containerForm.getModelObject().getSelectedArkRoles().isEmpty()) {
			this.error("At least one Role must be selected");
			target.add(feedBackPanel);
		}
		else {
			if (containerForm.getModelObject().getArkModule().getId() == null) {
				// Save
				iAdminService.createArkModuleRole(containerForm.getModelObject());
			}
			else {
				// Update
				iAdminService.updateArkModuleRole(containerForm.getModelObject());
			}
	
			this.info("Ark Module Role: " + containerForm.getModelObject().getArkModuleRole().getArkModule().getName() + " was created/updated successfully.");
			target.add(feedBackPanel);
		}
		
		onSavePostProcess(target);
	}

	protected void onCancel(AjaxRequestTarget target) {
		containerForm.setModelObject(new AdminVO());
	}

	protected void onDeleteConfirmed(AjaxRequestTarget target, String selectionO) {
		// Delete
		iAdminService.deleteArkModule(containerForm.getModelObject());

		this.info("Ark Module Role: " + containerForm.getModelObject().getArkModule().getName() + " was deleted successfully.");
		editCancelProcess(target);
	}

	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);
	}

	protected boolean isNew() {
		if (containerForm.getModelObject().getArkModuleFunction().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}
}
