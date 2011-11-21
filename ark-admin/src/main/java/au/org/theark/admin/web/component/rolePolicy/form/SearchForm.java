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
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.admin.model.vo.AdminVO;
import au.org.theark.admin.service.IAdminService;
import au.org.theark.admin.web.component.ContainerForm;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkRole;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.form.AbstractSearchForm;

public class SearchForm extends AbstractSearchForm<AdminVO> {
	/**
	 * 
	 */
	private static final long					serialVersionUID	= -204010204180506704L;

	@SpringBean(name = au.org.theark.admin.service.Constants.ARK_ADMIN_SERVICE)
	private IAdminService<Void>				iAdminService;

	private CompoundPropertyModel<AdminVO>	cpmModel;
	private ArkCrudContainerVO					arkCrudContainerVo;
	private ContainerForm						containerForm;
	private FeedbackPanel						feedbackPanel;
	private DropDownChoice<ArkRole>			arkRoleDropDown;
	private DropDownChoice<ArkModule>		arkModuleDropDown;

	/**
	 * Constructor
	 * @param id
	 * @param cpmModel
	 * @param arkCrudContainerVo
	 * @param feedbackPanel
	 * @param containerForm
	 */
	public SearchForm(String id, CompoundPropertyModel<AdminVO> cpmModel, ArkCrudContainerVO arkCrudContainerVo, FeedbackPanel feedbackPanel, ContainerForm containerForm) {
		super(id, cpmModel, feedbackPanel, arkCrudContainerVo);

		this.containerForm = containerForm;
		this.arkCrudContainerVo = arkCrudContainerVo;
		this.feedbackPanel = feedbackPanel;
		setMultiPart(true);

		this.setCpmModel(cpmModel);

		initialiseSearchForm();
		addSearchComponentsToForm();
	}

	protected void initialiseSearchForm() {
		initArkRoleDropDown();
		initArkModuleDropDown();
	}

	private void initArkRoleDropDown() {
		List<ArkRole> arkRoleList = iAdminService.getArkRoleList();
		// Restrict searching/selecting of Super Administrator
		arkRoleList.remove(iAdminService.getArkRoleByName(au.org.theark.core.security.RoleConstants.ARK_ROLE_SUPER_ADMINISTATOR));
		ChoiceRenderer<ArkRole> defaultChoiceRenderer = new ChoiceRenderer<ArkRole>("name", "id");
		arkRoleDropDown = new DropDownChoice<ArkRole>("arkRoleModuleFunctionVo.arkRole", arkRoleList, defaultChoiceRenderer);
		arkRoleDropDown.add(new AjaxFormComponentUpdatingBehavior("onChange") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 5591846326218931210L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				// Reset the Module list when the Role changes
				ArkRole arkRole = getModelObject().getArkRoleModuleFunctionVo().getArkRole();
				updateModuleDropDown(arkRole);
            target.add(arkModuleDropDown);
			}
		});
	}
	
	private void initArkModuleDropDown() {
		List<ArkModule> arkModuleList = iAdminService.getArkModuleList();
		ChoiceRenderer<ArkModule> defaultChoiceRenderer = new ChoiceRenderer<ArkModule>("name", "id");
		arkModuleDropDown = new DropDownChoice<ArkModule>("arkRoleModuleFunctionVo.arkModule", arkModuleList, defaultChoiceRenderer);
		arkModuleDropDown.setOutputMarkupPlaceholderTag(true);
	}
	
	protected void updateModuleDropDown(ArkRole arkRole) {
		List<ArkModule> arkModuleList = iAdminService.getArkModuleList(arkRole);
		arkModuleDropDown.getChoices().clear();
		arkModuleDropDown.setChoices(arkModuleList);
	}

	protected void onSearch(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		int count = iAdminService.getArkRoleModuleFunctionVOCount(containerForm.getModelObject().getArkRoleModuleFunctionVo());
		if (count == 0) {
			this.info("There are no records that matched your query. Please modify your filter");
			target.add(feedbackPanel);
		}

		arkCrudContainerVo.getSearchResultPanelContainer().setVisible(true);
		target.add(arkCrudContainerVo.getSearchResultPanelContainer());
	}

	private void addSearchComponentsToForm() {
		add(arkRoleDropDown);
		add(arkModuleDropDown);
	}

	protected void onNew(AjaxRequestTarget target) {
		target.add(feedbackPanel);
		containerForm.setModelObject(new AdminVO());
		arkCrudContainerVo.getSearchResultPanelContainer().setVisible(false);
		arkCrudContainerVo.getSearchPanelContainer().setVisible(false);
		arkCrudContainerVo.getDetailPanelContainer().setVisible(true);
		arkCrudContainerVo.getDetailPanelFormContainer().setEnabled(true);
		arkCrudContainerVo.getViewButtonContainer().setVisible(true);
		arkCrudContainerVo.getViewButtonContainer().setEnabled(true);
		arkCrudContainerVo.getEditButtonContainer().setVisible(false);

		// Refresh the markup containers
		target.add(arkCrudContainerVo.getSearchResultPanelContainer());
		target.add(arkCrudContainerVo.getDetailPanelContainer());
		target.add(arkCrudContainerVo.getDetailPanelFormContainer());
		target.add(arkCrudContainerVo.getSearchPanelContainer());
		target.add(arkCrudContainerVo.getViewButtonContainer());
		target.add(arkCrudContainerVo.getEditButtonContainer());

		// Refresh base container form to remove any feedBack messages
		target.add(containerForm);
	}

	/**
	 * @param cpmModel
	 *           the cpmModel to set
	 */
	public void setCpmModel(CompoundPropertyModel<AdminVO> cpmModel) {
		this.cpmModel = cpmModel;
	}

	/**
	 * @return the cpmModel
	 */
	public CompoundPropertyModel<AdminVO> getCpmModel() {
		return cpmModel;
	}
}
