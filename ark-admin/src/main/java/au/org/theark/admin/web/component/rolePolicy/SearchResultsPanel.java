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
package au.org.theark.admin.web.component.rolePolicy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.admin.model.vo.ArkRoleModuleFunctionVO;
import au.org.theark.admin.service.IAdminService;
import au.org.theark.admin.web.component.rolePolicy.form.ContainerForm;
import au.org.theark.core.model.study.entity.ArkPermission;
import au.org.theark.core.model.study.entity.ArkRolePolicyTemplate;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkDataProvider;

public class SearchResultsPanel extends Panel {
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 5237384531161620862L;
	protected transient Logger		log					= LoggerFactory.getLogger(SearchResultsPanel.class);
	@SpringBean(name = au.org.theark.admin.service.Constants.ARK_ADMIN_SERVICE)
	private IAdminService<Void>	iAdminService;
	private FeedbackPanel			feedbackPanel;

	public SearchResultsPanel(String id, ContainerForm containerForm, FeedbackPanel feedbackPanel, ArkCrudContainerVO arkCrudContainerVo) {
		super(id);
		this.feedbackPanel = feedbackPanel;
		setOutputMarkupId(true);
		setOutputMarkupPlaceholderTag(true);
	}

	@SuppressWarnings("unchecked")
	public DataView<ArkRoleModuleFunctionVO> buildDataView(ArkDataProvider<ArkRoleModuleFunctionVO, IAdminService> dataProvider) {
		DataView<ArkRoleModuleFunctionVO> dataView = new DataView<ArkRoleModuleFunctionVO>("arkRoleModuleFunctionVoList", dataProvider) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= -7977497161071264676L;

			@Override
			protected void populateItem(final Item<ArkRoleModuleFunctionVO> item) {

				final ArkRoleModuleFunctionVO arkRoleModuleFunctionVo = item.getModelObject();
				item.setOutputMarkupId(true);
				item.setOutputMarkupPlaceholderTag(true);

				WebMarkupContainer rowEditWMC = new WebMarkupContainer("rowEditWMC", item.getModel());
				AjaxButton listEditButton = new AjaxButton("listEditButton", new StringResourceModel(au.org.theark.core.Constants.EDIT, this, null)) {
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 197521505680635043L;

					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
						item.setEnabled(true);
						item.get("arkCreatePermission").setEnabled(true);
						item.get("arkReadPermission").setEnabled(true);
						item.get("arkUpdatePermission").setEnabled(true);
						item.get("arkDeletePermission").setEnabled(true);
						item.get("rowSaveWMC").setVisible(true);
						target.add(item);
					}

					@Override
					protected void onError(AjaxRequestTarget target, Form<?> form) {
						log.error("onError called when listEditButton pressed");
					}
				};
				listEditButton.setDefaultFormProcessing(false);
				rowEditWMC.add(listEditButton);
				item.add(rowEditWMC);

				if (arkRoleModuleFunctionVo.getArkRole() != null) {
					item.add(new Label("arkRole", arkRoleModuleFunctionVo.getArkRole().getName()));
				}
				else {
					item.add(new Label("arkRole", ""));
				}

				if (arkRoleModuleFunctionVo.getArkModule() != null) {
					item.add(new Label("arkModule", arkRoleModuleFunctionVo.getArkModule().getName()));
				}
				else {
					item.add(new Label("arkModule", ""));
				}

				if (arkRoleModuleFunctionVo.getArkFunction() != null) {
					item.add(new Label("arkFunction", arkRoleModuleFunctionVo.getArkFunction().getName()));
				}
				else {
					item.add(new Label("arkFunction", ""));
				}

				item.addOrReplace(new CheckBox("arkCreatePermission", new PropertyModel(arkRoleModuleFunctionVo, "arkCreatePermission")).setEnabled(false));
				item.addOrReplace(new CheckBox("arkReadPermission", new PropertyModel(arkRoleModuleFunctionVo, "arkReadPermission")).setEnabled(false));
				item.addOrReplace(new CheckBox("arkUpdatePermission", new PropertyModel(arkRoleModuleFunctionVo, "arkUpdatePermission")).setEnabled(false));
				item.addOrReplace(new CheckBox("arkDeletePermission", new PropertyModel(arkRoleModuleFunctionVo, "arkDeletePermission")).setEnabled(false));

				WebMarkupContainer rowSaveWMC = new WebMarkupContainer("rowSaveWMC", item.getModel());
				AjaxButton listSaveButton = new AjaxButton("listSaveButton", new StringResourceModel(au.org.theark.core.Constants.SAVE, this, null)) {
					/**
					 * 
					 */
					private static final long	serialVersionUID	= -7382768898426488999L;

					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
						onSave(target, arkRoleModuleFunctionVo);
						target.add(SearchResultsPanel.this);
						target.add(feedbackPanel);
					}

					@Override
					protected void onError(AjaxRequestTarget target, Form<?> form) {
						log.error("onError called when listSaveButton pressed");
					}
				};
				rowSaveWMC.add(listSaveButton);
				rowSaveWMC.setVisible(false);
				item.add(rowSaveWMC);

				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 1938679383897533820L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};
		return dataView;
	}

	/**
	 * Execute the save for the item (arkRoleModuleFunctionVo) selected
	 * 
	 * @param target
	 * @param arkRoleModuleFunctionVo
	 */
	private void onSave(AjaxRequestTarget target, final ArkRoleModuleFunctionVO arkRoleModuleFunctionVo) {
		List<String> permissionList = new ArrayList<String>(4);
		
		// Delete all permissions first
		ArkRolePolicyTemplate arkRolePolicyTemplateToDelete = new ArkRolePolicyTemplate();
		arkRolePolicyTemplateToDelete.setArkRole(arkRoleModuleFunctionVo.getArkRole());
		arkRolePolicyTemplateToDelete.setArkModule(arkRoleModuleFunctionVo.getArkModule());
		arkRolePolicyTemplateToDelete.setArkFunction(arkRoleModuleFunctionVo.getArkFunction());

		List<ArkRolePolicyTemplate> arkRolePolicyTemplateList = iAdminService.getArkRolePolicyTemplateList(arkRolePolicyTemplateToDelete);
		for (Iterator<ArkRolePolicyTemplate> iterator = arkRolePolicyTemplateList.iterator(); iterator.hasNext();) {
			ArkRolePolicyTemplate rowToDelete = (ArkRolePolicyTemplate) iterator.next();
			AdminVO adminVoToDelete = new AdminVO();
			adminVoToDelete.setArkRolePolicyTemplate(rowToDelete);
			iAdminService.deleteArkRolePolicyTemplate(adminVoToDelete);
		}

		// Save each permission where selected
		if (arkRoleModuleFunctionVo.getArkCreatePermission()) {
			AdminVO adminVoCreatePermission = new AdminVO();
			ArkRolePolicyTemplate arkRolePolicyTemplate = new ArkRolePolicyTemplate();
			arkRolePolicyTemplate.setArkRole(arkRoleModuleFunctionVo.getArkRole());
			arkRolePolicyTemplate.setArkModule(arkRoleModuleFunctionVo.getArkModule());
			arkRolePolicyTemplate.setArkFunction(arkRoleModuleFunctionVo.getArkFunction());
			ArkPermission arkPermission = new ArkPermission();
			arkPermission = iAdminService.getArkPermissionByName(au.org.theark.core.security.PermissionConstants.CREATE);
			arkRolePolicyTemplate.setArkPermission(arkPermission);
			adminVoCreatePermission.setArkRolePolicyTemplate(arkRolePolicyTemplate);
			iAdminService.createArkRolePolicyTemplate(adminVoCreatePermission);
			permissionList.add(arkPermission.getName());
		}

		if (arkRoleModuleFunctionVo.getArkReadPermission()) {
			AdminVO adminVoReadPermission = new AdminVO();
			ArkRolePolicyTemplate arkRolePolicyTemplate = new ArkRolePolicyTemplate();
			arkRolePolicyTemplate.setArkRole(arkRoleModuleFunctionVo.getArkRole());
			arkRolePolicyTemplate.setArkModule(arkRoleModuleFunctionVo.getArkModule());
			arkRolePolicyTemplate.setArkFunction(arkRoleModuleFunctionVo.getArkFunction());
			ArkPermission arkPermission = new ArkPermission();
			arkPermission = iAdminService.getArkPermissionByName(au.org.theark.core.security.PermissionConstants.READ);
			arkRolePolicyTemplate.setArkPermission(arkPermission);
			adminVoReadPermission = new AdminVO();
			adminVoReadPermission.setArkRolePolicyTemplate(arkRolePolicyTemplate);
			iAdminService.createArkRolePolicyTemplate(adminVoReadPermission);
			permissionList.add(arkPermission.getName());
		}

		if (arkRoleModuleFunctionVo.getArkUpdatePermission()) {
			AdminVO adminVoUpdatePermission = new AdminVO();
			ArkRolePolicyTemplate arkRolePolicyTemplate = new ArkRolePolicyTemplate();
			arkRolePolicyTemplate.setArkRole(arkRoleModuleFunctionVo.getArkRole());
			arkRolePolicyTemplate.setArkModule(arkRoleModuleFunctionVo.getArkModule());
			arkRolePolicyTemplate.setArkFunction(arkRoleModuleFunctionVo.getArkFunction());
			ArkPermission arkPermission = new ArkPermission();
			arkPermission = iAdminService.getArkPermissionByName(au.org.theark.core.security.PermissionConstants.UPDATE);
			arkRolePolicyTemplate.setArkPermission(arkPermission);
			adminVoUpdatePermission = new AdminVO();
			adminVoUpdatePermission.setArkRolePolicyTemplate(arkRolePolicyTemplate);
			iAdminService.createArkRolePolicyTemplate(adminVoUpdatePermission);
			permissionList.add(arkPermission.getName());
		}

		if (arkRoleModuleFunctionVo.getArkDeletePermission()) {
			AdminVO adminVoDeletePermission = new AdminVO();
			ArkRolePolicyTemplate arkRolePolicyTemplate = new ArkRolePolicyTemplate();
			arkRolePolicyTemplate.setArkRole(arkRoleModuleFunctionVo.getArkRole());
			arkRolePolicyTemplate.setArkModule(arkRoleModuleFunctionVo.getArkModule());
			arkRolePolicyTemplate.setArkFunction(arkRoleModuleFunctionVo.getArkFunction());
			ArkPermission arkPermission = new ArkPermission();
			arkPermission = iAdminService.getArkPermissionByName(au.org.theark.core.security.PermissionConstants.DELETE);
			arkRolePolicyTemplate.setArkPermission(arkPermission);
			adminVoDeletePermission = new AdminVO();
			adminVoDeletePermission.setArkRolePolicyTemplate(arkRolePolicyTemplate);
			iAdminService.createArkRolePolicyTemplate(adminVoDeletePermission);
			permissionList.add(arkPermission.getName());
		}
		this.info("Role Policy for function " + arkRoleModuleFunctionVo.getArkFunction().getName() + " updated with the following permissions: " + permissionList);
	}
}
