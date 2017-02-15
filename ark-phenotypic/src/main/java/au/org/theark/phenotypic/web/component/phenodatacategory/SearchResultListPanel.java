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
package au.org.theark.phenotypic.web.component.phenodatacategory;

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
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.pheno.entity.PhenoDataSetCategory;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.CustomFieldCategoryOrderingHelper;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.vo.CustomFieldCategoryVO;
import au.org.theark.core.vo.PhenoDataSetCategoryVO;
import au.org.theark.core.web.component.ArkCRUDHelper;
import au.org.theark.core.web.component.ArkDataProvider2;
import au.org.theark.core.web.component.export.ExportToolbar;
import au.org.theark.core.web.component.export.ExportableTextColumn;
import au.org.theark.core.web.component.link.ArkBusyAjaxLink;
import au.org.theark.phenotypic.util.PhenoDataSetCategoryOrderingHelper;

/**
 * @author elam
 * 
 */
@SuppressWarnings( { "unchecked", "serial" })
public class SearchResultListPanel extends Panel {

	private static final long							serialVersionUID	= -1L;
	private CompoundPropertyModel<PhenoDataSetCategoryVO>		cpModel;
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
	public SearchResultListPanel(String id, CompoundPropertyModel<PhenoDataSetCategoryVO> cpModel, ArkCrudContainerVO arkCrudContainerVO, FeedbackPanel feedBackPanel){
		super(id);
		this.cpModel = cpModel;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.feedbackPanel = feedBackPanel;
	}

	public DataView<PhenoDataSetCategory> buildDataView(ArkDataProvider2<PhenoDataSetCategory, PhenoDataSetCategory> subjectProvider) {

		DataView<PhenoDataSetCategory> phenoDataSetCategoryDataView = new DataView<PhenoDataSetCategory>("phenoDataSetCategoryList", subjectProvider) {
			@Override
			protected void populateItem(final Item<PhenoDataSetCategory> item) {
				PhenoDataSetCategory category = item.getModelObject();
				//id
				if (category.getId() != null) {
					item.add(new Label(Constants.PHENODATASETCATEGORY_ID, category.getId().toString()));
				}
				else {
					item.add(new Label(Constants.PHENODATASETCATEGORY_ID, ""));
				}
				// Component Name Link
				item.add(buildLinkWMC(item));
				
				// Description
				if (category.getDescription() != null) {
					item.add(new Label(Constants.PHENODATASETCATEGORY_DESCRIPTION, category.getDescription()));
				}
				else {
					item.add(new Label(Constants.PHENODATASETCATEGORY_DESCRIPTION, ""));
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

		addToolbars(phenoDataSetCategoryDataView);

		return phenoDataSetCategoryDataView;
	}

	private WebMarkupContainer buildLinkWMC(final Item<PhenoDataSetCategory> item) {

		WebMarkupContainer phenoDatasetCategoryLinkWMC = new WebMarkupContainer("phenoDataSetCategoryLinkWMC", item.getModel());
		ArkBusyAjaxLink link = new ArkBusyAjaxLink(Constants.PHENODATASETCATEGORY_NAME) {
			@Override
			public void onClick(AjaxRequestTarget target) {
				// Sets the selected object into the model
				PhenoDataSetCategory cf = (PhenoDataSetCategory) (getParent().getDefaultModelObject());
				CompoundPropertyModel<PhenoDataSetCategoryVO> newModel = new CompoundPropertyModel<PhenoDataSetCategoryVO>(new PhenoDataSetCategoryVO());
				newModel.getObject().setPhenoDataSetCategory(cf);
				//newModel.getObject().setUseCustomFieldCategoryDisplay(cpModel.getObject().isUseCustomFieldCategoryDisplay());
				DetailPanel detailPanel = new DetailPanel("detailPanel", feedbackPanel, newModel, arkCrudContainerVO);
				arkCrudContainerVO.getDetailPanelContainer().addOrReplace(detailPanel);
				ArkCRUDHelper.preProcessDetailPanelOnSearchResults(target, arkCrudContainerVO);
			}
		};
		// Add the label for the link
		PhenoDataSetCategory category = item.getModelObject();
		Label nameLinkLabel = new Label("nameLbl", category.getName());
		//Label nameLinkLabel = new Label("nameLbl",PhenoDataSetCategoryOrderingHelper.getInstance().preTextDecider(category).concat(category.getName()));
		link.add(nameLinkLabel);
		phenoDatasetCategoryLinkWMC.add(link);
		return phenoDatasetCategoryLinkWMC;
	}

	private void addToolbars(DataView<PhenoDataSetCategory> phenoDataSetCategoryDataView) {
		List<IColumn<PhenoDataSetCategory>> columns = new ArrayList<IColumn<PhenoDataSetCategory>>();
		columns.add(new ExportableTextColumn<PhenoDataSetCategory>(Model.of("name"), "name"));
		columns.add(new ExportableTextColumn<PhenoDataSetCategory>(Model.of("description"), "description"));
		columns.add(new ExportableTextColumn<PhenoDataSetCategory>(Model.of("parentCategory"), "parentCategory.name"));
		columns.add(new ExportableTextColumn<PhenoDataSetCategory>(Model.of("orderNumber"), "orderNumber"));

		DataTable table = new DataTable("datatable", columns, phenoDataSetCategoryDataView.getDataProvider(), iArkCommonService.getRowsPerPage());

		List<String> headers = new ArrayList<String>(0);
		headers.add("FIELD_NAME");
		headers.add("DESCRIPTION");
		String filename = "pheno_dataset_category";
		RepeatingView toolbars = new RepeatingView("toolbars");
		ExportToolbar<String> exportToolBar = new ExportToolbar<String>(table, headers, filename);
		toolbars.add(new Component[] { exportToolBar });
		add(toolbars);
	}
	
}
