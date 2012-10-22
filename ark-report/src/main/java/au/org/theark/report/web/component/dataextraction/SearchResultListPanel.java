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
package au.org.theark.report.web.component.dataextraction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.report.entity.DemographicField;
import au.org.theark.core.model.report.entity.DemographicFieldSearch;
import au.org.theark.core.model.report.entity.Search;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.SearchVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.report.web.component.dataextraction.form.ContainerForm;

public class SearchResultListPanel extends Panel {


	private static final long	serialVersionUID	= 1L;

	private ContainerForm		containerForm;
	private ArkCrudContainerVO	arkCrudContainerVO;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService				iArkCommonService;
	
	/**
	 * 
	 * @param id
	 * @param crudContainerVO
	 * @param searchContainerForm
	 */
	public SearchResultListPanel(String id, ArkCrudContainerVO crudContainerVO, ContainerForm searchContainerForm) {
		super(id);
		arkCrudContainerVO = crudContainerVO;
		containerForm = searchContainerForm;
	}

	/**
	 * 
	 * @param iModel
	 * @param searchContainer
	 * @return
	 */
	public PageableListView<Search> buildPageableListView(IModel iModel) {

		PageableListView<Search> sitePageableListView = new PageableListView<Search>("searchList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {

			private static final long	serialVersionUID	= 1L;

			@Override
			protected void populateItem(final ListItem<Search> item) {

				Search search = item.getModelObject();

				/* The Search ID */
				if (search.getId() != null) {
					// Add the study Component Key here
					item.add(new Label("search.id", search.getId().toString()));
				}
				else {
					item.add(new Label("search.id", ""));
				}
				/* Search Name Link */
				item.add(buildLink(search));

				/* The Search Name
				if (search.getName() != null) {
					item.add(new Label("search.name", search.getName()));
				}
				else {
					item.add(new Label("search.name", "no name given"));
				} */
				
				// TODO when displaying text escape any special characters
				/* Description *
				if (search.getDescription() != null) {
					item.add(new Label("search.description", search.getDescription()));// the ID here must match the ones in mark-up
				}
				else {
					item.add(new Label("search.description", ""));// the ID here must match the ones in mark-up
				}*/

				/* For the alternative stripes */
				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					private static final long	serialVersionUID	= 1L;

					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));

			}
		
		};
		return sitePageableListView;
	}

	@SuppressWarnings( { "unchecked", "serial" })
	private AjaxLink buildLink(final Search search) {

		ArkBusyAjaxLink link = new ArkBusyAjaxLink("search.name") {

			@Override
			public void onClick(AjaxRequestTarget target) {

				SearchVO searchVo = containerForm.getModelObject();
				//searchVo.setMode(Constants.MODE_EDIT);
				searchVo.setSearch(search);// Sets the selected object into the model
				Collection<DemographicField> availableDemographicFields = iArkCommonService.getAllDemographicFields();
				containerForm.getModelObject().setAvailableDemographicFields(availableDemographicFields);
				Collection<DemographicField> selectedDemographicFields =iArkCommonService.getSelectedDemographicFieldsForSearch(search, true);
				containerForm.getModelObject().setSelectedDemographicFields(selectedDemographicFields);

				// Render the UI
				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);
			}
		};

		// Add the label for the link
		Label nameLinkLabel = new Label("nameLbl", search.getName());
		link.add(nameLinkLabel);
		return link;

	}

}
