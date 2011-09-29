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
package au.org.theark.phenotypic.web.component.fieldData;

import java.text.SimpleDateFormat;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import au.org.theark.core.Constants;
import au.org.theark.core.model.pheno.entity.FieldData;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkBusyAjaxLink;
import au.org.theark.core.web.component.ArkDataProvider;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.fieldData.form.ContainerForm;

@SuppressWarnings({ "serial", "unchecked" })
public class SearchResultListPanel extends Panel {

	
	private ContainerForm		containerForm;
	private ArkCrudContainerVO arkCrudContainerVO;
	private PageableListView<FieldData>	listView;
	
	public SearchResultListPanel(String id, ArkCrudContainerVO arkCrudContainerVO,  PageableListView<FieldData>	listView, ContainerForm containerForm) {
		super(id);
		
		this.containerForm = containerForm;
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.listView = listView;

	}

	/**
	 * 
	 * @param iModel
	 * @return the pageableListView of FieldData
	 */
	public PageableListView<FieldData> buildPageableListView(IModel iModel) {

		PageableListView<FieldData> sitePageableListView = new PageableListView<FieldData>("fieldDataList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {
			@Override
			protected void populateItem(final ListItem<FieldData> item) {
				FieldData fieldData = item.getModelObject();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DD_MM_YYYY);

				// Link of FieldData ID
				item.add(buildLink(fieldData));

				/* The FieldData Collection */
				if (fieldData.getCollection() != null) {
					// Add the id component here
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_COLLECTION, fieldData.getCollection().getName()));
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_COLLECTION, ""));
				}

				/* The FieldData SubjectUid */
				if (fieldData.getLinkSubjectStudy() != null) {
					// Add the id component here
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_SUBJECTUID, fieldData.getLinkSubjectStudy().getSubjectUID()));
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_SUBJECTUID, ""));
				}

				/* The FieldData Date Collected */
				if (fieldData.getDateCollected() != null) {
					// Add the id component here
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_DATE_COLLECTED, simpleDateFormat.format(fieldData.getDateCollected())));
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_DATE_COLLECTED, ""));
				}

				/* The FieldData Field */
				if (fieldData.getField() != null) {
					// Add the id component here
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_FIELD, fieldData.getField().getName()));
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_FIELD, ""));
				}

				/* The FieldData Value */
				if (fieldData.getValue() != null) {
					// Add the id component here
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_VALUE, fieldData.getValue().toString()));
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_VALUE, ""));
				}

				/* The FieldData Passed Quality Control flag */
				if (fieldData.getPassedQualityControl()) {
					item.add(new ContextImage(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_PASSED_QUALITY_CONTROL, new Model<String>("images/icons/tick.png")));
				}
				else {
					item.add(new ContextImage(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_PASSED_QUALITY_CONTROL, new Model<String>("images/icons/cross.png")));
				}

				/* For the alternative stripes */
				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));

			}
		};
		return sitePageableListView;
	}

	private AjaxLink buildLink(final FieldData fieldData) {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("fieldData.id") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				// Sets the selected object into the model
				PhenoCollectionVO phenoCollectionVo = containerForm.getModelObject();
				phenoCollectionVo.setFieldData(fieldData);
				
				
				arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(false);
				arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
				arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
				arkCrudContainerVO.getSearchPanelContainer().setVisible(false);
				// Button containers
				// View Field, thus view container visible
				arkCrudContainerVO.getViewButtonContainer().setVisible(true);// saveBtn
				arkCrudContainerVO.getViewButtonContainer().setEnabled(true);
				arkCrudContainerVO.getEditButtonContainer().setVisible(false);
			}
		};

		// Add the label for the link
		// TODO when displaying text escape any special characters
		Label nameLinkLabel = new Label("nameLbl", fieldData.getId().toString());
		link.add(nameLinkLabel);
		return link;
	}

	public DataView<PhenoCollectionVO> buildDataView(ArkDataProvider<PhenoCollectionVO, IPhenotypicService> fieldDataProvider) {
		DataView<PhenoCollectionVO> fieldDataDataView = new DataView<PhenoCollectionVO>("fieldDataList", fieldDataProvider) {

			@Override
			protected void populateItem(final Item<PhenoCollectionVO> item) {
				FieldData fieldData = item.getModelObject().getFieldData();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DD_MM_YYYY_HH_MM_SS);

				// Link of FieldData ID
				item.add(buildLink(fieldData));

				/* The FieldData Collection */
				if (fieldData.getCollection() != null) {
					// Add the id component here
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_COLLECTION, fieldData.getCollection().getName()));
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_COLLECTION, ""));
				}

				/* The FieldData SubjectUid */
				if (fieldData.getLinkSubjectStudy() != null) {
					// Add the id component here
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_SUBJECTUID, fieldData.getLinkSubjectStudy().getSubjectUID()));
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_SUBJECTUID, ""));
				}

				/* The FieldData Date Collected */
				if (fieldData.getDateCollected() != null) {
					// Add the id component here
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_DATE_COLLECTED, simpleDateFormat.format(fieldData.getDateCollected())));
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_DATE_COLLECTED, ""));
				}

				/* The FieldData Field */
				if (fieldData.getField() != null) {
					// Add the id component here
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_FIELD, fieldData.getField().getName()));
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_FIELD, ""));
				}

				/* The FieldData Value */
				if (fieldData.getValue() != null) {
					// Add the id component here
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_VALUE, fieldData.getValue().toString()));
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_VALUE, ""));
				}

				/* The FieldData Passed Quality Control flag */
				if (fieldData.getPassedQualityControl()) {
					item.add(new ContextImage(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_PASSED_QUALITY_CONTROL, new Model<String>("images/icons/tick.png")));
				}
				else {
					item.add(new ContextImage(au.org.theark.phenotypic.web.Constants.FIELD_DATAVO_FIELD_DATA_PASSED_QUALITY_CONTROL, new Model<String>("images/icons/cross.png")));
				}

				/* For the alternative stripes */
				item.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
					@Override
					public String getObject() {
						return (item.getIndex() % 2 == 1) ? "even" : "odd";
					}
				}));

			}
		};
		return fieldDataDataView;
	}

}
