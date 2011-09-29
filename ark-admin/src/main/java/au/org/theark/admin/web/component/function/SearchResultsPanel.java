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
package au.org.theark.admin.web.component.function;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.admin.service.IAdminService;
import au.org.theark.admin.web.component.function.form.ContainerForm;
import au.org.theark.core.model.study.entity.ArkFunction;
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
	public DataView<ArkFunction> buildDataView(ArkDataProvider<ArkFunction, IAdminService> dataProvider) {
		DataView<ArkFunction> dataView = new DataView<ArkFunction>("arkFunctionList", dataProvider) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 2981419595326128410L;

			@Override
			protected void populateItem(final Item<ArkFunction> item) {
				ArkFunction arkFunction = item.getModelObject();

				item.add(buildLink(arkFunction));

				if (arkFunction.getName() != null) {
					// the ID here must match the ones in mark-up
					item.add(new Label("arkFunction.name", arkFunction.getName()));
				}
				else {
					item.add(new Label("arkFunction.name", ""));
				}

				if (arkFunction.getDescription() != null) {
					// the ID here must match the ones in mark-up
					item.add(new Label("arkFunction.description", arkFunction.getDescription()));
				}
				else {
					item.add(new Label("arkFunction.description", ""));
				}
				
				if (arkFunction.getArkFunctionType() != null) {
					// the ID here must match the ones in mark-up
					item.add(new Label("arkFunction.arkFunctionType", arkFunction.getArkFunctionType().getName()));
				}
				else {
					item.add(new Label("arkFunction.arkFunctionType", ""));
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

	@SuppressWarnings("unchecked")
	public PageableListView<ArkFunction> buildPageableListView(IModel iModel, final WebMarkupContainer searchResultsContainer) {
		PageableListView<ArkFunction> pageableListView = new PageableListView<ArkFunction>("arkFunctionList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 3350183112731574263L;

			@Override
			protected void populateItem(final ListItem<ArkFunction> item) {

				ArkFunction arkFunction = item.getModelObject();

				item.add(buildLink(arkFunction));

				if (arkFunction.getName() != null) {
					// the ID here must match the ones in mark-up
					item.add(new Label("arkFunction.name", arkFunction.getName()));
				}
				else {
					item.add(new Label("arkFunction.name", ""));
				}

				if (arkFunction.getDescription() != null) {
					// the ID here must match the ones in mark-up
					item.add(new Label("arkFunction.description", arkFunction.getDescription()));
				}
				else {
					item.add(new Label("arkFunction.description", ""));
				}

				if (arkFunction.getArkFunctionType() != null) {
					// the ID here must match the ones in mark-up
					item.add(new Label("arkFunction.arkFunctionType", arkFunction.getArkFunctionType().getName()));
				}
				else {
					item.add(new Label("arkFunction.arkFunctionType", ""));
				}

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
		return pageableListView;
	}

	private AjaxLink<ArkFunction> buildLink(final ArkFunction arkFunction) {
		ArkBusyAjaxLink<ArkFunction> link = new ArkBusyAjaxLink<ArkFunction>("link") {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				Long id = arkFunction.getId();
				ArkFunction ArkFunction = iAdminService.getArkFunction(id);
				containerForm.getModelObject().setArkFunction(ArkFunction);

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
		Label linkLabel = new Label("arkFunction.id", arkFunction.getId().toString());
		link.add(linkLabel);
		return link;
	}
}
