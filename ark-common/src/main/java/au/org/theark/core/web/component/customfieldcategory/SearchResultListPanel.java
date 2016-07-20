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
package au.org.theark.core.web.component.customfieldcategory;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldCategory;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.CustomFieldCategoryOrderingHelper;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldCategoryVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.core.web.component.export.ExportToolbar;
import au.org.theark.core.web.component.export.ExportableTextColumn;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;

/**
 * @author elam
 * 
 */
@SuppressWarnings( { "unchecked", "serial" })
public class SearchResultListPanel extends Panel {

	private static final long							serialVersionUID	= -1L;
	private CompoundPropertyModel<CustomFieldCategoryVO>		cpModel;
	private FeedbackPanel								feedbackPanel;
	private ArkCrudContainerVO							arkCrudContainerVO;

	@SpringBean(name = au.org.theark.core.Constants.ARK_COMMON_SERVICE)
	private IArkCommonService		iArkCommonService;
	
	/**
	 * Constructor
	 * @param id
	 * @param cpModel
	 * @param arkCrudContainerVO
	 * @param feedBackPanel
	 * @param unitTypeDropDownOn
	 */
	public SearchResultListPanel(String id, CompoundPropertyModel<CustomFieldCategoryVO> cpModel, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel){
		super(id);
		this.cpModel = cpModel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedBackPanel;
	}

	public DataView<CustomFieldCategory> buildDataView(ArkDataProvider2<CustomFieldCategory, CustomFieldCategory> subjectProvider) {

		DataView<CustomFieldCategory> customFieldCategoryDataView = new DataView<CustomFieldCategory>("customFieldCategoryList", subjectProvider) {
			@Override
			protected void populateItem(final Item<CustomFieldCategory> item) {
				CustomFieldCategory category = item.getModelObject();
				//id
				if (category.getId() != null) {
					item.add(new Label(Constants.CUSTOMFIELDCATEGORY_ID, category.getId().toString()));
				}
				else {
					item.add(new Label(Constants.CUSTOMFIELDCATEGORY_ID, ""));
				}
				// Component Name Link
				item.add(buildLinkWMC(item));
				
				
				//Custom Field Name
				if (category.getCustomFieldType() != null) {
					item.add(new Label(Constants.FIELDVO_CUSTOMFIELDCATEGORY_CUSTOM_FIELD_TYPE_NAME, category.getCustomFieldType().getName()));
				}
				else {
					item.add(new Label(Constants.FIELDVO_CUSTOMFIELDCATEGORY_CUSTOM_FIELD_TYPE_NAME, ""));
				}
				
				// Description
				if (category.getDescription() != null) {
					item.add(new Label(Constants.CUSTOMFIELDCATEGORY_DESCRIPTION, category.getDescription()));
				}
				else {
					item.add(new Label(Constants.CUSTOMFIELDCATEGORY_DESCRIPTION, ""));
				}
				//Parent Category
				if (category.getParentCategory() != null) {
					item.add(new Label(Constants.FIELDVO_CUSTOMFIELDCATEGORY_PARENTCATEGORY_NAME, category.getParentCategory().getName()));
				}
				else {
					item.add(new Label(Constants.FIELDVO_CUSTOMFIELDCATEGORY_PARENTCATEGORY_NAME, ""));
				}
				
				//Order Number
				if (category.getOrderNumber() != null) {
					item.add(new Label(Constants.CUSTOMFIELDCATEGORY_ORDERNUMBER, category.getOrderNumber().toString()));
				}
				else {
					item.add(new Label(Constants.CUSTOMFIELDCATEGORY_ORDERNUMBER, ""));
				}
			
				/* For the alternative stripes */
				item.add(new AttributeModifier("class", new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));

			}
		};

		addToolbars(customFieldCategoryDataView);

		return customFieldCategoryDataView;
	}

	private WebMarkupContainer buildLinkWMC(final Item<CustomFieldCategory> item) {

		WebMarkupContainer customfieldCategoryLinkWMC = new WebMarkupContainer("customfieldCategoryLinkWMC", item.getModel());
		ArkBusyAjaxLink link = new ArkBusyAjaxLink(Constants.CUSTOMFIELDCATEGORY_NAME) {
			@Override
			public void onClick(AjaxRequestTarget target) {
				// Sets the selected object into the model
				CustomFieldCategory cf = (CustomFieldCategory) (getParent().getDefaultModelObject());
				CompoundPropertyModel<CustomFieldCategoryVO> newModel = new CompoundPropertyModel<CustomFieldCategoryVO>(new CustomFieldCategoryVO());
				newModel.getObject().setCustomFieldCategory(cf);
				//newModel.getObject().setUseCustomFieldCategoryDisplay(cpModel.getObject().isUseCustomFieldCategoryDisplay());

				DetailPanel detailPanel = new DetailPanel("detailPanel", feedbackPanel, newModel, arkCrudContainerVO);
				arkCrudContainerVO.getDetailPanelContainer().addOrReplace(detailPanel);
				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);
				//Added on 2016-05-26 to stop showing the previous feed back message when deleting or updating in the form.
				//This will clear the feedback message.
				Session.get().cleanupFeedbackMessages();
				target.add(feedbackPanel);

			}
		};

		// Add the label for the link
		CustomFieldCategory category = item.getModelObject();
		//Label nameLinkLabel = new Label("nameLbl", category.getName());
		Label nameLinkLabel = new Label("nameLbl",CustomFieldCategoryOrderingHelper.getInstance().preTextDecider(category).concat(category.getName()));
		link.add(nameLinkLabel);
		customfieldCategoryLinkWMC.add(link);
		return customfieldCategoryLinkWMC;
	}

	private void addToolbars(DataView<CustomFieldCategory> customFieldCategoryDataView) {
		List<IColumn<CustomFieldCategory>> columns = new ArrayList<IColumn<CustomFieldCategory>>();
		columns.add(new ExportableTextColumn<CustomFieldCategory>(Model.of("name"), "name"));
		columns.add(new ExportableTextColumn<CustomFieldCategory>(Model.of("customFieldType"), "customFieldType.name"));
		columns.add(new ExportableTextColumn<CustomFieldCategory>(Model.of("description"), "description"));
		columns.add(new ExportableTextColumn<CustomFieldCategory>(Model.of("parentCategory"), "parentCategory.name"));
		columns.add(new ExportableTextColumn<CustomFieldCategory>(Model.of("orderNumber"), "orderNumber"));
		
		DataTable table = new DataTable("datatable", columns, customFieldCategoryDataView.getDataProvider(), iArkCommonService.getUserConfig(au.org.theark.core.Constants.CONFIG_ROWS_PER_PAGE).getIntValue());
		List<String> headers = new ArrayList<String>(0);
		headers.add("FIELD_NAME");
		headers.add("CUSTOM_FIELD_TYPE");
		headers.add("DESCRIPTION");
		headers.add("PARENT_CATEGORY");
		headers.add("ORDER_NUMBER");
		
		String filename = "data_dictionary";
		RepeatingView toolbars = new RepeatingView("toolbars");
		ExportToolbar<String> exportToolBar = new ExportToolbar<String>(table, headers, filename);
		toolbars.add(new Component[] { exportToolBar });
		add(toolbars);
	}
	
}
