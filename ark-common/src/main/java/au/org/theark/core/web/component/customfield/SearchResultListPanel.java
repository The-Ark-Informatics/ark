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
package au.org.theark.core.web.component.customfield;

import java.util.ArrayList;
import java.util.List;

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
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldVO;
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
	private CompoundPropertyModel<CustomFieldVO>		cpModel;
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
	public SearchResultListPanel(String id, CompoundPropertyModel<CustomFieldVO> cpModel, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel) {
		super(id);
		this.cpModel = cpModel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedBackPanel;
		
	}

	public DataView<CustomField> buildDataView(ArkDataProvider2<CustomField, CustomField> subjectProvider) {

		DataView<CustomField> customFieldDataView = new DataView<CustomField>("customFieldList", subjectProvider) {

			@Override
			protected void populateItem(final Item<CustomField> item) {
				CustomField field = item.getModelObject();

				if (field.getId() != null) {
					// Add the id component here
					item.add(new Label(Constants.CUSTOMFIELD_ID, field.getId().toString()));
				}
				else {
					item.add(new Label(Constants.CUSTOMFIELD_ID, ""));
				}

				// Component Name Link
				item.add(buildLinkWMC(item));
				// Field Type
				if (field.getFieldType() != null) {
					item.add(new Label(Constants.CUSTOMFIELD_FIELD_TYPE, field.getFieldType().getName()));
				}
				else {
					item.add(new Label(Constants.CUSTOMFIELD_FIELD_TYPE, ""));
				}
				// Field Label
				if (field.getFieldLabel() != null) {
					item.add(new Label(Constants.CUSTOMFIELD_FIELD_LABEL, field.getFieldLabel()));
				}
				else {
					item.add(new Label(Constants.CUSTOMFIELD_FIELD_LABEL, ""));
				}
				// custom field type
				if (field.getCustomFieldType() != null) {
					item.add(new Label(Constants.CUSTOMFIELD_CUSTOME_FIELD_TYPE, field.getCustomFieldType().getName()));
				}
				else {
					item.add(new Label(Constants.CUSTOMFIELD_CUSTOME_FIELD_TYPE, ""));
				}
				//custom file category
				if (field.getCustomFieldCategory() != null) {
					item.add(new Label(Constants.CUSTOMFIELD_CATEGORY, field.getCustomFieldCategory().getName()));
				}
				else {
					item.add(new Label(Constants.CUSTOMFIELD_CATEGORY, ""));
				}
				//custom file category order number
				if (field.getCustomFieldCategory()!=null && field.getCustomFieldCategory().getOrderNumber() != null) {
					item.add(new Label(Constants.CUSTOMFIELD_CATEGORY_ORDERNUMBER, field.getCustomFieldCategory().getOrderNumber().toString()));
				}
				else {
					item.add(new Label(Constants.CUSTOMFIELD_CATEGORY_ORDERNUMBER, ""));
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

		addToolbars(customFieldDataView);

		return customFieldDataView;
	}

	private WebMarkupContainer buildLinkWMC(final Item<CustomField> item) {

		WebMarkupContainer customfieldLinkWMC = new WebMarkupContainer("customfieldLinkWMC", item.getModel());
		ArkBusyAjaxLink link = new ArkBusyAjaxLink(Constants.CUSTOMFIELD_NAME) {
			@Override
			public void onClick(AjaxRequestTarget target) {
				// Sets the selected object into the model
				CustomField cf = (CustomField) (getParent().getDefaultModelObject());
				CompoundPropertyModel<CustomFieldVO> newModel = new CompoundPropertyModel<CustomFieldVO>(new CustomFieldVO());
				newModel.getObject().setCustomField(cf);
				newModel.getObject().setUseCustomFieldDisplay(cpModel.getObject().isUseCustomFieldDisplay());

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
		CustomField field = item.getModelObject();
		Label nameLinkLabel = new Label("nameLbl", field.getName());
		link.add(nameLinkLabel);
		customfieldLinkWMC.add(link);
		return customfieldLinkWMC;
	}

	private void addToolbars(DataView<CustomField> customFieldDataView) {
		List<IColumn<CustomField>> columns = new ArrayList<IColumn<CustomField>>();
		columns.add(new ExportableTextColumn<CustomField>(Model.of("name"), "name"));
		columns.add(new ExportableTextColumn<CustomField>(Model.of("fieldType"), "fieldType.name"));
		columns.add(new ExportableTextColumn<CustomField>(Model.of("description"), "description"));
		columns.add(new ExportableTextColumn<CustomField>(Model.of("fieldLabel"), "fieldLabel"));
		columns.add(new ExportableTextColumn<CustomField>(Model.of("unitType"), "unitType.name"));
		columns.add(new ExportableTextColumn<CustomField>(Model.of("encodedValues"), "encodedValues"));
		columns.add(new ExportableTextColumn<CustomField>(Model.of("minValue"), "minValue"));
		columns.add(new ExportableTextColumn<CustomField>(Model.of("maxValue"), "maxValue"));
		columns.add(new ExportableTextColumn<CustomField>(Model.of("missingValue"), "missingValue"));

		DataTable table = new DataTable("datatable", columns, customFieldDataView.getDataProvider(), iArkCommonService.getUserConfig(au.org.theark.core.Constants.CONFIG_ROWS_PER_PAGE).getIntValue());
		List<String> headers = new ArrayList<String>(0);
		headers.add("FIELD_NAME");
		headers.add("FIELD_TYPE");
		headers.add("DESCRIPTION");
		headers.add("QUESTION");
		headers.add("UNITS");
		headers.add("ENCODED_VALUES");
		headers.add("MINIMUM_VALUE");
		headers.add("MAXIMUM_VALUE");
		headers.add("MISSING_VALUE");

		String filename = "data_dictionary";
		RepeatingView toolbars = new RepeatingView("toolbars");
		ExportToolbar<String> exportToolBar = new ExportToolbar<String>(table, headers, filename);
		toolbars.add(new Component[] { exportToolBar });
		add(toolbars);
	}
}
