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
package au.org.theark.phenotypic.web.component.field;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import au.org.theark.core.model.pheno.entity.Field;
import au.org.theark.core.model.pheno.entity.FieldType;
import au.org.theark.core.vo.ArkCrudContainerVO;
import au.org.theark.core.web.component.ArkBusyAjaxLink;
import au.org.theark.phenotypic.model.vo.FieldVO;
import au.org.theark.phenotypic.service.Constants;
import au.org.theark.phenotypic.service.IPhenotypicService;
import au.org.theark.phenotypic.web.component.field.form.ContainerForm;

@SuppressWarnings({ "serial", "unchecked" })
public class SearchResultListPanel extends Panel {
	
	@SpringBean(name = Constants.PHENOTYPIC_SERVICE)
	private IPhenotypicService	iPhenotypicService;

	private ArkCrudContainerVO	arkCrudContainerVO;
	private ContainerForm		containerForm;
	
	/**
	 * 
	 * @param id
	 * @param arkCrudContainerVO
	 * @param containerForm
	 */
	public SearchResultListPanel(String id, ArkCrudContainerVO arkCrudContainerVO, PageableListView<Field>	listView,ContainerForm containerForm) {
		super(id);
		this.arkCrudContainerVO = arkCrudContainerVO;
		this.containerForm = containerForm;
	}

	/**
	 * 
	 * @param iModel
	 * @return the pageableListView of Field
	 */
	public PageableListView<Field> buildPageableListView(IModel iModel) {

		PageableListView<Field> sitePageableListView = new PageableListView<Field>("fieldList", iModel, au.org.theark.core.Constants.ROWS_PER_PAGE) {
			@Override
			protected void populateItem(final ListItem<Field> item) {
				Field field = item.getModelObject();

				/* The Field ID */
				if (field.getId() != null) {
					// Add the id component here
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_ID, field.getId().toString()));
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_ID, ""));
				}

				// Component Name Link
				item.add(buildLink(field));

				// Field Type
				if (field.getFieldType() != null) {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_FIELD_TYPE, field.getFieldType().getName()));// the ID here
					// must match the
					// ones in mark-up
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_FIELD_TYPE, ""));// the ID here must match the ones in mark-up
				}

				// Description
				if (field.getDescription() != null) {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_DESCRIPTION, field.getDescription()));// the ID here must match
					// the ones in mark-up
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_DESCRIPTION, ""));// the ID here must match the ones in mark-up
				}

				// Units
				if (field.getName() != null) {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_UNITS, field.getUnits()));// the ID here must match the ones in
					// mark-up
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_UNITS, ""));// the ID here must match the ones in mark-up
				}

				// Min
				if (field.getMinValue() != null) {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_MIN_VALUE, field.getMinValue()));// the ID here must match the
					// ones in mark-up
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_MIN_VALUE, ""));// the ID here must match the ones in mark-up
				}

				// Max
				if (field.getMinValue() != null) {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_MAX_VALUE, field.getMaxValue()));// the ID here must match the
					// ones in mark-up
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_MAX_VALUE, ""));// the ID here must match the ones in mark-up
				}
				
				// Missing Value
				if (field.getMissingValue() != null) {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_MISSING_VALUE, field.getMissingValue()));// the ID here must match the
					// ones in mark-up
				}
				else {
					item.add(new Label(au.org.theark.phenotypic.web.Constants.FIELDVO_FIELD_MISSING_VALUE, ""));// the ID here must match the ones in mark-up
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

	private AjaxLink buildLink(final Field field) {
		ArkBusyAjaxLink link = new ArkBusyAjaxLink("field.name") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				// Sets the selected object into the model
				FieldVO fieldVo = containerForm.getModelObject();
				fieldVo.setField(field);

				arkCrudContainerVO.getDetailPanelFormContainer().setEnabled(false);
				arkCrudContainerVO.getDetailPanelContainer().setVisible(true);
				arkCrudContainerVO.getSearchResultPanelContainer().setVisible(false);
				arkCrudContainerVO.getSearchPanelContainer().setVisible(false);
				// Button containers
				// View Field, thus view container visible
				arkCrudContainerVO.getViewButtonContainer().setVisible(true);// saveBtn
				arkCrudContainerVO.getViewButtonContainer().setEnabled(true);
				arkCrudContainerVO.getEditButtonContainer().setVisible(false);

				// Have to Edit, before allowing delete
				// detailPanel.getDetailForm().getDeleteButton().setEnabled(false);

				// Disable fieldType dropdown if data exists
				boolean hasData = iPhenotypicService.fieldHasData(field);
				DropDownChoice<FieldType>	fieldTypeDdc = (DropDownChoice<FieldType>) arkCrudContainerVO.getDetailPanelFormContainer().get("field.fieldType");
				fieldTypeDdc.setEnabled(!hasData);
				//Replaced the above instead of using detailPanel which will be a lot heavy passing it around. TODO:Remove commented code after testing(NN)
				//detailPanel.getDetailForm().getFieldTypeDdc().setEnabled(!hasData);

				target.add(arkCrudContainerVO.getSearchPanelContainer());
				target.add(arkCrudContainerVO.getDetailPanelContainer());
				target.add(arkCrudContainerVO.getSearchResultPanelContainer());
				target.add(arkCrudContainerVO.getViewButtonContainer());
				target.add(arkCrudContainerVO.getEditButtonContainer());
				target.add(arkCrudContainerVO.getDetailPanelFormContainer());
				target.add(arkCrudContainerVO.getDetailPanelContainer());
			}
		};

		// Add the label for the link
		// TODO when displaying text escape any special characters
		Label nameLinkLabel = new Label("nameLbl", field.getName());
		link.add(nameLinkLabel);
		return link;
	}

}
