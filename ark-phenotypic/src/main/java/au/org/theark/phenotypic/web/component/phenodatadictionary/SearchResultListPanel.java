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
package au.org.theark.phenotypic.web.component.phenodatadictionary;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.pheno.entity.PhenoDataSetField;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.vo.PhenoDataSetFieldVO;
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
	private CompoundPropertyModel<PhenoDataSetFieldVO>		cpModel;
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
	public SearchResultListPanel(String id, CompoundPropertyModel<PhenoDataSetFieldVO> cpModel, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel) {
		super(id);
		this.cpModel = cpModel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedBackPanel;
		
	}

	public DataView<PhenoDataSetField> buildDataView(ArkDataProvider2<PhenoDataSetField, PhenoDataSetField> subjectProvider) {

		DataView<PhenoDataSetField> phonoFieldDataView = new DataView<PhenoDataSetField>("phenoDataSetFieldLst", subjectProvider) {

			@Override
			protected void populateItem(final Item<PhenoDataSetField> item) {
				PhenoDataSetField field = item.getModelObject();

				if (field.getId() != null) {
					// Add the id component here
					item.add(new Label(Constants.PHENODATASET_ID, field.getId().toString()));
				}
				else {
					item.add(new Label(Constants.PHENODATASET_ID, ""));
				}
				// Component Name Link
				item.add(buildLinkWMC(item));
				// Field Type
				if (field.getFieldType() != null) {
					item.add(new Label(Constants.PHENODATASET_FIELD_TYPE, field.getFieldType().getName()));
				}
				else {
					item.add(new Label(Constants.PHENODATASET_FIELD_TYPE, ""));
				}
				// Field Label
				if (field.getFieldLabel() != null) {
					item.add(new Label(Constants.PHENODATASET_FIELD_LABEL, field.getFieldLabel()));
				}
				else {
					item.add(new Label(Constants.PHENODATASET_FIELD_LABEL, ""));
				}
				if (field.getDefaultValue() != null) {
					item.add(new Label(Constants.PHENODATASET_DEFAULT_VALUE, field.getDefaultValue()));
				}
				else {
					item.add(new Label(Constants.PHENODATASET_DEFAULT_VALUE, ""));
				}
				if (field.getMissingValue() != null) {
					item.add(new Label(Constants.PHENODATASET_MISSING_VALUE, field.getMissingValue()));
				}
				else {
					item.add(new Label(Constants.PHENODATASET_MISSING_VALUE, ""));
				}
				/*if (field.getRequired() != null && field.getRequired()==true) {
					item.add(new ContextImage(Constants.PHENODATASET_REQUIRED, new Model<String>("images/icons/tick.png")));
				}
				else {
					item.add(new ContextImage(Constants.PHENODATASET_REQUIRED, new Model<String>("images/icons/cross.png")));
				}*/
				
				/* For the alternative stripes */
				item.add(new AttributeModifier("class", new AbstractReadOnlyModel() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));

			}
		};

		addToolbars(phonoFieldDataView);

		return phonoFieldDataView;
	}

	private WebMarkupContainer buildLinkWMC(final Item<PhenoDataSetField> item) {

		WebMarkupContainer phenoDataSetfieldLinkWMC = new WebMarkupContainer("phenoDataSetfieldLinkWMC", item.getModel());
		ArkBusyAjaxLink link = new ArkBusyAjaxLink(Constants.PHENODATASET_NAME) {
			@Override
			public void onClick(AjaxRequestTarget target) {
				// Sets the selected object into the model
				PhenoDataSetField phenoField = (PhenoDataSetField) (getParent().getDefaultModelObject());
				CompoundPropertyModel<PhenoDataSetFieldVO> newModel = new CompoundPropertyModel<PhenoDataSetFieldVO>(new PhenoDataSetFieldVO());
				newModel.getObject().setPhenoDataSetField(phenoField);
				//newModel.getObject().setUsePhenoDataSetFieldDisplay(cpModel.getObject().isUsePhenoDataSetFieldDisplay());

				DetailPanel detailPanel = new DetailPanel("detailPanel", feedbackPanel, newModel, arkCrudContainerVO);
						//unitTypeDropDownOn,subjectCustomField);
				arkCrudContainerVO.getDetailPanelContainer().addOrReplace(detailPanel);
				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);

			}
		};

		// Add the label for the link
		PhenoDataSetField field = item.getModelObject();
		Label nameLinkLabel = new Label("nameLbl", field.getName());
		link.add(nameLinkLabel);
		phenoDataSetfieldLinkWMC.add(link);
		return phenoDataSetfieldLinkWMC;
	}

	private void addToolbars(DataView<PhenoDataSetField> phenoDataSetFieldDataView) {
		List<IColumn<PhenoDataSetField>> columns = new ArrayList<IColumn<PhenoDataSetField>>();
		columns.add(new ExportableTextColumn<PhenoDataSetField>(Model.of("name"), "name"));
		columns.add(new ExportableTextColumn<PhenoDataSetField>(Model.of("fieldType"), "fieldType.name"));
		columns.add(new ExportableTextColumn<PhenoDataSetField>(Model.of("description"), "description"));
		columns.add(new ExportableTextColumn<PhenoDataSetField>(Model.of("fieldLabel"), "fieldLabel"));
		columns.add(new ExportableTextColumn<PhenoDataSetField>(Model.of("unitTypeInText"), "unitTypeInText"));
		columns.add(new ExportableTextColumn<PhenoDataSetField>(Model.of("encodedValues"), "encodedValues"));
		columns.add(new ExportableTextColumn<PhenoDataSetField>(Model.of("minValue"), "minValue"));
		columns.add(new ExportableTextColumn<PhenoDataSetField>(Model.of("maxValue"), "maxValue"));
		columns.add(new ExportableTextColumn<PhenoDataSetField>(Model.of("missingValue"), "missingValue"));
		columns.add(new ExportableTextColumn<PhenoDataSetField>(Model.of("defaultValue"), "defaultValue"));
		//columns.add(new ExportableTextColumn<PhenoDataSetField>(Model.of("required"), "required"));

		DataTable table = new DataTable("datatable", columns, phenoDataSetFieldDataView.getDataProvider(), iArkCommonService.getRowsPerPage());
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
		headers.add("DEFAULT_VALUE");
		//headers.add("REQUIRED");
	
		String filename = "pheno_dataset_dictionary";
		RepeatingView toolbars = new RepeatingView("toolbars");
		ExportToolbar<String> exportToolBar = new ExportToolbar<String>(table, headers, filename);
		toolbars.add(new Component[] { exportToolBar });
		add(toolbars);
	}
}
