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
package au.org.theark.admin.web.component.rolePolicy.form;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.admin.service.IAdminService;
import au.org.theark.admin.web.component.ContainerForm;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkPermission;
import au.org.theark.core.model.study.entity.ArkRole;
import au.org.theark.core.model.study.entity.ArkRolePolicyTemplate;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractDetailForm;

public class DetailForm extends AbstractDetailForm<AdminVO> {

	private static final long				serialVersionUID	= 5096967681735723818L;
	protected transient Logger				log					= LoggerFactory.getLogger(DetailForm.class);

	@SpringBean(name = au.org.theark.admin.service.Constants.ARK_ADMIN_SERVICE)
	private IAdminService<Void>			iAdminService;

	private DropDownChoice<ArkRole>		arkRoleDropDown;
	private DropDownChoice<ArkModule>	arkModuleDropDown;
	private DropDownChoice<ArkFunction>	arkFunctionDropDown;
	private CheckBox							createChk;
	private CheckBox							readChk;
	private CheckBox							updateChk;
	private CheckBox							deleteChk;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param crudVO
	 * @param feedbackPanel
	 * @param containerForm
	 */
	public DetailForm(String id, FeedbackPanel feedbackPanel, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVo) {
		super(id, feedbackPanel, containerForm, arkCrudContainerVo);
		this.containerForm = containerForm;
		arkCrudContainerVO = arkCrudContainerVo;
		setMultiPart(true);
	}

	public void initialiseDetailForm() {
		// Role selection
		initArkRoleDropDown();

		// Module selection
		initArkModuleDropDown();
		
		// Function selection
		initArkFunctionDropDown();

		createChk = new CheckBox("arkCreatePermission");
		readChk = new CheckBox("arkReadPermission");
		updateChk = new CheckBox("arkUpdatePermission");
		deleteChk = new CheckBox("arkDeletePermission");

		attachValidators();
		addDetailFormComponents();
	}

