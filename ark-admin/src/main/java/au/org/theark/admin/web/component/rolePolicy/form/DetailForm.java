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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.admin.model.vo.ArkRoleModuleFunctionVO;
import au.org.theark.admin.service.IAdminService;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkPermission;
import au.org.theark.core.model.study.entity.ArkRole;
import au.org.theark.core.model.study.entity.ArkRolePolicyTemplate;
import au.org.theark.core.model.study.entity.ArkUserRole;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractDetailForm;

public class DetailForm extends AbstractDetailForm<AdminVO> {
	/**
	 * 
	 */
	private static final long				serialVersionUID	= 5096967681735723818L;
	protected transient Logger				log					= LoggerFactory.getLogger(DetailForm.class);

	@SpringBean(name = au.org.theark.admin.service.Constants.ARK_ADMIN_SERVICE)
	private IAdminService<Void>			iAdminService;

	private int									mode;
	private DropDownChoice<ArkRole>		arkRoleDropDown;
	private DropDownChoice<ArkModule>	arkModuleDropDown;
	@SuppressWarnings("unchecked")
	private PageableListView				pageableListView;

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

	@SuppressWarnings("unchecked")
	public void initialiseDetailForm() {
		// Role selection
		initArkRoleDropDown();

		// Module selection
		initArkModuleDropDown();

		IModel<List<ArkUserRole>> iModel = new LoadableDetachableModel() {
			private static final long	serialVersionUID	= 1L;

			@Override
			protected Object load() {
				return getModelObject().getArkRoleModuleFunctionVoList();
			}
		};

		// Maybe user getModelObject().getArkRolePolicyTemplateList().size()
		pageableListView = new PageableListView("arkRoleModuleFunctionVoList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem item) {
				ArkRoleModuleFunctionVO	arkRoleModuleFunctionVo = (ArkRoleModuleFunctionVO) item.getModelObject();
				item.add(new Label("arkFunction.name", arkRoleModuleFunctionVo.getArkFunction().getName()));
				item.addOrReplace(new CheckBox("arkCreatePermission"));
				item.addOrReplace(new CheckBox("arkReadPermission"));
				item.addOrReplace(new CheckBox("arkUpdatePermission"));
				item.addOrReplace(new CheckBox("arkDeletePermission"));
				item.setEnabled(false);

				item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {

					/**
					 * 
					 */
					private static final long	serialVersionUID	= -3761802307040022900L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};

		pageableListView.setReuseItems(true);

		PagingNavigator pageNavigator = new PagingNavigator("navigator", pageableListView);
		add(pageNavigator);

		attachValidators();
		addDetailFormComponents();
	}

	@SuppressWarnings("unchecked")
	private void initArkRoleDropDown() {
		List<ArkRole> arkRoleList = iAdminService.getArkRoleList();
		ChoiceRenderer<ArkRole> defaultChoiceRenderer = new ChoiceRenderer<ArkRole>("name", "id");
		arkRoleDropDown = new DropDownChoice("arkRolePolicyTemplate.arkRole", arkRoleList, defaultChoiceRenderer);
		arkRoleDropDown.add(new AjaxFormComponentUpdatingBehavior("onChange") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 5591846326218931210L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {

			}
		});
		arkRoleDropDown.setEnabled(false);
	}

	@SuppressWarnings("unchecked")
	private void initArkModuleDropDown() {
		List<ArkModule> arkModuleList = iAdminService.getArkModuleList();
		ChoiceRenderer<ArkModule> defaultChoiceRenderer = new ChoiceRenderer<ArkModule>("name", "id");
		arkModuleDropDown = new DropDownChoice("arkRolePolicyTemplate.arkModule", arkModuleList, defaultChoiceRenderer);
		arkModuleDropDown.add(new AjaxFormComponentUpdatingBehavior("onChange") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -1917750577626157879L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {

			}
		});
		arkModuleDropDown.setEnabled(false);
	}

	private void addDetailFormComponents() {
		arkCrudContainerVO.getDetailPanelFormContainer().add(arkRoleDropDown);
		arkCrudContainerVO.getDetailPanelFormContainer().add(arkModuleDropDown);
		arkCrudContainerVO.getDetailPanelFormContainer().add(pageableListView);
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

	/**
	 * @return the mode
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * @param mode
	 *           the mode to set
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}
}
