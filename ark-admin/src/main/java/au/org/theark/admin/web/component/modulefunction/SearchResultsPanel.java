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
package au.org.theark.admin.web.component.modulefunction;

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
import au.org.theark.admin.web.component.modulefunction.form.ContainerForm;
import au.org.theark.core.model.study.entity.ArkModuleFunction;
import au.org.theark.core.vo.ArkCrudContainerVO;
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
	public DataView<ArkModuleFunction> buildDataView(ArkDataProvider<ArkModuleFunction, IAdminService> dataProvider) {
		DataView<ArkModuleFunction> dataView = new DataView<ArkModuleFunction>("arkModuleFunctionList", dataProvider) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 2981419595326128410L;

			@Override
			protected void populateItem(final Item<ArkModuleFunction> item) {
				ArkModuleFunction arkModuleFunction = item.getModelObject();

				item.add(buildLink(arkModuleFunction));

				if (arkModuleFunction.getArkModule() != null) {
					// the ID here must match the ones in mark-up
					item.add(new Label("arkModuleFunction.arkModule", arkModuleFunction.getArkModule().getName()));
				}
				else {
					item.add(new Label("arkModuleFunction.arkModule", ""));
				}

				if (arkModuleFunction.getArkFunction() != null) {
					// the ID here must match the ones in mark-up
					item.add(new Label("arkModuleFunction.arkFunction", arkModuleFunction.getArkFunction().getName()));
				}
				else {
					item.add(new Label("arkModuleFunction.arkFunction", ""));
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
	private AjaxLink buildLink(final ArkModuleFunction arkModuleFunction) {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("link") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				Long id = arkModuleFunction.getId();
				ArkModuleFunction arkModuleFunction = iAdminService.getArkModuleFunction(id);
				containerForm.getModelObject().setArkModuleFunction(arkModuleFunction);

				arkCrudContainerVo.getSearchResultPanelContainer().setVisible(false);
				arkCrudContainerVo.getSearchPanelContainer().setVisible(false);
				arkCrudContainerVo.getDetailPanelContainer().setVisible(true);
				arkCrudContainerVo.getDetailPanelFormContainer().setEnabled(false);
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
		};

		// Add the label for the link
		Label linkLabel = new Label("arkModuleFunction.id", arkModuleFunction.getId().toString());
		link.add(linkLabel);
		return link;
	}
}