	private void initArkRoleDropDown() {
		List<ArkRole> arkRoleList = iAdminService.getArkRoleList();
		ChoiceRenderer<ArkRole> defaultChoiceRenderer = new ChoiceRenderer<ArkRole>("name", "id");
		arkRoleDropDown = new DropDownChoice<ArkRole>("arkRolePolicyTemplate.arkRole", arkRoleList, defaultChoiceRenderer);
		arkRoleDropDown.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long	serialVersionUID	= 5591846326218931210L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				List<ArkModule> arkModuleList = iAdminService.getArkModuleListByArkRole(containerForm.getModelObject().getArkRolePolicyTemplate().getArkRole());
				arkModuleDropDown.getChoices().clear();
				arkModuleDropDown.setChoices(arkModuleList);
				target.add(arkModuleDropDown);
			}
		});
		arkRoleDropDown.setEnabled(isNew());
	}

	private void initArkModuleDropDown() {
		List<ArkModule> arkModuleList = iAdminService.getArkModuleListByArkRole(containerForm.getModelObject().getArkRolePolicyTemplate().getArkRole());
		ChoiceRenderer<ArkModule> defaultChoiceRenderer = new ChoiceRenderer<ArkModule>("name", "id");
		arkModuleDropDown = new DropDownChoice<ArkModule>("arkRolePolicyTemplate.arkModule", arkModuleList, defaultChoiceRenderer);
		arkModuleDropDown.add(new AjaxFormComponentUpdatingBehavior("onChange") {

			private static final long	serialVersionUID	= -1917750577626157879L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				List<ArkFunction> arkFunctionList = iAdminService.getArkFunctionListByArkModule(containerForm.getModelObject().getArkModule());
				arkFunctionDropDown.getChoices().clear();
				arkFunctionDropDown.setChoices(arkFunctionList);
				target.add(arkFunctionDropDown);
			}
		});
		arkModuleDropDown.setEnabled(isNew());
	}
	
	private void initArkFunctionDropDown() {
		List<ArkFunction> arkFunctionList = iAdminService.getArkFunctionListByArkModule(containerForm.getModelObject().getArkModule());
		ChoiceRenderer<ArkFunction> defaultChoiceRenderer = new ChoiceRenderer<ArkFunction>("name", "id");
		arkFunctionDropDown = new DropDownChoice<ArkFunction>("arkRolePolicyTemplate.arkFunction", arkFunctionList, defaultChoiceRenderer);
		arkFunctionDropDown.setEnabled(isNew());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.web.form.AbstractDetailForm#addDetailFormComponents()
	 */
	@Override
	protected void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(arkRoleDropDown);
		arkCrudContainerVO.getDetailPanelFormContainer().add(arkModuleDropDown);
		arkCrudContainerVO.getDetailPanelFormContainer().add(arkFunctionDropDown);
		arkCrudContainerVO.getDetailPanelFormContainer().add(createChk);
		arkCrudContainerVO.getDetailPanelFormContainer().add(readChk);
		arkCrudContainerVO.getDetailPanelFormContainer().add(updateChk);
		arkCrudContainerVO.getDetailPanelFormContainer().add(deleteChk);
		add(arkCrudContainerVO.getDetailPanelFormContainer());
	}

	@Override
	protected void attachValidators() {
		// Set required field here
		arkRoleDropDown.setRequired(true);
	}

	protected void onSave(Form<AdminVO> containerForm, AjaxRequestTarget target) {
		// Save each object/permission
		ArkRolePolicyTemplate arkRolePolicyTemplate = containerForm.getModelObject().getArkRolePolicyTemplate();
		ArkRole arkRole = containerForm.getModelObject().getArkRolePolicyTemplate().getArkRole();
		ArkModule arkModule = containerForm.getModelObject().getArkRolePolicyTemplate().getArkModule();
		ArkFunction arkFunction = containerForm.getModelObject().getArkRolePolicyTemplate().getArkFunction();
		ArkPermission arkPermission = new ArkPermission();

		// Create permission
		if (containerForm.getModelObject().getArkCreatePermission()) {
			AdminVO adminVo = new AdminVO();
			adminVo.setArkRole(arkRole);
			adminVo.setArkModule(arkModule);
			adminVo.setArkFunction(arkFunction);
			adminVo.setArkRolePolicyTemplate(arkRolePolicyTemplate);
			arkPermission = iAdminService.getArkPermissionByName(au.org.theark.core.security.PermissionConstants.CREATE);
			arkRolePolicyTemplate.setArkPermission(arkPermission);
			iAdminService.createOrUpdateArkRolePolicyTemplate(adminVo);
		}

		// Read permission
		if (containerForm.getModelObject().getArkReadPermission()) {
			AdminVO adminVo = new AdminVO();
			adminVo.setArkRole(arkRole);
			adminVo.setArkModule(arkModule);
			adminVo.setArkFunction(arkFunction);
			adminVo.setArkRolePolicyTemplate(arkRolePolicyTemplate);
			arkPermission = iAdminService.getArkPermissionByName(au.org.theark.core.security.PermissionConstants.READ);
			arkRolePolicyTemplate.setArkPermission(arkPermission);
			iAdminService.createOrUpdateArkRolePolicyTemplate(adminVo);
		}

		// Update permission
		if (containerForm.getModelObject().getArkUpdatePermission()) {
			AdminVO adminVo = new AdminVO();
			adminVo.setArkRole(arkRole);
			adminVo.setArkModule(arkModule);
			adminVo.setArkFunction(arkFunction);
			adminVo.setArkRolePolicyTemplate(arkRolePolicyTemplate);
			arkPermission = iAdminService.getArkPermissionByName(au.org.theark.core.security.PermissionConstants.UPDATE);
			arkRolePolicyTemplate.setArkPermission(arkPermission);
			iAdminService.createOrUpdateArkRolePolicyTemplate(adminVo);
		}

		// Delete permission
		if (containerForm.getModelObject().getArkDeletePermission()) {
			AdminVO adminVo = new AdminVO();
			adminVo.setArkRole(arkRole);
			adminVo.setArkModule(arkModule);
			adminVo.setArkFunction(arkFunction);
			adminVo.setArkRolePolicyTemplate(arkRolePolicyTemplate);
			arkPermission = iAdminService.getArkPermissionByName(au.org.theark.core.security.PermissionConstants.DELETE);
			arkRolePolicyTemplate.setArkPermission(arkPermission);
			iAdminService.createOrUpdateArkRolePolicyTemplate(adminVo);
		}

		this.info("Ark Role Policy for Function: " + containerForm.getModelObject().getArkRolePolicyTemplate().getArkFunction().getName() + " was created/updated successfully.");
		target.add(feedBackPanel);
	}

	protected void onCancel(AjaxRequestTarget target) {
		containerForm.setModelObject(new AdminVO());
	}

	protected void onDeleteConfirmed(AjaxRequestTarget target, String selection) {
		// Delete
		iAdminService.deleteArkRolePolicyTemplate(containerForm.getModelObject());

		this.info("Ark Role Policy for Function: " + containerForm.getModelObject().getArkRolePolicyTemplate().getArkFunction().getName() + " was deleted successfully.");
		target.add(feedBackPanel);
		editCancelProcess(target);
		
	}

	protected void processErrors(AjaxRequestTarget target) {
		target.add(feedBackPanel);
	}

	protected boolean isNew() {
		if (containerForm.getModelObject().getArkRolePolicyTemplate().getId() == null) {
			return true;
		}
		else {
			return false;
		}
	}
}
