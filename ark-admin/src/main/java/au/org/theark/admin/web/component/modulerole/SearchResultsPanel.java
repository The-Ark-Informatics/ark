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
package au.org.theark.admin.web.component.modulerole;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.admin.service.IAdminService;
import au.org.theark.admin.web.component.ContainerForm;
import au.org.theark.core.model.study.entity.ArkModuleRole;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.ArkDataProvider;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;

public class SearchResultsPanel extends Panel {
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 5237384531161620862L;
	protected transient Logger		log					= LoggerFactory.getLogger(SearchResultsPanel.class);

	@SpringBean(name = au.org.theark.admin.service.Constants.ARK_ADMIN_SERVICE)
	private IAdminService<Void>	iAdminService;

	private ContainerForm			containerForm;

	private ArkCrudContainerVO		arkCrudContainerVo;

	public SearchResultsPanel(String id, ContainerForm containerForm, ArkCrudContainerVO arkCrudContainerVo) {
		super(id);
		this.containerForm = containerForm;
		this.arkCrudContainerVo = arkCrudContainerVo;
	}

	@SuppressWarnings("unchecked")
	public DataView<ArkModuleRole> buildDataView(ArkDataProvider<ArkModuleRole, IAdminService> dataProvider) {
		DataView<ArkModuleRole> dataView = new DataView<ArkModuleRole>("arkModuleRoleList", dataProvider) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final Item<ArkModuleRole> item) {
				ArkModuleRole arkModuleRole = item.getModelObject();

				item.add(new Label("arkModuleRole.id", arkModuleRole.getId().toString()));
				
				item.add(buildLink(arkModuleRole));

				if (arkModuleRole.getArkRole() != null) {
					item.add(new Label("arkModuleRole.arkRole", arkModuleRole.getArkRole().getName()));
				}
				else {
					item.add(new Label("arkModuleRole.arkRole", ""));
				}

				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					/**
					 * 
					 */
					private static final long	serialVersionUID	= 5761909841047153853L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));
			}
		};
		return dataView;
	}

	@SuppressWarnings( { "unchecked", "serial" })
	private AjaxLink buildLink(final ArkModuleRole arkModuleRole) {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("link") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				Long id = arkModuleRole.getId();
				ArkModuleRole arkModuleRole = iAdminService.getArkModuleRole(id);
				containerForm.getModelObject().setArkModuleRole(arkModuleRole);
				containerForm.getModelObject().setAvailableArkRoles(iAdminService.getArkRoleList());
				containerForm.getModelObject().setSelectedArkRoles(iAdminService.getArkRoleListByArkModule(arkModuleRole.getArkModule()));
				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVo);
				// Refresh base container form to remove any feedBack messages
				target.add(containerForm);
			}
		};

		// Add the label for the link
		Label linkLabel = new Label("arkModuleRole.arkModule", arkModuleRole.getArkModule().getName());
		link.add(linkLabel);
		return link;
	}
}
