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

import au.org.theark.core.model.study.entity.CustomField;
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
	private CompoundPropertyModel<CustomFieldVO>	cpModel;

	private FeedbackPanel								feedbackPanel;
	private ArkCrudContainerVO							arkCrudContainerVO;

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

				// Field Label
				if (field.getFieldType() != null) {
					item.add(new Label(Constants.CUSTOMFIELD_FIELD_LABEL, field.getFieldLabel()));
				}
				else {
					item.add(new Label(Constants.CUSTOMFIELD_FIELD_LABEL, ""));
				}

				// Field Type
				if (field.getFieldType() != null) {
					item.add(new Label(Constants.CUSTOMFIELD_FIELD_TYPE, field.getFieldType().getName()));
				}
				else {
					item.add(new Label(Constants.CUSTOMFIELD_FIELD_TYPE, ""));
				}

				// Description
				if (field.getDescription() != null) {
					item.add(new Label(Constants.CUSTOMFIELD_DESCRIPTION, field.getDescription()));
				}
				else {
					item.add(new Label(Constants.CUSTOMFIELD_DESCRIPTION, ""));
				}

				// Units
				if (field.getUnitType() != null && field.getUnitType().getName() != null) {
					item.add(new Label(Constants.CUSTOMFIELD_UNIT_TYPE, field.getUnitType().getName()));
				}
				else {
					item.add(new Label(Constants.CUSTOMFIELD_UNIT_TYPE, ""));
				}

				// Min
				if (field.getMinValue() != null) {
					item.add(new Label(Constants.CUSTOMFIELD_MIN_VALUE, field.getMinValue()));
				}
				else {
					item.add(new Label(Constants.CUSTOMFIELD_MIN_VALUE, ""));
				}

				// Max
				if (field.getMaxValue() != null) {
					item.add(new Label(Constants.CUSTOMFIELD_MAX_VALUE, field.getMaxValue()));
				}
				else {
					item.add(new Label(Constants.CUSTOMFIELD_MAX_VALUE, ""));
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

		DataTable table = new DataTable("datatable", columns, customFieldDataView.getDataProvider(), au.org.theark.core.Constants.ROWS_PER_PAGE);
		List<String> headers = new ArrayList<String>(0);
		headers.add("FIELD_NAME");
		headers.add("FIELD_TYPE");
		headers.add("DESCRIPTION");
		headers.add("QUESTION");
		headers.add("UNITS");
		headers.add("ENCDODED_VALUES");
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
